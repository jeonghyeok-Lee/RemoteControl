package com.java.master;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.java.master.thread.MasterConnectThread;
import com.java.master.thread.MasterCoummunicationThread;

public class MasterJFrame extends JFrame {
	JLabel labelIP = null, labelHost = null, labelResult = null;
	JButton btnDisConnect = null;
	boolean connectCheck = true;
	
	public MasterJFrame() {
		setUI();
		settingThread();
	}
	
	private void setUI() {
		setTitle("마스터 프로그램");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();						// 현재 컴포넌트를 포함할 수 있는 컨테이너 가져옴
		container.setLayout(new BorderLayout(3,3));
		
		JPanel panel = new JPanel(new GridLayout(3,2));
		
		btnDisConnect = new JButton("연결해제");
		btnDisConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				connectCheck = false;
			}
			
		});
				
		panel.add(new JLabel("Connect Client IP   : "));
		panel.add(labelIP = new JLabel("000.000.000.000"));
		panel.add(new JLabel("Connect Client Host : "));
		panel.add(labelHost = new JLabel("미접속"));
		panel.add(btnDisConnect);
		panel.add(labelResult = new JLabel("연결확인"));
		
		container.add(panel, BorderLayout.NORTH);
		
		
		setSize(400,200);
		setVisible(true);
	}
	
	// 스레드 설정
	private void settingThread() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9095);
			socket = serverSocket.accept();
			MasterConnectThread connectThread = new MasterConnectThread(socket,labelResult,labelIP,labelHost, connectCheck);
			connectThread.start();
			MasterCoummunicationThread mouseThread = new MasterCoummunicationThread(socket);
			mouseThread.start();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			
		}
	}
}