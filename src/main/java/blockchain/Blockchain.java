package blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.LinkedList;

@Getter
@ToString
@Slf4j
public class Blockchain {

    private static final String BITS = "1f00ffff";
    private static final String SENDER_NAME = "Artem";
    private final LinkedList<Block> chain;

    public Blockchain() {
        chain = new LinkedList<>();

        Block genesisBlock = createBlock(0, "0");
        chain.add(genesisBlock);

    }
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        Blockchain blockchain = new Blockchain();
        blockchain.addBlock(blockchain.getChain().getLast().getHeight() + 1,
                blockchain.getChain().getLast().getBlockHeader().getBlockHash());
        blockchain.getChain().forEach(b -> {
            try {
                System.out.println(objectMapper
                                .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(b));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Block createBlock(int blockHeight, String prevBlockHash) {
        BlockHeader blockHeader = BlockHeader.builder()
                .withVersion(1)
                .withPrevBlockHash(prevBlockHash)
                .withTimestamp(Instant.now().getEpochSecond())
                .withDifficultyBits(BITS)
                .build();

        Block block = Block.builder()
                .withHeight(blockHeight)
                .withBlockSize(1)
                .withBlockHeader(blockHeader)
                .withTxCount(1)
                .withTxs(new Transaction())
                .build();

        block.getTxs().setMessage(SENDER_NAME, block.getHeight());

        block.calculateMerkleRoot();
        String genesisHash = block.getBlockHeader().mine();
        block.setBlockHash(genesisHash);
        return block;
    }

    public void addBlock(int blockHeight, String prevBlockHash) {
        this.chain.add(createBlock(blockHeight, prevBlockHash));
    }
}
