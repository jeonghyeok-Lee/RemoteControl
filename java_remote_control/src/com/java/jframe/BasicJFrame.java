package com.java.jframe;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.java.jframe.panel.NorthPanel;
import com.java.jframe.panel.SouthPanel;

/*
 * 생성자 생성 후 setContain() 호출하여야 표시 됨
 * */

public class BasicJFrame extends JFrame {	
	
	private String title = null;							// 타이틀명
	private int xSize = 0;									// 가로 사이즈
	private int ySize = 0;									// 세로 사이즈
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

	

	// 로고, 버전표시가 있는 생성자(타이틀명, x사이즈, y사이즈) / layout이 BorderLayout
	public BasicJFrame(String title, int xSize, int ySize) {
		this.title = title;
		this.xSize = xSize;
		this.ySize = ySize;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// x버튼 클릭 시 취소
		
		setTitle(title);
		setSize(xSize, ySize);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		panelNorth = new NorthPanel();
		panelSouth = new SouthPanel("Version 1.0.1");
		

	}
	
	/*
	 * 마무리 설정 
	 * Visible - True 
	 * 사이즈변경 불가능 
	 * 가운데 정렬
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
