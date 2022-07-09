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
	private JPanel panelNorth = null
			,panelCenter = null
			,panelEast = null
			,panelWest = null
			,panelSouth = null;
	
	
	public JPanel getPanelCenter() {
		return panelCenter;
	}

	public void setPanelCenter(JPanel panelCenter) {
		this.panelCenter = panelCenter;
	}

	public JPanel getPanelEast() {
		return panelEast;
	}

	public void setPanelEast(JPanel panelEast) {
		this.panelEast = panelEast;
	}

	public JPanel getPanelWest() {
		return panelWest;
	}

	public void setPanelWest(JPanel panelWest) {
		this.panelWest = panelWest;
	}

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
		
		panelNorth = new NorthPanel();
		panelSouth = new SouthPanel("Version 1.0.1");
		

	}
	
	/*
	 * ������ ���� 
	 * Visible - True 
	 * ������� �Ұ��� 
	 * ��� ����
	 */
	public void setContain() {
		
		if(panelNorth != null) contain.add(panelNorth,BorderLayout.NORTH);
		if(panelCenter != null) contain.add(panelCenter,BorderLayout.CENTER);
		if(panelEast != null) contain.add(panelEast,BorderLayout.EAST);
		if(panelWest != null) contain.add(panelWest,BorderLayout.WEST);
		if(panelSouth != null) contain.add(panelSouth,BorderLayout.SOUTH);

		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	
}
