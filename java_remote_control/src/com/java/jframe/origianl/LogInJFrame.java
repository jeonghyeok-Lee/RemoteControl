package com.java.jframe;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
import com.java.master.MasterJFrame;
import com.java.slave.SlaveJFrame;

public class LogInJFrame extends JFrame {
	private JTextField txtID = null;
	private JPasswordField txtPW = null;
	private JLabel lbID = null, lbPW = null;
	private boolean checkProgram = false;
	
	private UserDAO dao = null;
	private ArrayList<UserDTO> dto = null;
	
	private DefaultJFrame jframe = null;
	
	// checkProgram - 슬레이브인지 아닌지 유무 파악용 true-슬레이브 false - 마스터
	public LogInJFrame(boolean checkProgram) {
		this.checkProgram = checkProgram;
		setUI();
	}
	
	private void setUI() {
		jframe = new DefaultJFrame("로그인 폼",450,220);
		jframe.setPanel();
		
		dao = new UserDAO();
		JPanel center = jframe.getCenterPanel();
		
		center.add(setCenter());
		
		jframe.addContain();
		
	}
	
	private JPanel setCenter() {
		JPanel centerContent = new JPanel(new GridLayout(0,2,10,10));
		JPanel btnContentLeft = new JPanel(new GridLayout(0,2));
		JPanel btnContentRight = new JPanel(new GridLayout(0,2,10,10));
		
		
		JButton btnLogin = new JButton("로그인");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!isStringEmpty(txtID.getText().toString()) && !isStringEmpty(txtPW.getText().toString())) {	// 입력란이 비어있지않다면
					String where = "";
					if(checkProgram) {	
						where = "where user_id = '" + txtID.getText() +"' and user_password = '"+txtPW.getText()+"' and user_rank = '슬레이브'";
					}else {
						where = "where user_id = '" + txtID.getText() +"' and user_password = '"+txtPW.getText()+"' and user_rank = '마스터'";
					}
					
					try {
						dto = dao.userSelect(where);
						if(dto.isEmpty()) {
							String msgText = "아이디 혹은 비밀번호를 잘못입력하셨습니다.";
							JLabel[] msg = new JLabel[] {new JLabel(msgText) };
							JButton btnOk = new JButton("확인");
							DefaultJFrame error = new DefaultJFrame("경고", 300, 120, msg, btnOk, false);
							btnOk.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									error.dispose(); // 현재 창닫기
								}
							});
							error.addContaionEmpty();
						}
					}catch(Exception e1) {	// 메시지는 로그로 남을 것임 -> dao를 통해 검색하는 값[list]가 비어있기 때문 해당 처리를 위에서 해주었기 때문에 나와도 상관없음
						e1.getStackTrace(); 
					}finally {						
						txtID.setText("");
						txtPW.setText("");
					}
					dto = dao.userSelect(where);
					System.out.println(where);
					System.out.println("ID : " + dto.get(0).getUserId() + " PW : " + dto.get(0).getUserPassword());
					
					if(checkProgram) {				// 슬레이브쪽에서 로그인 시도 시 
						new SlaveJFrame(dto, dao);		// 회원으로 로그인
						jframe.dispose();
						
					}else {							// 마스터쪽에서 로그인 시도 시
						new MasterJFrame(dto,dao);
						jframe.dispose();
					}
						
				}else {
					String msgText = "아이디 혹은 비밀번호를 모두 입력해주세요.";
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnOk = new JButton("확인");
					DefaultJFrame error = new DefaultJFrame("경고", 300, 120, msg, btnOk, false);
					btnOk.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.dispose(); // 현재 창닫기
						}
					});
					error.addContaionEmpty();
					txtID.setText("");
					txtPW.setText("");
				}	
			}
		});
		
		JButton btnAccount = new JButton("회원가입");
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new AccountSignUp(checkProgram);
			}
			
		});
		
		centerContent.add(lbID = new JLabel("ID : "));
		lbID.setHorizontalAlignment(JLabel.CENTER);
		centerContent.add(txtID = new JTextField(15));
		centerContent.add(lbPW = new JLabel("PW : "));
		lbPW.setHorizontalAlignment(JLabel.CENTER);
		centerContent.add(txtPW = new JPasswordField(15));
		btnContentLeft.add(new JLabel());
		btnContentLeft.add(btnLogin);
		
		if(checkProgram) {
			JButton btnNonLogin = new JButton("비로그인");
			btnNonLogin.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new SlaveJFrame();	// 비회원으로 로그임
					dispose();
				}
			});
			btnContentRight.add(btnNonLogin);
		}else {
			btnContentRight.add(new JLabel());
		}
		btnContentRight.add(btnAccount);
		
		centerContent.add(btnContentLeft);
		centerContent.add(btnContentRight);
		
		return centerContent;
		
	}
	
	private boolean isStringEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}
	
}
