package tintolmarket.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block2 {
    private String hash;
    private String previousHash;
    private String data;
    private int index;

    Block2(int index, String data, String previousHash) throws NoSuchAlgorithmException {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    private Long timestamp;

    public String calculateHash() throws NoSuchAlgorithmException {
        String text = String.valueOf(index + previousHash + String.valueOf(timestamp + String.valueOf(data)));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        final byte bytes[] = digest.digest(text.getBytes());
        final StringBuilder hexString = new StringBuilder();

        for(final byte b: bytes){
            String hex = Integer.toHexString(0xff &b);
            if(hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}







/*public class Main {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(4);

        blockchain.addBlock(new Block("Block 1"));
        blockchain.addBlock(new Block("Block 2"));
        blockchain.addBlock(new Block("Block 3"));

        System.out.println("Is blockchain valid? " + blockchain.isChainValid());
    }
}*/
