package com.java.jframe.panel;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class MiddlePanel extends JPanel {

	// 그룹박스 만들기 색상 / 두께 / 표시여부
	public JPanel addGroupBox(Color color, int width, boolean activate ) {

		JPanel group = new JPanel();
		// group에 border주기
		group.setBorder(new LineBorder(color, width, activate));
		return group;
	}
}
