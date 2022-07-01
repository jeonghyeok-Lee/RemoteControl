package com.java.master;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
import com.java.jframe.origianl.DefaultJFrame;
import com.java.jframe.origianl.LogInJFrame;
import com.java.master.thread.MasterConnectThread;
import com.java.utility.IpAddress;
import com.java.utility.RegularExpression;

public class MasterJFrame extends JFrame {
	// 연결된 슬레이브에 대한 정보를 담는 JLabel
	private JLabel labelIP = null
			, labelHost = null;
	
	private JButton btnDisConnect = null;
	
	// 마스터에 대한 정보를 담는 JLabel
	private JLabel labelMyIp = null
			, labelMyHost = null
			, labelMyPort = null;
	
	private JButton btnStart = null;
	private UserDAO userDAO = null;
	private ArrayList<UserDTO> userDTO = null;		// 마스터정보가 담긴 user테이블에대한 dao/dto
	private String id = null, pw = null;
	private int userNo = 0; 						//유저번호
	private ServerSocket serverSocket = null;		// 서버소켓
	private MasterConnectThread connectThread =  null;
	private final static int PORT = 9095;			// 포트
	private DefaultJFrame jframe = null;			
	private JPanel west = null, east = null;
	private CardLayout card = null;					// 동쪽 배치관리자
	private JPanel eastContent = null;				// 동쪽 내용
	private MasterJFrame myJFrame = null;			// 자기자신
	private JLabel labelTimer = null; 				// 연결시간을 표시할 레이블
	private JPanel recordContent = new JPanel();			// 사용기록을 출력할 패널 

	
	public JPanel getRecordContent() {
		return recordContent;
	}

	public JLabel getLabelTimer() {
		return labelTimer;
	}

	public JLabel getLabelIP() {
		return labelIP;
	}

	public JLabel getLabelHost() {
		return labelHost;
	}


	public MasterJFrame(ArrayList<UserDTO> userDTO,UserDAO userDAO) {
		this.userDTO = userDTO;
		this.userDAO = userDAO;
		setUI1();
		myJFrame = this;
	}
	
	//테스트용
	public MasterJFrame(String id, String pw) {
		this.id = id;
		this.pw = pw;
		
		userDAO= new UserDAO();
		userDTO = userDAO.userSelect( "where user_id = '" + id +"' and user_password = '"+pw+"'");
		
		setUI1();
		// 생성되면 해당 생성된 프레임을 myJFrame에
		myJFrame = this;
	}

