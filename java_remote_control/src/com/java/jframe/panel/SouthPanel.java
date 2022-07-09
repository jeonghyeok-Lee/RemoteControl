package com.java.jframe.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

// 아래 버전을 표시 할 패널 설정
public class SouthPanel extends JPanel {
	private JLabel lbversion = null;			// 버전 표시 레이블
	
	// 버전표시가 있는 생성자
	public SouthPanel(String version) {
		setLayout(new GridLayout(0,1));

		setPreferredSize(new Dimension(this.getWidth(),15));
		
		lbversion = new JLabel(version);
		lbversion.setHorizontalAlignment(JLabel.RIGHT);
		lbversion.setOpaque(true); // 배경색 적용을 위함
		lbversion.setBackground(new Color(204,229,255));
		
		this.add(lbversion);
	}
	
	// 아무것도 없는 생성자
	public SouthPanel() {
		
	}
	
}
