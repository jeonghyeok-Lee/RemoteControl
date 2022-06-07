package com.java.master.thread;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JLabel;

import com.java.master.MasterExecutionJFrame;
import com.java.master.MasterJFrame;
import com.java.utility.Screen;
import com.java.utility.TimerClass;

public class MasterConnectThread extends Thread {

	private JLabel labelIP = null, labelHost = null;
	private ServerSocket server = null;
	private MasterJFrame myJFrame = null;
	private boolean chkPlace = false;		// 마스터의 활동영역이 열려있는지파악 true -> 활동영역 활성화 상태
	private Screen screen = new Screen(); 	// 현재 디스플레이의 크기를 파악하기위한 Screen클래스
	private TimerClass timer = null;				// 연결시간을 계산하는 클래스
	private JLabel labelTimer = null; 				// 연결시간을 표시할 레이블
	
	public MasterConnectThread(ServerSocket server, MasterJFrame myJFrame) {
		this.server = server;
		this.myJFrame = myJFrame;
		labelIP = myJFrame.getLabelIP();
		labelHost =  myJFrame.getLabelHost();
		labelTimer = myJFrame.getLabelTimer();
	}

	// 지속 접속
	public void run() {
		run2();
	}
	
	// 초기화
	private void labelInit() {	
		labelIP.setText("000.000.000.000");
		labelHost.setText("Unknown");
		if(timer!=null) timer.getTimer().cancel();
		labelTimer.setText("00:00:00");
	}

	public void run2() {
		InetAddress myNet = null, inet = null; 		// ip/hostName등을 알아내기 위한 변수
		ObjectInputStream objectInStream = null;		// 값을 읽기 위한 InputStream
		ObjectOutputStream objectOutStream = null; 	// 값을 객체로 전달하기 위한 ObjectOutputStream
		String str = ""; 							// 전달할 문자열을 저장하기 위한 str변수
		MasterExecutionJFrame placeJframe = null; 	// 마스터가 활동할 영역인 프레임
		Point mouse = null;
		Socket socket = null;
		try {
			myNet = InetAddress.getLocalHost();

			// 연결해제 연결을 지속하기 위한 while문
			while (true) {
				// 버튼에서 처리시 버튼이 소켓이 수신을 대기하는동안 계속 눌려있으므로 여기서 처리
				System.out.println("소켓 대기");
				socket = server.accept();
				System.out.println(socket);
				System.out.println("소켓 연결");
				timer = new TimerClass(labelTimer); // 타이머 진행
				
				// 슬레이브에 값 보내기 위해 스트림 생성
				objectOutStream = new ObjectOutputStream(socket.getOutputStream());
				
				objectInStream = new ObjectInputStream(socket.getInputStream());
				String data = (String)objectInStream.readObject();
				System.out.println(data);
				
				// 현재 소켓의 ip를 가져와 IP/Host에 설정
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// 슬레이브에게 보낼 값 정의
				str = myNet.getHostAddress().toString() + "-" 
						+ myNet.getHostName().toString() + "-"
						+ screen.getWidth() + "-" 
						+ screen.getHeight();
				
				System.out.println(str);
				objectOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				objectOutStream.flush();
				
				while (true) {
					try { // 데이터 수신 도중에 EOFException 발생시 초기화를 해주고 break 걸어주어 다시 accept
						data = (String)objectInStream.readObject();
					}catch(SocketException e) { // 연결된 소켓이 강제 종료되었을 경우
						System.out.println("SocketException 예외 발생");
					}catch(EOFException e) { // EOFException - 읽을 데이터가 없어 발생하는 문제 현재는 상대가 직접 종료하여 데이터가 전송이 안될때
						System.out.println("EOFException 예외 발생");
						labelInit();
						break;
					}

					mouse = MouseInfo.getPointerInfo().getLocation();
					
					// 현재 마우스의 위치가 0이면서 chkPlace가 false일때 == 활동영역 생성
					// => chkPlace - false ==> 현재 활동영역이 생성되지 않음
					if (mouse.x == 0 && !chkPlace) {	
						new Robot().mouseMove((int) screen.getWidth(), mouse.y);
						placeJframe = new MasterExecutionJFrame();
						chkPlace = true;
					} 
					
					if(placeJframe != null &&placeJframe.isFrameState()) {
						placeJframe = null;
						chkPlace = false;
					}
					try {
						// 객체로 값 전달
						// placeJframe에서 리스너의 값이 true라면 해당 값을 전달
						if (placeJframe != null) {
							if (placeJframe.isKeyListen()) {
								objectOutStream.writeObject(placeJframe.getKey());
								objectOutStream.flush();
								placeJframe.setKeyListen(false);
							} else if (placeJframe.isMouseWheelListen()) {
								objectOutStream.writeObject(placeJframe.getMouseWheel());
								objectOutStream.flush();
								placeJframe.setMouseWheelListen(false);
							} else if (placeJframe.isMouseListen()) {
								objectOutStream.writeObject(placeJframe.getMouse());
								objectOutStream.flush();
								placeJframe.setMouseListen(false);
							} else {
								objectOutStream.writeObject(mouse);
								objectOutStream.flush();
							}
						} else {
							objectOutStream.writeObject(mouse);
							objectOutStream.flush();

						}
					} catch (SocketException e) { // 클라이언트가 강제 종료하였을 경우를 캐치
						System.out.println("SocketException 예외 발생");
						labelInit();
						break;
					}
					
					sleep(10); // 만약 마우스가 잘 안먹는다 싶으면 해당 부분을 더 낮추기
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException 발생\n"+e.getMessage());
			labelInit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectInStream != null) objectInStream.close();
				if (objectOutStream != null) objectOutStream.close();
				if (socket != null) socket.close();
				if(placeJframe != null) placeJframe.dispose();
				labelInit();
				System.out.println("소켓 닫기");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
