package blockchain;

import java.time.Instant;
import java.util.LinkedList;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

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

  /**
   * Utility method for creating a block.
   *
   * @param blockHeight the height of a block.
   * @param prevBlockHash the hash of a previous block.
   * @return block.
   */
  private Block createBlock(int blockHeight, String prevBlockHash) {

    BlockHeader blockHeader =
        BlockHeader.builder()
            .withVersion(1)
            .withPrevBlockHash(prevBlockHash)
            .withTimestamp(Instant.now().getEpochSecond())
            .withDifficultyBits(BITS)
            .build();

    Block block =
        Block.builder()
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

  /**
   * Method for adding the block to the blockchain
   *
   * @param blockHeight the height of a block.
   * @param prevBlockHash the hash of a previous block.
   */
  public void addBlock(int blockHeight, String prevBlockHash) {
    String realPreviousHash = chain.getLast().getBlockHeader().getBlockHash();
    if (!realPreviousHash.equals(prevBlockHash)) {
      throw new IllegalArgumentException("The block is altered!");
    }
    this.chain.add(createBlock(blockHeight, prevBlockHash));
  }
}
