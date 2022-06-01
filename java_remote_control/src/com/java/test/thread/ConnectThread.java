package com.java.test.thread;

import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JLabel;

public class ConnectThread extends Thread {
	Socket socket = null;
	JLabel labelIp = null;
	JLabel labelHost = null;
	JLabel labelResult = null;
	
	public ConnectThread(Socket socket, JLabel labelIp, JLabel labelHost, JLabel labelResult) {
		this.socket = socket;
		this.labelIp = labelIp;
		this.labelHost = labelHost;
		this.labelResult = labelResult;
	}
	public void run() {
		try {
			labelResult.setText("사용자가 접속하였습니다.");
			InetAddress inet = socket.getInetAddress();
			labelIp.setText(inet.getHostAddress());
			labelHost.setText(inet.getHostName());
		}catch(Exception e) {
			e.getMessage();
		}
	}
}
