package com.java.slave.thread;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.java.slave.SlaveJFrame;
import com.java.utility.Screen;

// �������� ���� ���� �����ϴ� ������
public class SlaveReadingThread extends Thread {
	private Socket socket = null;
	private JLabel labelIP = null, labelHost = null;
	private JPanel content = null;
	private CardLayout card = null;			// �����̺������ӿ��� ����ϴ� ���̾ƿ��� �����ϱ� ���� ����
	private JButton button = null;			// �����̺��� ��ư�� �����ϱ� ���� ����
	private boolean firstConnect = false;
	private boolean checkStart = false; 	// �������� Ȱ�������� ����Ǿ��� ��� true
	private double masterWidth = 0.0f; 		// �������� ����� ���α���
	private double masterHeight = 0.0f; 	// �������� ����� ���� ����
	private Screen screen = new Screen();
	private double myWidth = 0.0f;
	private double myHeight = 0.0f;
	private double xRatio = 0.0f; 			// ������ ��� �����̺��� ȭ�� x�����
	private double yRatio = 0.0f;			// ������ ��� �����̺��� ȭ�� y�����

	private SlaveJFrame jframe = null;

	// ������ (�������, ��µ� ip/host���̺�, ���ῡ���� ȭ����ȯ�� ���� card�� content)
	public SlaveReadingThread(Socket socket, JLabel labelIP, JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;

	}

	public SlaveReadingThread(Socket socket, SlaveJFrame jframe) {
		this.socket = socket;
		this.jframe = jframe;
		labelIP = jframe.getLabelIP();
		labelHost = jframe.getLabelHost();
		card = jframe.getCard();
		content = jframe.getCenterContent();
		button = jframe.getButton();
		button.setText("��������");
	}

	public void run() {
		ObjectInputStream objectInStream = null;
		Robot robot = null;

		try {
			// �������� ���� �޾ƿ��� ���� InputStream
			objectInStream = new ObjectInputStream(socket.getInputStream());
			robot = new Robot();
			System.out.println("1��");
			// ���� ����ؼ� �����ϱ� ���� �ݺ���
			while (true) {
				if (!firstConnect) { // ó�� �����ϴ� ���� ���
					System.out.println("����");
					String str = objectInStream.readUTF();
					System.out.println(str);

					String[] strSplit = str.split("-");

					labelIP.setText(strSplit[0]);
					labelHost.setText(strSplit[1]);
					masterWidth = Double.parseDouble(strSplit[2]);
					masterHeight = Double.parseDouble(strSplit[3]);
					System.out.println("masterWidth : " + masterWidth + " masterHeight " + masterHeight);
					System.out.println("masterWidth - 10 : " + (masterWidth - 10));
					myWidth = screen.getWidth();
					myHeight = screen.getHeight();
					System.out.println("mywidth : " + myWidth + " myheight " + myHeight);

					xRatio = myWidth / masterWidth; // ���� ���
					yRatio = myHeight / masterHeight;
					System.out.println("xRatio : " + xRatio + " yRatio " + yRatio);

					System.out.println("���� ���� ����");
					firstConnect = true;
				} else { // ���� �޾� ����
					Object value = objectInStream.readObject();
					// System.out.println(value.getClass().getSimpleName());
					if (value instanceof Point) {
						Point mouse = (Point) value;
						if (mouse.x == 0)
							checkStart = true;
						if (checkStart) { // �������� Ȱ�������� Ȱ��ȭ �Ǿ��ִٸ� ���콺�� �����Ϳ��� ����Ѵ�� ����
							int x = 0;
							int y = 0;
							if (mouse.x == 0)
								x = 0;
							else
								x = (int) ((double) mouse.x * xRatio);
							if (mouse.y == 0)
								y = 0;
							else
								y = (int) ((double) mouse.y * yRatio);

							if (x >= (int) myWidth - 10)
								checkStart = false; // ������ ������ �̵���
							System.out.println(checkStart);

							// System.out.println("x "+ x +" y "+y);
							// System.out.println("mouse.x "+ mouse.x +" y "+ mouse.y);
							robot.mouseMove(x, y);
						} else {
							// System.out.println("mouse.x "+ mouse.x +" y "+ mouse.y);
						}
					} else if (value instanceof KeyEvent) {
						KeyEvent key = (KeyEvent) value;
						robot.keyPress(key.getKeyCode());
						System.out.println("keycode : " + key.getKeyCode());
					} else if (value instanceof MouseWheelEvent) {
						MouseWheelEvent e = (MouseWheelEvent) value;
						robot.mouseWheel(e.getWheelRotation());
						System.out.println("���̵�");
					} else if (value instanceof MouseEvent) {
						MouseEvent e = (MouseEvent) value;
						robot.mousePress(e.getMaskForButton(e.getButton()));
						robot.mouseRelease(e.getMaskForButton(e.getButton()));
						System.out.println("���콺 Ŭ��");
					}
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("IP�� �߸���\n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException �߻�-Read\n" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (objectInStream != null)
					objectInStream.close();
				if (socket != null)
					socket.close();
				labelIP.setText("000.000.000.000");
				labelHost.setText("0000");
				card.show(content, "disconnect");
				button.setText("����");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
