package com.newer.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author lfw
 *
 */
public class Client {

	// 从文件读取数据
		FileInputStream fileIn;

		// 套接字
		Socket socket;

		OutputStream out;

		String serverAddress = "127.0.0.1";
		int serverPort = 9000;

		public void start() {

			//
			Scanner sc = new Scanner(System.in);
			System.out.printf("请输入要上传的文件： ");
			String file = sc.next();
			sc.close();

			// 建立连接
			try {
				socket = new Socket(serverAddress, serverPort);
				// 从套接字获得数据，发送数据
				out = socket.getOutputStream();

				byte[] buf = new byte[1024 * 4];
				int size;

				// 打开文件输入流，从文件读取数据
				fileIn = new FileInputStream(file);
				while (-1 != (size = fileIn.read(buf))) {
					out.write(buf, 0, size);
					out.flush();
				}
				System.out.println("发送成功");

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					// 从本地文件读取
					fileIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public static void main(String[] args) {
		
			Client client = new Client();
			client.start();
		}
	
	
}
