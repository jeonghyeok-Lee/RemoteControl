package com.java.master.thread;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import javax.swing.JLabel;

public class MasterCoummunicationThread extends Thread implements Serializable {
	Socket socket = null;
	String str = null;
	int count = 0;
	JLabel lbMousePointer = null;
	
	public MasterCoummunicationThread(Socket socket) {
		this.socket = socket;
	}
	
	public MasterCoummunicationThread(Socket socket, JLabel lbMousePointer) {
		this.socket = socket;
		this.lbMousePointer = lbMousePointer;
	}
	
	PointerInfo mPointer = MouseInfo.getPointerInfo();
	
	public void run() {
		DataOutputStream dataOutStream = null;
		// ��ü�� �����ϱ� ���� ObjectOutputStream Ÿ���� ���� ����
		ObjectOutputStream objectOutStream = null;
		try {
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			// ����ȭ ���θ����� �ٽ� ����
//			objectOutStream = new ObjectOutputStream(socket.getOutputStream());
			// ���߿� �ֱ������� ������ ���� �ð����
//			Timer timer = new Timer();
//			TimerTask timerTask = new TimerTask() {
//
//				@Override
//				public void run() {
//					mPointer  = MouseInfo.getPointerInfo();
//					System.out.println(mPointer.getLocation());
//				}
//				
//			};
//			
//			timer.schedule(timerTask, 0,1000);
//			
//			objectOutStream.writeObject(mPointer);
//			objectOutStream.flush();
			
			mPointer = MouseInfo.getPointerInfo();
			str = mPointer.getLocation().x+","+mPointer.getLocation().y;
			
			System.out.println(str);
			dataOutStream.writeUTF(str);
			dataOutStream.flush();
//			
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
//				if(dataOutStream != null) dataOutStream.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
