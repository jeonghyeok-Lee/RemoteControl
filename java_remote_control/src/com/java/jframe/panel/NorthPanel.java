package com.java.jframe.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NorthPanel extends JPanel {
	private JLabel logo = null;

	// �ΰ� �ִ� ������
	public NorthPanel() {
		add(logo = new JLabel("�ΰ�"));
	}
}
