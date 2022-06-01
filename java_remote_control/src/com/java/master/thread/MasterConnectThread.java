package com.java.master.thread;

import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.java.master.MasterExecutionJFrame;
import com.java.utility.Screen;

public class MasterConnectThread extends Thread {
	
	private JLabel labelIP = null, labelHost = null;
	private Socket socket = null;

	private ServerSocket server = null;
	
	private JFrame jframe = null;
	
	private boolean chkPlace = false;
	
	private int keycode = 0;
	private boolean keyListen = false;
	
	public MasterConnectThread() {
		// TODO Auto-generated constructor stub
	}
	
	public MasterConnectThread(ServerSocket server, JLabel labelIP, JLabel labelHost) {
		this.server = server;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public MasterConnectThread(Socket socket, JLabel labelIP, JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public MasterConnectThread(ServerSocket server, JLabel labelIP, JLabel labelHost, JFrame jframe) {
		this.server = server;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
		this.jframe = jframe;
	}

	// 지속 접속
	public void run() {
		run2();

	}

	public void run2() {
		InetAddress myNet = null, inet = null;
		DataOutputStream dataOutStream = null;
		DataInputStream dataInStream = null;
		ObjectOutputStream  objectOutStream = null;
		String str = "";
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Screen screen = new Screen();
		MasterExecutionJFrame placeJframe = null;

		try {
			myNet = InetAddress.getLocalHost();
//			toolkit.addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
			
			// 연결해제 연결을 지속하기 위한 while문
			while (true) {
				System.out.println("소켓 대기");
				// 버튼에서 처리시 버튼이 소켓이 수신을 대기하는동안 계속 눌려있으므로 여기서 처리
				socket = server.accept(); 
				
				System.out.println("연결 확인");
				
				// 슬레이브에서 보낸 값 읽기
				dataInStream = new DataInputStream(socket.getInputStream());
				// 슬레이브에 값 보내기
				dataOutStream = new DataOutputStream(socket.getOutputStream());			
				objectOutStream = new ObjectOutputStream(socket.getOutputStream());
				
				// 상대에게서 넘어온 문자열 즉, strValue을 문자열로 저장
				String strValue = dataInStream.readUTF();
				inet = socket.getInetAddress();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(strValue);

				// 슬레이브에게 보낼 값 정의
				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :" + myNet.getHostName().toString();
				dataOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				dataOutStream.flush();
				
				while (true) {
			
					
					// 객체로 값 전달
					Point mouse = MouseInfo.getPointerInfo().getLocation();
					if(mouse.x == 0 && !chkPlace) {
						mouse.setLocation(screen.getWidth(), (double)mouse.y);
						placeJframe = new MasterExecutionJFrame();
						chkPlace = true;
					}
					
					if(placeJframe != null && placeJframe.isKeyListen()) {
						objectOutStream.writeObject(placeJframe.getKeycode());
						objectOutStream.flush();	
						placeJframe.setKeyListen(false);
						sleep(1);
					}else {
						objectOutStream.writeObject(mouse);
						objectOutStream.flush();	
						sleep(1);
					}
					
				}
				
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectOutStream != null) objectOutStream.close();
				if (dataInStream != null) dataInStream.close();
				if (dataOutStream != null) dataOutStream.close();
				if (socket != null) socket.close();
				System.out.println("소켓 닫기");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
    private static class Listener implements AWTEventListener {
    	
        public void eventDispatched(AWTEvent event) {
            System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
            System.out.println(event);

        }

    }

}
