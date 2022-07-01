package com.java.master;

import com.java.jframe.BasicJFrame;

public class MasterMain extends BasicJFrame {
	

	public MasterMain() {
		super("메인화면", 500, 400);
		super.setCenterPanel(new MasterCenter());
		super.setContain();
	}

}
