package com.java.master.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JLabel;

public class MasterConnectThread extends Thread {
	// 다중접속을 위함
//	static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<PrintWriter>());

	Socket socket = null;
	JLabel labelResult = null, labelIP = null, labelHost = null;
	boolean check = true;

	public MasterConnectThread(Socket socket, JLabel labelResult, JLabel labelIP, JLabel labelHost) {
		this.socket = socket;
		this.labelResult = labelResult;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}

	public MasterConnectThread(Socket socket, JLabel labelResult, JLabel labelIP, JLabel labelHost, boolean check) {
		this.socket = socket;
		this.labelResult = labelResult;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
		this.check = check;
	}

	public void run() {
		InetAddress myNet = null, inet = null;
		String str = null;
		DataOutputStream dataOutStream = null;
		DataInputStream dataInStream = null;

		try {
			if (check) {
				// 추후 제거
				labelResult.setText("사용자가 접속하였습니다.");

				// 상대에게서 받는 문자열을 읽어들이기 위한 DataInputStream객체 생성
				dataInStream = new DataInputStream(socket.getInputStream());
				// 상대에게서 넘어온 문자열 즉, hostName을 문자열로 저장
				String hostName = dataInStream.readUTF();

				inet = socket.getInetAddress();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				myNet = InetAddress.getLocalHost();
				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :"
						+ myNet.getHostName().toString();
				dataOutStream = new DataOutputStream(socket.getOutputStream());
				dataOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				dataOutStream.flush();

			} else {
				labelResult.setText("연결이 해제되었습니다.");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
//				if (dataOutStream != null) dataOutStream.close();
//				if (socket != null) socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
