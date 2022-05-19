package com.java.slave;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.java.slave.thread.SlaveConnectThread;
import com.java.slave.thread.SlaveCoummunicationThread;

public class SlaveJFrame extends JFrame {
	JTextField txtIp = null;
	JTextField txtPort = null;
	JLabel labelResult = null, labelIP = null, labelHost = null;
	JButton button = null;	
	Socket socket = null;
	
	public SlaveJFrame() {
		setUI();
	}
	
	private void setUI() {
		setTitle("슬레이브 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();						// 현재 컴포넌트를 포함할 수 있는 컨테이너 가져옴
		container.setLayout(new BorderLayout(3,3));
		
		JPanel panel = new JPanel(new GridLayout(4,2));
		panel.add(new JLabel("Connect Server IP   : "));
		panel.add(txtIp = new JTextField(15));
		panel.add(new JLabel("Connect Server Port : "));
		panel.add(txtPort = new JTextField(15));
		panel.add(button = new JButton("연결하기"));
		panel.add(labelResult = new JLabel("연결확인"));
		panel.add(labelIP = new JLabel("Connect Server IP"));
		panel.add(labelHost = new JLabel("Connect Server Host"));
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				    socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
				    Thread connectThread = new SlaveConnectThread(socket, labelResult, labelIP, labelHost);
				    Thread communicationThread = new SlaveCoummunicationThread(socket);
				    connectThread.start();
				    communicationThread.start();
				}catch(Exception e1) {
					e1.getMessage();
				}
			}
		});
		
		container.add(panel, BorderLayout.NORTH);
		
		setSize(400,200);
		setVisible(true);
	}
}
