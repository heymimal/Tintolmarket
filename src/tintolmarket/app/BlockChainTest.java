package tintolmarket.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlockChainTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
       /* BlockUtils blockUtils = new BlockUtils();
        blockUtils.addBlock("100");
        blockUtils.addBlock("200");
        blockUtils.addBlock("300");
        System.out.println(blockUtils.isChainValid());
        blockUtils.getBlockChain().get(2).setData("5000");
        System.out.println(blockUtils.isChainValid());
        System.out.println(blockUtils.getBlockChain());*/

        // create a new blockchain
        Blockchain blockchain = new Blockchain();

        // create a genesis block
        List<Transaction> transactions = new ArrayList<Transaction>();
        BlockTintol genesisBlock = new BlockTintol(0, new Date().getTime(), transactions, null, "");
        String hash = calculateHash(genesisBlock);
        genesisBlock.setHash(hash);
        blockchain.addBlock(genesisBlock);

        // create and add new blocks to the blockchain
        List<Transaction> transactions2 = new ArrayList<Transaction>();
        transactions2.add(new Transaction("sender1", "recipient1", 10.0));
        transactions2.add(new Transaction("sender2", "recipient2", 20.0));
        BlockTintol block2 = new BlockTintol(1, new Date().getTime(), transactions2, genesisBlock.getHash(), "");
        String hash2 = calculateHash(block2);
        block2.setHash(hash2);
        blockchain.addBlock(block2);

        List<Transaction> transactions3 = new ArrayList<Transaction>();
        transactions3.add(new Transaction("sender3", "recipient3", 30.0));
        BlockTintol block3 = new BlockTintol(2, new Date().getTime(), transactions3, block2.getHash(), "");
        String hash3 = calculateHash(block3);
        block3.setHash(hash3);
        blockchain.addBlock(block3);

        for (BlockTintol block : blockchain.getBlocks()) {
            System.out.println("Block #" + block.getIndex());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Transactions: ");
            for (Transaction transaction : block.getTransactions()) {
                System.out.println("  Sender: " + transaction.getSender());
                System.out.println("  Recipient: " + transaction.getRecipient());
                System.out.println("  Amount: " + transaction.getAmount());
            }
            System.out.println();
        }

    }

    // calculate the hash of a block using SHA-256
    public static String calculateHash(BlockTintol block) {
        String data = block.getIndex() + block.getTimestamp() + block.getTransactions().toString() + block.getPreviousHash();
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