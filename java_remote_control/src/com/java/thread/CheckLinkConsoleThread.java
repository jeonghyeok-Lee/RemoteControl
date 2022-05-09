package com.java.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class CheckLinkConsoleThread extends Thread {
	Socket socket = null;
	
	public CheckLinkConsoleThread(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		BufferedReader reader = null;
		try {
			// ���Ͽ��� �Ѿ�� ����Ʈ������ �����͸� ������ ���ۿ� ����
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			InetAddress inet = socket.getInetAddress();
			System.out.println("Connet Client IP   : " + inet.getHostAddress());
			System.out.println("Connet Client Host : " + inet.getHostName());
//			System.out.println(new String(inet));
			
		}catch(IOException e) {
			System.out.println("����� ���� �߻� : "+e.getMessage());
		}finally {
			try {
				reader.close();
			}catch(Exception e) {
				e.getMessage();
			}
		}
	}
}
