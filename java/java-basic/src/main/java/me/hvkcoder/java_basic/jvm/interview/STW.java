package me.hvkcoder.java_basic.jvm.interview;

/**
 * @author h_vk
 * @since 2021/7/16
 */
public class STW {
  static long counter;

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Main start");
    startBusinessThread();
    startProblemThread();
    // 等待线程启动执行
    Thread.sleep(500);
    // 强制执行 GC, 所有的线程都将停止 STW（stop the word）
    System.gc();
    System.out.println("Main end");
  }

  public static void startBusinessThread() {
    new Thread(
            () -> {
              System.out.println("业务线程-1 start");
              for (; ; ) {
                System.out.println("执行业务1");
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            })
        .start();

    new Thread(
            () -> {
              System.out.println("业务线程-2 start");
              for (; ; ) {
                System.out.println("执行业务2");
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            })
        .start();
  }

  public static void startProblemThread() {
    new Thread(new MyRun()).start();
  }

  public static class MyRun implements Runnable {

    public void method() {
      Thread.currentThread().getId();
    }

    @Override
    public void run() {
      System.out.println("Problem start");
      // C1对可数循环的过度优化，需要等待该循环结束才会进入 线程安全点，因此大数循环建议使用 long
      for (int i = 0; i < 100000000; i++) {
        for (int j = 0; j < 1000; j++) {
          counter += i % 33;
          counter += i % 333;
        }
        // 线程安全点发生在方法调用前
        method();
      }
      System.out.println("Problem end");
    }
  }
}
