package blockchain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString
public class Transaction {
  private String message;

  public void setMessage(String sender, int height) {
    this.message = String.format("%s sent %d coins to Alice", sender, height);
    log.info("Work with transaction " + message + " begin");
  }
}
