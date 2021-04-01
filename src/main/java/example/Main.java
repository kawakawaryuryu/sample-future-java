package example;

import java.util.concurrent.ExecutionException;

public class Main {

  public static void main(String[] args) {
    // cf
    CompletableFutureSample completableFutureSample = new CompletableFutureSample();
    completableFutureSample.cf();

    // future
    FutureSample futureSample = new FutureSample();
    try {
//      futureSample.future();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
