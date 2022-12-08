package blockchain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

@EqualsAndHashCode
@Getter
@Builder(setterPrefix = "with")
@Slf4j
@ToString
public class Block {
  private final int height;
  private final int blockSize;
  private final BlockHeader blockHeader;
  private final int txCount;
  private final Transaction txs;

  /**
   * Method to calculate hash of merkle tree. In this version of blockchain architecture, only 1
   * transaction/block is possible, so the merkle tree hash is basically the hash of the transaction
   * message.
   */
  public void calculateMerkleRoot() {
    this.blockHeader.setMerkleRoot(DigestUtils.sha256Hex(txs.getMessage()));
  }

  /** Method that sets the value of block hash calculated after the mining. */
  public void setBlockHash(String blockHash) {
    this.blockHeader.setBlockHash(blockHash);
  }
}
