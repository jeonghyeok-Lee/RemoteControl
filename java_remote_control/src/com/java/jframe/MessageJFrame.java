package com.java.jframe;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// 알림창을 표시할 JFrame
public class MessageJFrame extends JFrame {
	private Container contain = null;	
	
	private JPanel panelCenter = null;
	private JLabel lbMessage = null;
	
	private JPanel panelSouth = null;
	private JButton btnYes = null;
	private JButton btnNo = null;
	private boolean checkYes = false; // 확인버튼을 클릭했는지 파악하기 위한 변수 / true -> 체크함
	
	public boolean isCheckYes() {
		return checkYes;
	}

	// 메시지가 문자열로 들어오는 생성자 (타이틀 , 메시지 내용, 취소버튼 여부)
	public MessageJFrame(String title, String message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// 메시지 부분 설정
		panelCenter = new JPanel(new GridLayout(0,1));
		lbMessage = new JLabel(message);
		lbMessage.setHorizontalAlignment(JLabel.CENTER); // 레이블 가운데 정렬
		panelCenter.add(lbMessage);
		
		// 버튼 부분 설정
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	// 메시지가 String타입 배열로 들어오는 생성자
	public MessageJFrame(String title, String[] message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// 메시지 부분 설정
		panelCenter = new JPanel(new GridLayout(0,1));
		for(String content : message) {
			lbMessage = new JLabel(content);
			lbMessage.setHorizontalAlignment(JLabel.CENTER); 
			panelCenter.add(lbMessage);
		}
		// 버튼 부분 설정
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	
	// 메시지가 Label로 들어올 경우의 생성자
	public MessageJFrame(String title, JLabel message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// 메시지 부분 설정
		panelCenter = new JPanel(new GridLayout(0,1));
		message.setHorizontalAlignment(JLabel.CENTER); // 레이블 가운데 정렬
		panelCenter.add(message);
		
		// 버튼 부분 설정
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
	}
		
	
	// 메시지가 JLabel타입 배열로 들어올 경우의 생성자
	public MessageJFrame(String title, JLabel[] message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// 메시지 부분 설정
		panelCenter = new JPanel(new GridLayout(0,1));
		for(JLabel content : message) {
			content.setHorizontalAlignment(JLabel.CENTER); 
			panelCenter.add(content);
		}
		
		// 버튼 부분 설정
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	
	// 남쪽부분 컨텐츠를 메소드로 묶음
	private void southContent(boolean cancel) {
		panelSouth = new JPanel();
		btnYes = new JButton("확인");
		btnYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkYes = true;
				dispose();
			}
			
		});
		panelSouth.add(btnYes);
		if(cancel) {
			btnNo = new JButton("취소");
			btnNo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
				
			});
			panelSouth.add(btnNo);
		}
	}
	
}
