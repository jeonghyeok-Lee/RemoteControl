package com.java.jframe.panel;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NorthPanel extends JPanel {
	private JLabel logo = null;

	// 로고가 있는 생성자
	public NorthPanel() {
		add(logo = new JLabel("로고"));
	}
}
