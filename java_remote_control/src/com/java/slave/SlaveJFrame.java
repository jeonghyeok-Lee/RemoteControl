package com.java.slave;

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
import com.java.slave.thread.SlaveReadingThread;
import com.java.slave.thread.SlaveWritingThread;

public class SlaveJFrame extends JFrame {
	
	private JTextField txtIp = null;
	private JTextField txtPort = null;
	private JLabel labelIP = null
			, labelHost = null
			, labelResut = null;
	private JButton button = null;	
	
	private Socket socket = null;
	private DefaultJFrame jframe = null;
	
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
		third.add(labelResut = new JLabel("연결여부"));
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
						
						SlaveReadingThread rThread = new SlaveReadingThread(socket,labelIP,labelHost);
						SlaveWritingThread wThread = new SlaveWritingThread(socket);
						rThread.start();
						wThread.start();
						
						txtIp.setText("");
						txtPort.setText("");
						labelIP.setText("Connect Server IP");
						labelHost.setText("Connect Server Port");
						labelResut.setText("연결중");
					}else {
						socket = null;
						System.out.println("연결해제");
						labelResut.setText("연결 전");
					}
					
				}catch(Exception e1) {
					e1.getMessage();
				}

			}
		});
		
		return panel;
	}
	
}
