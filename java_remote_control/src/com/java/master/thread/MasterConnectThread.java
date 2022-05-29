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

	// ���� ����
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
			// �������� ������ �����ϱ� ���� while��
			while (true) {
				System.out.println("���� ���");
				// ��ư���� ó���� ��ư�� ������ ������ ����ϴµ��� ��� ���������Ƿ� ���⼭ ó��
				socket = server.accept(); 
				
				System.out.println("���� Ȯ��");
				
				// �����̺꿡�� ���� �� �б�
				dataInStream = new DataInputStream(socket.getInputStream());
				// �����̺꿡 �� ������
				dataOutStream = new DataOutputStream(socket.getOutputStream());
				
				// ��뿡�Լ� �Ѿ�� ���ڿ� ��, strValue�� ���ڿ��� ����
				String strValue = dataInStream.readUTF();
				inet = socket.getInetAddress();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(strValue);

				// �����̺꿡�� ���� �� ����
				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :" + myNet.getHostName().toString();
				dataOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
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
				System.out.println("���� �ݱ�");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
