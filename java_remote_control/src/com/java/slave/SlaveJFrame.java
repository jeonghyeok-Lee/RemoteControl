package com.java.slave;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
	
	private SlaveReadingThread rThread = null;
//	private SlaveWritingThread wThread = null;
	
	private boolean connected = false;
	
	public SlaveJFrame() {
		setUI();
	}
	
	private void setUI() {
		jframe = new DefaultJFrame("슬레이브 프로그램", 500, 300);
		jframe.setPanel();
		JPanel center = jframe.getCenterPanel();
		
		center.add(setCenter());
		
		jframe.addContain();
		
	}
	
	private JPanel setCenter() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		
		JPanel first = new JPanel(new GridLayout(0,2));
		JPanel second = new JPanel(new GridLayout(0,2));
		JPanel third = new JPanel(new GridLayout(0,2));
		JPanel fourth = new JPanel(new GridLayout(0,2));
		
		button = new JButton("연결하기");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(socket == null) { // 연결하기 버튼을 처음 눌른 경우 --> 한번도 연결이 된적이 없음
						socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
						
						rThread = new SlaveReadingThread(socket,labelIP,labelHost);
//						wThread = new SlaveWritingThread(socket);
						rThread.start();
//						wThread.start();
						
						txtIp.setText("");
						txtPort.setText("");
						labelResut.setText("연결중");
						
					}else { 	
						// socket이 null이 아닌 경우 버튼이 눌렸던 적이 있음 -> 실행된 경력이 있음 
						// -> isClose()의 문제점<최초 연결된 적이 없는경우 소켓이 닫혀있음에도 false 반환>해소
						if(!socket.isClosed()) { // 소켓이 현재 열려있다면		
							System.out.println("소켓이 열려있음");
							if(rThread.getState() != Thread.State.TERMINATED) rThread.interrupt();
//							if(wThread.getState() != Thread.State.TERMINATED) wThread.interrupt();
							if(!socket.isClosed()) socket.close();
							
							labelResut.setText("연결 전");
						}else { 							// 소켓이 현재 닫혀있다면
							socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
							
							rThread = new SlaveReadingThread(socket,labelIP,labelHost);
//							wThread = new SlaveWritingThread(socket);
							rThread.start();
//							wThread.start();
							
							txtIp.setText("");
							txtPort.setText("");
							labelResut.setText("연결중");
						}
					}
					

					
				}catch(Exception e1) {
					e1.getMessage();
				}

			}
		});
		
		
		first.add(new JLabel("Connect Server IP   : "));
		first.add(txtIp = new JTextField(15));
		second.add(new JLabel("Connect Server Port : "));
		second.add(txtPort = new JTextField(15));
		third.add(labelResut = new JLabel("연결여부"));
		third.add(button);
		fourth.add(labelIP = new JLabel("Connect Server IP"));
		fourth.add(labelHost = new JLabel("Connect Server Host"));
		
		panel.add(first);
		panel.add(second);
		panel.add(third);
		panel.add(fourth);
		
		
		return panel;
	}
	
}
