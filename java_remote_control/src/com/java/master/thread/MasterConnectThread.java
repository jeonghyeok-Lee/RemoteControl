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
	private boolean chkPlace = false;		// �������� Ȱ�������� �����ִ����ľ� true -> Ȱ������ Ȱ��ȭ ����
	private Screen screen = new Screen(); 	// ���� ���÷����� ũ�⸦ �ľ��ϱ����� ScreenŬ����
	private TimerClass timer = null;				// ����ð��� ����ϴ� Ŭ����
	private JLabel labelTimer = null; 				// ����ð��� ǥ���� ���̺�
	
	public MasterConnectThread(ServerSocket server, MasterJFrame myJFrame) {
		this.server = server;
		this.myJFrame = myJFrame;
		labelIP = myJFrame.getLabelIP();
		labelHost =  myJFrame.getLabelHost();
		labelTimer = myJFrame.getLabelTimer();
	}

	// ���� ����
	public void run() {
		run2();
	}
	
	// �ʱ�ȭ
	private void labelInit() {	
		labelIP.setText("000.000.000.000");
		labelHost.setText("Unknown");
		if(timer!=null) timer.getTimer().cancel();
		labelTimer.setText("00:00:00");
	}

	public void run2() {
		InetAddress myNet = null, inet = null; 		// ip/hostName���� �˾Ƴ��� ���� ����
		ObjectInputStream objectInStream = null;		// ���� �б� ���� InputStream
		ObjectOutputStream objectOutStream = null; 	// ���� ��ü�� �����ϱ� ���� ObjectOutputStream
		String str = ""; 							// ������ ���ڿ��� �����ϱ� ���� str����
		MasterExecutionJFrame placeJframe = null; 	// �����Ͱ� Ȱ���� ������ ������
		Point mouse = null;
		Socket socket = null;
		try {
			myNet = InetAddress.getLocalHost();

			// �������� ������ �����ϱ� ���� while��
			while (true) {
				// ��ư���� ó���� ��ư�� ������ ������ ����ϴµ��� ��� ���������Ƿ� ���⼭ ó��
				System.out.println("���� ���");
				socket = server.accept();
				System.out.println(socket);
				System.out.println("���� ����");
				timer = new TimerClass(labelTimer); // Ÿ�̸� ����
				
				// �����̺꿡 �� ������ ���� ��Ʈ�� ����
				objectOutStream = new ObjectOutputStream(socket.getOutputStream());
				
				objectInStream = new ObjectInputStream(socket.getInputStream());
				String data = (String)objectInStream.readObject();
				System.out.println(data);
				
				// ���� ������ ip�� ������ IP/Host�� ����
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// �����̺꿡�� ���� �� ����
				str = myNet.getHostAddress().toString() + "-" 
						+ myNet.getHostName().toString() + "-"
						+ screen.getWidth() + "-" 
						+ screen.getHeight();
				
				System.out.println(str);
				objectOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
				objectOutStream.flush();
				
				while (true) {
					try { // ������ ���� ���߿� EOFException �߻��� �ʱ�ȭ�� ���ְ� break �ɾ��־� �ٽ� accept
						data = (String)objectInStream.readObject();
					}catch(SocketException e) { // ����� ������ ���� ����Ǿ��� ���
						System.out.println("SocketException ���� �߻�");
					}catch(EOFException e) { // EOFException - ���� �����Ͱ� ���� �߻��ϴ� ���� ����� ��밡 ���� �����Ͽ� �����Ͱ� ������ �ȵɶ�
						System.out.println("EOFException ���� �߻�");
						labelInit();
						break;
					}

					mouse = MouseInfo.getPointerInfo().getLocation();
					
					// ���� ���콺�� ��ġ�� 0�̸鼭 chkPlace�� false�϶� == Ȱ������ ����
					// => chkPlace - false ==> ���� Ȱ�������� �������� ����
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
						// ��ü�� �� ����
						// placeJframe���� �������� ���� true��� �ش� ���� ����
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
					} catch (SocketException e) { // Ŭ���̾�Ʈ�� ���� �����Ͽ��� ��츦 ĳġ
						System.out.println("SocketException ���� �߻�");
						labelInit();
						break;
					}
					
					sleep(10); // ���� ���콺�� �� �ȸԴ´� ������ �ش� �κ��� �� ���߱�
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException �߻�\n"+e.getMessage());
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
				System.out.println("���� �ݱ�");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
