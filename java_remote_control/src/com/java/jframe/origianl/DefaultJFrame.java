package com.java.jframe.origianl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DefaultJFrame extends JFrame {
	private String title = null;
	private int xSize = 0;
	private int ySize = 0;
	private Container contain = null;
	private JPanel northPanel = null
			,centerPanel = null
			,eastPanel = null
			,westPanel = null
			,southPanel = null;
	
	private JPanel group = null;
	
	private JLabel lbversion = null;
	

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

	public Container getContain() {
		return contain;
	}

	public void setContain(Container contain) {
		this.contain = contain;
	}

	public JPanel getNorthPanel() {
		return northPanel;
	}

	public void setNorthPanel(JPanel northPanel) {
		this.northPanel = northPanel;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
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

	public JPanel getSouthPanel() {
		return southPanel;
	}

	public void setSouthPanel(JPanel southPanel) {
		this.southPanel = southPanel;
	}

	public JLabel getLbversion() {
		return lbversion;
	}

	public void setLbversion(JLabel lbversion) {
		this.lbversion = lbversion;
	}

	public JPanel getGroup() {
		return group;
	}

	public void setGroup(JPanel group) {
		this.group = group;
	}
	
	// ???? ???????? ???????? ?????? <?????? ???? -> setPanel -> setContain>
	public DefaultJFrame(String title, int xSize, int ySize) {
		this.title = title;
		this.xSize = xSize;
		this.ySize = ySize;
//		setPanel();
			
	}
	
	
	// ???????? ???????? ??????(??????, x????, y????, ????, ????, ????????????)
	public DefaultJFrame(String title, int xSize, int ySize, JLabel[] content, JButton btnOk, boolean btnNo) {
		this.title = title;
		this.xSize = xSize;
		this.ySize = ySize;
		
		setPanel();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		for(JLabel con : content) {
			centerPanel.add(con);
		}
		southPanel.add(btnOk);
		if(btnNo) {
			JButton btnCancel = new JButton("????");
			btnCancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();	// ???? ??????
				}
			});
			southPanel.add(btnCancel);
		}
			
	}
	
	// ???????? ???????? ????
	public void setPanelEmpty() {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		setSize(xSize, ySize);
	}
	
	// ???? Layout?? BorderLayout???? ?? ?????? ?????? ???????? ????
	public void setPanel() {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contain = getContentPane();
		contain.setLayout(new BorderLayout(10,3));
		
		northPanel = new JPanel();
		centerPanel = new JPanel();
		eastPanel = new JPanel();
		westPanel = new JPanel();
		southPanel = new JPanel();
		
		setSize(xSize, ySize);
	}
	
	// ???? JPanel?? Container?? ????
	public void addContain() {
		
		northPanel.add(addLogo());
		southPanel.add(setVersion());
		
		addContaionEmpty();
	}
	
	// ?????? ?????? ???? ?????????? ????
	public void addContaionEmpty() {
		
		contain.add(northPanel,BorderLayout.NORTH);
		contain.add(centerPanel,BorderLayout.CENTER);
		contain.add(eastPanel,BorderLayout.EAST);
		contain.add(westPanel,BorderLayout.WEST);
		contain.add(southPanel,BorderLayout.SOUTH);
		
		setFrameVisible();
	}
	
	public void setFrameVisible() {
		setVisible(true);
		setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	// ???????? ?????? ???? / ???? / ????????
	public JPanel addGroupBox(Color color, int width, boolean useCheck ) {
		group = new JPanel();
		// group?? border????
		group.setBorder(new LineBorder(color, width, useCheck));
		
		return group;
	}
	
	// ????????
	public JPanel addLogo() {
		JPanel northContent = new JPanel();
		northContent.add(new JLabel("????"));
		
		return northContent;
	}
	
	// ????????????
	public JPanel setVersion() {
		
		JPanel southContent = new JPanel(new GridLayout(0,1));
		
		southContent.setPreferredSize(new Dimension(this.getWidth(),15));
		
		lbversion = new JLabel("Version 1.0.1     ");
		lbversion.setHorizontalAlignment(JLabel.RIGHT);
		lbversion.setOpaque(true); // ?????? ?????? ????
		lbversion.setBackground(new Color(204,229,255));
		
		southContent.add(lbversion);
		
		return southContent;
	}

	public void frameDispose() {
		dispose();
	}
}
