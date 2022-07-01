package com.java.jframe.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

// �Ʒ� ������ ǥ�� �� �г� ����
public class SouthPanel extends JPanel {
	private JLabel lbversion = null;			// ���� ǥ�� ���̺�
	
	public SouthPanel(String version) {
		setLayout(new GridLayout(0,1));

		setPreferredSize(new Dimension(this.getWidth(),15));
		
		lbversion = new JLabel(version);
		lbversion.setHorizontalAlignment(JLabel.RIGHT);
		lbversion.setOpaque(true); // ���� ������ ����
		lbversion.setBackground(new Color(204,229,255));
		
		this.add(lbversion);
	}
}
