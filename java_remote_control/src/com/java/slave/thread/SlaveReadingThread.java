package com.java.slave.thread;

import java.awt.Point;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;

// 서버에서 오는 값을 수행하는 스레드
public class SlaveReadingThread extends Thread {
	private Socket socket = null;
	JLabel labelIP = null, labelHost = null;
	
	public SlaveReadingThread(Socket socket,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public void run() {
		DataInputStream dataInStream = null;
		ObjectInputStream objectInStream = null;
		Robot robot = null;	
		
		int com = 0;
		
		try {
			// 서버에서 값을 받아오기 위한 InputStream
			dataInStream = new DataInputStream(socket.getInputStream());
			objectInStream = new ObjectInputStream(socket.getInputStream());
			robot = new Robot();
			// 값을 계속해서 수행하기 위한 반복문
			while(true) {
				
				if(com == 0) {
					String str = dataInStream.readUTF();
					System.out.println(str);
					
					String[] strSplit = str.split("-");
					
					labelIP.setText(strSplit[0]);
					labelHost.setText(strSplit[1]);
					System.out.println("서버 연결 성공");
				}else {
					Point mouse = (Point)objectInStream.readObject();
					robot.mouseMove(mouse.x, mouse.y);
				}
			}
		}catch (UnknownHostException e) {
			System.out.println("IP가 잘못됨\n"+e.getMessage());
		}catch (IOException e) {
			System.out.println("IOException 발생\n"+e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("연결 해제됨");
			try {
				if(objectInStream != null) objectInStream.close();
				if(dataInStream != null) dataInStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
