package tintolmarket.app.testesBlockChain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlockTintol implements Serializable {
    private String previousHash;
    private long blk_id;
    private long n_trx;
    private List<Transaction> transactions;
    private byte[] signature;

    // constructor
    public BlockTintol(long blk_id, String previousHash) {
        this.blk_id = blk_id;
        this.previousHash = previousHash;
        this.transactions = new ArrayList<>();
        n_trx = transactions.size();

    }

    // getters
    public long getIndex() {
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

    public boolean addTransaction(Transaction t){
        this.transactions.add(t);
        n_trx++;
        return n_trx == 5;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public long getN_trx() {
        return n_trx;
    }

    public boolean isFull() {
        return this.n_trx == 5;
    }
}
