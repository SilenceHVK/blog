package me.hvkcoder.java_basic.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;


/**
 * @author h_vk
 * @since 2022/12/1
 */
@Slf4j
public class FTPUtil extends FTPClient implements Closeable {

	/**
	 * 创建 FTP 连接
	 *
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 */
	public FTPUtil(String ip, int port, String username, String password) throws IOException {
		this.connect(ip, port);
		this.login(username, password);
		// 验证 FTP 连接是否成功
		int replyCode = this.getReplyCode();
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			this.disconnect();
			log.error("--ftp连接失败--");
			System.exit(1);
		}
		log.info("--ftp连接成功--");
		this.enterLocalPassiveMode();
	}

	/**
	 * 断开 FTP 连接
	 *
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		if (this.isConnected()) {
			log.info("--ftp连接已关闭--");
			this.logout();
			this.disconnect();
		}
	}


	/**
	 * 递归文件
	 *
	 * @param ftpFiles
	 * @param dirFunc
	 * @param fileFunc
	 * @throws IOException
	 */
	public void recursionFile(FTPFile[] ftpFiles, Consumer<String> dirFunc, Consumer<String> fileFunc) throws IOException {
		// 获取当前工作目录
		String workingDirectory = this.printWorkingDirectory();
		Arrays.stream(ftpFiles).forEach(ftpFile -> {
			try {
				String fileName = ftpFile.getName();
				if (ftpFile.isDirectory()) {
					File changeDir = new File(workingDirectory, fileName);
					dirFunc.accept(fileName);
					// 切换下级目录
					this.changeWorkingDirectory(changeDir.getAbsolutePath());
					recursionFile(this.listFiles(), dirFunc, fileFunc);
				} else {
					fileFunc.accept(fileName);
					// 返回上一级目录
					this.changeToParentDirectory();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * 递归文件
	 *
	 * @param dirFunc
	 * @param fileFunc
	 */
	public void recursionFile(Consumer<String> dirFunc, Consumer<String> fileFunc) throws IOException {
		recursionFile(this.listFiles(), dirFunc, fileFunc);
	}

	public static void main(String[] args) {
		try (FTPUtil ftpUtil = new FTPUtil("master.cluster.local", 21, "root", "123456")) {
			ftpUtil.recursionFile(System.out::println, System.out::println);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
