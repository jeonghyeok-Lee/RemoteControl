package com.java.slave.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.java.slave.SlaveJFrame;

// 서버에 값을 전달하는 스레드
public class SlaveWritingThread extends Thread {
	private Socket socket = null;
	private ObjectOutputStream objectOutStream = null;
	private SlaveJFrame myJFrame = null;
	private int userNo = 0;
	private String userIp = "";
	private boolean nonLogin = false;
	private boolean end = false;		// 종료여부
	
	public void setEnd(boolean end) {
		this.end = end;
	}

	public SlaveWritingThread(Socket socket, int userNo) {
		this.socket = socket;
		this.userNo = userNo;
	}
	
	public SlaveWritingThread(Socket socket, String userIp) {
		this.socket = socket;
		this.userIp = userIp;
		nonLogin = true;
	}
	
	public void run() {
		try {
			System.out.println("writing start");
			objectOutStream = new ObjectOutputStream(socket.getOutputStream());
			String str ="";// 전송할 값
			while(true) {
				if(!end) {
					if(!nonLogin) { // 회원이고 종료가 되지 않았다면
						str = userNo + "";
					}else {
						str = userIp;
					}
					objectOutStream.writeObject(str);
					objectOutStream.flush();
				}else {
					str = "end";
					objectOutStream.writeObject(str);
					objectOutStream.flush();
					break;
				}
				sleep(10);
			}

			System.out.println("writing end");
			
		} catch (IOException e) {
			System.out.println("IOException 발생-write\n"+e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

}
