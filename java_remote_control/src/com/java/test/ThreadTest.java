package com.java.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ThreadTest extends JFrame implements ActionListener {

	JLabel blThread1;
	JLabel blThread2;
	JPanel pnl;
	JButton btnStart;

	ThreadTest(){
		blThread1 = new JLabel("1");
		blThread2 = new JLabel("2");
		pnl = new JPanel();
		
		this.add(pnl);
		pnl.add(blThread1);
		pnl.add(blThread2);
		pnl.setPreferredSize(new Dimension(400,250));
		pnl.setLayout(new FlowLayout(FlowLayout.CENTER));
		blThread1.setPreferredSize(new Dimension(380,90));
		blThread2.setPreferredSize(new Dimension(380,90));
		blThread1.setBackground(Color.white);
		blThread2.setBackground(Color.white);
		blThread1.setOpaque(true);
		blThread2.setOpaque(true);
		
		btnStart = new JButton("Start!");
		btnStart.setPreferredSize(new Dimension(380,40));
		btnStart.addActionListener(this);
		pnl.add(btnStart);
		
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ThreadExtends t1_extends = new ThreadExtends(blThread1);
		t1_extends.start();
		
		ThreadInterface iRunnable= new ThreadInterface(blThread2);
		Thread t2_implements = new Thread(iRunnable);
		t2_implements.start();

	}

}
