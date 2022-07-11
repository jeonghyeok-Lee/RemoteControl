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
	private CardLayout layoutCard = null;		// �������� CardLayout
	
	public MasterEast() {

		JPanel group = addGroupBox(Color.black, 1, true);
		JPanel content = eastContent();
		group.add(content);
		add(group);
	}

	// East�� �� ������ ������ ��͵� �Լ�
	private JPanel eastContent() {
		JPanel content = new JPanel(layoutCard = new CardLayout());
		
		content.add(setController(), "controller");
		content.add(setOption(), "option");
		layoutCard.show(content, "option");
		
		return content;
	}

	// ��Ʈ�� �κ��� ����
	private JPanel setController() {
		JPanel controller = new JPanel();
		JButton btnStart = new JButton("On"); 		// ���� ������ ���� ��ư
		IpAddress myIP = new IpAddress(); 			// �� ip�� Ȯ���ϱ� ���� IpAddressŸ�� ���� ����

		controller.setLayout(new GridLayout(0, 2));
		// �����ֱ� (��, ��, �Ʒ�, ��)
		controller.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));
		
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// �����尡 ����� ����
			}
			
		});

		// ���� ������Ʈ�� �迭�� �����
		JComponent[] compornet = new JComponent[] { 
				new JLabel("IP "), lbMyIp = new JLabel(myIP.getHostAddress()),
				new JLabel("Host "), lbMyHost = new JLabel(myIP.getHostName()), 
				new JLabel("Port "), lbMyHost = new JLabel(Integer.toString(PORT)) 
		};
		
		// ����
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
		
		// �ɼǿ� �� ��ҿ� ���� �ٸ� ���·� ���� �� �����Ƿ� �� ������ ����� �г��� ����
		JPanel[] line = new JPanel[] {
				new JPanel(new GridLayout(0,1))
				,new JPanel(),new JPanel(),new JPanel(), new JPanel()
		};
		
		JPanel buttonPanel = new JPanel();					// ��ư�� ���� �г� ����
		JButton btnChangePort = new JButton("����");			// ��Ʈ ������ ���� ��ư 
		JButton btnClearOption = new JButton("�ʱ�ȭ");		// �⺻ ���·� ���� ��Ű�� ���� ��ư
		JButton btnOKOption = new JButton("Ȯ��");			// ������ �����Ű�� ���� ��ư
		
		// ��ư�� �� �гο� �� ����
		buttonPanel.add(btnClearOption);				
		buttonPanel.add(btnOKOption);
		
		// ù��° ���� ���� ����
		JPanel first = new JPanel();
		first.add(new JLabel("Port"));
		first.add(new JLabel(Integer.toString(PORT)));
		first.add(btnChangePort);
		
		// ù��° ���� ����
		line[0].add(first);
		
		option.add(line[0]);
		option.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		return option;
	}
}
