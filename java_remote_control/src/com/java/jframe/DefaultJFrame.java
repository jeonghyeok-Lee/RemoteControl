package com.java.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


// 나중에 수정
public class DefaultJFrame extends JFrame {
	private Container contain = null;
	private String titleName = "";
	private int xSize = 0
			, ySize = 0;
	private JPanel northPanel = null
			, centerPanel = null
			, eastPanel = null
			, wastPanel = null
			, southPanel = null;
	private JLabel version = null;

	public DefaultJFrame(String titleName, int xSize, int ySize) {
		this.titleName = titleName;
		this.xSize = xSize;
		this.ySize = ySize;

		defaultSetUI();

	}

	private void defaultSetUI() {
		setTitle(titleName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contain = getContentPane();
		contain.setLayout(new BorderLayout(10, 3));
		this.setLocationRelativeTo(null);

		northPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel(new FlowLayout());
		wastPanel = new JPanel();
		southPanel = new JPanel(new GridLayout(0, 1));

		version = new JLabel("Version 1.0.0 ");
		version.setHorizontalAlignment(JLabel.RIGHT);
		version.setOpaque(true); // 배경색 적용을 위함
		version.setBackground(new Color(204, 229, 255));

		southPanel.add(version);

		contain.add(northPanel, BorderLayout.NORTH);
		contain.add(centerPanel, BorderLayout.CENTER);
		contain.add(eastPanel, BorderLayout.EAST);
		contain.add(wastPanel, BorderLayout.WEST);
		contain.add(southPanel, BorderLayout.SOUTH);

		setSize(xSize, ySize);
		setVisible(true);
	}

	public static void main(String[] args) {
		new DefaultJFrame("테스트", 500, 300);

	}
}
