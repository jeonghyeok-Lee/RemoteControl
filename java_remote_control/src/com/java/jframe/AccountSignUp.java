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
		setTitle("ȸ������");
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
		
		JButton btnCheckID = new JButton("�ߺ� Ȯ��");
		btnCheckID.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("�ߺ�Ȯ��");
			}
			
		});
		
		JButton btnCheckPW = new JButton("��й�ȣ Ȯ��");
		btnCheckPW.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(txtPW.getText().equals(txtPWCheck.getText())) {
					System.out.println("��й�ȣ�� ��ġ�մϴ�.");
				}else {
					System.out.println("��й�ȣ�� Ʋ���ϴ�.");
				}
				
			}
			
		});
		
		JButton btnSign = new JButton("�����ϱ�");
		btnSign.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
			
		});
		
		JButton btnCheckEmail = new JButton("������û");
		btnCheckEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String userAddress = txtEmail.getText() + cmbEmail.getSelectedItem();
				System.out.println(userAddress);
				number = (int)(Math.random()*9999) +1;
				System.out.println(number);
				EmailSend emailSend = new EmailSend(userAddress, "������ȣ", "������ȣ : "+ number + "�Դϴ�.");
				emailSend.setSend();
				
			}
			
		});
		
		
		JLabel lbVersion = new JLabel("Version 1.0.0");
		lbVersion.setHorizontalAlignment(JLabel.RIGHT);
		lbVersion.setOpaque(true); // ���� ������ ����
		lbVersion.setBackground(new Color(204,229,255));
		
		
		JComponent[] compornet = new JComponent[]{
			new JLabel("ID"),txtID,btnCheckID
			,new JLabel("PW"),txtPW,new JLabel()
			,new JLabel("PW Ȯ��"),txtPWCheck,btnCheckPW
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
		
		northPanel.add(new JLabel("�ΰ�"));
		southPanel.add(lbVersion);
		
		contain.add(northPanel,BorderLayout.NORTH);
		contain.add(centerPanel,BorderLayout.CENTER);
		contain.add(southPanel,BorderLayout.SOUTH);
		
		setSize(400,300);
		setVisible(true);
		
	}

}
