package com.java.test.thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JLabel;

public class ReceiverThread extends Thread {
	Socket socket;
	JLabel txtMsg;
	String userName;

	ReceiverThread(Socket socket, JLabel txtMsg, String userName) {
		this.socket = socket;
		this.txtMsg = txtMsg;
		this.userName = userName;
	}
	
	public ReceiverThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				String str = reader.readLine();
				if (str == null)
					break;
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
