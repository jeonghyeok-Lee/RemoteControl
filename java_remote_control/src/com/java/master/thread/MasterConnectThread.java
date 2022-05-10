package com.java.master.thread;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JLabel;

public class MasterConnectThread extends Thread {
	Socket socket = null;
	JLabel labelResult = null, labelIP = null, labelHost = null;
	
	public MasterConnectThread(Socket socket, JLabel labelResult,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelResult = labelResult;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public void run() {
		InetAddress myNet = null, inet;
		String str = null;
		DataOutputStream dataOutStream = null;
		try {
			labelResult.setText("����ڰ� �����Ͽ����ϴ�.");
			inet = socket.getInetAddress();
			labelIP.setText(inet.getHostAddress().toString());
			labelHost.setText( inet.getLocalHost().getHostName().toString());
			myNet = InetAddress.getLocalHost();
			str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :" + myNet.getHostName().toString();
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			dataOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
			dataOutStream.flush();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if(dataOutStream != null) dataOutStream.close();
				if(socket != null) socket.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
