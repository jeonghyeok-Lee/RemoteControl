package com.java.slave.thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;

// �������� ���� ���� �����ϴ� ������
public class SlaveReadingThread extends Thread {
	private Socket socket = null;
	JLabel labelIP = null, labelHost = null;
	DataInputStream dataInStream = null;
	
	public SlaveReadingThread(Socket socket,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public void run() {
		try {
			// �������� ���� �޾ƿ��� ���� InputStream
			dataInStream = new DataInputStream(socket.getInputStream());
			
			// ���� ����ؼ� �����ϱ� ���� �ݺ���
			while(true) {
				
				String str = dataInStream.readUTF();
				System.out.println(str);
				
				String[] strSplit = str.split("-");
				
				labelIP.setText(strSplit[0]);
				labelHost.setText(strSplit[1]);
				System.out.println("���� ���� ����");
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
				if(dataInStream != null) dataInStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
