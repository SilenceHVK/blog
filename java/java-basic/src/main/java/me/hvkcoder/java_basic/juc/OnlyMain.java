package me.hvkcoder.java_basic.juc;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

/**
 * 获取 JVM 默认线程
 *
 * @author h_vk
 * @since 2022/1/10
 */
public class OnlyMain {
	public static void main(String[] args) {
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
		Arrays.stream(threadInfos).forEach(threadInfo -> System.out.println("[" + threadInfo.getThreadId() + "]：" + threadInfo.getThreadName()));
	}
}
