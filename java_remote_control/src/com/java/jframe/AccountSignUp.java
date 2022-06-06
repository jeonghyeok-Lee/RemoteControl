package com.java.jframe;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
import com.java.utility.EmailSend;

// ȸ�������� ���� JFrame
public class AccountSignUp extends JFrame {
	private boolean checkProgram = false; 								// ���������� �����̺����� ������ ���� ������ - false / �����̺� - true
	private int number = 0; 											// ������ȣ�� ��� ����
	private DefaultJFrame jframe = null;
	private int count = 0; 												// ������û�� �õ��ߴ��� üũ�ϱ� ���� ����
	private boolean chkID = false, chkPw = false, chkEmail = false;  

	UserDAO dao = null;
	ArrayList<UserDTO> dto = null;

	public AccountSignUp(boolean checkProgram) {
		this.checkProgram = checkProgram;
		setUI();
	}

	private void setUI() {
		jframe = new DefaultJFrame("ȸ������", 600, 400);
		jframe.setPanel();
		
		// x Ŭ���� ���� �����Ӹ� ����
		jframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		dao = new UserDAO();

		JPanel center = jframe.getCenterPanel();

		center.add(setCenter());

		jframe.addContain();

	}
	
	private JPanel setCenter() {

		JPanel group = jframe.addGroupBox(Color.white, 1, true);

		JPanel centerContent = new JPanel(new GridLayout(0, 3, 10, 5));

		JTextField txtID = new JTextField(15);
		JPasswordField txtPW = new JPasswordField(15);
		JPasswordField txtPWCheck = new JPasswordField(15);
		JTextField txtName = new JTextField(15);
		JTextField txtEmail = new JTextField(15);
		JTextField txtEmailCheck = new JTextField(15);
		
		JComboBox<String> cmbEmail = new JComboBox<String>();

		cmbEmail.addItem("@gmail.com");
		cmbEmail.addItem("@naver.com");
		cmbEmail.addItem("@kyungmin.ac.kr");

		JButton btnCheckID = new JButton("�ߺ� Ȯ��");
		btnCheckID.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dto = dao.userSelect("where user_id = '" + txtID.getText() + "'");
				String msgText = "";
				String txt = txtID.getText();
				int txtlength = txt.length();
				
				System.out.println("���� ��� ���� : "+checkPattern(txt, "id"));
				
				// ���ڿ� ���� ����
				if(txtlength <= 20 &&txtlength >= 8) {
					// db�� ���� �ʰ� ���Ͽ� ��߳��� �ʾ��� ���
					if (dto.isEmpty() && checkPattern(txtID.getText(),"id")) {
						msgText = "��밡���� ���̵��Դϴ�.";
						chkID = true;
					}
					else if(!dto.isEmpty()) {
						msgText = "���̵� �����մϴ�.";	
						txtID.setText("");
					}else if(!checkPattern(txt,"id")) {
						msgText = "���̵� �����ڿ� ���ڷθ� �ۼ����ּ���.";
						txtID.setText("");
					}
				}else {
					msgText = "8 ~ 20���� �̳��� �ۼ����ּ���.";
				}
				
				System.out.println(msgText);	
				
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("Ȯ��");
				DefaultJFrame error = new DefaultJFrame("�˸�", 300, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // ���� â�ݱ�
						
					}
				});
				error.addContaionEmpty();
			}

		});

		JButton btnCheckPW = new JButton("��й�ȣ Ȯ��");
		btnCheckPW.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msgText = "";
				String txt = txtPW.getText();
				int txtlength = txt.length();
				
				System.out.println("���� ��� ���� : "+checkPattern(txt, "pw"));
				
				if(txtlength <= 20 && txtlength >= 8) {
					if (txt.equals(txtPWCheck.getText()) && checkPattern(txt,"pw")) {
						msgText = "��й�ȣ�� ��ġ�մϴ�.";
						chkPw = true;
					} else {
						msgText = "��й�ȣ�� Ʋ���ϴ�.";
					}
				}else {
					msgText = "8 ~ 20���� �̳��� �ۼ����ּ���.";
				}
				
				System.out.println(msgText);
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("Ȯ��");
				DefaultJFrame error = new DefaultJFrame("�˸�", 250, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // ���� â�ݱ�
					}
				});
				error.addContaionEmpty();
			}

		});

		JButton btnCheckEmail = new JButton("������û");
		btnCheckEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msgText = "";
				if(!checkPattern(txtEmail.getText(),"email")) {
					msgText = "�̸����� �ùٸ��� �ۼ����� �ʾҽ��ϴ�.";
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnOk = new JButton("Ȯ��");
					DefaultJFrame error = new DefaultJFrame("���", 280, 120, msg, btnOk, false);
					btnOk.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.frameDispose(); // ���� â�ݱ�
						}
					});
					error.addContaionEmpty();
				}else{
//					String userAddress = txtEmail.getText() + cmbEmail.getSelectedItem();
					String userAddress = txtEmail.getText();
					System.out.println(checkPattern(userAddress,"email"));
					System.out.println(userAddress);
					number = (int) (Math.random() * 9999) + 1;
					System.out.println(number);
					EmailSend emailSend = new EmailSend(userAddress, "������ȣ", "������ȣ : " + number + "�Դϴ�.");
					emailSend.setSend();
					count++;
				}
			}

		});
		
		JButton btnEmailOK = new JButton("Ȯ��");
		btnEmailOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ������û��ư�� Ŭ���Ǿ��� �� ����ǵ��� �����ؾ���
				if (count > 0) {
					System.out.println(number);
					if (txtEmailCheck.getText().equals(Integer.toString(number))) {
						System.out.println("������ȣ�� �����մϴ�.");
						chkEmail = true;
					}else {
						System.out.println("������ȣ�� �ٸ��ϴ�.");
					}
				} else {
					// ��� �޽��� ����� ���� frame ����
					JLabel[] msg = new JLabel[] { 
							new JLabel("������û�� ������� �ʾҽ��ϴ�.")
							, new JLabel("������û�� �õ����ּ���") };
					
					JButton btnOk = new JButton("Ȯ��");

					DefaultJFrame error = new DefaultJFrame("���", 250, 150, msg, btnOk, false);

					btnOk.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							error.frameDispose(); // ���� â�ݱ�
						}

					});
					error.addContaionEmpty();
				}
				
			}
			
		});
		
		JButton btnSign = new JButton("�����ϱ�");
		btnSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dto = dao.userSelect("");
				int result = 0;
				int rowCount = dto.size();
				String msgText = "";
				System.out.println(rowCount);
				// ���̵� ��ø/��й�ȣ ��ġ ���� / ������ȣ ��û �Ϸ�� ����
				if(chkID && chkPw && chkEmail) {
					String query = "";
					System.out.println("������ �����ϰ� �ֽ��ϴ�.");
					dto = dao.userSelect("");
					rowCount = dto.size();
					query = "insert into user(user_no,user_id,user_password,user_name,user_email,user_rank) values("
							+(++rowCount)+",'"
							+txtID.getText()
							+"','"
							+txtPW.getText()
							+"','"
							+txtName.getText()
							+"','"
							+txtEmail.getText();
					if(!checkProgram) {
						query += "','������')";
					}
					else {
						query += "','�����̺�')";
					}
					result = dao.userUpdate(query);
					if(result != 0) {
						msgText = "���Կ� �����Ͽ����ϴ�.";
					}else {
						msgText = "���Կ� �����Ͽ����ϴ�.";
					}
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnOk = new JButton("Ȯ��");
					DefaultJFrame error = new DefaultJFrame("�˸�", 300, 120, msg, btnOk, false);
					btnOk.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.frameDispose(); // ���� â�ݱ�
						}
					});
					error.addContaionEmpty();
				}
			}
		});


		JComponent[] compornet = new JComponent[] { 
				new JLabel("ID"), txtID, btnCheckID
				, new JLabel("PW"), txtPW,new JLabel()
				, new JLabel("PW Ȯ��"), txtPWCheck, btnCheckPW
				, new JLabel("Name"), txtName, new JLabel()
				, new JLabel("Email"), txtEmail, btnCheckEmail
				, new JLabel("Email Check"), txtEmailCheck, btnEmailOK
				, new JLabel(), new JLabel(), new JLabel()
				, new JLabel(), btnSign, new JLabel() };

		for (JComponent com : compornet) {
			if (com instanceof JLabel) {
				((JLabel) com).setHorizontalAlignment(0);
			}

			if (com instanceof JButton) {
				com.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
			}

			centerContent.add(com);
		}

		group.add(centerContent);

		return group;
	}
	
	// ���Խ�
	public boolean checkPattern(String str, String part) {
		String pattern ="";
		if(part == "id")
			pattern = "^[\\w]*{8,20}$";
		else if(part == "pw")
			pattern = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{8,20}$";
		else if(part == "email")
			pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
		
		boolean regex = Pattern.matches(pattern, str);
		return regex;
	}

}
