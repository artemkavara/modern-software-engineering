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

  public void calculateMerkleRoot() {
    this.blockHeader.setMerkleRoot(DigestUtils.sha256Hex(txs.getMessage()));
  }

  public void setBlockHash(String blockHash) {
    this.blockHeader.setBlockHash(blockHash);
  }
}
