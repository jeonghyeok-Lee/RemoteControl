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
	
	// checkProgram - 관리자인지 아닌지 유무 파악용
	public LogInJFrame(boolean checkProgram) {
		this.checkProgram = checkProgram;
		setUI(checkProgram);
	}
	
	private void setUI(boolean checkProgram) {
		jframe = new DefaultJFrame("로그인 폼",450,220);
		jframe.setPanel();
		
		dao = new UserDAO();
		JPanel center = jframe.getCenterPanel();
		
		center.add(setCenter(checkProgram));
		
		jframe.addContain();
		
	}
	
	private JPanel setCenter(boolean checkProgram) {
		JPanel centerContent = new JPanel(new GridLayout(0,2,10,10));
		JPanel btnContentLeft = new JPanel(new GridLayout(0,2));
		JPanel btnContentRight = new JPanel(new GridLayout(0,2,10,10));
		
		
		JButton btnLogin = new JButton("로그인");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!isStringEmpty(txtID.getText().toString()) && !isStringEmpty(txtPW.getText().toString())) {
					dto = dao.userSelect("where user_id = '" + txtID.getText() +"' and user_password = '"+txtPW.getText()+"'"  );
					System.out.println("ID : " + dto.get(0).getUserId() + " PW : " + dto.get(0).getUserPassword());
					if(checkProgram) {	// 슬레이브쪽에서 로그인 시도 시 
						new SlaveJFrame();						
					}else {				// 마스터쪽에서 로그인 시도 시
//						new MasterJFrame();
						new MasterJFrame(txtID.getText(),txtPW.getText());
					}
					jframe.dispose();
						
				}else {
					System.out.println("ID/PW를 정확하게 입력하여주세요");
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
					new SlaveJFrame();
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
