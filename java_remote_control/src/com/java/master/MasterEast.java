package com.java.master;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.java.jframe.panel.MiddlePanel;
import com.java.utility.IpAddress;

public class MasterEast extends MiddlePanel {
	private JLabel lbMyIp = null, lbMyHost = null, lbMyPort = null;
	private final int PORT = 9095;
	
	public MasterEast() {

		JPanel group = addGroupBox(Color.black, 1, true);
		JPanel content = eastContent();
		group.add(content);
		add(group);
	}

	// East에 들어갈 컨텐츠 내용을 모와둔 함수
	private JPanel eastContent() {
		JPanel content = new JPanel(new CardLayout());
		
		content.add(setController());
		return content;
	}

	// 컨트롤 부분을 설정
	private JPanel setController() {
		JPanel controller = new JPanel();
		JButton btnStart = new JButton("On"); 		// 서버 시작을 위한 버튼
		IpAddress myIP = new IpAddress(); 			// 내 ip를 확인하기 위한 IpAddress타입 변수 생성

		controller.setLayout(new GridLayout(0, 2));
		// 여백주기 (위, 좌, 아래, 우)
		controller.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));
		
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// 스레드가 실행될 공간
			}
			
		});

		// 구성 컴포넌트를 배열로 만들기
		JComponent[] compornet = new JComponent[] { 
				new JLabel("IP "), lbMyIp = new JLabel(myIP.getHostAddress()),
				new JLabel("Host "), lbMyHost = new JLabel(myIP.getHostName()), 
				new JLabel("Port "), lbMyHost = new JLabel(Integer.toString(PORT)) 
		};
		
		// 삽입
		for(JComponent com : compornet) {
			controller.add(com);
		}
		controller.add(btnStart);

		return controller;
	}

	private JPanel setEastNetwork() {
		JPanel network = new JPanel();

		return network;
	}

	private JPanel setEastAccount() {
		JPanel account = new JPanel();

		return account;
	}

	private JPanel setEastOption() {
		JPanel option = new JPanel();

		return option;
	}
}
