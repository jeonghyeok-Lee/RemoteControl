package com.java.master;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import com.java.jframe.DefaultJFrame;
import com.java.utility.Screen;

public class MasterExecutionJFrame extends JFrame {
	
	private Screen screen = null;
	private DefaultJFrame jframe = null;
	private int keycode = 0;
	private boolean keyListen = false;
	
	public void setKeyListen(boolean keyListen) {
		this.keyListen = keyListen;
	}


	public int getKeycode() {
		return keycode;
	}


	public void setKeycode(int keycode) {
		this.keycode = keycode;
	}


	public boolean isKeyListen() {
		return keyListen;
	}

	public MasterExecutionJFrame() {
		setUI();
	}
	
	
	private void setUI() {
		screen = new Screen();
		
		jframe = new DefaultJFrame("�������",(int)screen.getWidth(),(int)screen.getHeight());
		
		jframe.setPanelEmpty();
		
		jframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jframe.setUndecorated(true);
		jframe.setBackground(new Color(0,0,0,100));
		
		jframe.requestFocus();
		jframe.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
//				 System.out.println(e.getKeyChar()+" keyTyped key");
	             
	             //shift�� ���� ���ȴ��� Ȯ���ϴ� ���
	             if((e.getModifiers() & 1) != 0){ // 1�� shift, 2�� CtrlŰ �Դϴ�.
	                 //�̸� & �������Ͽ� ������ 0�̾ƴ� ���ڸ� ��ȯ������  �ش� Ű�� ������ Ȯ�� �� �� �ֽ��ϴ�.
	                 System.out.printf("shift�� �������ϴ�.\n");
	             }else
	             {
	                 System.out.printf("shift�� ������ �ʾҽ��ϴ�.\n");
	             }
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keycode = e.getKeyCode();
				System.out.println(keycode);
				keyListen = true;
	        }

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		
		jframe.setFrameVisible();
	}
	
	

}
