package com.java.test;

import javax.swing.JLabel;

public class ThreadInterface implements Runnable {
	int i=0;
	JLabel lb;
	ThreadInterface(JLabel lb){
		this.lb = lb;
	}
	@Override
	public void run() {
		while(i<100){
			try {
				Thread.sleep((int)(Math.random()*300));
				lb.setText("2�� ������ �۵���... " +i +"ȸ " + Math.random());
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lb.setText("2�� ������ ����");

	}

}
