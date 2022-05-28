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
import com.java.utility.IpAddress;

public class MasterJFrame extends JFrame {
	// 연결된 슬레이브에 대한 정보를 담는 JLabel
	JLabel labelIP = null
			, labelHost = null
			, labelResult = null;
	
	JButton btnDisConnect = null;
	
	// 마스터에 대한 정보를 담는 JLabel
	JLabel labelMyIp = null
			, labelMyHost = null
			, labelMyPort = null;
	
	JButton btnStart = null;
	
	MasterConnectThread connectThread =  null;
	
	int port = 9095;
	
	ServerSocket serverSocket = null;
	Socket socket = null;
	
	boolean connectCheck = false;
	DefaultJFrame jframe = null;
	
	public MasterJFrame() {
		setUI1();
	}

	private void setUI1() {
		jframe = new DefaultJFrame("마스터 프로그램", 500, 600);

		JPanel west = jframe.getWestPanel();
		JPanel east = jframe.getEastPanel();

		east.setLayout(new GridLayout(0,1));
		east.add(setEastController());
		east.add(setEastNetwork());
		east.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		west.add(setWest());
		
		jframe.addContain();
	}

	// 동쪽부분 설정
	private JPanel setEastController() {
		
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// 실제 데이터 역할 수행
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// 여백주기 시계 반대 방향
		eastContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

		btnStart = new JButton("ON");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(serverSocket == null) {
						serverSocket = new ServerSocket(port);
						btnStart.setText("OFF");
						connectThread = new MasterConnectThread(serverSocket, labelIP, labelHost);
						connectThread.start();
					}else {
						connectThread.setStr(null);
						System.out.println(connectThread.getStr());
						serverSocket.close();
						labelIP.setText("000.000.000.000");
						labelHost.setText("미접속");
						labelResult.setText("연결확인");
						btnStart.setText("ON");
					}
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			}

		});

		IpAddress myip = new IpAddress();
		
		eastContent.add(new JLabel("IP  "));
		eastContent.add(labelMyIp = new JLabel(myip.getHostAddress()));
		eastContent.add(new JLabel("Host "));
		eastContent.add(labelMyHost = new JLabel(myip.getHostName()));
		eastContent.add(new JLabel("Port  "));
		eastContent.add(labelMyPort = new JLabel(Integer.toString(port)));		
		eastContent.add(new JLabel("Server"));
		eastContent.add(btnStart);

		group.add(eastContent);
		return group;
	}
	
	private JPanel setEastNetwork() {
		
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// 실제 데이터 역할 수행
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// 여백주기 시계 반대 방향
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		btnDisConnect = new JButton("연결해제");
		btnDisConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(connectThread!=null) {
					connectThread.interrupt();
					labelIP.setText("000.000.000.000");
					labelHost.setText("미접속");
					labelResult.setText("연결확인");
					System.out.println("연결해제됨");
				}else {
					System.out.println("현재 연결이 되어있지 않습니다.");
				}
				
			}

		});

		eastContent.add(new JLabel("IP  "));
		eastContent.add(labelIP = new JLabel("000.000.000.000"));
		eastContent.add(new JLabel("Host "));
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
		westContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
		
		btnController.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setBackground(new Color(255,128,0));
				
			}
			
		});
		
		westContent.add(btnController);
		westContent.add(btnNetwork);
		westContent.add(btnAccount);
		westContent.add(btnOption);
		
		return westContent;
	}
	
}