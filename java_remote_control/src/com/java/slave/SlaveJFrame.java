package com.java.slave;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
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
	private boolean nonLogIn = false; // 비회원으로 접근했는지 확인용 비회원 = true
	private boolean disconnect = false;	// 슬레이브에서 연결해제버튼을 클릭했는지 확인용

	private SlaveJFrame myJFrame = null;
	private CardLayout card = null;
	private JPanel centerContent = null;
	
	private JPanel east = null; // 회원으로 로그인시 우측에 컴포넌트가 보일 수 있도록 컨테이너를 잡기위한 변수 
	
	private ArrayList<UserDTO> userDTO = null;	// 회원정보가 담긴 ArrayList
	private UserDAO userDAO = null;

	// 스레드에서 사용하기 위한 생성자
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
	
	// 비회원을 위한 생성자
	public SlaveJFrame() {
		nonLogIn = true;
		setUI();
		myJFrame = this;
		System.out.println("비회원으로 접속");
	}
	
	// 회원을 위한 생성자
	public SlaveJFrame(ArrayList<UserDTO> userDTO, UserDAO userDAO) {
		this.userDTO = userDTO;
		this.userDAO = userDAO;
		setUI();
		myJFrame = this;
		System.out.println("회원으로 접속");
	}
	

	private void setUI() {
		jframe = new DefaultJFrame("슬레이브 프로그램", 450, 300);
		jframe.setPanel();
		JPanel center = jframe.getCenterPanel();
		if(!nonLogIn) {// 회원일 경우
			east = jframe.getEastPanel();
		}
		
		center.setLayout(new GridLayout(0,1));
		center.add(setCenter());
		
//		JPanel rap = new JPanel(new GridLayout(0,3));
//		
//		button = new JButton("연결");
//		button.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					
//					if(socket == null) { // 연결하기 버튼을 처음 눌른 경우 --> 한번도 연결이 된적이 없음
//						socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));														
//						
//						rThread = new SlaveReadingThread(socket, myJFrame);
////						wThread = new SlaveWritingThread(socket, myJFrame);
////						wThread.start();
//						rThread.start();
//						
//						disconnect = false;
//						
//						txtIp.setText("");
//						txtPort.setText("");
//						card.show(centerContent, "connect");
//						
//					}else { 	
//						// socket이 null이 아닌 경우 버튼이 눌렸던 적이 있음 -> 실행된 경력이 있음 
//						// -> isClose()의 문제점<최초 연결된 적이 없는경우 소켓이 닫혀있음에도 false 반환>해소
//						if(!socket.isClosed()) { // 소켓이 현재 열려있다면		
//							System.out.println("소켓이 열려있음");
//							disconnect = true;
//							if(rThread.getState() != Thread.State.TERMINATED) rThread.interrupt();
//							try {
//								if(socket != null) socket.close();
//							}catch(Exception e1) {
//								e1.printStackTrace();
//							}
////							if(wThread.getState() != Thread.State.TERMINATED) wThread.interrupt();
//							card.show(centerContent, "disconnect");
//							
//						}else { 							// 소켓이 현재 닫혀있다면
//							socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
//							
//							rThread = new SlaveReadingThread(socket, myJFrame);
////							wThread = new SlaveWritingThread(socket, myJFrame);
////							wThread.start();
//							rThread.start();
//							
//							disconnect = false;
//							
//							txtIp.setText("");
//							txtPort.setText("");
//							card.show(centerContent, "connect");
//						}
//					}
//				}catch(Exception e1) {
//					e1.getMessage();
//				}finally {
//					
//				}
//			}
//		});
//
//		JComponent[] component = new JComponent[] {
//			new JLabel(),new JLabel(),new JLabel(),
//			new JLabel(),button,new JLabel(),
//			new JLabel(),new JLabel(),new JLabel()
//		};
//		
//		for(JComponent com : component) {
//			rap.add(com);
//		}
//		
//		center.add(rap);
		if(!nonLogIn) {// 회원일 경우
			east.add(setEast());
		}
		
		jframe.addContain();
		
	}

	// 중앙 컴포넌트를 설정
	private JPanel setCenter() {
		
		card = new CardLayout();
		
		JPanel group = new JPanel(new GridLayout(0,1));
		
		centerContent = new JPanel(card);
		
		centerContent.add(setCenterConnected(),"connect");
		centerContent.add(setCenterDisConnected(),"disconnect");
		
		card.show(centerContent, "disconnect");
		
		JPanel rap = new JPanel(new GridLayout(0,3));

		button = new JButton("연결");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(socket == null) { // 연결하기 버튼을 처음 눌른 경우 --> 한번도 연결이 된적이 없음
						socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));														
						
						rThread = new SlaveReadingThread(socket, myJFrame);
						if(!nonLogIn) {
							wThread = new SlaveWritingThread(socket, userDTO.get(0).getUserNo());
						}else {
							wThread = new SlaveWritingThread(socket, InetAddress.getLocalHost().getHostAddress().toString());
						}
						wThread.start();
						rThread.start();
						
						disconnect = false;
						
						txtIp.setText("");
						txtPort.setText("");
						card.show(centerContent, "connect");
						
					}else { 	
						// socket이 null이 아닌 경우 버튼이 눌렸던 적이 있음 -> 실행된 경력이 있음 
						// -> isClose()의 문제점<최초 연결된 적이 없는경우 소켓이 닫혀있음에도 false 반환>해소
						if(!socket.isClosed()) { // 소켓이 현재 열려있다면		
							System.out.println("소켓이 열려있음");
							disconnect = true;
							if(rThread.getState() != Thread.State.TERMINATED) rThread.interrupt();
							if(wThread.getState() != Thread.State.TERMINATED) wThread.setEnd(true);
							try {
								if(socket != null) socket.close();
							}catch(Exception e1) {
								e1.printStackTrace();
							}
//							if(wThread.getState() != Thread.State.TERMINATED) wThread.interrupt();
							card.show(centerContent, "disconnect");
							
						}else { 							// 소켓이 현재 닫혀있다면
							socket = new Socket(txtIp.getText().toString(), Integer.parseInt(txtPort.getText()));
							
							rThread = new SlaveReadingThread(socket, myJFrame);
							if(!nonLogIn) {
								wThread = new SlaveWritingThread(socket, userDTO.get(0).getUserNo());
							}else {
								wThread = new SlaveWritingThread(socket, InetAddress.getLocalHost().getHostAddress().toString());
							}
							wThread.start();
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
				new JLabel(), new JLabel(), new JLabel()
				, new JLabel(), button,new JLabel()
				, new JLabel(), new JLabel(), new JLabel() 
		};

		for (JComponent com : component) {
			rap.add(com);
		}
		
		group.add(centerContent);
		group.add(rap);
		
		
		return group;
	}
	
	// 연결된 상태일때의 영역을 보여주는 판넬
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
	
	// 연결되지 않은 상태일때의 영역을 보여주는 판넬
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

	// 동쪽 컴포넌트를 설정
	private JPanel setEast() {
		
		JPanel group = jframe.addGroupBox(Color.black, 1, true);
		
		JPanel eastContent = new JPanel(new GridLayout(0,1)); // 이후 0,1로 수정
		
		JPanel recordList = jframe.addGroupBox(Color.black, 1, true);	// 사용기록을 출력할 패널
		
		JScrollPane yScroll = new JScrollPane(recordList);
		
		// 사용기록이 있을 경우 출력
		
		JComponent[] component = new JComponent[] { 
				new JLabel("Record"),recordList
		};
		
		for(JComponent com : component) {
			eastContent.add(com);
		}
		
		group.add(eastContent);
		
		return group;
	}
}
