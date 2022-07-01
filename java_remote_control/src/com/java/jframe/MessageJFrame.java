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

// �˸�â�� ǥ���� JFrame
public class MessageJFrame extends JFrame {
	private Container contain = null;	
	
	private JPanel panelCenter = null;
	private JLabel lbMessage = null;
	
	private JPanel panelSouth = null;
	private JButton btnYes = null;
	private JButton btnNo = null;
	private boolean checkYes = false; // Ȯ�ι�ư�� Ŭ���ߴ��� �ľ��ϱ� ���� ���� / true -> üũ��
	
	public boolean isCheckYes() {
		return checkYes;
	}

	// �޽����� ���ڿ��� ������ ������ (Ÿ��Ʋ , �޽��� ����, ��ҹ�ư ����)
	public MessageJFrame(String title, String message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// �޽��� �κ� ����
		panelCenter = new JPanel(new GridLayout(0,1));
		lbMessage = new JLabel(message);
		lbMessage.setHorizontalAlignment(JLabel.CENTER); // ���̺� ��� ����
		panelCenter.add(lbMessage);
		
		// ��ư �κ� ����
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	// �޽����� StringŸ�� �迭�� ������ ������
	public MessageJFrame(String title, String[] message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// �޽��� �κ� ����
		panelCenter = new JPanel(new GridLayout(0,1));
		for(String content : message) {
			lbMessage = new JLabel(content);
			lbMessage.setHorizontalAlignment(JLabel.CENTER); 
			panelCenter.add(lbMessage);
		}
		// ��ư �κ� ����
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	
	// �޽����� Label�� ���� ����� ������
	public MessageJFrame(String title, JLabel message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// �޽��� �κ� ����
		panelCenter = new JPanel(new GridLayout(0,1));
		message.setHorizontalAlignment(JLabel.CENTER); // ���̺� ��� ����
		panelCenter.add(message);
		
		// ��ư �κ� ����
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
	}
		
	
	// �޽����� JLabelŸ�� �迭�� ���� ����� ������
	public MessageJFrame(String title, JLabel[] message, boolean cancel) {
		setTitle(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		// �޽��� �κ� ����
		panelCenter = new JPanel(new GridLayout(0,1));
		for(JLabel content : message) {
			content.setHorizontalAlignment(JLabel.CENTER); 
			panelCenter.add(content);
		}
		
		// ��ư �κ� ����
		southContent(cancel);
		
		contain.add(panelCenter,BorderLayout.CENTER);
		contain.add(panelSouth,BorderLayout.SOUTH);
		
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	
	// ���ʺκ� �������� �޼ҵ�� ����
	private void southContent(boolean cancel) {
		panelSouth = new JPanel();
		btnYes = new JButton("Ȯ��");
		btnYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkYes = true;
				dispose();
			}
			
		});
		panelSouth.add(btnYes);
		if(cancel) {
			btnNo = new JButton("���");
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
