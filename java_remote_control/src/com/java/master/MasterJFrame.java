package com.java.master;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.java.jframe.DefaultJFrame;
import com.java.master.thread.MasterConnectThread;
import com.java.utility.IpAddress;

public class MasterJFrame extends JFrame {
	// ����� �����̺꿡 ���� ������ ��� JLabel
	private JLabel labelIP = null
			, labelHost = null
			, labelResult = null;
	
	private JButton btnDisConnect = null;
	
	// �����Ϳ� ���� ������ ��� JLabel
	private JLabel labelMyIp = null
			, labelMyHost = null
			, labelMyPort = null;
	
	private JButton btnStart = null;
	
	private boolean serverOnOff = false;

	public boolean isServerOnOff() {
		return serverOnOff;
	}

	private ServerSocket serverSocket = null;
	private MasterConnectThread connectThread =  null;
	private final static int PORT = 9095;
	
	private DefaultJFrame jframe = null;
	private JPanel west = null, east = null;
	
	private CardLayout card = null;
	JPanel eastContent = null;
	
	public MasterJFrame() {
		setUI1();
	}

	private void setUI1() {
		jframe = new DefaultJFrame("������ ���α׷�", 500, 350);
		jframe.setPanel();
		
		east = jframe.getEastPanel();
		west = jframe.getWestPanel();
		
		east.add(setEast());
		west.add(setWest());
		
		jframe.addContain();

	}
	
	private JPanel setEast() {
		
		card = new CardLayout();
		
		eastContent = new JPanel(card);
		
		eastContent.add(setEastController(), "controller");
		eastContent.add(setEastNetwork() , "network");
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
		card.show(eastContent, "controller");
		
		return eastContent;
		
	}
	// ���ʺκ� ����
	private JPanel setEastController() {
		
		// �׷�ڽ� ���� ����
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// ���� ������ ���� ����
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// �����ֱ� �ð� �ݴ� ����
		eastContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

		btnStart = new JButton("ON");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {

					if(btnStart.getText().equals("ON")) {
						btnStart.setText("OFF");
						serverSocket = new ServerSocket(PORT);
						serverOnOff = true;
						connectThread = new MasterConnectThread(serverSocket, labelIP, labelHost, jframe);
						connectThread.start();
						
					}else {
						// ���� �������� ���°� ����Ǿ����� �ʴٸ� ����
						if(connectThread.getState() != Thread.State.TERMINATED) connectThread.interrupt();
						// ���� ���������� �����ִ��� Ȯ��
						if(isOpen(serverSocket)) {
							serverSocket.close();
						}
						
						labelIP.setText("000.000.000.000");
						labelHost.setText("������");
						labelResult.setText("����Ȯ��");
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
		eastContent.add(labelMyPort = new JLabel(Integer.toString(PORT)));		
		eastContent.add(new JLabel("Server"));
		eastContent.add(btnStart);

		group.add(eastContent);
		return group;
	}
	
	private JPanel setEastNetwork() {
		
		// �׷�ڽ� ���� ����
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// ���� ������ ���� ����
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// �����ֱ� �ð� �ݴ� ����
		eastContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

		btnDisConnect = new JButton("��������");
		btnDisConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // ���� ����Ǿ��ִ� ������ ���� ���� �ٽ� ����
				if(connectThread.getState() != Thread.State.TERMINATED) {
					connectThread.interrupt();
					System.out.println("connectThread ���ͷ�Ʈ");
					
					connectThread =  new MasterConnectThread(serverSocket, labelIP, labelHost, jframe);
					connectThread.start();
					System.out.println("connectThread �ٽý���");
					
					labelIP.setText("000.000.000.000");
					labelHost.setText("������");
					labelResult.setText("����Ȯ��");
				}else {
					System.out.println("���� ������ �Ǿ����� �ʽ��ϴ�.");
				}
				
			}

		});

		eastContent.add(new JLabel("IP  "));
		eastContent.add(labelIP = new JLabel("000.000.000.000"));
		eastContent.add(new JLabel("Host "));
		eastContent.add(labelHost = new JLabel("������"));	
		eastContent.add(labelResult = new JLabel("����Ȯ��"));
		eastContent.add(btnDisConnect);

		group.add(eastContent);
		return group;
	}

	// ���ʺκ� ����
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
				System.out.println("Controller Ŭ��");
				card.show(eastContent,"controller");
			}
			
		});
		
		btnNetwork.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Network Ŭ��");
				card.show(eastContent,"network");
			}
			
		});
		
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Account Ŭ��");
			}
			
		});
		
		btnOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Option Ŭ��");
			}
			
		});
		
		westContent.add(btnController);
		westContent.add(btnNetwork);
		westContent.add(btnAccount);
		westContent.add(btnOption);
		
		return westContent;
	}
	
	public static boolean isOpen(ServerSocket server) {
		return server.isBound() && !server.isClosed();
	}
	
	
}