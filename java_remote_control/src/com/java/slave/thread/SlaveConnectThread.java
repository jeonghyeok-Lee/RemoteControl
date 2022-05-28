package com.java.slave.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JLabel;

public class SlaveConnectThread extends Thread {
	Socket socket = null;
	JLabel labelIP = null, labelHost = null;
	
	public SlaveConnectThread(Socket socket,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public void run() {
		DataInputStream dataInStream = null;
		DataOutputStream dataOutStream = null;
		InetAddress myNet = null;
		try {
			myNet = InetAddress.getLocalHost();
			String hostName =  myNet.getHostName().toString();
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			
			dataInStream = new DataInputStream(socket.getInputStream());
			while(true) {
				
				System.out.println("서버 연결 중");
				
				dataOutStream.writeUTF(hostName); // 문자열의 형태로 값 전달
				dataOutStream.flush();
				
				String str = dataInStream.readUTF();
				System.out.println(str);
				
				
				String[] strSplit = str.split("-");
				
				labelIP.setText(strSplit[0]);
				labelHost.setText(strSplit[1]);
				System.out.println("서버 연결 성공");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("연결 해제됨");
			try {
				if(dataInStream != null) dataInStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	}

	}

}
