import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> blockchain;

    public Blockchain() {
        this.blockchain = new ArrayList<>();
    }

    public void addBlock(Block block) {
        this.blockchain.add(block);
    }

    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Block block : blockchain) {
            sb.append(block).append("\n");
        }
        return sb.toString();
    }
}