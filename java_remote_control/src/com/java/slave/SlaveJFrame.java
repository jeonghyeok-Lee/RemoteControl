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
	private boolean nonLogIn = false; // ��ȸ������ �����ߴ��� Ȯ�ο� ��ȸ�� = true
	private boolean disconnect = false;	// �����̺꿡�� ����������ư�� Ŭ���ߴ��� Ȯ�ο�

	private SlaveJFrame myJFrame = null;
	private CardLayout cardCenter = null;
	private JPanel centerContent = null;
	private CardLayout cardEast = null;
	private JPanel eastContent  = null;
	
	private JPanel east = null; // ȸ������ �α��ν� ������ ������Ʈ�� ���� �� �ֵ��� �����̳ʸ� ������� ���� 
	
	private ArrayList<UserDTO> userDTO = null;	// ȸ�������� ��� ArrayList
	private UserDAO userDAO = null;
	private int userNo = 0; 						//������ȣ
	
	// �����忡�� ����ϱ� ���� ������
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
	
	// ��ȸ���� ���� ������
	public SlaveJFrame() {
		nonLogIn = true;
		setUI();
		myJFrame = this;
		System.out.println("��ȸ������ ����");
	}
	
	// ȸ���� ���� ������
	public SlaveJFrame(ArrayList<UserDTO> userDTO, UserDAO userDAO) {
		this.userDTO = userDTO;
		this.userDAO = userDAO;
		setUI();
		myJFrame = this;
		System.out.println("ȸ������ ����");
	}
	
	// �׽�Ʈ��
	public SlaveJFrame(String id , String pw) {
		userDAO= new UserDAO();
		userDTO = userDAO.userSelect( "where user_id = '" + id +"' and user_password = '"+pw+"'");
		userNo = userDTO.get(0).getUserNo();
		setUI();
		myJFrame = this;
		System.out.println("ȸ������ ����");
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
		jframe = new DefaultJFrame("�����̺� ���α׷�", x, y);
		jframe.setPanel();
		JPanel center = jframe.getCenterPanel();
		JPanel west = jframe.getWestPanel();
		JPanel east = jframe.getEastPanel();
		
		center.setLayout(new GridLayout(0,1));
		center.add(setCenter());
		
		// ȸ���� ���
		if(!nonLogIn) {
			west.add(setWest());
			east.add(setEast());
		}
		
		jframe.addContain();
		
	}

	// �߾� ������Ʈ�� ����
	private JPanel setCenter() {
		
		cardCenter = new CardLayout();
		
		JPanel group = new JPanel(new GridLayout(0,1));
		
		centerContent = new JPanel(cardCenter);
		
		centerContent.add(setCenterConnected(),"connect");
		centerContent.add(setCenterDisConnected(),"disconnect");
		
		cardCenter.show(centerContent, "disconnect");
		
		JPanel rap = new JPanel(new GridLayout(0,3));

		button = new JButton("����");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					if(socket == null) { // �����ϱ� ��ư�� ó�� ���� ��� --> �ѹ��� ������ ������ ����
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
						// socket�� null�� �ƴ� ��� ��ư�� ���ȴ� ���� ���� -> ����� ����� ���� 
						// -> isClose()�� ������<���� ����� ���� ���°�� ������ ������������ false ��ȯ>�ؼ�
						if(!socket.isClosed()) { // ������ ���� �����ִٸ�		
							System.out.println("������ ��������");
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
							
						}else { 							// ������ ���� �����ִٸ�
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
	
	// ����� �����϶��� ������ �����ִ� �ǳ�
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
	
	// ������� ���� �����϶��� ������ �����ִ� �ǳ�
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
		// �׷�ڽ� ���� ����
				JPanel group = jframe.addGroupBox(Color.black,2,true);
				
				JPanel eastContent = new JPanel(new GridLayout(0,1));
				
				// �� ���ٸ��� �г��� �߰��Ͽ� ������
				JPanel line[] = new JPanel[] {
						new JPanel(),new JPanel(),new JPanel(),new JPanel(),new JPanel()
				};
				
				
				// ��й�ȣ ����� ��ư
				JButton btnChangePassword = new JButton("change");
				btnChangePassword.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						passwordChangeJFrame();
					}
					
				});
				
				// ��й�ȣ ����κ��� ������ ����
				JPanel pwPart = new JPanel();
				JLabel password = new JLabel(userDTO.get(0).getUserPassword());
				pwPart.add(password);
				pwPart.add(btnChangePassword);
				
				// �α׾ƿ��κ� ������ ���� 
				// �α׾ƿ� ��ư
				JButton btnLogout = new JButton("LogOut");
				btnLogout.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						new LogInJFrame(true);
						jframe.dispose();
					}
					
				});
				JButton btnWithdrawal = new JButton("Ż��");
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
				
				// �� �г��� ������Ʈ �߰�
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
	
	// ���� ������Ʈ�� ����
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
				System.out.println("Record Ŭ��");
				cardEast.show(eastContent,"record");
			}
			
		});
		
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Account Ŭ��");
				cardEast.show(eastContent,"account");
			}
			
		});
		
