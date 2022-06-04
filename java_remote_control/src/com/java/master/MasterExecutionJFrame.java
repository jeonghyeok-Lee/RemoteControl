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
		
		jframe = new DefaultJFrame("실행장소",(int)screen.getWidth(),(int)screen.getHeight());
		
		jframe.setPanelEmpty();
		
		jframe.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		jframe.setUndecorated(true);
		jframe.setBackground(new Color(0,0,0,100));
		
		jframe.requestFocus();
		// 키보드 리스너
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
				key = e;
				keyListen = true;
	        }

			@Override
			public void keyReleased(KeyEvent e) {

				
			}
		});
		
		// 마우스리스너
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
			
			
			// 추후 마우스 움직임을 여기서 계산
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					System.out.println(e.getX());
					if(e.getX() == (int)new Screen().getWidth()-1) {
						new Robot().mouseMove(0, e.getY());
//						jframeDispose = true;
						//지금 만들어야하는것은 해당 마우스값이 가장 우측으로 갈 경우 
						// 해당 jframe을 종료하고 마우스 값이동을 대기
						
						
					}
				} catch (AWTException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		
		// 마우스 휠 이동용 리스너
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
