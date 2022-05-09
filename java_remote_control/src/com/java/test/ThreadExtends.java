 package com.java.test;

import javax.swing.JLabel;

public class ThreadExtends extends Thread {
	int i=0;
	JLabel lb;
	ThreadExtends(JLabel lb){
		this.lb = lb;
	}
	public void run(){
		while(i<100){
			try {
				Thread.sleep((int)(Math.random()*300));
				lb.setText("1번 쓰레드 작동중... " +i +"회 " + Math.random());
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lb.setText("1번 쓰레드 종료");
	}
}
