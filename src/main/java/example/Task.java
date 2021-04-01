package example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Task {

  public void runTask() {
    try {
      Thread.sleep(2000);
      log.info("run task completed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public String supplyTask() {
    return supplyTask(2000);
  }

  public String supplyTask(int waitTimeMs) {
    try {
      Thread.sleep(waitTimeMs);
      log.info("supply task completed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "supply task returned";
  }

  public String functionTask(String message) {
    try {
      Thread.sleep(2000);
      log.info("function task message = " + message);
      log.info("function task completed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return message;
  }

  public void consumeTask(String message) {
    try {
      Thread.sleep(2000);
      log.info("consume task message = " + message);
      log.info("consume task completed");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void throwException() {
    throw new RuntimeException("error!!!");
  }
}
