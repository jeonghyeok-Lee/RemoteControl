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
	private CardLayout layoutCard = null;		// 컨텐츠의 CardLayout
	
	public MasterEast() {

		JPanel group = addGroupBox(Color.black, 1, true);
		JPanel content = eastContent();
		group.add(content);
		add(group);
	}

	// East에 들어갈 컨텐츠 내용을 모와둔 함수
	private JPanel eastContent() {
		JPanel content = new JPanel(layoutCard = new CardLayout());
		
		content.add(setController(), "controller");
		content.add(setOption(), "option");
		layoutCard.show(content, "option");
		
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

	private JPanel setNetwork() {
		JPanel network = new JPanel();

		return network;
	}

	private JPanel setAccount() {
		JPanel account = new JPanel();

		return account;
	}

	private JPanel setOption() {
		JPanel option = new JPanel(new GridLayout(0,1));
		
		// 옵션에 들어갈 요소에 따라 다른 형태로 잡힐 수 있으므로 각 라인을 잡아줄 패널을 설정
		JPanel[] line = new JPanel[] {
				new JPanel(new GridLayout(0,1))
				,new JPanel(),new JPanel(),new JPanel(), new JPanel()
		};
		
		JPanel buttonPanel = new JPanel();					// 버튼을 위한 패널 설정
		JButton btnChangePort = new JButton("변경");			// 포트 변경을 위한 버튼 
		JButton btnClearOption = new JButton("초기화");		// 기본 상태로 변경 시키기 위한 버튼
		JButton btnOKOption = new JButton("확인");			// 설정을 적용시키기 위한 버튼
		
		// 버튼이 들어갈 패널에 값 적용
		buttonPanel.add(btnClearOption);				
		buttonPanel.add(btnOKOption);
		
		// 첫번째 라인 내용 설정
		JPanel first = new JPanel();
		first.add(new JLabel("Port"));
		first.add(new JLabel(Integer.toString(PORT)));
		first.add(btnChangePort);
		
		// 첫번째 라인 적용
		line[0].add(first);
		
		option.add(line[0]);
		option.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return option;
	}
}
