package Bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/24.
 */
public class Block {
    String blockId, version, transactionsHash, stateHash, previousBlockHash;
    ArrayList<Transcation> transactions;

    public Block() {
    }

    public Block(String blockId, String version, String transactionsHash, String stateHash, String previousBlockHash, ArrayList<Transcation> transactions) {
        this.blockId = blockId;
        this.version = version;
        this.transactionsHash = transactionsHash;
        this.stateHash = stateHash;
        this.previousBlockHash = previousBlockHash;
        this.transactions = transactions;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTransactionsHash() {
        return transactionsHash;
    }

    public void setTransactionsHash(String transactionsHash) {
        this.transactionsHash = transactionsHash;
    }

    public String getStateHash() {
        return stateHash;
    }

    public void setStateHash(String stateHash) {
        this.stateHash = stateHash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public ArrayList<Transcation> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transcation> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Block{" +
                "blockId='" + blockId + '\'' +
                ", version='" + version + '\'' +
                ", transactionsHash='" + transactionsHash + '\'' +
                ", stateHash='" + stateHash + '\'' +
                ", previousBlockHash='" + previousBlockHash + '\'' +
                ", transactions=" + transactions +
                '}';
    }
}
