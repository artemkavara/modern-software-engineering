import blockchain.Blockchain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
  public static void main(String[] args) {
    ObjectMapper objectMapper = new ObjectMapper();
    Blockchain blockchain = new Blockchain();
    blockchain.addBlock(
        blockchain.getChain().getLast().getHeight() + 1,
        blockchain.getChain().getLast().getBlockHeader().getBlockHash());
    blockchain
        .getChain()
        .forEach(
            b -> {
              try {
                System.out.println(
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b));
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            });
  }
}
