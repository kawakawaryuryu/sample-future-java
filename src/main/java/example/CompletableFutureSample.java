package example;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureSample {

  private static final ExecutorService fixedThreadExecutorService = Executors.newFixedThreadPool(2);
  private static final ExecutorService cachedThreadExecutorService = Executors.newCachedThreadPool();
  private static final ExecutorService workStealingExecutorService = Executors.newWorkStealingPool(2);

  void cf() {
    Task task = new Task();

    // runAsync
    log.info("cf runAsync");
    CompletableFuture<Void> cf = CompletableFuture.runAsync(task::runTask);
    cf.join();
    System.out.println();

    // supplyAsync
    // thenApply, thenAcceptにAsyncなし
    log.info("cf supplyAsync - thenApply - thenAccept");
    CompletableFuture<Void> cfNoAsync = CompletableFuture.supplyAsync(task::supplyTask, fixedThreadExecutorService)
        .thenApply(task::functionTask)
        .thenAccept(task::consumeTask);
    cfNoAsync.join();
    System.out.println();

    // supplyAsync
    // thenApply, thenAcceptにAsyncあり
    log.info("cf supplyAsync - thenApplyAsync - thenAcceptAsync");
    CompletableFuture<Void> cfAsync = CompletableFuture.supplyAsync(task::supplyTask, fixedThreadExecutorService)
        .thenApplyAsync(task::functionTask)
        .thenAcceptAsync(task::consumeTask);
    cfAsync.join();
    System.out.println();

    // completedFuture
    log.info("cf completedFuture");
    CompletableFuture<String> cfCompletedFuture = CompletableFuture.completedFuture("ok");
    String message = cfCompletedFuture.join();
    log.info(message);
    System.out.println();

    // failedFuture
    log.info("cf failedFuture");
    CompletableFuture<Void> cfFailedFuture = CompletableFuture.failedFuture(new RuntimeException("error"));
    try {
      cfFailedFuture.join();
    } catch (Exception e) {
      log.info(e.getMessage());
    }
    System.out.println();

    // exceptionally
    log.info("cf exceptionally");
    CompletableFuture<Void> cfExceptionally = CompletableFuture.runAsync(task::throwException)
        .exceptionally(exception -> {
          log.info("error: " + exception.getMessage());
          return null;
        });
    cfExceptionally.join();
    System.out.println();

    // cancel
    log.info("cf cancel");
    CompletableFuture<Void> cfCancel = CompletableFuture.runAsync(task::runTask);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    cfCancel.cancel(true);
    log.info("cancel result = {}", cfCancel.isCancelled());
    System.out.println();

    // cancel not done CompletableFuture
    log.info("cf cancel not done CompletableFuture");
    CompletableFuture<String> cfCancelNotDone = new CompletableFuture<>();
    cfCancelNotDone.cancel(true);
    cfCancelNotDone.completeAsync(task::supplyTask);
    log.info("cancel not done CompletableFuture result = {}", cfCancelNotDone.isCancelled());
    System.out.println();

    // allOf
    log.info("cf allOf");
    CompletableFuture<Void> cfAllOf = CompletableFuture.allOf(
        CompletableFuture.runAsync(task::runTask),
        CompletableFuture.supplyAsync(task::supplyTask)
            .thenAccept(task::consumeTask),
        CompletableFuture.completedFuture("ok")
    );
    cfAllOf.join();
    System.out.println();

    // fixedThreadPoolExecutorService
    log.info("cf allOf with fixedThreadPoolExecutorService");
    CompletableFuture<Void> cfAllOfWithFixedThreadPool = CompletableFuture.allOf(
        CompletableFuture.runAsync(task::runTask, fixedThreadExecutorService),
        CompletableFuture.supplyAsync(task::supplyTask, fixedThreadExecutorService)
            .thenAcceptAsync(task::consumeTask, fixedThreadExecutorService),
        CompletableFuture.completedFuture("ok")
    );
    cfAllOfWithFixedThreadPool.join();
    fixedThreadExecutorService.shutdown();
    System.out.println();

    // cachedThreadPoolExecutorService
    log.info("cf allOf with cachedThreadPoolExecutorService");
    CompletableFuture<Void> cfAllOfWithCachedThreadPool = CompletableFuture.allOf(
        CompletableFuture.runAsync(task::runTask, cachedThreadExecutorService),
        CompletableFuture.supplyAsync(task::supplyTask, cachedThreadExecutorService)
            .thenAcceptAsync(task::consumeTask, cachedThreadExecutorService),
        CompletableFuture.completedFuture("ok")
    );
    cfAllOfWithCachedThreadPool.join();
    cachedThreadExecutorService.shutdown();
    System.out.println();

    // workStealingExecutorService
    log.info("cf allOf with workStealingExecutorService");
    CompletableFuture<Void> cfAllOfWithWorkStealing = CompletableFuture.allOf(
        CompletableFuture.runAsync(task::runTask, workStealingExecutorService),
        CompletableFuture.supplyAsync(task::supplyTask, workStealingExecutorService)
            .thenAcceptAsync(task::consumeTask, workStealingExecutorService),
        CompletableFuture.completedFuture("ok")
    );
    cfAllOfWithWorkStealing.join();
    workStealingExecutorService.shutdown();
    System.out.println();
  }
}
