package tintolmarket.app.testesBlockChain;

import java.util.List;

public class BlockTintol {
    private int blk_id;
    private List<Transaction> transactions;
    private String previousHash;
    private String hash;
    private byte[] signature;
    private int n_trx;

    // constructor
    public BlockTintol(int blk_id, byte[] signature, List<Transaction> transactions, String previousHash) {
        this.blk_id = blk_id;
        this.signature = signature;
        this.transactions = transactions;
        this.previousHash = previousHash;
        n_trx = transactions.size();
    }

    // getters
    public int getIndex() {
        return blk_id;
    }

    public byte[] getSignature() {
        return signature;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void addTransaction(Transaction t){
        this.transactions.add(t);
        n_trx++;
    }

    public int getN_trx() {
        return n_trx;
    }
}
