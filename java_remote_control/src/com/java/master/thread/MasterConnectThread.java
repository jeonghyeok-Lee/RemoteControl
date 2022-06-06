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
	private boolean chkPlace = false;		// �������� Ȱ�������� �����ִ����ľ� true -> Ȱ������ Ȱ��ȭ ����
	private Screen screen = new Screen(); 	// ���� ���÷����� ũ�⸦ �ľ��ϱ����� ScreenŬ����

	public MasterConnectThread(ServerSocket server, MasterJFrame myJFrame) {
		this.server = server;
		this.myJFrame = myJFrame;
		labelIP = myJFrame.getLabelIP();
		labelHost =  myJFrame.getLabelHost();
	}

	// ���� ����
	public void run() {
		run2();
	}
	
	private void labelInit() {
		labelIP.setText("000.000.000.000");
		labelHost.setText("Unknown");
	}

	public void run2() {
		InetAddress myNet = null, inet = null; 		// ip/hostName���� �˾Ƴ��� ���� ����
		DataInputStream datatInStream = null;		// ���� �б� ���� InputStream
		ObjectOutputStream objectOutStream = null; 	// ���� ��ü�� �����ϱ� ���� ObjectOutputStream
		String str = ""; 							// ������ ���ڿ��� �����ϱ� ���� str����
		MasterExecutionJFrame placeJframe = null; 	// �����Ͱ� Ȱ���� ������ ������
		Point mouse = null;
		Socket socket = null;
		try {
			myNet = InetAddress.getLocalHost();

			// �������� ������ �����ϱ� ���� while��
			loopOut:
			while (true) {
				// ��ư���� ó���� ��ư�� ������ ������ ����ϴµ��� ��� ���������Ƿ� ���⼭ ó��
				System.out.println("���� ���");
				socket = server.accept();
				System.out.println(socket);
				System.out.println("���� ����");
				
				// �����̺꿡 �� ������ ���� ��Ʈ�� ����
				objectOutStream = new ObjectOutputStream(socket.getOutputStream());
				
				// ���� ������ ip�� ������ IP/Host�� ����
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// �����̺꿡�� ���� �� ����
				str = myNet.getHostAddress().toString() + "-" + myNet.getHostName().toString();
				System.out.println(str);
				objectOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
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
					
					// ��ü�� �� ����
					// placeJframe���� �������� ���� true��� �ش� ���� ����
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
			System.out.println("IOException �߻�\n"+e.getMessage());
			labelInit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectOutStream != null) objectOutStream.close();
				if (socket != null) socket.close();
				labelInit();
				System.out.println("���� �ݱ�");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


}
