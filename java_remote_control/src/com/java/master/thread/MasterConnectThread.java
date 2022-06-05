package com.java.master.thread;

import java.awt.AWTEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	// ���� ����
	public void run() {
		run2();

	}

	public void run2() {
		InetAddress myNet = null, inet = null; // ip/hostName���� �˾Ƴ��� ���� ����
		DataOutputStream dataOutStream = null; // ���� �����ϱ����� OutputStream
		DataInputStream dataInStream = null; // ���� �б� ���� InputStream
		ObjectOutputStream objectOutStream = null; // ���� ��ü�� �����ϱ� ���� ObjectOutputStream
		String str = ""; // ������ ���ڿ��� �����ϱ� ���� str����
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Screen screen = new Screen(); // ���� ���÷����� ũ�⸦ �ľ��ϱ����� ScreenŬ����
		MasterExecutionJFrame placeJframe = null;
		Point mouse = null;

		try {
			myNet = InetAddress.getLocalHost();
//			toolkit.addAWTEventListener(new Listener(), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);

			// �������� ������ �����ϱ� ���� while��
			while (true) {
				System.out.println("���� ���");
				// ��ư���� ó���� ��ư�� ������ ������ ����ϴµ��� ��� ���������Ƿ� ���⼭ ó��
				socket = server.accept();

				System.out.println("���� Ȯ��");

				// �����̺꿡�� ���� �� �б�
//				dataInStream = new DataInputStream(socket.getInputStream());
				// �����̺꿡 �� ������
				dataOutStream = new DataOutputStream(socket.getOutputStream());
				objectOutStream = new ObjectOutputStream(socket.getOutputStream());

				// ��뿡�Լ� �Ѿ�� ���ڿ� ��, strValue�� �Ѿ�� ���ڿ��� ����
//				String strValue = dataInStream.readUTF();
				// ���� ������ ip�� ������
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// �����̺꿡�� ���� �� ����
				str = "Connect IP : " + myNet.getHostAddress().toString() + "-ConnectHostName :"
						+ myNet.getHostName().toString();
				dataOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
				dataOutStream.flush();

				while (true) {

					mouse = MouseInfo.getPointerInfo().getLocation();
//					System.out.println("x :"+mouse.x + " checkPlace : "+ chkPlace);
					if (mouse.x == 0 && !chkPlace) {
						new Robot().mouseMove((int) screen.getWidth(), mouse.y);
						placeJframe = new MasterExecutionJFrame();
						chkPlace = true;
					} 
					
					// ��ü�� �� ����
					if (placeJframe != null && placeJframe.isKeyListen()) {
						objectOutStream.writeObject(placeJframe.getKey());
						objectOutStream.flush();
						placeJframe.setKeyListen(false);
					} else if (placeJframe != null && placeJframe.isMouseWheelListen()) {
						objectOutStream.writeObject(placeJframe.getMouseWheel());
						objectOutStream.flush();
						placeJframe.setMouseWheelListen(false);
					} else if (placeJframe != null && placeJframe.isMouseListen()) {
						objectOutStream.writeObject(placeJframe.getMouse());
						objectOutStream.flush();
						placeJframe.setMouseListen(false);
					} else {
						objectOutStream.writeObject(mouse);
						objectOutStream.flush();
					}
					sleep(1);

				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectOutStream != null)
					objectOutStream.close();
//				if (dataInStream != null) dataInStream.close();
				if (dataOutStream != null)
					dataOutStream.close();
				if (socket != null)
					socket.close();
				System.out.println("���� �ݱ�");
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
