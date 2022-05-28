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

import com.java.jframe.DefaultJFrame;
import com.java.slave.thread.SlaveConnectThread;
import com.java.slave.thread.SlaveCoummunicationThread;

public class SlaveJFrame extends JFrame {
	JTextField txtIp = null;
	JTextField txtPort = null;
	JLabel labelResult = null, labelIP = null, labelHost = null;
	JButton button = null;	
	Socket socket = null;
	DefaultJFrame jframe = null;
	
	SlaveConnectThread connectThread = null;
	
	public SlaveJFrame() {
		setUI();
	}
	
	private void setUI() {
		jframe = new DefaultJFrame("슬레이브 프로그램", 500, 300);

		JPanel center = jframe.getCenterPanel();
		
		center.add(setCenter());
		
		jframe.addContain();
		
	}
	
	private JPanel setCenter() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		
		JPanel first = new JPanel(new GridLayout(0,2));
		first.add(new JLabel("Connect Server IP   : "));
		first.add(txtIp = new JTextField(15));

		JPanel second = new JPanel(new GridLayout(0,2));
		second.add(new JLabel("Connect Server Port : "));
		second.add(txtPort = new JTextField(15));
		
		JPanel third = new JPanel(new GridLayout(0,2));
		third.add(new JLabel());
		third.add(button = new JButton("연결하기"));
		
		JPanel fourth = new JPanel(new GridLayout(0,2));
		fourth.add(labelIP = new JLabel("Connect Server IP"));
		fourth.add(labelHost = new JLabel("Connect Server Host"));
		
		panel.add(first);
		panel.add(second);
		panel.add(third);
		panel.add(fourth);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println(socket);
					if(socket == null) {						
						socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
						connectThread = new SlaveConnectThread(socket, labelIP, labelHost);
						connectThread.start();
					}
					
				}catch(Exception e1) {
					e1.getMessage();
				}
			}
		});
		
		return panel;
	}
}
