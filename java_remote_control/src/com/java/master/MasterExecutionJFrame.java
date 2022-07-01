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
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.java.jframe.DefaultJFrame;
import com.java.utility.Screen;

public class MasterExecutionJFrame extends JFrame {
	
	private Screen screen = null;
	private DefaultJFrame jframe = null;
	
	private boolean keyListen = false;			// 키 이벤트 발생시 true
	private boolean mouseListen = false;		// 마우스 이벤트 발생시 true
	private boolean mouseWheelListen = false;	// 마우스 휠 이벤트 발생시 true
	private boolean frameState = false;			// 현재 프레임이 살아있을경우 false
	private boolean slaveEnd = false; 			// 슬레이브가 종료 되었을 경우 true;
	
	private KeyEvent key = null;				// 키 이벤트 발생시의 키 이벤트
	private MouseEvent mouse = null;			// 마우스 이벤트 발생시의 마우스 이벤트
	private MouseWheelEvent mouseWheel = null;	// 마우스 휠 이벤트 발생시의 마우스 휠 이벤트
	
	
	public boolean isSlaveEnd() {
		return slaveEnd;
	}
	public void setSlaveEnd(boolean slaveEnd) {
		this.slaveEnd = slaveEnd;
	}
	
	public boolean isFrameState() {
		return frameState;
	}
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
		this.requestFocus();
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
	             if((e.getModifiers() & 1) != 0){ // 1은 shift, 2는 Ctrl키 8은 alt
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
				System.out.println(key.getKeyChar() + " keycode : " + key.getKeyCode());
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
					if(slaveEnd) {
						jframe.dispose();
					}
					if(e.getX() == (int)new Screen().getWidth()-10) {
						new Robot().mouseMove(1, e.getY());
						jframe.requestFocus();
						frameState = true;
						jframe.dispose();	// 현재 자신 종료
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
