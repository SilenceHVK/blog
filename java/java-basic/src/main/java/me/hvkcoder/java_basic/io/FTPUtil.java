package me.hvkcoder.java_basic.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.Closeable;
import java.io.IOException;


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
			log.info("关闭.....");
			this.logout();
			this.disconnect();
		}
	}
	
	public static void main(String[] args) {
		try (FTPUtil ftpUtil = new FTPUtil("master.cluster.local", 21, "root", "123456")) {
			FTPFile[] ftpFiles = ftpUtil.listFiles();
			System.out.println(ftpFiles.length);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
