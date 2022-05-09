package com.java.master;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CheckLinkProgram extends JFrame {
	private static final long serialVersionUID = -1117545047093150805L;//
	JLabel labelIP = null, labelHost = null, labelResult = null;
	
	public CheckLinkProgram() {
		setUI();
		setThread();
	}
	
	private void setUI() {
		setTitle("������ ���α׷�");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();						// ���� ������Ʈ�� ������ �� �ִ� �����̳� ������
		container.setLayout(new BorderLayout(3,3));
		
		JPanel panel = new JPanel(new GridLayout(3,2));
		panel.add(new JLabel("Connect Client IP   : "));
		panel.add(labelIP = new JLabel(""));
		panel.add(new JLabel("Connect Client Host : "));
		panel.add(labelHost = new JLabel(""));
		panel.add(labelResult = new JLabel("����Ȯ��"));
		
		container.add(panel, BorderLayout.NORTH);
		
		setSize(400,200);
		setVisible(true);
	}
	
	private void setThread() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9095);
			socket = serverSocket.accept();
			labelResult.setText("����ڰ� �����Ͽ����ϴ�.");
			InetAddress inet = socket.getInetAddress();
			labelIP.setText(inet.getHostAddress());
			labelHost.setText(inet.getHostName());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}