package com.java.slave.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
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
		DataOutputStream dataOutStream = null;
		InetAddress myNet = null;
		try {
			
			labelResult.setText("서버 연결 중");
			
			myNet = InetAddress.getLocalHost();
			String hostName =  myNet.getHostName().toString();
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			dataOutStream.writeUTF(hostName); // 문자열의 형태로 값 전달
			dataOutStream.flush();
			
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
