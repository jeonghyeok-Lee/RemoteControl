package com.java.slave;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.java.db.dao.ConnectUserDAO;
import com.java.db.dao.UsageRecordDAO;
import com.java.db.dao.UserDAO;
import com.java.db.dto.ConnectUserDTO;
import com.java.db.dto.UsageRecordDTO;
import com.java.db.dto.UserDTO;
import com.java.jframe.origianl.DefaultJFrame;
import com.java.jframe.origianl.LogInJFrame;
import com.java.slave.thread.SlaveReadingThread;
import com.java.slave.thread.SlaveWritingThread;
import com.java.utility.RegularExpression;

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
	private CardLayout cardCenter = null;
	private JPanel centerContent = null;
	private CardLayout cardEast = null;
	private JPanel eastContent  = null;
	
	private JPanel east = null; // 회원으로 로그인시 우측에 컴포넌트가 보일 수 있도록 컨테이너를 잡기위한 변수 
	
	private ArrayList<UserDTO> userDTO = null;	// 회원정보가 담긴 ArrayList
	private UserDAO userDAO = null;
	private int userNo = 0; 						//유저번호
	
	// 스레드에서 사용하기 위한 생성자
	public boolean isDisconnect() {
		return disconnect;
	}
	public JButton getButton() {
		return button;
	}
	
	public CardLayout getCard() {
		return cardCenter;
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
	
	// 테스트용
	public SlaveJFrame(String id , String pw) {
		userDAO= new UserDAO();
		userDTO = userDAO.userSelect( "where user_id = '" + id +"' and user_password = '"+pw+"'");
		userNo = userDTO.get(0).getUserNo();
		setUI();
		myJFrame = this;
		System.out.println("회원으로 접속");
	}
	

	private void setUI() {
		int x = 0;
		int y =0;
		if(nonLogIn) {
			x = 450;
			y = 350;
		}else {
			x = 900;
			y = 400;
		}
		jframe = new DefaultJFrame("슬레이브 프로그램", x, y);
		jframe.setPanel();
		JPanel center = jframe.getCenterPanel();
		JPanel west = jframe.getWestPanel();
		JPanel east = jframe.getEastPanel();
		
		center.setLayout(new GridLayout(0,1));
		center.add(setCenter());
		
		// 회원일 경우
		if(!nonLogIn) {
			west.add(setWest());
			east.add(setEast());
		}
		
		jframe.addContain();
		
	}

	// 중앙 컴포넌트를 설정
	private JPanel setCenter() {
		
		cardCenter = new CardLayout();
		
		JPanel group = new JPanel(new GridLayout(0,1));
		
		centerContent = new JPanel(cardCenter);
		
		centerContent.add(setCenterConnected(),"connect");
		centerContent.add(setCenterDisConnected(),"disconnect");
		
		cardCenter.show(centerContent, "disconnect");
		
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
						cardCenter.show(centerContent, "connect");
						
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
							cardCenter.show(centerContent, "disconnect");
							
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
							cardCenter.show(centerContent, "connect");
						}
					}
				}catch(Exception e1) {
					e1.getMessage();
				}
			}
		});

		JComponent[] component = new JComponent[] { 
				new JLabel(), new JLabel(), new JLabel()
				, new JLabel(), button,new JLabel()
				, new JLabel(), new JLabel(), new JLabel() 
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
		
		first.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 10));
		second.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 10));
		
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
		
		first.add(new JLabel());
		first.add(new JLabel());
		first.add(new JLabel("Connect Server IP   : "));
		first.add(txtIp = new JTextField(15));
		second.add(new JLabel("Connect Server Port : "));
		second.add(txtPort = new JTextField(15));
		second.add(new JLabel());
		second.add(new JLabel());
		
		panel.add(first);
		panel.add(second);
		
		return panel;
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
						new LogInJFrame(true);
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
	
	// 동쪽 컴포넌트를 설정
	private JPanel setEast() {
		
		JPanel group = jframe.addGroupBox(Color.black, 1, true);
		
		cardEast = new CardLayout();
		
		eastContent = new JPanel(cardEast);
		
		eastContent.add(setEastRecord(),"record");
		eastContent.add(setEastAccount(),"account");
//		eastContent.add(setEastOption(),"option");
		
		group.add(eastContent);
		
		return group;
	}

	private JPanel setWest() {
		JPanel westContent = new JPanel();
		
		JButton btnController = new JButton("Record");
		JButton btnAccount = new JButton("Account");
//		JButton btnOption = new JButton("Option");
		
		westContent.setLayout(new GridLayout(0,1));
		westContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
		
		btnController.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Record 클릭");
				cardEast.show(eastContent,"record");
			}
			
		});
		
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Account 클릭");
				cardEast.show(eastContent,"account");
			}
			
		});
		
