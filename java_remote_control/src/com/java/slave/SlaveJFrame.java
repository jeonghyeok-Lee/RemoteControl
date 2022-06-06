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
import com.java.slave.thread.SlaveWritingThread;

public class SlaveJFrame extends JFrame {
	
	private JTextField txtIp = null;
	private JTextField txtPort = null;
	private JLabel labelIP = null , labelHost = null;
	private JButton button = null;	
	private Socket socket = null;
	private DefaultJFrame jframe = null;
	private SlaveReadingThread rThread = null;
	private SlaveWritingThread wThread = null;
	private boolean nonLogIn = false; // ��ȸ������ �����ߴ��� Ȯ�ο� ��ȸ�� = true
	private boolean disconnect = false;	// �����̺꿡�� ����������ư�� Ŭ���ߴ��� Ȯ�ο�

	private SlaveJFrame myJFrame = null;
	private CardLayout card = null;
	private JPanel centerContent = null;

	// �����忡�� ����ϱ� ���� ������
	public boolean isDisconnect() {
		return disconnect;
	}
	public JButton getButton() {
		return button;
	}
	
	public CardLayout getCard() {
		return card;
	}
	
	public JPanel getCenterContent() {
		return centerContent;
	}
	
	public JLabel getLabelIP() {
		return labelIP;
	}
	
	public JLabel getLabelHost() {
		return labelHost;
	}
	
	public SlaveJFrame() {
		setUI();
		myJFrame = this;
	}
	
	public SlaveJFrame(boolean nonLogIn) {
		this.nonLogIn = nonLogIn;
		setUI();
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
		
		button = new JButton("����");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(socket == null) { // �����ϱ� ��ư�� ó�� ���� ��� --> �ѹ��� ������ ������ ����
						socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));														
						
						rThread = new SlaveReadingThread(socket, myJFrame);
//						wThread = new SlaveWritingThread(socket, myJFrame);
//						wThread.start();
						rThread.start();
						
						disconnect = false;
						
						txtIp.setText("");
						txtPort.setText("");
						card.show(centerContent, "connect");
						
					}else { 	
						// socket�� null�� �ƴ� ��� ��ư�� ���ȴ� ���� ���� -> ����� ����� ���� 
						// -> isClose()�� ������<���� ����� ���� ���°�� ������ ������������ false ��ȯ>�ؼ�
						if(!socket.isClosed()) { // ������ ���� �����ִٸ�		
							System.out.println("������ ��������");
							disconnect = true;
							if(rThread.getState() != Thread.State.TERMINATED) rThread.interrupt();
							try {
								if(socket != null) socket.close();
							}catch(Exception e1) {
								e1.printStackTrace();
							}
//							if(wThread.getState() != Thread.State.TERMINATED) wThread.interrupt();
							card.show(centerContent, "disconnect");
							
						}else { 							// ������ ���� �����ִٸ�
							socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
							
							rThread = new SlaveReadingThread(socket, myJFrame);
//							wThread = new SlaveWritingThread(socket, myJFrame);
//							wThread.start();
							rThread.start();
							
							disconnect = false;
							
							txtIp.setText("");
							txtPort.setText("");
							card.show(centerContent, "connect");
						}
					}
				}catch(Exception e1) {
					e1.getMessage();
				}finally {
					
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
		first.add(labelIP = new JLabel("000.000.000.000"));
		second.add(new JLabel("Connect Server Host : "));
		second.add(labelHost = new JLabel("Unknown"));
		
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
