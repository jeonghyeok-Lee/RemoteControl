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
			// 임시로 하는 방법임 수정 필요
			int mouseX = Integer.parseInt(strSplit[0]);
			int mouseY = Integer.parseInt(strSplit[1]);

			PointerInfo mPointer = MouseInfo.getPointerInfo();
			System.out.println("현재 마우스 위치 : x -  " + mPointer.getLocation().x + " y - " + mPointer.getLocation().y);
			robot.mouseMove(mouseX, mouseY);
			System.out.println("마우스 이동 : x -  " + mouseX + " y - " + mouseY);
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