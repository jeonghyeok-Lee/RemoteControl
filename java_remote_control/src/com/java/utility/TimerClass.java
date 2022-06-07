package com.java.utility;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

// 연결시간을 확인하기 위한 클래스
public class TimerClass {
	private static int sec = 0;	//초
	private static int min = 0;	//분
	private static int hour = 0;//시

	private Timer timer = null;

	public Timer getTimer() {
		return timer;
	}

	// 타이머 생성자
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
		timer.schedule(task,0, 1000); 	// 0초 뒤 1초마다 task 실행
	}

}
