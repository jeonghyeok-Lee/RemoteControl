package com.java.master;

import com.java.jframe.BasicJFrame;

public class MasterMain extends BasicJFrame {
	

	public MasterMain() {
		super("����ȭ��", 500, 400);
		super.setPanelCenter(new MasterCenter());
		super.setContain();
	}

}