	private void setUI1() {
		jframe = new DefaultJFrame("마스터 프로그램", 600, 500);
		jframe.setPanel();
		
		
		userNo = userDTO.get(0).getUserNo();
		
		east = jframe.getCenterPanel();
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
		eastContent.add(setEastAccount(),"account");
//		eastContent.add(setEastOption(),"option");
		
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		card.show(eastContent, "controller");
		
		return eastContent;
		
	}
	// 동쪽부분 설정
	private JPanel setEastController() {
		
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// 실제 데이터 역할 수행
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// 여백주기 시계 반대 방향
		eastContent.setBorder(BorderFactory.createEmptyBorder(110, 30, 30, 30));

		btnStart = new JButton("ON");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {

					if(btnStart.getText().equals("ON")) {
						btnStart.setText("OFF");
						serverSocket = new ServerSocket(PORT);
//						connectThread = new MasterConnectThread(serverSocket, labelIP, labelHost, jframe);
						connectThread = new MasterConnectThread(serverSocket,myJFrame, userNo);
						connectThread.start();
						
						
					}else {
						// 현재 스레드의 상태가 종료되어있지 않다면 종료
						if(connectThread.getState() != Thread.State.TERMINATED) connectThread.interrupt();
						// 현재 서버소켓이 열려있는지 확인
						if(isOpen(serverSocket)) {
							serverSocket.close();
						}
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
		
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// 실제 데이터 역할 수행
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 1));
		// 여백주기 시계 반대 방향
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		btnDisConnect = new JButton("연결해제");
		btnDisConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // 현재 실행되어있는 스레드 종료 이후 다시 실행
				if(connectThread.getState() != Thread.State.TERMINATED) {
					connectThread.interrupt();
					System.out.println("connectThread 인터럽트");
					
					labelIP.setText("000.000.000.000");
					labelHost.setText("Unknown");
					
					connectThread =  new MasterConnectThread(serverSocket,myJFrame, userNo);
					connectThread.start();

					System.out.println("connectThread 다시실행");
				}else {
					System.out.println("현재 연결이 되어있지 않습니다.");
				}
				
			}

		});
		
		JPanel frist = new JPanel(new GridLayout(0, 1));
		JPanel second = new JPanel();
		JPanel third = new JPanel();

		JPanel line1 = new JPanel(new GridLayout(0,2));
		JPanel line2 = new JPanel(new GridLayout(0,2));
		JPanel line3 = new JPanel(new GridLayout(0,2));
		JPanel line4 = new JPanel(new GridLayout(0,2));
		
		
		line1.add(new JLabel("Host "));
		line1.add(labelHost = new JLabel("미접속"));
		line2.add(new JLabel("IP  "));
		line2.add(labelIP = new JLabel("000.000.000.000"));
		line3.add(new JLabel("Access"));
		line3.add(labelTimer = new JLabel("00:00:00"));
		line4.add(new JLabel("Record"));
		line4.add(new JLabel(""));
		
		frist.add(line1);
		frist.add(line2);
		frist.add(line3);
		frist.add(line4);

		/*
		 * 접속한 소켓과 자신이 연결된 연결번호를 ConnectUserDAO/ConnectNonUserDAO에서 가져온 뒤에 
		 * 연결번호에 해당하는 사용기록 번호를 UsageRecordDAO/UsageRecordNonDAO에서 가져와 
		 * records열에 해당하는 값을 가져와 사용기록 패널에 추가
		 * 
		 * */
		
		// 레코드부분을 감쌀 그룹
		
		JPanel recordGroup = jframe.addGroupBox(Color.black,1,true);
		recordGroup.setLayout(new GridLayout(0,1));
		recordGroup.setPreferredSize(new Dimension(230,100));
		recordGroup.add(recordContent);

		JScrollPane scrollPane = new JScrollPane(recordGroup);
