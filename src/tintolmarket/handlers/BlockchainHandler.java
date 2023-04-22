package tintolmarket.handlers;

import tintolmarket.app.testesBlockChain.BlockTintol;
import tintolmarket.app.testesBlockChain.Transaction;
import tintolmarket.app.security.Cifra_Server;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockchainHandler {

    private String blockPath = "block_1.blk";

    private Cifra_Server cs;

    public BlockchainHandler(Cifra_Server cs){
        this.cs = cs;
    }

    public void addTransaction(Transaction t) throws IOException, ClassNotFoundException {
        BlockTintol bloco = getBlock();
        boolean cheio = bloco.addTransaction(t);
        String hash = null;

        if(cheio) {
            System.out.println("bloco estah cheio");
            bloco.setSignature(this.cs.getServerSignature(bloco));
            hash = calculateHash(bloco);
            //createBlock(bloco.getIndex(), hash);
            createBlock2(bloco.getIndex(), hash);

        }

        System.out.println("transacao adicionada");
        updateBlock(bloco);
        System.out.println("block atualizado");
    }

    private void createBlock2(long index, String hash) throws IOException {
        FileOutputStream fileOut;
        BlockTintol bloco = new BlockTintol(index, hash);

        fileOut = new FileOutputStream("block_2.blk", false);
        ObjectOutputStream outst = new ObjectOutputStream(fileOut);
        outst.writeObject(bloco);
        outst.close();
        fileOut.close();
    }

    public BlockTintol getBlock() throws IOException, ClassNotFoundException {

        FileInputStream file = new FileInputStream(blockPath);
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

        fileOut = new FileOutputStream(blockPath, false);
        ObjectOutputStream outst = new ObjectOutputStream(fileOut);
        outst.writeObject(bloco);
        outst.close();
        fileOut.close();
    }

    public void updateBlock(BlockTintol b) throws IOException {
        FileOutputStream fileOut;

        fileOut = new FileOutputStream(blockPath, false);
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
