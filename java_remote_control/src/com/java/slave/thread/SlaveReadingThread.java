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

// 서버에서 오는 값을 수행하는 스레드
public class SlaveReadingThread extends Thread {
	private Socket socket = null;
	private JLabel labelIP = null, labelHost = null;
	private JPanel content = null;
	private CardLayout card = null;			// 슬레이브프레임에서 사용하는 레이아웃을 제어하기 위한 변수
	private JButton button = null;			// 슬레이브의 버튼을 제어하기 위한 변수
	private boolean firstConnect = false;
	private boolean checkStart = false; 	// 마스터의 활동영역이 연결되었을 경우 true
	private double masterWidth = 0.0f; 		// 마스터의 모니터 가로길이
	private double masterHeight = 0.0f; 	// 마스터의 모니터 세로 길이
	private Screen screen = new Screen();
	private double myWidth = 0.0f;
	private double myHeight = 0.0f;
	private double xRatio = 0.0f; 			// 마스터 대비 슬레이브의 화면 x축비율
	private double yRatio = 0.0f;			// 마스터 대비 슬레이브의 화면 y축비율

	private SlaveJFrame jframe = null;

	// 생성자 (연결소켓, 출력될 ip/host레이블, 연결에따른 화면전환을 위한 card와 content)
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
		button.setText("연결해제");
	}

	public void run() {
		ObjectInputStream objectInStream = null;
		Robot robot = null;

		try {
			// 서버에서 값을 받아오기 위한 InputStream
			objectInStream = new ObjectInputStream(socket.getInputStream());
			robot = new Robot();
			System.out.println("1차");
			// 값을 계속해서 수행하기 위한 반복문
			while (true) {
				if (!firstConnect) { // 처음 접속하는 것을 경우
					System.out.println("진입");
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

					xRatio = myWidth / masterWidth; // 비율 계산
					yRatio = myHeight / masterHeight;
					System.out.println("xRatio : " + xRatio + " yRatio " + yRatio);

					System.out.println("서버 연결 성공");
					firstConnect = true;
				} else { // 값을 받아 수행
					Object value = objectInStream.readObject();
					// System.out.println(value.getClass().getSimpleName());
					if (value instanceof Point) {
						Point mouse = (Point) value;
						if (mouse.x == 0)
							checkStart = true;
						if (checkStart) { // 마스터의 활동영역이 활성화 되어있다면 마우스를 마스터에서 명령한대로 수행
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
								checkStart = false; // 오른쪽 끝으로 이동시
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
						System.out.println("휠이동");
					} else if (value instanceof MouseEvent) {
						MouseEvent e = (MouseEvent) value;
						robot.mousePress(e.getMaskForButton(e.getButton()));
						robot.mouseRelease(e.getMaskForButton(e.getButton()));
						System.out.println("마우스 클릭");
					}
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("IP가 잘못됨\n" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException 발생-Read\n" + e.getMessage());
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
				button.setText("연결");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
