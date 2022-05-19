package com.java.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.java.slave.SlaveJFrame;

public class LogInJFrame extends JFrame {
	JTextField txtID = null;
	JPasswordField txtPW = null;
	JLabel lbID = null, lbPW = null;
	
	// checkProgram - 관리자인지 아닌지 유무 파악용
	public LogInJFrame(boolean checkProgram) {
		setUI(checkProgram);
	}
	
	private void setUI(boolean checkProgram) {
		setTitle("로그인폼");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		this.setLocationRelativeTo(null);
		
		JPanel northPanel = new JPanel(new FlowLayout());
		JPanel centerPanel = new JPanel(new GridLayout(3,2,10,10));
		JPanel eastPanel = new JPanel(new FlowLayout());
		JPanel southPanel = new JPanel(new GridLayout(1,1));
		
		JLabel laResult = new JLabel("Version 1.0.0");
		laResult.setHorizontalAlignment(JLabel.RIGHT);
		laResult.setOpaque(true); // 배경색 적용을 위함
		laResult.setBackground(new Color(204,229,255));
		
		JButton btnLogin = new JButton("로그인");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton)e.getSource();
				if(!isStringEmpty(txtID.getText().toString()) && !isStringEmpty(txtPW.getText().toString())) {
					laResult.setText("ID : " + txtID.getText() + " PW : " + txtPW.getText());
				}else {
					laResult.setText("ID/PW를 정확하게 입력하여주세요");
				}	
			}
			private boolean isStringEmpty(String string) {
				return string == null || string.trim().isEmpty();
			}
		});
//		btnLogin.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		if(checkProgram) {
			JButton btnNonLogin = new JButton("비로그인");
			btnNonLogin.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					laResult.setText("비로그인으로 접속을 시도하였습니다.");
					new SlaveJFrame();
				}
				
			});
			eastPanel.add(btnNonLogin);
		}
		
		northPanel.add(new JLabel("로고"));
		
		centerPanel.add(lbID = new JLabel("ID : "));
		lbID.setHorizontalAlignment(JLabel.RIGHT);
		centerPanel.add(txtID = new JTextField(15));
		centerPanel.add(lbPW = new JLabel("PW : "));
		lbPW.setHorizontalAlignment(JLabel.RIGHT);
		centerPanel.add(txtPW = new JPasswordField(15));

		eastPanel.add(btnLogin);
		southPanel.add(laResult);
		
		contain.add(northPanel,BorderLayout.NORTH);
		contain.add(centerPanel,BorderLayout.CENTER);
		contain.add(eastPanel,BorderLayout.EAST);
		contain.add(southPanel,BorderLayout.SOUTH);

		setSize(400,200);
		setVisible(true);
	}
}
