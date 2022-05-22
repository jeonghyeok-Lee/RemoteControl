package com.java.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.java.utility.EmailSend;

public class AccountSignUp extends JFrame {
	int number = 0;

	public AccountSignUp() {
		setUI();
	}
	
	private void setUI() {
		setTitle("회원가입");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		this.setLocationRelativeTo(null);
		
		JPanel northPanel = new JPanel(new FlowLayout());
		JPanel centerPanel = new JPanel(new GridLayout(0,3,10,5));
		JPanel southPanel = new JPanel(new GridLayout(0,1));
		
		JTextField txtID = new JTextField(15);
		JPasswordField txtPW = new JPasswordField(15);
		JPasswordField txtPWCheck = new JPasswordField(15);
		JTextField txtName = new JTextField(15);
		JTextField txtEmail = new JTextField(15);
		JTextField txtEmailCheck = new JTextField(15);
		JComboBox cmbEmail = new JComboBox();		
		
		cmbEmail.addItem("@naver.com");
		cmbEmail.addItem("@gmail.com");
		
		JButton btnCheckID = new JButton("중복 확인");
		btnCheckID.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("중복확인");
			}
			
		});
		
		JButton btnCheckPW = new JButton("비밀번호 확인");
		btnCheckPW.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtPW.getText().equals(txtPWCheck.getText())) {
					System.out.println("비밀번호가 일치합니다.");
				}else {
					System.out.println("비밀번호가 틀립니다.");
				}
				
			}
			
		});
		
		JButton btnSign = new JButton("가입하기");
		btnSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
			
		});
		
		JButton btnCheckEmail = new JButton("인증요청");
		btnCheckEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userAddress = txtEmail.getText() + cmbEmail.getSelectedItem();
				System.out.println(userAddress);
				number = (int)(Math.random()*9999) +1;
				System.out.println(number);
				EmailSend emailSend = new EmailSend(userAddress, "인증번호", "인증번호 : "+ number + "입니다.");
				emailSend.setSend();
				
			}
			
		});
		
		
		JLabel lbVersion = new JLabel("Version 1.0.0");
		lbVersion.setHorizontalAlignment(JLabel.RIGHT);
		lbVersion.setOpaque(true); // 배경색 적용을 위함
		lbVersion.setBackground(new Color(204,229,255));
		
		
		JComponent[] compornet = new JComponent[]{
			new JLabel("ID"),txtID,btnCheckID
			,new JLabel("PW"),txtPW,new JLabel()
			,new JLabel("PW 확인"),txtPWCheck,btnCheckPW
			,new JLabel("Name"),txtName,new JLabel()
			,new JLabel("Email"),txtEmail,cmbEmail
			,new JLabel("Email Check"), txtEmailCheck,btnCheckEmail
			,new JLabel(),new JLabel(),new JLabel()
			,new JLabel(),btnSign,new JLabel()
		};
		
		for(JComponent com : compornet){
			if(com instanceof JLabel) {
				((JLabel)com).setHorizontalAlignment(0);
			}
			
			if(com instanceof JButton) {
				com.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
			}
			
			centerPanel.add(com);
		}
		
		northPanel.add(new JLabel("로고"));
		southPanel.add(lbVersion);
		
		contain.add(northPanel,BorderLayout.NORTH);
		contain.add(centerPanel,BorderLayout.CENTER);
		contain.add(southPanel,BorderLayout.SOUTH);
		
		setSize(400,300);
		setVisible(true);
		
	}

}
