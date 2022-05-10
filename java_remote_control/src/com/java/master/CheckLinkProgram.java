package com.java.master;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.java.master.thread.MasterConnectThread;

public class CheckLinkProgram extends JFrame {
	private static final long serialVersionUID = -1117545047093150805L;//
	JLabel labelIP = null, labelHost = null, labelResult = null;
	
	public CheckLinkProgram() {
		setUI();
		settingThread();
	}
	
	private void setUI() {
		setTitle("마스터 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();						// 현재 컴포넌트를 포함할 수 있는 컨테이너 가져옴
		container.setLayout(new BorderLayout(3,3));
		
		JPanel panel = new JPanel(new GridLayout(3,2));
		panel.add(new JLabel("Connect Client IP   : "));
		panel.add(labelIP = new JLabel("000.000.000.000"));
		panel.add(new JLabel("Connect Client Host : "));
		panel.add(labelHost = new JLabel("미접속"));
		panel.add(labelResult = new JLabel("연결확인"));
		
		container.add(panel, BorderLayout.NORTH);
		
		setSize(400,200);
		setVisible(true);
	}
	
	// 스레드로 설정할것
	private void settingThread() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9095);
			socket = serverSocket.accept();
			Thread connectThread = new MasterConnectThread(socket,labelResult,labelIP,labelHost);
			connectThread.start();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		} 
	}
}