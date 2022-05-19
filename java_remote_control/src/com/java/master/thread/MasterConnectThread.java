package com.java.master.thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JLabel;

public class MasterConnectThread extends Thread {
	// ���������� ����
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
				// ���� ����
				labelResult.setText("����ڰ� �����Ͽ����ϴ�.");

				// ��뿡�Լ� �޴� ���ڿ��� �о���̱� ���� DataInputStream��ü ����
				dataInStream = new DataInputStream(socket.getInputStream());
				// ��뿡�Լ� �Ѿ�� ���ڿ� ��, hostName�� ���ڿ��� ����
				String hostName = dataInStream.readUTF();

				inet = socket.getInetAddress();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				myNet = InetAddress.getLocalHost();
				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :"
						+ myNet.getHostName().toString();
				dataOutStream = new DataOutputStream(socket.getOutputStream());
				dataOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
				dataOutStream.flush();

			} else {
				labelResult.setText("������ �����Ǿ����ϴ�.");
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
