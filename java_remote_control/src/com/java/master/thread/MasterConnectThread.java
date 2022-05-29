package com.java.master.thread;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
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
	private PointerInfo mPointer = null;

	public MasterConnectThread(ServerSocket server, JLabel labelIP, JLabel labelHost) {
		this.server = server;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
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
		String str = "";

		try {
			myNet = InetAddress.getLocalHost();
			// 연결해제 연결을 지속하기 위한 while문
			while (true) {
				System.out.println("소켓 대기");
				// 버튼에서 처리시 버튼이 소켓이 수신을 대기하는동안 계속 눌려있으므로 여기서 처리
				socket = server.accept(); 
				
				System.out.println("연결 확인");
				
				// 슬레이브에서 보낸 값 읽기
				dataInStream = new DataInputStream(socket.getInputStream());
				// 슬레이브에 값 보내기
				dataOutStream = new DataOutputStream(socket.getOutputStream());
				
				// 상대에게서 넘어온 문자열 즉, strValue을 문자열로 저장
				String strValue = dataInStream.readUTF();
				inet = socket.getInetAddress();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(strValue);

				// 슬레이브에게 보낼 값 정의
				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :" + myNet.getHostName().toString();
				dataOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				dataOutStream.flush();
				
				while (true) {

					mPointer = MouseInfo.getPointerInfo();
					str = mPointer.getLocation().x + "-" + mPointer.getLocation().y;
					
					sleep(1);
					
					System.out.println(str);
					dataOutStream.writeUTF(str);
					dataOutStream.flush();
				}
				

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (dataInStream != null) dataInStream.close();
				if (dataOutStream != null) dataOutStream.close();
				if (socket != null) socket.close();
				System.out.println("소켓 닫기");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
