package com.java.jframe.panel;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class MiddlePanel extends JPanel {

	// �׷�ڽ� ����� ���� / �β� / ǥ�ÿ���
	public JPanel addGroupBox(Color color, int width, boolean activate ) {

		JPanel group = new JPanel();
		// group�� border�ֱ�
		group.setBorder(new LineBorder(color, width, activate));
		return group;
	}
}
