package com.java.jframe;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.java.jframe.panel.NorthPanel;
import com.java.jframe.panel.SouthPanel;

/*
 * ������ ���� �� setContain() ȣ���Ͽ��� ǥ�� ��
 * */

public class BasicJFrame extends JFrame {	
	
	private String title = null;							// Ÿ��Ʋ��
	private int xSize = 0;									// ���� ������
	private int ySize = 0;									// ���� ������
	private Container contain = null;
	private JPanel northPanel = null
			,centerPanel = null
			,eastPanel = null
			,westPanel = null
			,southPanel = null;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getxSize() {
		return xSize;
	}

	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getySize() {
		return ySize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public void setCenterPanel(JPanel centerPanel) {
		this.centerPanel = centerPanel;
	}

	public JPanel getEastPanel() {
		return eastPanel;
	}

	public void setEastPanel(JPanel eastPanel) {
		this.eastPanel = eastPanel;
	}

	public JPanel getWestPanel() {
		return westPanel;
	}

	public void setWestPanel(JPanel westPanel) {
		this.westPanel = westPanel;
	}


	// �ΰ�, ����ǥ�ð� �ִ� ������(Ÿ��Ʋ��, x������, y������) / layout�� BorderLayout
	public BasicJFrame(String title, int xSize, int ySize) {
		this.title = title;
		this.xSize = xSize;
		this.ySize = ySize;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// x��ư Ŭ�� �� ���
		
		setTitle(title);
		setSize(xSize, ySize);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		northPanel = new NorthPanel();
		southPanel = new SouthPanel("Version 1.0.1");
		

	}
	
	/*
	 * ������ ���� 
	 * Visible - True 
	 * ������� �Ұ��� 
	 * ��� ����
	 */
	public void setContain() {
		
		if(northPanel != null) contain.add(northPanel,BorderLayout.NORTH);
		if(centerPanel != null) contain.add(centerPanel,BorderLayout.CENTER);
		if(eastPanel != null) contain.add(eastPanel,BorderLayout.EAST);
		if(westPanel != null) contain.add(westPanel,BorderLayout.WEST);
		if(southPanel != null) contain.add(southPanel,BorderLayout.SOUTH);

		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	
}
