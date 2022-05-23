package com.java.master;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.java.jframe.DefaultJFrame;
import com.java.master.thread.MasterConnectThread;
import com.java.master.thread.MasterCoummunicationThread;

public class MasterJFrame extends JFrame {
	JLabel labelIP = null, labelHost = null, labelResult = null;
	JButton btnDisConnect = null;
	boolean connectCheck = true;
	DefaultJFrame jframe = null;

	public MasterJFrame() {
		setUI1();
		settingThread();
	}
	
	// 초기형태
//	private void setUI() {
//		setTitle("마스터 프로그램");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		Container container = getContentPane(); // 현재 컴포넌트를 포함할 수 있는 컨테이너 가져옴
//		container.setLayout(new BorderLayout(10, 3));
//
//		JPanel panel = new JPanel(new GridLayout(3, 2));
//		panel.setBorder(new LineBorder(Color.red, 1, true));
//
//		btnDisConnect = new JButton("연결해제");
//		btnDisConnect.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				connectCheck = false;
//			}
//
//		});
//
//		panel.add(new JLabel("Connect Client IP   : "));
//		panel.add(labelIP = new JLabel("000.000.000.000"));
//		panel.add(new JLabel("Connect Client Host : "));
//		panel.add(labelHost = new JLabel("미접속"));
//		panel.add(btnDisConnect);
//		panel.add(labelResult = new JLabel("연결확인"));
//
//		container.add(panel, BorderLayout.NORTH);
//
//		setSize(400, 200);
//		setVisible(true);
//	}

	private void setUI1() {
		jframe = new DefaultJFrame("마스터 프로그램", 500, 500);

		JPanel west = jframe.getWestPanel();
		JPanel east = jframe.getEastPanel();

		east.add(setEast());
		east.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		west.add(setWest());
		
		jframe.addContain();
	}

	// 동쪽부분 설정
	private JPanel setEast() {
		
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox();
		
		// 실제 데이터 역할 수행
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(3, 2));
		// 여백주기 시계 반대 방향
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		btnDisConnect = new JButton("연결해제");
		btnDisConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectCheck = false;
			}

		});

		eastContent.add(new JLabel("Connect Client IP   : "));
		eastContent.add(labelIP = new JLabel("000.000.000.000"));
		eastContent.add(new JLabel("Connect Client Host : "));
		eastContent.add(labelHost = new JLabel("미접속"));
		eastContent.add(labelResult = new JLabel("연결확인"));
		eastContent.add(btnDisConnect);

		group.add(eastContent);
		return group;
	}

	// 서쪽부분 설정
	private JPanel setWest() {
		
		JPanel westContent = new JPanel();
		
		JButton btnController = new JButton("Controller");
		JButton btnNetwork = new JButton("Network");
		JButton btnAccount = new JButton("Account");
		JButton btnOption = new JButton("Option");
		
		westContent.setLayout(new GridLayout(0,1));
		westContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 10));
		
		westContent.add(btnController);
		westContent.add(btnNetwork);
		westContent.add(btnAccount);
		westContent.add(btnOption);
		
		return westContent;
	}
	
	// 스레드 설정
	private void settingThread() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9095);
			socket = serverSocket.accept();
			MasterConnectThread connectThread = new MasterConnectThread(socket, labelResult, labelIP, labelHost,
					connectCheck);
			connectThread.start();
			MasterCoummunicationThread mouseThread = new MasterCoummunicationThread(socket);
			mouseThread.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {

		}
	}
}