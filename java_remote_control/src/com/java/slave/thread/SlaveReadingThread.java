package com.java.slave.thread;

import java.awt.CardLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.java.slave.SlaveJFrame;

// 서버에서 오는 값을 수행하는 스레드
public class SlaveReadingThread extends Thread {
	private Socket socket = null;
	private JLabel labelIP = null, labelHost = null;
	private JPanel content = null;
	private CardLayout card = null;
	private JButton button = null;
	private boolean firstConnect = false;
	private boolean checkStart =  false; // 마스터의 활동영역이 연결되었을 경우 true
	
	private SlaveJFrame jframe = null;
	
	// 생성자 (연결소켓, 출력될 ip/host레이블, 연결에따른 화면전환을 위한 card와 content)
	public SlaveReadingThread(Socket socket,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;

	}
	
	public SlaveReadingThread(Socket socket,SlaveJFrame jframe) {
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
			while(true) {
				if(!firstConnect) {	// 처음 접속하는 것을 경우
					System.out.println("진입");
					String str = objectInStream.readUTF();
					System.out.println(str);
					
					String[] strSplit = str.split("-");
					
					labelIP.setText(strSplit[0]);
					labelHost.setText(strSplit[1]);
					System.out.println("서버 연결 성공");
					firstConnect = true;
				}
				else {		// 값을 받아 수행
					Object value = objectInStream.readObject();
					System.out.println(value.getClass().getSimpleName());
					if(value instanceof Point) {
						Point mouse = (Point)value;
						if(mouse.x == 0) checkStart = true;
						if(checkStart) robot.mouseMove(mouse.x, mouse.y);
					}else if(value instanceof KeyEvent){
						KeyEvent key = (KeyEvent)value;
						robot.keyPress(key.getKeyCode());
						System.out.println("keycode : " + key.getKeyCode());
					}else if(value instanceof MouseWheelEvent) {
						MouseWheelEvent e = (MouseWheelEvent)value;
						robot.mouseWheel(e.getWheelRotation());
						System.out.println("휠이동");
					}else if(value instanceof MouseEvent) {
						MouseEvent e = (MouseEvent)value;
						robot.mousePress(e.getMaskForButton(e.getButton()));
						robot.mouseRelease(e.getMaskForButton(e.getButton()));
						System.out.println("마우스 클릭");
					}
				}			
			}
		}catch (UnknownHostException e) {
			System.out.println("IP가 잘못됨\n"+e.getMessage());
		}catch (IOException e) {
			System.out.println("IOException 발생-Read\n"+e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(objectInStream != null) objectInStream.close();
				if(socket != null) socket.close();
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
