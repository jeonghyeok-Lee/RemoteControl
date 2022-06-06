package com.java.master.thread;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JLabel;

import com.java.master.MasterExecutionJFrame;
import com.java.master.MasterJFrame;
import com.java.utility.Screen;

public class MasterConnectThread extends Thread {

	private JLabel labelIP = null, labelHost = null;
	private ServerSocket server = null;
	private MasterJFrame myJFrame = null;
	private boolean chkPlace = false;		// 마스터의 활동영역이 열려있는지파악 true -> 활동영역 활성화 상태
	private Screen screen = new Screen(); 	// 현재 디스플레이의 크기를 파악하기위한 Screen클래스

	public MasterConnectThread(ServerSocket server, MasterJFrame myJFrame) {
		this.server = server;
		this.myJFrame = myJFrame;
		labelIP = myJFrame.getLabelIP();
		labelHost =  myJFrame.getLabelHost();
	}

	// 지속 접속
	public void run() {
		run2();
	}
	
	private void labelInit() {
		labelIP.setText("000.000.000.000");
		labelHost.setText("Unknown");
	}

	public void run2() {
		InetAddress myNet = null, inet = null; 		// ip/hostName등을 알아내기 위한 변수
		DataInputStream datatInStream = null;		// 값을 읽기 위한 InputStream
		ObjectOutputStream objectOutStream = null; 	// 값을 객체로 전달하기 위한 ObjectOutputStream
		String str = ""; 							// 전달할 문자열을 저장하기 위한 str변수
		MasterExecutionJFrame placeJframe = null; 	// 마스터가 활동할 영역인 프레임
		Point mouse = null;
		Socket socket = null;
		try {
			myNet = InetAddress.getLocalHost();

			// 연결해제 연결을 지속하기 위한 while문
			loopOut:
			while (true) {
				// 버튼에서 처리시 버튼이 소켓이 수신을 대기하는동안 계속 눌려있으므로 여기서 처리
				System.out.println("소켓 대기");
				socket = server.accept();
				System.out.println(socket);
				System.out.println("소켓 연결");
				
				// 슬레이브에 값 보내기 위해 스트림 생성
				objectOutStream = new ObjectOutputStream(socket.getOutputStream());
				
				// 현재 소켓의 ip를 가져와 IP/Host에 설정
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// 슬레이브에게 보낼 값 정의
				str = myNet.getHostAddress().toString() + "-" + myNet.getHostName().toString();
				System.out.println(str);
				objectOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				objectOutStream.flush();

				while (true) {
					System.out.println(socket.isConnected() + " " + socket.isClosed() + " " + !(socket.isConnected() && ! socket.isClosed()));
					if( !(socket.isConnected() && ! socket.isClosed())) {
						break;
					}
					
					mouse = MouseInfo.getPointerInfo().getLocation();

					if (mouse.x == 0 && !chkPlace) {
						new Robot().mouseMove((int) screen.getWidth(), mouse.y);
						placeJframe = new MasterExecutionJFrame();
						chkPlace = true;
					} 
					
					// 객체로 값 전달
					// placeJframe에서 리스너의 값이 true라면 해당 값을 전달
					if(placeJframe != null) {
						if(placeJframe.isKeyListen()) {
							objectOutStream.writeObject(placeJframe.getKey());
							objectOutStream.flush();
							placeJframe.setKeyListen(false);
						}else if(placeJframe.isMouseWheelListen()) {
							objectOutStream.writeObject(placeJframe.getMouseWheel());
							objectOutStream.flush();
							placeJframe.setMouseWheelListen(false);
						}else if(placeJframe.isMouseListen()) {
							objectOutStream.writeObject(placeJframe.getMouse());
							objectOutStream.flush();
							placeJframe.setMouseListen(false);
						}else {
							objectOutStream.writeObject(mouse);
							objectOutStream.flush();
						}
					}else {
						objectOutStream.writeObject(mouse);
						objectOutStream.flush();
					}
					
//					if (placeJframe != null && placeJframe.isKeyListen()) {
//						objectOutStream.writeObject(placeJframe.getKey());
//						objectOutStream.flush();
//						placeJframe.setKeyListen(false);
//					} else if (placeJframe != null && placeJframe.isMouseWheelListen()) {
//						objectOutStream.writeObject(placeJframe.getMouseWheel());
//						objectOutStream.flush();
//						placeJframe.setMouseWheelListen(false);
//					} else if (placeJframe != null && placeJframe.isMouseListen()) {
//						objectOutStream.writeObject(placeJframe.getMouse());
//						objectOutStream.flush();
//						placeJframe.setMouseListen(false);
//					}else {
//						objectOutStream.writeObject(mouse);
//						objectOutStream.flush();
//					}
					sleep(100);
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
//			objectOutStream = null;
//			socket = null;
		} catch (IOException e) {
			System.out.println("IOException 발생\n"+e.getMessage());
			labelInit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectOutStream != null) objectOutStream.close();
				if (socket != null) socket.close();
				labelInit();
				System.out.println("소켓 닫기");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
