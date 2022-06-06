package com.java.slave.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.java.slave.SlaveJFrame;

// ������ ���� �����ϴ� ������
public class SlaveWritingThread extends Thread {
	private Socket socket = null;
	private DataOutputStream dataOutStream = null;
	private boolean checkConnect = false;
	private SlaveJFrame myJFrame = null;
	
	public SlaveWritingThread(Socket socket, SlaveJFrame myJFrame) {
		this.socket = socket;
		this.myJFrame = myJFrame;
	}
	
	public void run() {
		try {
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			
			// ���� ��ȣ ����
			System.out.println("�����ȣ ����");		
			while(true) {
				if(myJFrame.isDisconnect()) {
					checkConnect = true;
				}
				dataOutStream.writeBoolean(checkConnect);
				dataOutStream.flush();
			}

			
		} catch (IOException e) {
			System.out.println("IOException �߻�-Read\n"+e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(dataOutStream != null) dataOutStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
