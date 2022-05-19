package com.java.slave.thread;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

public class SlaveCoummunicationThread extends Thread implements Serializable {
	Socket socket = null;

	public SlaveCoummunicationThread(Socket socket) {
		this.socket = socket;

	}

	public void run() {
		DataInputStream dataInStream = null;
		ObjectInputStream objectInStream = null;
		Robot robot = null;
		try {
			dataInStream = new DataInputStream(socket.getInputStream());
			// objectInStream = new ObjectInputStream(socket.getInputStream());
			// PointerInfo mPointer = (PointerInfo)objectInStream.readObject();
			robot = new Robot();
			String str = dataInStream.readUTF();
			String[] strSplit = str.split(",");

			System.out.println(str);
			// �ӽ÷� �ϴ� ����� ���� �ʿ�
			int mouseX = Integer.parseInt(strSplit[0]);
			int mouseY = Integer.parseInt(strSplit[1]);

			PointerInfo mPointer = MouseInfo.getPointerInfo();
			System.out.println("���� ���콺 ��ġ : x -  " + mPointer.getLocation().x + " y - " + mPointer.getLocation().y);
			robot.mouseMove(mouseX, mouseY);
			System.out.println("���콺 �̵� : x -  " + mouseX + " y - " + mouseY);
			// System.out.println(mPointer.getLocation().toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// if(dataInStream != null) dataInStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}