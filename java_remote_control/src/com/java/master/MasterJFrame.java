package com.java.master;

import java.awt.CardLayout;
import java.awt.Color;
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
import javax.swing.JTextField;
import javax.swing.Timer;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
import com.java.jframe.DefaultJFrame;
import com.java.jframe.LogInJFrame;
import com.java.master.thread.MasterConnectThread;
import com.java.utility.IpAddress;
import com.java.utility.RegularExpression;

public class MasterJFrame extends JFrame {
	// ����� �����̺꿡 ���� ������ ��� JLabel
	private JLabel labelIP = null
			, labelHost = null;
	
	private JButton btnDisConnect = null;
	
	// �����Ϳ� ���� ������ ��� JLabel
	private JLabel labelMyIp = null
			, labelMyHost = null
			, labelMyPort = null;
	
	private JButton btnStart = null;
	private UserDAO userDAO = null;
	private ArrayList<UserDTO> userDTO = null;
	private String id = null, pw = null;
	private int userNo = 0; 						//������ȣ
	private Timer timer = null; 					// ����ð��� ����ϱ� ���� Ÿ�̸�
	private ServerSocket serverSocket = null;		// ��������
	private MasterConnectThread connectThread =  null;
	private final static int PORT = 9095;			// ��Ʈ
	private DefaultJFrame jframe = null;			
	private JPanel west = null, east = null;
	private CardLayout card = null;					// ���� ��ġ������
	private JPanel eastContent = null;				// ���� ����
	private MasterJFrame myJFrame = null;			// �ڱ��ڽ�
	
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
	
	//�׽�Ʈ��
	public MasterJFrame(String id, String pw) {
		this.id = id;
		this.pw = pw;
		
		userDAO= new UserDAO();
		userDTO = userDAO.userSelect( "where user_id = '" + id +"' and user_password = '"+pw+"'");
		
		setUI1();
		// �����Ǹ� �ش� ������ �������� myJFrame��
		myJFrame = this;
	}

	private void setUI1() {
		jframe = new DefaultJFrame("������ ���α׷�", 700, 400);
		jframe.setPanel();
		
		userNo = userDTO.get(0).getUserNo();
		
		east = jframe.getEastPanel();
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
		eastContent.add(setEastOption(),"option");
		
		eastContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		card.show(eastContent, "controller");
		
		return eastContent;
		
	}
	// ���ʺκ� ����
	private JPanel setEastController() {
		
		// �׷�ڽ� ���� ����
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// ���� ������ ���� ����
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// �����ֱ� �ð� �ݴ� ����
		eastContent.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

		btnStart = new JButton("ON");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {

					if(btnStart.getText().equals("ON")) {
						btnStart.setText("OFF");
						serverSocket = new ServerSocket(PORT);
//						connectThread = new MasterConnectThread(serverSocket, labelIP, labelHost, jframe);
						connectThread = new MasterConnectThread(serverSocket,myJFrame);
						connectThread.start();
						
					}else {
						// ���� �������� ���°� ����Ǿ����� �ʴٸ� ����
						if(connectThread.getState() != Thread.State.TERMINATED) connectThread.interrupt();
						// ���� ���������� �����ִ��� Ȯ��
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
		
		// �׷�ڽ� ���� ����
		JPanel group = jframe.addGroupBox(Color.black,2,true);
		
		// ���� ������ ���� ����
		JPanel eastContent = new JPanel();

		eastContent.setLayout(new GridLayout(0, 2));
		// �����ֱ� �ð� �ݴ� ����
		eastContent.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

		btnDisConnect = new JButton("��������");
		btnDisConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { // ���� ����Ǿ��ִ� ������ ���� ���� �ٽ� ����
				if(connectThread.getState() != Thread.State.TERMINATED) {
					connectThread.interrupt();
					System.out.println("connectThread ���ͷ�Ʈ");
					
					labelIP.setText("000.000.000.000");
					labelHost.setText("Unknown");
					
					connectThread =  new MasterConnectThread(serverSocket,myJFrame);
					connectThread.start();

					System.out.println("connectThread �ٽý���");
				}else {
					System.out.println("���� ������ �Ǿ����� �ʽ��ϴ�.");
				}
				
			}

		});
		

		eastContent.add(new JLabel("Host "));
		eastContent.add(labelHost = new JLabel("������"));	
		eastContent.add(new JLabel("IP  "));
		eastContent.add(labelIP = new JLabel("000.000.000.000"));
		eastContent.add(new JLabel("Access"));
		eastContent.add(new JLabel("00/00/00/00:00 00��"));
		eastContent.add(btnDisConnect);

		group.add(eastContent);
		return group;
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
				new LogInJFrame(false);
				jframe.dispose();
			}
			
		});
		JPanel logoutPart = new JPanel();
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
	
	private JPanel setEastOption() {
		// �׷�ڽ� ���� ����
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
		
		// ù��° ����
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
	
	// ���ʺκ� ����
	private JPanel setWest() {
		
		JPanel westContent = new JPanel();
		
		JButton btnController = new JButton("Controller");
		JButton btnNetwork = new JButton("Network");
		JButton btnAccount = new JButton("Account");
		JButton btnOption = new JButton("Option");
		
		westContent.setLayout(new GridLayout(0,1));
		westContent.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
		
		btnController.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Controller Ŭ��");
				card.show(eastContent,"controller");
			}
			
		});
		
		btnNetwork.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Network Ŭ��");
				card.show(eastContent,"network");
			}
			
		});
		
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Account Ŭ��");
				card.show(eastContent,"account");
			}
			
		});
		
		btnOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Option Ŭ��");
				card.show(eastContent,"option");
			}
			
		});
		
		westContent.add(btnController);
		westContent.add(btnNetwork);
		westContent.add(btnAccount);
		westContent.add(btnOption);
		
		return westContent;
	}
	
	// ���������� �����ִ��� Ȯ���ϱ� ���� �Լ�
	public static boolean isOpen(ServerSocket server) {
		return server.isBound() && !server.isClosed();
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
								new LogInJFrame(false);
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
	
	
}