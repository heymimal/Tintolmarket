package tintolmarket.handlers;

import tintolmarket.domain.blockchain.BlockTintol;
import tintolmarket.domain.blockchain.Transaction;
import tintolmarket.app.security.ServerSecurity;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class BlockchainHandler {

    private final File blockFolder = new File("blockchain");

    private long id;

    private final ServerSecurity cs;

    public BlockchainHandler(ServerSecurity cs)  {
        this.cs = cs;
        chainStartAndIntegrityCheck();
    }

    public void addTransaction(Transaction t) {
        try {
            BlockTintol bloco = getBlock(this.id);
            boolean cheio = bloco.addTransaction(t);
            byte[] hash = null;

            if(cheio) {
                // Bloco a ser fechado
                bloco.setSignature(this.cs.getServerSignature(bloco));
                updateBlock(bloco);

                // A gerar hash
                File blockFile = getblockFile(this.id);
                hash = calculateHash(blockFile);

                // A criar novo bloco
                setId(bloco.getIndex()+1);
                createBlock(this.id, hash);
            } else {
                updateBlock(bloco);
            }
        }  catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized String list(){
        StringBuilder sb = new StringBuilder();
        sb.append("Transacoes:\n");
        for(long i = 1; i <= this.id; i++){
            try {
                BlockTintol b = getBlock(i);
                sb.append(b.toString());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return sb.toString();
    }

    private File getblockFile(long id) {
        String path = "block_"+id +".blk";
        return new File(blockFolder,path);
    }

    private void setId(long id) {
        this.id = id;
    }

    private void chainStartAndIntegrityCheck() {
        long i = createBlockChain();
        verifyIntegrity(i);

    }

    private BlockTintol getBlock(long i) throws IOException, ClassNotFoundException {
        File blockFile = getblockFile(i);
        FileInputStream file = new FileInputStream(blockFile);
        if(file.available()>0) {
            ObjectInputStream in = new ObjectInputStream(file);
            BlockTintol b = (BlockTintol) in.readObject();
            System.out.println("index do bloco " + b.getIndex());
            in.close();
            return b;
        }

        return null;
    }

    private void createBlock(long id, byte[] hash) throws IOException {
        FileOutputStream fileOut;
        BlockTintol bloco = new BlockTintol(id, hash);

        File blockFile = getblockFile(id);

        fileOut = new FileOutputStream(blockFile, false);
        ObjectOutputStream outst = new ObjectOutputStream(fileOut);
        outst.writeObject(bloco);
        outst.close();
        fileOut.close();
    }

    private void updateBlock(BlockTintol b) throws IOException {
        FileOutputStream fileOut;

        File blockFile = getblockFile(b.getIndex());

        fileOut = new FileOutputStream(blockFile, false);
        ObjectOutputStream out2 = new ObjectOutputStream(fileOut);
        out2.writeObject(b);
        out2.close();
        fileOut.close();
    }

    private byte[] calculateHash(File filepath) throws IOException, NoSuchAlgorithmException {
        FileInputStream fis = new FileInputStream(filepath);
        byte[] data = new byte[(int) filepath.length()];
        fis.close();
        MessageDigest m = MessageDigest.getInstance("SHA-256");
        return m.digest(data);
    }

    private void verifyIntegrity(long i){
        try{
            byte[] previous_hash = null;
            while(i > 0){
                // Verifying the integrity of each block
                BlockTintol b = getBlock(i);
                if(b.isFull()){
                    // Verificacao
                    File blockFile = getblockFile(i);
                    byte[] currentHash = calculateHash(blockFile);
                    Boolean verifyBlock = cs.verificaBlockChain(b, previous_hash, currentHash);
                    if(verifyBlock){
                        previous_hash = b.getPreviousHash();
                        System.out.println("Verificacao do bloco " + i + "da blockchain realizada com sucesso!");
                        i--;
                    } else {
                        System.out.println("Verificacao do bloco " + i + "da blockchain falhada! A terminar o programa!");
                        System.exit(-5);
                    }
                } else {
                    previous_hash = b.getPreviousHash();
                    i--;
                }
            }
        }catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private long createBlockChain(){
        long i = 1;
        try {
            if(blockFolder.mkdir()){
                // created new directory, doesnt need to check integrity -> no blockchain created -> creates block1
                byte[] zeros = new byte[32];
                Arrays.fill(zeros, (byte) 0);
                createBlock(1,zeros);
                setId(1);
            } else{
                //verifies integrity and saves the latest id -> where its going to write the following transactions
                //Saving the latest id
                boolean found = false;
                while(!found){
                    File blockFile = getblockFile(i);
                    if(blockFile.exists()){
                        BlockTintol b = getBlock(i);
                        if(!b.isFull()){
                            System.out.println(i + " IS NOT FULL");
                            setId(i);
                            found = true;
                        } else {
                            System.out.println(i + " IS FULL");
                            i++;
                        }
                    } else { // this shouldnt happen for i=1, as a block is always created even if null
                        this.id = i;
                        found = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Blockchain has been compromised");
            throw new RuntimeException(e);
        }
        return i;
    }

}
