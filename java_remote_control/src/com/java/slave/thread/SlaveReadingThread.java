package com.java.slave.thread;

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

import javax.swing.JLabel;

// �������� ���� ���� �����ϴ� ������
public class SlaveReadingThread extends Thread {
	private Socket socket = null;
	JLabel labelIP = null, labelHost = null;
	boolean firstConnect = false;
	
	public SlaveReadingThread(Socket socket,JLabel labelIP,JLabel labelHost) {
		this.socket = socket;
		this.labelIP = labelIP;
		this.labelHost = labelHost;
	}
	
	public void run() {
		DataInputStream dataInStream = null;
		ObjectInputStream objectInStream = null;
		Robot robot = null;	
		
		try {
			// �������� ���� �޾ƿ��� ���� InputStream
			dataInStream = new DataInputStream(socket.getInputStream());
			objectInStream = new ObjectInputStream(socket.getInputStream());
			robot = new Robot();
			// ���� ����ؼ� �����ϱ� ���� �ݺ���
			System.out.println("while�� �Ա�");
			while(true) {
				if(!firstConnect) {
					String str = dataInStream.readUTF();
					System.out.println(str);
					
					String[] strSplit = str.split("-");
					
					labelIP.setText(strSplit[0]);
					labelHost.setText(strSplit[1]);
					System.out.println("���� ���� ����");
					firstConnect = true;
				}
				else {
					Object value = objectInStream.readObject();
					System.out.println(value.getClass().getSimpleName());
					if(value instanceof Point) {
						Point mouse = (Point)value;
						robot.mouseMove(mouse.x, mouse.y);
					}else if(value instanceof KeyEvent){
						KeyEvent key = (KeyEvent)value;
						robot.keyPress(key.getKeyCode());
						System.out.println("keycode : " + key.getKeyCode());
					}else if(value instanceof MouseWheelEvent) {
						MouseWheelEvent e = (MouseWheelEvent)value;
						robot.mouseWheel(e.getWheelRotation());
						System.out.println("���̵�");
					}else if(value instanceof MouseEvent) {
						MouseEvent e = (MouseEvent)value;
						robot.mousePress(e.getMaskForButton(e.getButton()));
						robot.mouseRelease(e.getMaskForButton(e.getButton()));
						System.out.println("���콺 Ŭ��");
					}
				}			
			}
		}catch (UnknownHostException e) {
			System.out.println("IP�� �߸���\n"+e.getMessage());
		}catch (IOException e) {
			System.out.println("IOException �߻�-Read\n"+e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(objectInStream != null) objectInStream.close();
				if(dataInStream != null) dataInStream.close();
				if(socket != null) socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
}
