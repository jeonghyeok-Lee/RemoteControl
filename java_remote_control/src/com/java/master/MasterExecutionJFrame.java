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
		
		jframe = new DefaultJFrame("실행장소",(int)screen.getWidth(),(int)screen.getHeight());
		
		jframe.setPanelEmpty();
		
		jframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jframe.setUndecorated(true);
		jframe.setBackground(new Color(0,0,0,100));
		
		jframe.requestFocus();
		jframe.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
//				 System.out.println(e.getKeyChar()+" keyTyped key");
	             
	             //shift가 같이 눌렸는지 확인하는 방법
	             if((e.getModifiers() & 1) != 0){ // 1은 shift, 2는 Ctrl키 입니다.
	                 //이를 & 연산을하여 같으면 0이아닌 숫자를 반환함으로  해당 키가 눌림을 확인 할 수 있습니다.
	                 System.out.printf("shift를 눌렀습니다.\n");
	             }else
	             {
	                 System.out.printf("shift를 누르지 않았습니다.\n");
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
