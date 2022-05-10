 package com.java.master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.java.thread.CheckLinkConsoleThread;

public class CheckLinkConsole {
	private final int port = 9095;
	
	public void checkLink() {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(port);
			System.out.println("서버 접속 대기중");
			Socket socket = serverSocket.accept();
			System.out.println("클라이언트 접속 진행 중");
			Thread checkThread = new CheckLinkConsoleThread(socket);
			checkThread.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
