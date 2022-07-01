package com.java.master;

import javax.swing.JLabel;

import com.java.jframe.panel.CenterPanel;

public class MasterCenter extends CenterPanel {

	private JLabel lbText = null;
	
	public MasterCenter() {
		lbText = new JLabel("Å×½ºÆ®");
		add(lbText);
	}
}
