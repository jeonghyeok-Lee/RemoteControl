package com.java.test;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;

public class MouseMovement {
	PointerInfo mPointer = null;
	
	
	public void mouseLocation() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				mPointer  = MouseInfo.getPointerInfo();
				System.out.println(mPointer.getLocation());
			}
			
		};
		timer.schedule(timerTask, 0,1000);
	}
	

	public static void main(String[] args) {
		PointerInfo mPointer = MouseInfo.getPointerInfo();
		MouseMovement mm = new MouseMovement();
		mm.mouseLocation();
	}
}
