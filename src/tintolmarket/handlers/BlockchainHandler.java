package tintolmarket.handlers;

import tintolmarket.app.testesBlockChain.BlockTintol;
import tintolmarket.app.testesBlockChain.Transaction;
import tintolmarket.app.security.Cifra_Server;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class BlockchainHandler {

    private final File blockFolder = new File("blockchain");

    private long id;

    private Cifra_Server cs;

    public void setId(long id) {
        this.id = id;
    }

    public BlockchainHandler(Cifra_Server cs) throws IOException, ClassNotFoundException {
        this.cs = cs;
        if(blockFolder.mkdir()){
            // created new directory, doesnt need to check integrity -> no blockchain created -> creates block1
            byte[] zeros = new byte[32];
            Arrays.fill(zeros, (byte) 0);
            createBlock(1,Arrays.toString(zeros));
            setId(1);
        } else{
            //verifies integrity and saves the latest id -> where its going to write the following transactions
            //just saving the id for now
            long i = 1;
            boolean found = false;
            while(!found){
                String path = "block_"+i +".blk";
                File blockFile = new File(blockFolder,path);
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
                } else { // this shouldnt happen, as a block is always created even if null
                    this.id = i;
                    found = true;
                }
            }


        }
    }

    public void addTransaction(Transaction t) throws IOException, ClassNotFoundException {
        BlockTintol bloco = getBlock(this.id);
        boolean cheio = bloco.addTransaction(t);
        String hash = null;

        if(cheio) {
            System.out.println("bloco estah cheio");
            bloco.setSignature(this.cs.getServerSignature(bloco));
            hash = calculateHash(bloco);
            updateBlock(bloco);
            setId(bloco.getIndex()+1);
            createBlock(this.id, hash);
        } else {
            updateBlock(bloco);
        }

    }

    public BlockTintol getBlock(long i) throws IOException, ClassNotFoundException {
        String path = "block_"+i +".blk";
        File blockFile = new File(blockFolder,path);
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

    public void createBlock(long id, String hash) throws IOException {
        FileOutputStream fileOut;
        BlockTintol bloco = new BlockTintol(id, hash);

        String path = "block_"+id +".blk";
        File blockFile = new File(blockFolder,path);

        fileOut = new FileOutputStream(blockFile, false);
        ObjectOutputStream outst = new ObjectOutputStream(fileOut);
        outst.writeObject(bloco);
        outst.close();
        fileOut.close();
    }

    public void updateBlock(BlockTintol b) throws IOException {
        FileOutputStream fileOut;

        String path = "block_"+b.getIndex() +".blk";
        File blockFile = new File(blockFolder,path);

        fileOut = new FileOutputStream(blockFile, false);
        ObjectOutputStream out2 = new ObjectOutputStream(fileOut);
        out2.writeObject(b);
        out2.close();
        fileOut.close();
    }

    public static String calculateHash(BlockTintol block) {
        String data = block.getIndex() + block.getTransactions().toString() + block.getPreviousHash();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(data.getBytes());
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
