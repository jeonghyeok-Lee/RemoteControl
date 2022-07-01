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
		
		northPanel = new NorthPanel();
		southPanel = new SouthPanel("Version 1.0.1");
		

	}
	
	/*
	 * 마무리 설정 
	 * Visible - True 
	 * 사이즈변경 불가능 
	 * 가운데 정렬
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
