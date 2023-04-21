package tintolmarket.app;

import java.util.List;
import java.util.ArrayList;

public class BlockTintol {
    private int index;
    private long timestamp;
    private List<Transaction> transactions;
    private String previousHash;
    private String hash;

    // constructor
    public BlockTintol(int index, long timestamp, List<Transaction> transactions, String previousHash, String hash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.hash = hash;
    }

    // getters
    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
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
}
