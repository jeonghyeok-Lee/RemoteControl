package com.java.jframe;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LogInJFrame extends JFrame {
	JTextField txtID = null, txtPW = null;
	JLabel lbID = null, lbPW = null;
	
	public LogInJFrame() {
		setUI();
	}
	
	private void setUI() {
		setTitle("로그인 페이지");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contain = getContentPane();
		contain.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		
		JLabel laResult = new JLabel("확인결과");
		JButton btnSend = new JButton("확인");
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton)e.getSource();
				if(!isStringEmpty(txtID.toString()) && !isStringEmpty(txtPW.toString())) {
					laResult.setText("ID : " + txtID.getText() + " PW : " + txtPW.getText());
				}else {
					laResult.setText("ID/PW를 정확하게 입력하여주세요");
				}
				
			}

			private boolean isStringEmpty(String string) {
				return string == null || string.trim().isEmpty();
			}
		});
		
		
		contain.add(lbID = new JLabel("ID : "));
		contain.add(txtID = new JTextField(15));
		contain.add(lbPW = new JLabel("PW : "));
		contain.add(txtPW = new JTextField(15));
		contain.add(btnSend);
		contain.add(laResult);
		
		setSize(500,400);
		setVisible(true);
	}
}
