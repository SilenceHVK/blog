package me.hvkcoder.java_basic.juc.aqs;

import lombok.extern.slf4j.Slf4j;

/**
 * JDK1.5 新增加在的 ReentrantLock 可重入锁（也叫 递归锁）
 *
 * <p>重入锁 指的是在同一个线程中，多个流程能不能获取同一把锁，在 Java 中所有的锁都是重入锁 ReentrantLock 默认实例的是非公平锁，
 * 非公平锁指的是线程先尝试插队，插队失败再排队 通过 ReentrantLock(true) 实例的是公平锁，公平锁指的是线程必须排队执行
 *
 * <p>提供 Condition 类，可以分组唤醒需要唤醒的线程
 *
 * <p>提供能够中断等待锁的线程的机制， lock.lockInterruptibly()
 *
 * @author h-vk
 * @since 2020/11/18
 */
@Slf4j
public class AQS04_ReentrantLock {

}
