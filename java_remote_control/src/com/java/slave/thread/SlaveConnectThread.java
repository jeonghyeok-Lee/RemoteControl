package com.java.slave.thread;

import java.io.Console;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JLabel;

public class SlaveConnectThread extends Thread {
	Socket socket = null;
	JLabel labelResult = null, labelIP = null, labelHost = null;
	
	public SlaveConnectThread(Socket socket,JLabel labelResult,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelResult = labelResult;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public void run() {
		DataInputStream dataInStream = null;
		try {
			labelResult.setText("서버 연결 중");
			dataInStream = new DataInputStream(socket.getInputStream());
			String str = dataInStream.readUTF();
			String[] strSplit = str.split("-");
			labelResult.setText("접속확인");
			labelIP.setText(strSplit[0]);
			labelHost.setText(strSplit[1]);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(dataInStream != null) dataInStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}

	}

}
