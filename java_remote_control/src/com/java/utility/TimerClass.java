package com.java.utility;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

// ����ð��� Ȯ���ϱ� ���� Ŭ����
public class TimerClass {
	private static int sec = 0;	//��
	private static int min = 0;	//��
	private static int hour = 0;//��

	private Timer timer = null;

	public Timer getTimer() {
		return timer;
	}

	// Ÿ�̸� ������
	public TimerClass() {
		sec = 0;
		min = 0;	
		hour = 0;
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if(sec <= 59) {
					sec++;
				}else {
					sec = 0;
					min++;
				}
				
				if(min>=59) {
					min = 0;
					hour++;
				}
				
				if(hour>=23) {
					hour = 0;
				}
			}
			
		};
		timer.schedule(task,0, 1000);
	}
	
	public TimerClass(JLabel labelTimer) {
		sec = 0;
		min = 0;	
		hour = 0;
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if(sec < 59) {
					sec++;
				}else {
					sec = 0;
					min++;
				}
				
				if(min>59) {
					min = 0;
					hour++;
				}
				
				if(hour>23) {
					hour = 0;
				}
				String text = hour +" : "+min +" : " + sec;
				labelTimer.setText(text);
			}
			
		};
		timer.schedule(task,0, 1000); 	// 0�� �� 1�ʸ��� task ����
	}

}
