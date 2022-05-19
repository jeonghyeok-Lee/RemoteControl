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
		setTitle("������ ���α׷�");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();						// ���� ������Ʈ�� ������ �� �ִ� �����̳� ������
		container.setLayout(new BorderLayout(3,3));
		
		JPanel panel = new JPanel(new GridLayout(3,2));
		
		btnDisConnect = new JButton("��������");
		btnDisConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				connectCheck = false;
			}
			
		});
				
		panel.add(new JLabel("Connect Client IP   : "));
		panel.add(labelIP = new JLabel("000.000.000.000"));
		panel.add(new JLabel("Connect Client Host : "));
		panel.add(labelHost = new JLabel("������"));
		panel.add(btnDisConnect);
		panel.add(labelResult = new JLabel("����Ȯ��"));
		
		container.add(panel, BorderLayout.NORTH);
		
		
		setSize(400,200);
		setVisible(true);
	}
	
	// ������ ����
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