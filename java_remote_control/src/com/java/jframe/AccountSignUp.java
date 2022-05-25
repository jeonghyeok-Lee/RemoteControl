package com.java.jframe;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
import com.java.utility.EmailSend;

public class AccountSignUp extends JFrame {
	private int number = 0; // ������ȣ�� ��� ����
	private DefaultJFrame jframe = null;
	private int count = 0; // ������û�� �õ��ߴ��� üũ�ϱ� ���� ����

	UserDAO dao = null;
	ArrayList<UserDTO> dto = null;

	public AccountSignUp() {
		setUI();
	}

	private void setUI() {
		jframe = new DefaultJFrame("ȸ������", 600, 400);
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
				if (dto.isEmpty()) {
					msgText = "��밡���� ���̵��Դϴ�.";
					System.out.println(msgText);
					
				}
				else {
					msgText = "���̵� �����մϴ�.";
					System.out.println(msgText);					
				}
				
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("Ȯ��");
				DefaultJFrame error = new DefaultJFrame("�˸�", 250, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // ���� â�ݱ�
						// �����ϰ� dispose�� �����ϸ� �׷��� �ڵ� �����ʿ�
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
				if (txtPW.getText().equals(txtPWCheck.getText())) {
					msgText = "��й�ȣ�� ��ġ�մϴ�.";
					System.out.println("��й�ȣ�� ��ġ�մϴ�.");
				} else {
					msgText = "��й�ȣ�� Ʋ���ϴ�.";
					System.out.println("��й�ȣ�� Ʋ���ϴ�.");
				}
				
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("Ȯ��");
				DefaultJFrame error = new DefaultJFrame("�˸�", 250, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // ���� â�ݱ�
						// �����ϰ� dispose�� �����ϸ� �׷��� �ڵ� �����ʿ�
					}
				});
				error.addContaionEmpty();

			}

		});

		JButton btnSign = new JButton("�����ϱ�");
		btnSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ������û��ư�� Ŭ���Ǿ��� �� ����ǵ��� �����ؾ���
				if (count > 0) {
					System.out.println(number);
					if (txtEmailCheck.getText().equals(Integer.toString(number))) {
						System.out.println("������ȣ�� �����մϴ�.");
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
							// �����ϰ� dispose�� �����ϸ� �׷��� �ڵ� �����ʿ�
						}

					});
					error.addContaionEmpty();

					System.out.println("������û�� �õ����ּ���");
				}

			}

		});

		JButton btnCheckEmail = new JButton("������û");
		btnCheckEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userAddress = txtEmail.getText() + cmbEmail.getSelectedItem();
				System.out.println(userAddress);
				number = (int) (Math.random() * 9999) + 1;
				System.out.println(number);
				EmailSend emailSend = new EmailSend(userAddress, "������ȣ", "������ȣ : " + number + "�Դϴ�.");
				emailSend.setSend();
				count++;
			}

		});

		JComponent[] compornet = new JComponent[] { new JLabel("ID"), txtID, btnCheckID, new JLabel("PW"), txtPW,
				new JLabel(), new JLabel("PW Ȯ��"), txtPWCheck, btnCheckPW, new JLabel("Name"), txtName, new JLabel(),
				new JLabel("Email"), txtEmail, cmbEmail, new JLabel("Email Check"), txtEmailCheck, btnCheckEmail,
				new JLabel(), new JLabel(), new JLabel(), new JLabel(), btnSign, new JLabel() };

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

}
