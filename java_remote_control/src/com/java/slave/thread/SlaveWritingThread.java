package com.java.slave.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;

// ������ ���� �����ϴ� ������
public class SlaveWritingThread extends Thread {
	private Socket socket = null;
	private DataOutputStream dataOutStream = null;
	
	public SlaveWritingThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			// �����̺꿡�� �����Ϳ��� ���� �����ϱ� ���� OutputStream
			InetAddress myNet = InetAddress.getLocalHost();
			String hostName =  myNet.getHostName().toString();
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			
			// ���� ����ؼ� �����ϱ� ���� �ݺ���
			while(true) {
				dataOutStream.writeUTF(hostName); // ���ڿ��� ���·� �� ����
				dataOutStream.flush();
			}
			
			
		}catch (UnknownHostException e) {
			System.out.println("IP�� �߸���\n"+e.getMessage());
		}catch (IOException e) {
			System.out.println("IOException �߻�\n"+e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("���� ������");
			try {
				if(dataOutStream != null) dataOutStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
