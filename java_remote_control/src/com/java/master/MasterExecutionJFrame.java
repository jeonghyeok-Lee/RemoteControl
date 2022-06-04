package com.java.master;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

import com.java.jframe.DefaultJFrame;
import com.java.utility.Screen;

public class MasterExecutionJFrame extends JFrame {
	
	private Screen screen = null;
	private DefaultJFrame jframe = null;
	
	private boolean keyListen = false;
	private boolean mouseListen = false;
	private boolean mouseWheelListen = false;

	private KeyEvent key = null;

	private MouseEvent mouse = null;
	
	private MouseWheelEvent mouseWheel = null;
	

	public KeyEvent getKey() {
		return key;
	}
	
	public boolean isMouseWheelListen() {
		return mouseWheelListen;
	}

	public void setMouseWheelListen(boolean mouseWheelListen) {
		this.mouseWheelListen = mouseWheelListen;
	}

	public MouseWheelEvent getMouseWheel() {
		return mouseWheel;
	}

	public MouseEvent getMouse() {
		return mouse;
	}

	public boolean isMouseListen() {
		return mouseListen;
	}


	public void setMouseListen(boolean mouseListen) {
		this.mouseListen = mouseListen;
	}


	public void setKeyListen(boolean keyListen) {
		this.keyListen = keyListen;
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
		// Ű���� ������
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
				key = e;
				keyListen = true;
	        }

			@Override
			public void keyReleased(KeyEvent e) {

				
			}
		});
		
		// ���콺������
		jframe.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				mouse = e;
				System.out.println(mouse);
				mouseListen = true;
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		jframe.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			
			// ���� ���콺 �������� ���⼭ ���
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					System.out.println(e.getX());
					if(e.getX() == (int)new Screen().getWidth()-1) {
						new Robot().mouseMove(0, e.getY());
//						jframeDispose = true;
						//���� �������ϴ°��� �ش� ���콺���� ���� �������� �� ��� 
						// �ش� jframe�� �����ϰ� ���콺 ���̵��� ���
						
						
					}
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		
		// ���콺 �� �̵��� ������
		jframe.addMouseWheelListener(new MouseWheelListener () {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				mouseWheel = e;
				mouseWheelListen = true;
				
			}
			
		});

		
		jframe.setFrameVisible();
	}
	
	

}