//		scrollPane.setViewportView(recordGroup);
		
		second.add(scrollPane);
		third.add(btnDisConnect);
		
		eastContent.add(frist);
		eastContent.add(second);
		eastContent.add(third);
		
		group.add(eastContent);
		return group;
	}

	private JPanel setEastAccount() {
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		JPanel eastContent = new JPanel(new GridLayout(0,1));
		
		// 각 한줄마다 패널을 추가하여 디자인
		JPanel line[] = new JPanel[] {
				new JPanel(),new JPanel(),new JPanel(),new JPanel(),new JPanel()
		};
		
		
		// 비밀번호 변경용 버튼
		JButton btnChangePassword = new JButton("change");
		btnChangePassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				passwordChangeJFrame();
			}
			
		});
		
		// 비밀번호 변경부분의 영역을 생성
		JPanel pwPart = new JPanel();
		JLabel password = new JLabel(userDTO.get(0).getUserPassword());
		pwPart.add(password);
		pwPart.add(btnChangePassword);
		
		// 로그아웃부분 영역을 만듬 
		// 로그아웃 버튼
		JButton btnLogout = new JButton("LogOut");
		btnLogout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new LogInJFrame(false);
				jframe.dispose();
			}
			
		});
		
		JButton btnWithdrawal = new JButton("탈퇴");
		btnWithdrawal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteUser();
			}
			
		});
		
		JPanel logoutPart = new JPanel();
		logoutPart.add(btnWithdrawal);
		logoutPart.add(btnLogout);
		
		JComponent[] component = new JComponent[] {
				new JLabel("User"), new JLabel(userDTO.get(0).getUserId())
				,new JLabel("Password"), pwPart
				,new JLabel("Name"), new JLabel(userDTO.get(0).getUserName())
				,new JLabel("Email"), new JLabel(userDTO.get(0).getUserEmail())
				,new JLabel(), new JLabel()
		};
		
		// 각 패널의 컴포넌트 추가
		for(int i = 0; i< line.length;i++) {
			line[i].setLayout(new GridLayout(0,2));
			if(i == 0) {
				line[i].add(component[i]);
				line[i].add(component[i+1]);
			}else {
				line[i].add(component[(2*i)]);
				line[i].add(component[(2*i)+1]);
			}
			eastContent.add(line[i]);
		}
		
		eastContent.add(logoutPart);
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		group.add(eastContent);
		return group;
	}
	
	private JPanel setEastOption() {
		// 그룹박스 역할 수행
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		JPanel eastContent = new JPanel(new GridLayout(0,1));
		int count = 0;
		
		JPanel[] line = new JPanel[] {
				new JPanel(new GridLayout(0,2)),new JPanel(), new JPanel(),new JPanel(), new JPanel()
		};
		
		
		JButton btnChangePort = new JButton("change");
		
		JPanel buttonPanel = new JPanel();
		JButton btnClearOption = new JButton("Clear");
		JButton btnOKOption = new JButton("OK");
		
		buttonPanel.add(btnClearOption);
		buttonPanel.add(btnOKOption);
		
		// 첫번째 라인
		JPanel first = new JPanel();
		first.add(new JLabel(Integer.toString(PORT)));
		first.add(btnChangePort);
		
		JComponent[] component = new JComponent[] {
			new JLabel("Port"), first
			
		};
		
		for(JComponent com : component) {
			if(count < 2)
				line[0].add(com);
			else if(count < 4 )
				line[1].add(com);
			else if(count < 6)
				line[2].add(com);
			else if(count < 8)
				line[3].add(com);
			else if(count < 10)
				line[4].add(com);
			count++;
		}
		
		for(JPanel list : line) {
			eastContent.add(list);
		}
		
		eastContent.add(buttonPanel);
		
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		group.add(eastContent);
		
		return group;
	}
	
	// 서쪽부분 설정
	private JPanel setWest() {
		
		JPanel westContent = new JPanel();
		
		JButton btnController = new JButton("Controller");
		JButton btnNetwork = new JButton("Network");
		JButton btnAccount = new JButton("Account");
//		JButton btnOption = new JButton("Option");
		
		westContent.setLayout(new GridLayout(0,1));
		westContent.setBorder(BorderFactory.createEmptyBorder(50, 50, 10, 10));
		
		btnController.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Controller 클릭");
				card.show(eastContent,"controller");
			}
			
		});
		
		btnNetwork.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Network 클릭");
				card.show(eastContent,"network");
			}
			
		});
		
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Account 클릭");
				card.show(eastContent,"account");
			}
			
		});
		
//		btnOption.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("Option 클릭");
//				card.show(eastContent,"option");
//			}
//			
//		});
		
		westContent.add(btnController);
		westContent.add(new JLabel());
		westContent.add(btnNetwork);
		westContent.add(new JLabel());
		westContent.add(btnAccount);
