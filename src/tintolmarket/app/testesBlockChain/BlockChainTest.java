package tintolmarket.app.testesBlockChain;

import tintolmarket.domain.Tipo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class BlockChainTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        // create a new blockchain
        Blockchain blockchain = new Blockchain();

        List<Transaction> transactions = new ArrayList<Transaction>();

        BlockTintol currentBlock = new BlockTintol(0, null, transactions, null);
        Transaction t = new Transaction("abc", 5, 10, "user1", Tipo.BUY);
        if(currentBlock.getN_trx() < 5 && currentBlock.getN_trx() > 3){
            currentBlock.addTransaction(t);
            currentBlock = new BlockTintol(1, null, transactions, currentBlock.getHash());
        }



        //cria um genesis block com index 0, com previoushash a null pq é o primeiro bloco. passamos as transacoes
        //dentro do construtor
        /*List<Transaction> transactions = new ArrayList<Transaction>();
        BlockTintol genesisBlock = new BlockTintol(0, null, transactions, null);
        String hash = calculateHash(genesisBlock);
        genesisBlock.setHash(hash);
        blockchain.addBlock(genesisBlock);*/

        // create and add new blocks to the blockchain
        List<Transaction> transactions2 = new ArrayList<Transaction>();
        //transactions2.add(new Transaction("sender1", "recipient1", 10.0));
        //transactions2.add(new Transaction("sender2", "recipient2", 20.0));
        transactions2.add(new Transaction("abc", 5, 10, "user1", Tipo.BUY));
        transactions2.add(new Transaction("abcd", 7, 10, "user3", Tipo.SELL));
        BlockTintol block2 = new BlockTintol(1, null, transactions2, genesisBlock.getHash());
        String hash2 = calculateHash(block2);
        block2.setHash(hash2);
        blockchain.addBlock(block2);

        List<Transaction> transactions3 = new ArrayList<Transaction>();
        transactions3.add(new Transaction("abcd", 6, 10, "user2", Tipo.BUY));
        BlockTintol block3 = new BlockTintol(2, null, transactions3, block2.getHash());
        String hash3 = calculateHash(block3);
        block3.setHash(hash3);
        blockchain.addBlock(block3);

        for (BlockTintol block : blockchain.getBlocks()) {
            System.out.println("Block #" + block.getIndex());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Transactions: ");
            for (Transaction transaction : block.getTransactions()) {
                System.out.println("  User: " + transaction.getUser());
                System.out.println("  Nome vinho: " + transaction.getNome_vinho());
                System.out.println("  nºEntidades: " + transaction.getnEntidades());
                System.out.println("  Valor: " + transaction.getValor());
                System.out.println("  Tipo: " + transaction.getTipo());
            }
            System.out.println();
        }

        System.out.println("Blockchain tem " + blockchain.getBlocks().size() + "blocos");

    }

    // calculate the hash of a block using SHA-256
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