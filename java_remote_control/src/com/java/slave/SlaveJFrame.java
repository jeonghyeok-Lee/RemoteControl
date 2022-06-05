package com.java.slave;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.java.jframe.DefaultJFrame;
import com.java.slave.thread.SlaveReadingThread;

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
	
	private boolean nonLogIn = false; // ��ȸ������ �����ߴ��� Ȯ�ο� ��ȸ�� = true
	
	CardLayout card = null;
	JPanel centerContent = null;
	
	public SlaveJFrame() {
		setUI();
	}
	
	public SlaveJFrame(boolean nonLogIn) {
		this.nonLogIn = nonLogIn;
		
	}
	
	private void setUI() {
		jframe = new DefaultJFrame("�����̺� ���α׷�", 450, 300);
		jframe.setPanel();
		JPanel center = jframe.getCenterPanel();
		if(!nonLogIn) {// ȸ���� ���
			JPanel east = jframe.getEastPanel();
		}
		
		center.setLayout(new GridLayout(0,1));
		center.add(setCenter());
		
		JPanel rap = new JPanel(new GridLayout(0,3));
		
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
						card.show(centerContent, "connect");
						
					}else { 	
						// socket�� null�� �ƴ� ��� ��ư�� ���ȴ� ���� ���� -> ����� ����� ���� 
						// -> isClose()�� ������<���� ����� ���� ���°�� ������ ������������ false ��ȯ>�ؼ�
						if(!socket.isClosed()) { // ������ ���� �����ִٸ�		
							System.out.println("������ ��������");
							if(rThread.getState() != Thread.State.TERMINATED) rThread.interrupt();
//							if(wThread.getState() != Thread.State.TERMINATED) wThread.interrupt();
							if(!socket.isClosed()) socket.close();
							card.show(centerContent, "disconnect");
							
						}else { 							// ������ ���� �����ִٸ�
							socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
							
							rThread = new SlaveReadingThread(socket,labelIP,labelHost);
//							wThread = new SlaveWritingThread(socket);
							rThread.start();
//							wThread.start();
							
							txtIp.setText("");
							txtPort.setText("");
							card.show(centerContent, "connect");
						}
					}
					

					
				}catch(Exception e1) {
					e1.getMessage();
				}

			}
		});

		JComponent[] component = new JComponent[] {
			new JLabel(),new JLabel(),new JLabel(),
			new JLabel(),button,new JLabel(),
			new JLabel(),new JLabel(),new JLabel()
		};
		
		for(JComponent com : component) {
			rap.add(com);
		}
		
		center.add(rap);
		
		jframe.addContain();
		
	}

	// �߾� ������Ʈ�� ����
	private JPanel setCenter() {
		
		card = new CardLayout();
		
		centerContent = new JPanel(card);
		
		centerContent.add(setCenterConnected(),"connect");
		centerContent.add(setCenterDisConnected(),"disconnect");
		
		card.show(centerContent, "disconnect");
		
		return centerContent;
	}
	
	// ����� �����϶��� ������ �����ִ� �ǳ�
	private JPanel setCenterConnected() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		
		JPanel first = new JPanel(new GridLayout(0,2));
		JPanel second = new JPanel(new GridLayout(0,2));
		
		first.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		second.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		first.add(new JLabel("Connect Server IP   : "));
		first.add(labelIP = new JLabel("Connect Server IP"));

		second.add(new JLabel("Connect Server Port : "));
		second.add(labelHost = new JLabel("Connect Server Host"));

		
		panel.add(first);
		panel.add(second);
		
		return panel;
	}
	
	// ������� ���� �����϶��� ������ �����ִ� �ǳ�
	private JPanel setCenterDisConnected() {
		
		JPanel panel = new JPanel(new GridLayout(0,1));
		
		JPanel first = new JPanel(new GridLayout(0,2));
		JPanel second = new JPanel(new GridLayout(0,2));
		
		first.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		second.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		first.add(new JLabel("Connect Server IP   : "));
		first.add(txtIp = new JTextField(15));

		second.add(new JLabel("Connect Server Port : "));
		second.add(txtPort = new JTextField(15));

		panel.add(first);
		panel.add(second);
		
		return panel;
	}
}
