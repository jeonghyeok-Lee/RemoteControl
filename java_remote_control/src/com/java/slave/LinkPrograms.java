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

public class LinkPrograms extends JFrame {
	JTextField txtIp = null;
	JTextField txtPort = null;
	JLabel labelResult = null;
	JButton button = null;	
	Socket socket = null;
	
	public LinkPrograms() {
		setUI();
	}
	
	private void setUI() {
		setTitle("�����̺� ���α׷�");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = getContentPane();						// ���� ������Ʈ�� ������ �� �ִ� �����̳� ������
		container.setLayout(new BorderLayout(3,3));
		
		JPanel panel = new JPanel(new GridLayout(3,2));
		panel.add(new JLabel("Connect Server IP   : "));
		panel.add(txtIp = new JTextField(15));
		panel.add(new JLabel("Connect Server Port : "));
		panel.add(txtPort = new JTextField(15));
		panel.add(button = new JButton("�����ϱ�"));
		panel.add(labelResult = new JLabel("����Ȯ��"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
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
