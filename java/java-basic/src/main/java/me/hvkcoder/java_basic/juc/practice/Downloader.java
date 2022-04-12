package me.hvkcoder.java_basic.juc.practice;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.Builder;
import lombok.Data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * 使用多线程实现下载器
 *
 * @author h_vk
 * @since 2021/8/22
 */
public class Downloader {
	/**
	 * 文件下载路径
	 */
	private static final String DOWNLOAD_PATH = "/Users/h_vk/Downloads/tmp/";

	/**
	 * 临时文件前缀
	 */
	private static final String TMP_PREFIX = ".tmp_";

	/**
	 * 设置文件数
	 */
	private static final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

	/**
	 * 下载进度调度
	 */
	private static final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

	/**
	 * 下载线程池
	 */
	private static final ThreadPoolExecutor downloadThreadPool = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM * 2, 0,
		TimeUnit.SECONDS,
		new ArrayBlockingQueue<>(10),
		ThreadFactoryBuilder.create().setNamePrefix("downloader-thread-").build()
	);

	/**
	 * 计数器
	 */
	private static final CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

	public static void main(String[] args) {

		try {
			// 获取文件下载链接
			String url = "";
			if (args.length == 0) {
				System.out.println("请输入下载链接");
				Scanner scanner = new Scanner(System.in);
				url = scanner.next();
			} else {
				url = args[0];
			}
			// 获取文件名称
			final String fileName = url.substring(url.lastIndexOf("/") + 1);
			// 获取下载文件大小
			final long fileSize = getFileSize(url);
			// 计算每个 chunk 的大小
			final long chunkSize = fileSize / THREAD_NUM;
			// 实例下载进度
			final DownloadProgress downloadProgress = DownloadProgress.builder().fileSize(fileSize).downloadedSize(new LongAdder()).build();
			scheduledThreadPool.scheduleAtFixedRate(downloadProgress, 1, 1, TimeUnit.SECONDS);
			for (int i = 0; i < THREAD_NUM; i++) {
				long startPos = chunkSize * i;
				long endPos = startPos + chunkSize;
				if (i == THREAD_NUM - 1) {
					endPos = 0;
				}
				if (i != 0) {
					startPos = startPos + 1;
				}
				final DownloadTask downloadTask = DownloadTask.builder()
					.url(url)
					.downloadProgress(downloadProgress)
					.fileName(fileName)
					.chunkIndex(i)
					.startPos(startPos)
					.endPos(endPos).build();
				downloadThreadPool.submit(downloadTask);
			}
			countDownLatch.await();

			// 合并文件，并删除文件
			if (merge(DOWNLOAD_PATH + fileName)) {
				deleteTmp(DOWNLOAD_PATH + fileName);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.print("\r");
			System.out.print("下载完成");
			scheduledThreadPool.shutdownNow();
			downloadThreadPool.shutdown();
		}
	}

	/**
	 * 获取 HttpURLConnection
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private static HttpURLConnection getHttpURLConnection(String url) throws IOException {
		final HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
		httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
		return httpURLConnection;
	}

	/**
	 * 获取文件大小
	 *
	 * @param url
	 * @return
	 */
	private static long getFileSize(String url) {
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = getHttpURLConnection(url);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
		return httpURLConnection.getContentLengthLong();
	}


	/**
	 * 合并 chunk 文件
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean merge(String fileName) {
		try (final RandomAccessFile accessFile = new RandomAccessFile(fileName, "rw")) {
			for (int i = 0; i < THREAD_NUM; i++) {
				try (final BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(fileName + TMP_PREFIX + i))) {
					final byte[] bytes = new byte[1024 * 100];
					int length = -1;
					while ((length = inputStream.read(bytes)) != -1) {
						accessFile.write(bytes, 0, length);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * 删除零时文件
	 *
	 * @param fileName
	 * @return
	 */
	public static boolean deleteTmp(String fileName) {
		for (int i = 0; i < THREAD_NUM; i++) {
			final File file = new File(fileName + ".tmp_" + i);
			if (file.exists()) {
				file.delete();
			}
		}
		return true;
	}


	/**
	 * 下载进度
	 */
	@Data
	@Builder
	private static class DownloadProgress implements Runnable {
		private final double KB = 1024d;
		private final double MB = 1024d * 1024d;

		/**
		 * 文件大小
		 */
		private long fileSize;

		/**
		 * 已下载文件大小
		 */
		private LongAdder downloadedSize;

		/**
		 * 上一次下载大小
		 */
		private double prevDownloadedSize;

		@Override
		public void run() {
			// 格式化文件大小与已下载大小
			final String fileSize = String.format("%.2f", this.fileSize / MB);
			final String downloadedSize = String.format("%.2f", this.downloadedSize.doubleValue() / MB);

			// 计算下载速度
			final int speed = (int) ((this.downloadedSize.doubleValue() - prevDownloadedSize) / KB);
			this.prevDownloadedSize = this.downloadedSize.doubleValue();

			// 计算剩余时间
			final double remainSize = this.fileSize - this.downloadedSize.doubleValue();
			final String remainTime = String.format("%.1f", remainSize / KB / speed);

			System.out.print("\r");
			System.out.printf("已下载：%sMB/%sMB，速度：%dkb/s，剩余时间：%ss", downloadedSize, fileSize, speed, remainTime);
		}
	}

	/**
	 * 下载任务
	 */
	@Data
	@Builder
	private static class DownloadTask implements Callable<Boolean> {
		/**
		 * 下载进度
		 */
		private DownloadProgress downloadProgress;

		/**
		 * 文件下载路径
		 */
		private String url;

		/**
		 * 文件名称
		 */
		private String fileName;

		/**
		 * chunk 开始位置
		 */
		private long startPos;

		/**
		 * chunk 结束位置
		 */
		private long endPos;

		/**
		 * chunk 索引
		 */
		private long chunkIndex;

		@Override
		public Boolean call() {
			HttpURLConnection httpURLConnection = null;
			try {
				httpURLConnection = getHttpURLConnection(url);
				httpURLConnection.setRequestProperty("RANGE", String.format("bytes=%d-", startPos) + (endPos == 0 ? "" : endPos));
				final File file = new File(DOWNLOAD_PATH + fileName + TMP_PREFIX + chunkIndex);
				// 判断 chunk 文件是否存在
				if (!file.exists()) {
					try (
						final BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
						final RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
					) {
						int length = -1;
						final byte[] bytes = new byte[1024 * 100];
						while ((length = inputStream.read(bytes)) != -1) {
							downloadProgress.getDownloadedSize().add(length);
							accessFile.write(bytes, 0, length);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				countDownLatch.countDown();
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}
			}
			return true;
		}
	}
}