//		btnOption.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("Option 클릭");
//				cardEast.show(eastContent,"option");
//			}
//			
//		});
		
		westContent.add(btnController);
		westContent.add(new JLabel());
		westContent.add(btnAccount);
//		westContent.add(btnOption);
		
		return westContent;
	}
	
	private JPanel setEastRecord() {
		
		JPanel group = jframe.addGroupBox(Color.black, 1, true);
		
		JPanel eastContent = new JPanel();
		
		JPanel recordGroup = jframe.addGroupBox(Color.black, 2, true);
		JPanel recordContent = new JPanel();
		
		recordGroup.setLayout(new GridLayout(0,1));
		recordGroup.setPreferredSize(new Dimension(300,200));

		int userNo = userDTO.get(0).getUserNo(); 																	// user테이블에서 유저번호
		ConnectUserDAO connectDAO = new ConnectUserDAO();
		ArrayList<ConnectUserDTO> connectDTO = connectDAO.connectSelect("where connect_slave = "+ userNo);			// connect_user 테이블에서 slave와 연결되어 있는 connect 번호를 확인
		int connectCount = connectDAO.getConnectRow("where connect_slave = "+ userNo);								// connect_user 테이블에서 slave와 연결된적이 있는 컬럼 개수 확인
		System.out.println(connectCount);
		
		UsageRecordDAO usageDAO = new UsageRecordDAO();
		System.out.println("1");
		ArrayList<UsageRecordDTO> usageDTO;
		for(int i = 0; i<=connectCount; i++) {
			int masterNo = connectDTO.get(i).getConnectMaster();
			ArrayList<UserDTO>newUserDTO = userDAO.userSelect("where user_no = " + masterNo);
			usageDTO = usageDAO.recordSelect("where usage_connect = " + connectDTO.get(i).getConnectNo());		
			String record = usageDTO.get(i).getUsageRecord();
			String[] recordSplit = record.split("/");
			recordContent.add(new JLabel(i+1 + ". "+ newUserDTO.get(i).getUserId() +" - "+recordSplit[i]));
		}
	
		recordGroup.add(recordContent);
		
		
		JScrollPane scrollPane = new JScrollPane(recordGroup, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel frist = new JPanel(new GridLayout(0, 1));
		JPanel line1 = new JPanel(new GridLayout(0,2));
		line1.add(new JLabel("Record"));
		line1.add(new JLabel(""));
		frist.add(line1);
		
		JPanel second = new JPanel();
		second.add(scrollPane);
		
		eastContent.add(frist);
		eastContent.add(second);
		
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
		
	
		JPanel buttonPanel = new JPanel();
		JButton btnClearOption = new JButton("Clear");
		JButton btnOKOption = new JButton("OK");
		
		buttonPanel.add(btnClearOption);
		buttonPanel.add(btnOKOption);
		
		// 첫번째 라인
		JPanel first = new JPanel();
		
		JComponent[] component = new JComponent[] {
			first
			
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
								new LogInJFrame(true);
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
					new LogInJFrame(true);
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