//		btnOption.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("Option Ŭ��");
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

		int userNo = userDTO.get(0).getUserNo(); 																	// user���̺��� ������ȣ
		ConnectUserDAO connectDAO = new ConnectUserDAO();
		ArrayList<ConnectUserDTO> connectDTO = connectDAO.connectSelect("where connect_slave = "+ userNo);			// connect_user ���̺��� slave�� ����Ǿ� �ִ� connect ��ȣ�� Ȯ��
		int connectCount = connectDAO.getConnectRow("where connect_slave = "+ userNo);								// connect_user ���̺��� slave�� ��������� �ִ� �÷� ���� Ȯ��
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
		// �׷�ڽ� ���� ����
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
		
		// ù��° ����
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
	
	
	// ��й�ȣ ������ ���� JFrame ����
	private void passwordChangeJFrame() {
		DefaultJFrame pwChangeJFrame = new DefaultJFrame("��й�ȣ ����",400,250);
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
		
		// ��й�ȣ ������ ���� Ȯ�ι�ư
		// ���� ��й�ȣ�� �´��� �ľ� + �´ٸ� ���ο��й�ȣ�� ��й�ȣ�� ����
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msgText = "";
				RegularExpression regex = new RegularExpression(txtPW.getText(),"pw");
				// ���� �Է� ��й�ȣ�� �´ٸ�
				if(userDTO.get(0).getUserPassword().equals(txtPW.getText())) {
					String query = "update user set user_password = '" +txtNewPW.getText()+"' where user_no = "+userNo;
					int result = userDAO.userUpdate(query);

					if(result != 0) {	// ������ ���������� �̷�����ٸ�
						msgText = "���濡 �����Ͽ����ϴ�.";
						JLabel[] msg = new JLabel[] {new JLabel(msgText) };
						JButton btnCheck = new JButton("Ȯ��");
						DefaultJFrame error = new DefaultJFrame("�˸�", 350, 120, msg, btnCheck, false);
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
					// �Է� ��й�ȣ�� �ƴϾ��� �� �̶��
					if(!userDTO.get(0).getUserPassword().equals(txtPW.getText())) {
						msgText = "�Է��Ͻ� ��й�ȣ�� ���� ��й�ȣ�� �ƴմϴ�.";
					}else if(!txtNewPW.equals(txtCheckPW)) {
						msgText = "���� �Է��Ͻ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
					}else if(!regex.isRegex()){
						msgText = "8 ~ 20���� �̳��� �ۼ����ּ���.";
					}else {
						msgText = "�� ������ ������ �������� �ʾҽ��ϴ�.";
					}
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnCheck = new JButton("Ȯ��");
					DefaultJFrame error = new DefaultJFrame("�˸�", 350, 120, msg, btnCheck, false);
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
		
		first.add(new JLabel("���� ��й�ȣ"));
		first.add(txtPW);
		second.add(new JLabel("���ο� ��й�ȣ"));
		second.add(txtNewPW);
		third.add(new JLabel("�ٽ� �Է�"));
		third.add(txtCheckPW);
		
		fourth.add(btnOk);
		
		center.add(first);
		center.add(second);
		center.add(third);
		center.add(fourth);
		
		pwChangeJFrame.addContain();
	}

	private void deleteUser() {
		String msgText = "������ Ż���Ͻðڽ��ϱ�?";
		JLabel[] msg = new JLabel[] {new JLabel(msgText) };
		JButton btnCheck = new JButton("Ȯ��");
		DefaultJFrame error = new DefaultJFrame("�˸�", 350, 120, msg, btnCheck, true);
		btnCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = "delete from user where user_no = " + userDTO.get(0).getUserNo();
				int reuslt = userDAO.userUpdate(query);
				if(reuslt != 0) {
					new LogInJFrame(true);
					error.dispose();
					jframe.dispose();
					System.out.println("���������� ������");
				}else {
					String text = "�����ϴµ� �����Ͽ����ϴ�.";
					JLabel[] msg0 = new JLabel[] {new JLabel(text)};
					JButton btnOk = new JButton("Ȯ��");
					DefaultJFrame error2 = new DefaultJFrame("�˸�", 350, 120, msg, btnCheck, false);
					btnCheck.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error2.dispose();
						}
					});
					error.dispose();
					System.out.println("�����ϴµ� �����Ͽ����ϴ�.");
				}
				
			}
		});
		error.addContaionEmpty();
	}
	
}
