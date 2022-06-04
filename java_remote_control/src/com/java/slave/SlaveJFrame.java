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
		jframe = new DefaultJFrame("�����̺� ���α׷�", 500, 300);
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
		
		button = new JButton("�����ϱ�");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(socket == null) { // �����ϱ� ��ư�� ó�� ���� ��� --> �ѹ��� ������ ������ ����
						socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
						
						rThread = new SlaveReadingThread(socket,labelIP,labelHost);
//						wThread = new SlaveWritingThread(socket);
						rThread.start();
//						wThread.start();
						
						txtIp.setText("");
						txtPort.setText("");
						labelResut.setText("������");
						
					}else { 	
						// socket�� null�� �ƴ� ��� ��ư�� ���ȴ� ���� ���� -> ����� ����� ���� 
						// -> isClose()�� ������<���� ����� ���� ���°�� ������ ������������ false ��ȯ>�ؼ�
						if(!socket.isClosed()) { // ������ ���� �����ִٸ�		
							System.out.println("������ ��������");
							if(rThread.getState() != Thread.State.TERMINATED) rThread.interrupt();
//							if(wThread.getState() != Thread.State.TERMINATED) wThread.interrupt();
							if(!socket.isClosed()) socket.close();
							
							labelResut.setText("���� ��");
						}else { 							// ������ ���� �����ִٸ�
							socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
							
							rThread = new SlaveReadingThread(socket,labelIP,labelHost);
//							wThread = new SlaveWritingThread(socket);
							rThread.start();
//							wThread.start();
							
							txtIp.setText("");
							txtPort.setText("");
							labelResut.setText("������");
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
		third.add(labelResut = new JLabel("���Ῡ��"));
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