//		westContent.add(btnOption);
		
		return westContent;
	}
	
	// 서버소켓이 열려있는지 확인하기 위한 함수
	public static boolean isOpen(ServerSocket server) {
		return server.isBound() && !server.isClosed();
	}
	
	// 비밀번호 변경을 위한 JFrame 생성
	private void passwordChangeJFrame() {
		DefaultJFrame pwChangeJFrame = new DefaultJFrame("비밀번호 변경",400,250);
		pwChangeJFrame.setPanel();
		pwChangeJFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel center = pwChangeJFrame.getCenterPanel();
		
		JPanel first = new JPanel(new GridLayout(0,2));
		JPanel second = new JPanel(new GridLayout(0,2));
		JPanel third = new JPanel(new GridLayout(0,2));
		JPanel fourth = new JPanel();
		
		JTextField txtPW = new JTextField(15);
		JPasswordField txtNewPW = new JPasswordField(15);
		JPasswordField txtCheckPW = new JPasswordField(15);
		
		// 비밀번호 변경을 위한 확인버튼
		// 현재 비밀번호와 맞는지 파악 + 맞다면 새로운비밀번호로 비밀번호를 수정
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msgText = "";
				RegularExpression regex = new RegularExpression(txtPW.getText(),"pw");
				// 현재 입력 비밀번호가 맞다면
				if(userDTO.get(0).getUserPassword().equals(txtPW.getText())) {
					String query = "update user set user_password = '" +txtNewPW.getText()+"' where user_no = "+userNo;
					int result = userDAO.userUpdate(query);

					if(result != 0) {	// 변경이 성공적으로 이루어졌다면
						msgText = "변경에 성공하였습니다.";
						JLabel[] msg = new JLabel[] {new JLabel(msgText) };
						JButton btnCheck = new JButton("확인");
						DefaultJFrame error = new DefaultJFrame("알림", 350, 120, msg, btnCheck, false);
						btnCheck.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								new LogInJFrame(false);
								error.dispose();
								pwChangeJFrame.dispose();
								jframe.dispose();
							}
						});
						error.addContaionEmpty();
					
					}
				}else {
					// 입력 비밀번호가 아니었던 것 이라면
					if(!userDTO.get(0).getUserPassword().equals(txtPW.getText())) {
						msgText = "입력하신 비밀번호는 현재 비밀번호가 아닙니다.";
					}else if(!txtNewPW.equals(txtCheckPW)) {
						msgText = "새로 입력하신 비밀번호가 일치하지 않습니다.";
					}else if(!regex.isRegex()){
						msgText = "8 ~ 20글자 이내로 작성해주세요.";
					}else {
						msgText = "알 수없는 이유로 수정되지 않았습니다.";
					}
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnCheck = new JButton("확인");
					DefaultJFrame error = new DefaultJFrame("알림", 350, 120, msg, btnCheck, false);
					btnCheck.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.dispose();
						}
					});
					error.addContaionEmpty();
				}

			}
			
		});
		
		first.add(new JLabel("현재 비밀번호"));
		first.add(txtPW);
		second.add(new JLabel("새로운 비밀번호"));
		second.add(txtNewPW);
		third.add(new JLabel("다시 입력"));
		third.add(txtCheckPW);
		
		fourth.add(btnOk);
		
		center.add(first);
		center.add(second);
		center.add(third);
		center.add(fourth);
		
		pwChangeJFrame.addContain();
	}
	
	private void deleteUser() {
		String msgText = "정말로 탈퇴하시겠습니까?";
		JLabel[] msg = new JLabel[] {new JLabel(msgText) };
		JButton btnCheck = new JButton("확인");
		DefaultJFrame error = new DefaultJFrame("알림", 350, 120, msg, btnCheck, true);
		btnCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = "delete from user where user_no = " + userDTO.get(0).getUserNo();
				int reuslt = userDAO.userUpdate(query);
				if(reuslt != 0) {
					new LogInJFrame(false);
					error.dispose();
					jframe.dispose();
					System.out.println("성공적으로 삭제됨");
				}else {
					String text = "삭제하는데 실패하였습니다.";
					JLabel[] msg0 = new JLabel[] {new JLabel(text)};
					JButton btnOk = new JButton("확인");
					DefaultJFrame error2 = new DefaultJFrame("알림", 350, 120, msg, btnCheck, false);
					btnCheck.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error2.dispose();
						}
					});
					error.dispose();
					System.out.println("삭제하는데 실패하였습니다.");
				}
				
			}
		});
		error.addContaionEmpty();
	}
	
}