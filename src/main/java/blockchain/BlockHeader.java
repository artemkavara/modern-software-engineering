package blockchain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private static String convertHexToLittleEnding(String hexNumber){
        if(hexNumber.length()%2 != 0) {
            hexNumber = "0" + hexNumber;
        }
        List<String> hexNumberList = new ArrayList<>();
        for (int i = 0; i<hexNumber.length(); i+=2){
            hexNumberList.add(hexNumber.substring(i, i+2));
        }
        Collections.reverse(hexNumberList);
        return String.join("", hexNumberList);
    }

    private static String convertIntToHexLittleEnding(int decNumber){
        return convertHexToLittleEnding(Integer.toHexString(decNumber));
    }

    private static String convertLongToHexLittleEnding(long decNumber){
        return convertHexToLittleEnding(Long.toHexString(decNumber));
    }

    private int calcDifficulty(){
        int exponent = Integer.parseInt(difficultyBits.substring(0, 2), 16);
        long coefficient = Long.parseLong(difficultyBits.substring(2),16);
        BigInteger target = BigInteger.valueOf(2);
        target = target.pow(8*(exponent-3));
        target = target.multiply(BigInteger.valueOf(coefficient));

        String targetHex = target.toString(16);

        return 64-targetHex.length();
    }

    private String concatenateString(){
        return convertIntToHexLittleEnding(version) +
                convertHexToLittleEnding(prevBlockHash) +
                convertHexToLittleEnding(merkleRoot) +
                convertLongToHexLittleEnding(timestamp) +
                convertHexToLittleEnding(difficultyBits) +
                convertIntToHexLittleEnding(nonce);
    }


    public String mine(){
        int difficulty = this.calcDifficulty();
        log.info("Nonce version: "+this.nonce+ ", difficulty: " + difficulty);

        String blockString = this.concatenateString();
        String hash1 = DigestUtils.sha256Hex(blockString);
        String hash2 = DigestUtils.sha256Hex(hash1);
        log.info("Calculated hash: "+hash2);


        if (Integer.parseInt(hash2.substring(0, difficulty),16) == 0) {
            return hash2;
        }
        this.nonce += 1;
        log.info("The hash is not difficult enough, trying again.");
        return this.mine();
    }

}
