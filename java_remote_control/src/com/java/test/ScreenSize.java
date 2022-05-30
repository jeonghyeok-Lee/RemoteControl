package com.java.test;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class ScreenSize {

	public static void main(String[] args) {
		ScreenSize screen = new ScreenSize();
		screen.getSingleScreenSize();
		screen.getMultiScreenSize();
	}
	
	// single monitor
	public void getSingleScreenSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		System.out.println("width : " + width + "\nheight : " + height);
	}
	
	//multi monitor
	public void getMultiScreenSize() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		System.out.println("width : " + width + "\nheight : " + height);
	}

}
