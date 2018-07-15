package com.newer.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 
 * @author lfw
 *
 */
public class Client {

	// 从文件读取数据
	FileInputStream in;

	// 套接字
	Socket socket;

	

	OutputStream out;

	// 建一个哈希表，用来存放散列值
	Map<String, String> map = new HashMap<>();

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

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			in = new FileInputStream(file);

			byte[] buf = new byte[1024 * 4];
			int size = 0;

			while (-1 != (size = in.read(buf))) {
				byteArrayOut.write(buf, 0, size);

			}

			byte[] info = byteArrayOut.toByteArray();
			try {
				// 将文件信息用SHA-256编码
				byte[] hash = MessageDigest.getInstance("SHA-256").digest(info);
				String hashStr = new BigInteger(1, hash).toString(16);
				for (String mapKey : map.keySet()) {
					if (hashStr.equals(mapKey)) {
						System.err.println("文件已存在,不可重复上传!");
						return;
					}
				}
				map.put(hashStr, file.substring(file.lastIndexOf("\\") + 1));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			out.write(info);
			System.out.println("上传成功!");
		}  catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 从本地文件读取
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {

		Client client = new Client();
		client.start();
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(new File("E:\\files", "上传记录.txt"));
			String str = "";
			for (String mapKey : client.map.keySet()) {
				str = mapKey + " : " + client.map.get(mapKey) + "\n";
				try {
					fileOut.write(str.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
