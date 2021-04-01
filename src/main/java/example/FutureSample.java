package example;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureSample {

  private static final ExecutorService fixedThreadExecutorService = Executors.newFixedThreadPool(2);
  private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
  private static final ExecutorService cachedThreadExecutorService = Executors
      .newCachedThreadPool();
  private static final ExecutorService workStealingExecutorService = Executors
      .newWorkStealingPool(2);

  void future() throws ExecutionException, InterruptedException {
    Task task = new Task();

    // submit fixedThreadExecutorService
    log.info("future submit fixedThreadExecutorService");
    Future<String> future1 = fixedThreadExecutorService.submit(() -> task.supplyTask());
    String message = future1.get();
    log.info(message);
    System.out.println();

    // cancel mayInterruptIfRunning = false
    // submit fixedThreadExecutorService
    log.info("future cancel(false) fixedThreadExecutorService");
    Future<String> future2 = fixedThreadExecutorService.submit(() -> task.supplyTask());
    boolean cancelResult = future2.cancel(false);
    log.info("cancel result = {}", cancelResult);
    System.out.println();

    // cancel mayInterruptIfRunning = true
    // submit fixedThreadExecutorService
    log.info("future submit(true) fixedThreadExecutorService");
    Future<String> future3 = fixedThreadExecutorService.submit(() -> task.supplyTask());
    boolean result = future3.cancel(true);
    log.info("cancel result = {}", result);
    System.out.println();

    // future execute
    log.info("future execute");
    singleThreadExecutor.execute(task::runTask);
    System.out.println();

    // invokeAll
    log.info("future invokeAll fixedThreadExecutorService");
    List<Future<String>> invokeAllResult = fixedThreadExecutorService.invokeAll(List.of(() -> task.supplyTask(5000), task::supplyTask));
    for (Future<String> r : invokeAllResult) {
      log.info("invokeAll result = {}", r.get());
    }
    System.out.println();

    // invokeAny
    log.info("future invokeAny fixedThreadExecutorService");
    String invokeAnyResult = fixedThreadExecutorService.invokeAny(List.of(() -> task.supplyTask(5000), task::supplyTask));
    log.info("invokeAny result = {}", invokeAnyResult);
    System.out.println();

    // future shutdown
//    log.info("future shutdown");
//    fixedThreadExecutorService.execute(task::runTask); // 正常に処理される
//    fixedThreadExecutorService.shutdown();
//    fixedThreadExecutorService.execute(task::supplyTask); // エラー(RejectExecutionException)
//    System.out.println();

    // future shutdownNow
//    log.info("future shutdownNow");
//    fixedThreadExecutorService.execute(task::runTask);
//    fixedThreadExecutorService.shutdownNow();
//    fixedThreadExecutorService.execute(task::supplyTask);
//    System.out.println();

    // future shutdownNow
//    log.info("future shutdownNow");
//    fixedThreadExecutorService.execute(task::runTask); // shutdownNowが呼ばれたら即座に処理が終了（今回はThread.sleep中に終了するのでsleepの割り込みが発生したとしてInterruptedExceptionが発生する）
//    fixedThreadExecutorService.shutdownNow();
//    fixedThreadExecutorService.execute(task::supplyTask); // エラー(RejectExecutionException)
//    System.out.println();

    // future awaitTermination
    log.info("future awaitTermination");
    fixedThreadExecutorService.execute(task::runTask);
    fixedThreadExecutorService.shutdown();
    boolean isTerminated = fixedThreadExecutorService.awaitTermination(5000, TimeUnit.SECONDS);
    log.info("" + isTerminated);
    System.out.println();

    fixedThreadExecutorService.shutdown();
    singleThreadExecutor.shutdown();
  }
}
