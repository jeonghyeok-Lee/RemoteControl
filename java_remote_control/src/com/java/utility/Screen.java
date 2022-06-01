package com.java.utility;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Screen {
	private double width = 0.0;
	private double height = 0.0;
	
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public double getWidth() {
		return width = screenSize.getWidth();
	}

	public double getHeight() {
		return height = screenSize.getHeight();
	}
	
}

