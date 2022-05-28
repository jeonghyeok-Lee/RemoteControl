package com.java.master.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;

public class MasterConnectThread extends Thread {
	private JLabel labelIP = null, labelHost = null;
	private Socket socket = null;
	private ServerSocket server = null;
	private String str = null;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public MasterConnectThread(ServerSocket server, JLabel labelIP, JLabel labelHost) {
		this.server = server;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	// 서버소켓이 아닌 소켓을 가져와서 처리 가능한 방법 알아보기
	public MasterConnectThread(Socket socket, JLabel labelIP, JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}

	// 지속 접속
	public void run() {
		run2();
	}
	
	public void run2() {
		InetAddress myNet = null, inet = null;
		DataOutputStream dataOutStream = null;
		DataInputStream dataInStream = null;

		try {
			myNet = InetAddress.getLocalHost();
			// 연결해제 연결을 지속하기 위한 while문
			while (true) {
				// 버튼에서 처리시 버튼이 소켓이 수신을 대기하는동안 계속 눌려있으므로 여기서 처리
				socket = server.accept();
				// 상대에게서 받는 문자열을 읽어들이기 위한 DataInputStream객체 생성
				dataInStream = new DataInputStream(socket.getInputStream());
				dataOutStream = new DataOutputStream(socket.getOutputStream());
				
				// 상대에게서 넘어온 문자열 즉, hostName을 문자열로 저장
				String hostName = dataInStream.readUTF();

				inet = socket.getInetAddress();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :" + myNet.getHostName().toString();
				
				dataOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				dataOutStream.flush();

				System.out.println("연결됨");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (dataOutStream != null) dataOutStream.close();
				if (socket != null) socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
