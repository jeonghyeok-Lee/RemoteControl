package com.java.slave.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JLabel;

// 서버에 값을 전달하는 스레드
public class SlaveWritingThread extends Thread {
	private Socket socket = null;
	private DataOutputStream dataOutStream = null;
	
	public SlaveWritingThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			// 슬레이브에서 마스터에게 값을 전달하기 위한 OutputStream
			InetAddress myNet = InetAddress.getLocalHost();
			String hostName =  myNet.getHostName().toString();
			dataOutStream = new DataOutputStream(socket.getOutputStream());
			
			// 값을 계속해서 전달하기 위한 반복문
			while(true) {
				dataOutStream.writeUTF(hostName); // 문자열의 형태로 값 전달
				dataOutStream.flush();
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
				if(dataOutStream != null) dataOutStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
