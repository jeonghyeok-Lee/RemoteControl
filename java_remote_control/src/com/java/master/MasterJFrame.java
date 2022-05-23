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
	
	// �ʱ�����
//	private void setUI() {
//		setTitle("������ ���α׷�");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		Container container = getContentPane(); // ���� ������Ʈ�� ������ �� �ִ� �����̳� ������
//		container.setLayout(new BorderLayout(10, 3));
//
//		JPanel panel = new JPanel(new GridLayout(3, 2));
//		panel.setBorder(new LineBorder(Color.red, 1, true));
//
//		btnDisConnect = new JButton("��������");
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
//		panel.add(labelHost = new JLabel("������"));
//		panel.add(btnDisConnect);
//		panel.add(labelResult = new JLabel("����Ȯ��"));
//
//		container.add(panel, BorderLayout.NORTH);
//
//		setSize(400, 200);
//		setVisible(true);
//	}

	private void setUI1() {
		jframe = new DefaultJFrame("������ ���α׷�", 500, 500);

		JPanel west = jframe.getWestPanel();
		JPanel east = jframe.getEastPanel();

		east.add(setEast());
		east.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		west.add(setWest());
		
		jframe.addContain();
	}

	// ���ʺκ� ����
	private JPanel setEast() {
		
		// �׷�ڽ� ���� ����
		JPanel group = jframe.addGroupBox();
		
		// ���� ������ ���� ����
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(3, 2));
		// �����ֱ� �ð� �ݴ� ����
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		btnDisConnect = new JButton("��������");
		btnDisConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectCheck = false;
			}

		});

		eastContent.add(new JLabel("Connect Client IP   : "));
		eastContent.add(labelIP = new JLabel("000.000.000.000"));
		eastContent.add(new JLabel("Connect Client Host : "));
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
		westContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 10));
		
		westContent.add(btnController);
		westContent.add(btnNetwork);
		westContent.add(btnAccount);
		westContent.add(btnOption);
		
		return westContent;
	}
	
	// ������ ����
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