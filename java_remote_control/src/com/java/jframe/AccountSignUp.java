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
	private int number = 0; // 인증번호를 담는 변수
	private DefaultJFrame jframe = null;
	private int count = 0; // 인증요청을 시도했는지 체크하기 위한 변수

	UserDAO dao = null;
	ArrayList<UserDTO> dto = null;

	public AccountSignUp() {
		setUI();
	}

	private void setUI() {
		jframe = new DefaultJFrame("회원가입", 600, 400);
		// x 클릭시 현재 프레임만 종료
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

		JButton btnCheckID = new JButton("중복 확인");
		btnCheckID.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dto = dao.userSelect("where user_id = '" + txtID.getText() + "'");
				String msgText = "";
				if (dto.isEmpty()) {
					msgText = "사용가능한 아이디입니다.";
					System.out.println(msgText);
					
				}
				else {
					msgText = "아이디가 존재합니다.";
					System.out.println(msgText);					
				}
				
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("확인");
				DefaultJFrame error = new DefaultJFrame("알림", 250, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // 현재 창닫기
						// 간단하게 dispose가 가능하면 그렇게 코드 수정필요
					}
				});
				error.addContaionEmpty();
			}

		});

		JButton btnCheckPW = new JButton("비밀번호 확인");
		btnCheckPW.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msgText = "";
				if (txtPW.getText().equals(txtPWCheck.getText())) {
					msgText = "비밀번호가 일치합니다.";
					System.out.println("비밀번호가 일치합니다.");
				} else {
					msgText = "비밀번호가 틀립니다.";
					System.out.println("비밀번호가 틀립니다.");
				}
				
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("확인");
				DefaultJFrame error = new DefaultJFrame("알림", 250, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // 현재 창닫기
						// 간단하게 dispose가 가능하면 그렇게 코드 수정필요
					}
				});
				error.addContaionEmpty();

			}

		});

		JButton btnSign = new JButton("가입하기");
		btnSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 인증요청버튼이 클릭되었을 때 실행되도록 설정해야함
				if (count > 0) {
					System.out.println(number);
					if (txtEmailCheck.getText().equals(Integer.toString(number))) {
						System.out.println("인증번호가 동일합니다.");
					}
				} else {
					// 경고 메시지 출력을 우한 frame 생성
					JLabel[] msg = new JLabel[] { 
							new JLabel("인증요청이 진행되지 않았습니다.")
							, new JLabel("인증요청을 시도해주세요") };
					
					JButton btnOk = new JButton("확인");

					DefaultJFrame error = new DefaultJFrame("경고", 250, 150, msg, btnOk, false);

					btnOk.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							error.frameDispose(); // 현재 창닫기
							// 간단하게 dispose가 가능하면 그렇게 코드 수정필요
						}

					});
					error.addContaionEmpty();

					System.out.println("인증요청을 시도해주세요");
				}

			}

		});

		JButton btnCheckEmail = new JButton("인증요청");
		btnCheckEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userAddress = txtEmail.getText() + cmbEmail.getSelectedItem();
				System.out.println(userAddress);
				number = (int) (Math.random() * 9999) + 1;
				System.out.println(number);
				EmailSend emailSend = new EmailSend(userAddress, "인증번호", "인증번호 : " + number + "입니다.");
				emailSend.setSend();
				count++;
			}

		});

		JComponent[] compornet = new JComponent[] { new JLabel("ID"), txtID, btnCheckID, new JLabel("PW"), txtPW,
				new JLabel(), new JLabel("PW 확인"), txtPWCheck, btnCheckPW, new JLabel("Name"), txtName, new JLabel(),
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
