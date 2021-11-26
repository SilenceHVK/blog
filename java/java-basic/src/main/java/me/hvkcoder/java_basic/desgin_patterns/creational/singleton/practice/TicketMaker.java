package me.hvkcoder.java_basic.desgin_patterns.creational.singleton.practice;

/**
 * 习题 1 每次调用 getNextTicketNumber 方法都会返回 1000、1001、1002....的数列
 *
 * <p>使用双检锁的方式解决
 *
 * @author h-vk
 * @since 2020/5/30
 */
public class TicketMaker {
  private static volatile TicketMaker INSTANCE;
  private int ticket = 1000;

  private TicketMaker() {}

  public static TicketMaker getInstance() {
    if (INSTANCE == null) {
      synchronized (TicketMaker.class) {
        if (INSTANCE == null) INSTANCE = new TicketMaker();
      }
    }
    return INSTANCE;
  }

  public static void main(String[] args) {
    for (int i = 0; i < 100; i++) {
      new Thread(() -> System.out.println(TicketMaker.getInstance().getNextTicketNumber())).start();
    }
  }

  public int getNextTicketNumber() {
    return ticket++;
  }
}
