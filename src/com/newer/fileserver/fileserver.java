
package com.newer.fileserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.corba.se.spi.orbutil.fsm.Input;

/**
 * 服务器：基于套接字 TCP 建立连接进行文件传输
 * 
 * @author lfw
 *
 */
public class fileserver {

	// 服务端
	ServerSocket serversocket;

	int port = 9000;

	// 线程池
	ExecutorService pool;
	
	String filePath = "E:/files";

	public void start() {

		try {
			serversocket = new ServerSocket(port);
			pool = Executors.newCachedThreadPool();

			while (true) {
				Socket socket = serversocket.accept();

				pool.execute(new Runnable() {

					@Override
					public void run() {

						ByteArrayOutputStream data = new ByteArrayOutputStream();

						try (InputStream in = socket.getInputStream();) {

							byte[] buf = new byte[1024 * 4];
							int size;
							while (-1 != (size = in.read(buf))) {
								data.write(buf, 0, size);
							}

						} catch (Exception e) {
						}

						// try {
						// InputStream in = socket.getInputStream();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }

						//接收到的数据
						byte[] info = data.toByteArray();
						String file = "";

						try {
							// 获得文件的散列值
							byte[] hash = MessageDigest.getInstance("SHA-256").digest(info);
							file = new BigInteger(1, hash).toString(16);
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}
						
						//
						try (FileOutputStream out = new FileOutputStream(new File(filePath,file))){
							out.write(info);
							System.out.println("上传完成");
						} catch (Exception e) {
							System.out.println("上传失败");
						}
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		fileserver server = new fileserver();
		server.start();
	}
}

