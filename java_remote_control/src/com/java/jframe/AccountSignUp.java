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

// 회원가입을 위한 JFrame
public class AccountSignUp extends JFrame {
	private boolean checkProgram = false; 								// 마스터인지 슬레이브인지 구분을 위한 마스터 - false / 슬레이브 - true
	private int number = 0; 											// 인증번호를 담는 변수
	private DefaultJFrame jframe = null;
	private int count = 0; 												// 인증요청을 시도했는지 체크하기 위한 변수
	private boolean chkID = false, chkPw = false, chkEmail = false;  

	UserDAO dao = null;
	ArrayList<UserDTO> dto = null;

	public AccountSignUp(boolean checkProgram) {
		this.checkProgram = checkProgram;
		setUI();
	}

	private void setUI() {
		jframe = new DefaultJFrame("회원가입", 600, 400);
		jframe.setPanel();
		
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
				String txt = txtID.getText();
				int txtlength = txt.length();
				
				System.out.println("패턴 통과 여부 : "+checkPattern(txt, "id"));
				
				// 문자열 길이 제한
				if(txtlength <= 20 &&txtlength >= 8) {
					// db가 비지 않고 패턴에 어긋나지 않았을 경우
					if (dto.isEmpty() && checkPattern(txtID.getText(),"id")) {
						msgText = "사용가능한 아이디입니다.";
						chkID = true;
					}
					else if(!dto.isEmpty()) {
						msgText = "아이디가 존재합니다.";	
						txtID.setText("");
					}else if(!checkPattern(txt,"id")) {
						msgText = "아이디를 영문자와 숫자로만 작성해주세요.";
						txtID.setText("");
					}
				}else {
					msgText = "8 ~ 20글자 이내로 작성해주세요.";
				}
				
				System.out.println(msgText);	
				
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("확인");
				DefaultJFrame error = new DefaultJFrame("알림", 300, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // 현재 창닫기
						
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
				String txt = txtPW.getText();
				int txtlength = txt.length();
				
				System.out.println("패턴 통과 여부 : "+checkPattern(txt, "pw"));
				
				if(txtlength <= 20 && txtlength >= 8) {
					if (txt.equals(txtPWCheck.getText()) && checkPattern(txt,"pw")) {
						msgText = "비밀번호가 일치합니다.";
						chkPw = true;
					} else {
						msgText = "비밀번호가 틀립니다.";
					}
				}else {
					msgText = "8 ~ 20글자 이내로 작성해주세요.";
				}
				
				System.out.println(msgText);
				JLabel[] msg = new JLabel[] {new JLabel(msgText) };
				JButton btnOk = new JButton("확인");
				DefaultJFrame error = new DefaultJFrame("알림", 250, 120, msg, btnOk, false);
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						error.frameDispose(); // 현재 창닫기
					}
				});
				error.addContaionEmpty();
			}

		});

		JButton btnCheckEmail = new JButton("인증요청");
		btnCheckEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msgText = "";
				if(!checkPattern(txtEmail.getText(),"email")) {
					msgText = "이메일이 올바르게 작성되지 않았습니다.";
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnOk = new JButton("확인");
					DefaultJFrame error = new DefaultJFrame("경고", 280, 120, msg, btnOk, false);
					btnOk.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.frameDispose(); // 현재 창닫기
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
					EmailSend emailSend = new EmailSend(userAddress, "인증번호", "인증번호 : " + number + "입니다.");
					emailSend.setSend();
					count++;
				}
			}

		});
		
		JButton btnEmailOK = new JButton("확인");
		btnEmailOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 인증요청버튼이 클릭되었을 때 실행되도록 설정해야함
				if (count > 0) {
					System.out.println(number);
					if (txtEmailCheck.getText().equals(Integer.toString(number))) {
						System.out.println("인증번호가 동일합니다.");
						chkEmail = true;
					}else {
						System.out.println("인증번호가 다릅니다.");
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
						}

					});
					error.addContaionEmpty();
				}
				
			}
			
		});
		
		JButton btnSign = new JButton("가입하기");
		btnSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dto = dao.userSelect("");
				int result = 0;
				int rowCount = dto.size();
				String msgText = "";
				System.out.println(rowCount);
				// 아이디 중첩/비밀번호 일치 여부 / 인증번호 요청 완료시 실행
				if(chkID && chkPw && chkEmail) {
					String query = "";
					System.out.println("가입을 진행하고 있습니다.");
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
						query += "','마스터')";
					}
					else {
						query += "','슬레이브')";
					}
					result = dao.userUpdate(query);
					if(result != 0) {
						msgText = "가입에 성공하였습니다.";
					}else {
						msgText = "가입에 실패하였습니다.";
					}
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnOk = new JButton("확인");
					DefaultJFrame error = new DefaultJFrame("알림", 300, 120, msg, btnOk, false);
					btnOk.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.frameDispose(); // 현재 창닫기
						}
					});
					error.addContaionEmpty();
				}
			}
		});


		JComponent[] compornet = new JComponent[] { 
				new JLabel("ID"), txtID, btnCheckID
				, new JLabel("PW"), txtPW,new JLabel()
				, new JLabel("PW 확인"), txtPWCheck, btnCheckPW
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
	
	// 정규식
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
