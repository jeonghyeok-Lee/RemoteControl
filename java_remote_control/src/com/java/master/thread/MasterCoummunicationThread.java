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
		// 객체를 전달하기 위한 ObjectOutputStream 타입의 변수 생성
		ObjectOutputStream objectOutStream = null;
		try {
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			// 직렬화 공부마저후 다시 적용
//			objectOutStream = new ObjectOutputStream(socket.getOutputStream());
			// 나중에 주기적으로 보내기 위한 시간계산
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
