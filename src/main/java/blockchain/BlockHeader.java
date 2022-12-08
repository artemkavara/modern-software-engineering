package blockchain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

@EqualsAndHashCode
@ToString
@Slf4j
@Builder(setterPrefix = "with")
@Getter
@Setter
public class BlockHeader {
  private final int version;
  private final String prevBlockHash;
  private String merkleRoot;
  private final long timestamp;
  private final String difficultyBits;
  private int nonce;
  private String blockHash;

  /**
   * Utility method to convert the hexadecimal number from big ending notation to little ending
   * notation. Example: given the hexadecimal number 1F2A34 in big ending notation. Then the same
   * number represented in the little ending notation is 342A1F.
   *
   * @param hexNumber hexadecimal number for transformation.
   * @return hexadecimal number in little ending notation.
   */
  private static String convertHexToLittleEnding(String hexNumber) {
    if (hexNumber.length() % 2 != 0) {
      hexNumber = "0" + hexNumber;
    }
    List<String> hexNumberList = new ArrayList<>();
    for (int i = 0; i < hexNumber.length(); i += 2) {
      hexNumberList.add(hexNumber.substring(i, i + 2));
    }
    Collections.reverse(hexNumberList);
    return String.join("", hexNumberList);
  }

  /**
   * Utility method to convert an integer decimal number to hexadecimal number.
   *
   * @param decNumber int for transformation.
   * @return hexadecimal number in little ending notation.
   */
  private static String convertIntToHexLittleEnding(int decNumber) {
    return convertHexToLittleEnding(Integer.toHexString(decNumber));
  }

  /**
   * Utility method to convert a long decimal number to hexadecimal number.
   *
   * @param decNumber long for transformation.
   * @return hexadecimal number in little ending notation.
   */
  private static String convertLongToHexLittleEnding(long decNumber) {
    return convertHexToLittleEnding(Long.toHexString(decNumber));
  }

  /**
   * Utility method to calculate the difficulty of mining. Given difficulty bits in hexadecimal
   * form. The first 2 digits represent the exponent, the latter represent the coefficient. The
   * target is calculated by formula coef*2^(8*(exp-3)) and then the target is converted to
   * hexadecimal number again. After that the number of zeros before the first significant digit is
   * calculated.
   *
   * @return the difficulty of the mining algorithm.
   */
  private int calcDifficulty() {
    int exponent = Integer.parseInt(difficultyBits.substring(0, 2), 16);
    long coefficient = Long.parseLong(difficultyBits.substring(2), 16);
    BigInteger target = BigInteger.valueOf(2);
    target = target.pow(8 * (exponent - 3));
    target = target.multiply(BigInteger.valueOf(coefficient));

    String targetHex = target.toString(16);

    return 64 - targetHex.length();
  }

  /**
   * Utility method for creating a string for mining.
   *
   * @return concatenated string ready for SHA256 encoding.
   */
  private String concatenateString() {
    return convertIntToHexLittleEnding(version)
        + convertHexToLittleEnding(prevBlockHash)
        + convertHexToLittleEnding(merkleRoot)
        + convertLongToHexLittleEnding(timestamp)
        + convertHexToLittleEnding(difficultyBits)
        + convertIntToHexLittleEnding(nonce);
  }

  /**
   * Main method for mining the block hash. The process is repeated until the hash satisfies the
   * difficulty condition.
   *
   * @return hash for block after the mining is successful.
   */
  public String mine() {
    int difficulty = this.calcDifficulty();
    log.info("Nonce version: " + this.nonce + ", difficulty: " + difficulty);

    String blockString = this.concatenateString();
    String hash1 = DigestUtils.sha256Hex(blockString);
    String hash2 = DigestUtils.sha256Hex(hash1);
    log.info("Calculated hash: " + hash2);

    if (Integer.parseInt(hash2.substring(0, difficulty), 16) == 0) {
      return hash2;
    }
    this.nonce += 1;
    log.info("The hash is not difficult enough, trying again.");
    return this.mine();
  }
}
