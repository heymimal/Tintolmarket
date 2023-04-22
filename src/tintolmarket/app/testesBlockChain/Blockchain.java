package tintolmarket.app.testesBlockChain;

import java.util.List;
import java.util.ArrayList;

public class Blockchain {
    private List<BlockTintol> blocks;

    // constructor
    public Blockchain() {
        blocks = new ArrayList<BlockTintol>();
    }

    // add a block to the blockchain
    public void addBlock(BlockTintol block) {
        blocks.add(block);
    }

    // get the last block in the blockchain
    public BlockTintol getLastBlock() {
        if (blocks.size() > 0) {
            return blocks.get(blocks.size() - 1);
        }
        return null;
    }

    // get the list of blocks in the blockchain
    public List<BlockTintol> getBlocks() {
        return blocks;
    }
}
