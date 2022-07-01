package com.java.master.thread;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.java.db.dao.ConnectNonUserDAO;
import com.java.db.dao.ConnectUserDAO;
import com.java.db.dao.UsageRecordDAO;
import com.java.db.dao.UsageRecordNonDAO;
import com.java.db.dto.ConncetNonUserDTO;
import com.java.db.dto.ConnectUserDTO;
import com.java.db.dto.UsageRecordDTO;
import com.java.db.dto.UsageRecordNonDTO;
import com.java.master.MasterExecutionJFrame;
import com.java.master.MasterJFrame;
import com.java.utility.RegularExpression;
import com.java.utility.Screen;
import com.java.utility.TimerClass;

public class MasterConnectThread extends Thread {

	private JLabel labelIP = null, labelHost = null;
	private ServerSocket server = null;
	private MasterJFrame myJFrame = null;
	private boolean chkPlace = false;							// 마스터의 활동영역이 열려있는지파악 true -> 활동영역 활성화 상태
	private Screen screen = new Screen(); 						// 현재 디스플레이의 크기를 파악하기위한 Screen클래스
	private TimerClass timer = null;							// 연결시간을 계산하는 클래스
	private JLabel labelTimer = null; 							// 연결시간을 표시할 레이블
	private MasterExecutionJFrame placeJframe = null; 			// 마스터가 활동할 영역인 프레임.
	private int userNo = 0;										// 유저테이블에서 자신이 로그인한 계정의 유저번호
	// 비회원접속 관련 테이블
	private ConnectNonUserDAO nonUserDAO = new ConnectNonUserDAO();				// 연결테이블에 대한 DAO
	private ArrayList<ConncetNonUserDTO> nonUserDTO = null;						// 연결테이블에 대한 튜플을 얻기위한 리스트
	private UsageRecordNonDAO usageNonDAO = new UsageRecordNonDAO();			// 사용기록테이블에 대한 DAO
	private ArrayList<UsageRecordNonDTO> usageNonDTO = null; 					// 사용기록테이블에 대한 튜플을 얻기위한 리스트
	
	// 회원접속 관련 테이블 
	private ConnectUserDAO userDAO = new ConnectUserDAO();
	private ArrayList<ConnectUserDTO> userDTO = null;
	private UsageRecordDAO usageDAO = new UsageRecordDAO();
	private ArrayList<UsageRecordDTO> usageDTO = null;

	private int connectNo = 0;									// 연결테이블에서 연결된 대상과 자신과의 매칭번호
	private int usageNo = 0;									// 사용기록테이블에서 기록번호
	
	private String record = "";									// 사용 기록이 담긴 레코드

	private boolean check = false; 								// ip인지 확인을 위한 boolean true ->  ip
	
	private JPanel recordContent = null;

	// 생성자 (서버소켓 , 생성자를 생성한 프레임, 로그인한 회원 번호)
	public MasterConnectThread(ServerSocket server, MasterJFrame myJFrame, int userNo) {
		this.server = server;
		this.myJFrame = myJFrame;
		this.userNo = userNo;
		labelIP = myJFrame.getLabelIP();
		labelHost =  myJFrame.getLabelHost();
		labelTimer = myJFrame.getLabelTimer();
		recordContent = myJFrame.getRecordContent();
	}

	// 지속 접속
	public void run() {
		run2();
	}
	
	// 초기화
	private void labelInit() {	
		if(placeJframe != null) {
			placeJframe.setSlaveEnd(true);
			placeJframe = null;
			chkPlace = false;
		}
		
		try {
			// 접속이 종료 될 때 usage_records에 [현재 날짜+labelTimer(사용시간)]를 삽입
			if (check) { // 비회원으로 접속한 경우
				// 연결번호에 맞는 사용기록 테이블을 호출
				usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
				usageNo = usageNonDTO.get(0).getUsageNo();
				
				// dto를 다시 설정
				usageNonDTO = usageNonDAO.recordSelect("where usage_no = " + usageNo);
				System.out.println(usageNonDTO.get(0).getUsageNo());
				String record = usageNonDTO.get(0).getUsageRecord();
				// record에 추가할 값
				String value = usageNonDTO.get(0).getUsageRecent() + " " + labelTimer.getText() + " / "
						+ record;
				System.out.println(value);

				String query = "update usage_record_non" 
						+ " set usage_records = '" + value + "'" 
						+ "where usage_no = " + usageNo;
				
				// query를 실행하여 테이블을 업데이트 이후 결과를 반환
				int resultUsage = usageNonDAO.usageNonUpdate(query);

				if (resultUsage != 0) {
					System.out.println("사용기록 레코드 수정 완료");
				} else {
					System.out.println("사용기록 레코드 수정 실패");
					throw new Exception();
				}
			}else { 	// 회원 접속일 경우
				// 연결번호에 맞는 사용기록 테이블을 호출
				usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
				usageNo = usageDTO.get(0).getUsageNo();
				
				usageDTO = usageDAO.recordSelect("where usage_no = " + usageNo);
				String record = usageDTO.get(0).getUsageRecord();
				// record에 추가할 값
				String value = usageDTO.get(0).getUsageRecent() + " " + labelTimer.getText() + " / "
						+ record;
				System.out.println(value);

				String query = "update usage_record" 
						+ " set usage_records = '" + value + "'" 
						+ "where usage_no = " + usageNo;
				
				// query를 실행하여 테이블을 업데이트 이후 결과를 반환
				int resultUsage = usageNonDAO.usageNonUpdate(query);

				if (resultUsage != 0) {
					System.out.println("사용기록 레코드 수정 완료");
				} else {
					System.out.println("사용기록 레코드 수정 실패");
					throw new Exception();
				
				}
			}
		}catch(SocketException e) { 
			System.out.println("초기화 도중 SocketException 예외 발생");
		}catch(EOFException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		recordContent.removeAll(); // 모든 자식 삭제
		recordContent.revalidate();
		recordContent.repaint();
		
		labelIP.setText("000.000.000.000");
		labelHost.setText("Unknown");
		if(timer!=null) timer.getTimer().cancel();
		labelTimer.setText("00:00:00");
		
	}

	public void run2() {
		InetAddress myNet = null, inet = null; 		// ip/hostName등을 알아내기 위한 변수
		ObjectInputStream objectInStream = null;		// 값을 읽기 위한 InputStream
		ObjectOutputStream objectOutStream = null; 	// 값을 객체로 전달하기 위한 ObjectOutputStream
		String str = ""; 							// 전달할 문자열을 저장하기 위한 str변수
		Point mouse = null;
		Socket socket = null;
		try {
			myNet = InetAddress.getLocalHost();

			// 연결해제 연결을 지속하기 위한 while문
			while (true) {
				// 버튼에서 처리시 버튼이 소켓이 수신을 대기하는동안 계속 눌려있으므로 여기서 처리
				System.out.println("소켓 대기");
				try {
					socket = server.accept();
					
					// 슬레이브에 값 보내기 위해 스트림 생성 / 값을 받기 위한 스트림 생성
					objectOutStream = new ObjectOutputStream(socket.getOutputStream());
					objectInStream = new ObjectInputStream(socket.getInputStream());
				}catch(SocketException e) { 
					System.out.println("소켓 접속후 스트림 생성 중 SocketException 예외 발생");
					labelInit();
				}
				
				System.out.println(socket);
				System.out.println("소켓 연결");
				timer = new TimerClass(labelTimer); // 타이머 진행
				
				// 현재 소켓의 ip를 가져와 IP/Host에 설정
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// 슬레이브에게 보낼 값 정의
				str = myNet.getHostAddress().toString() + "-" 
						+ myNet.getHostName().toString() + "-"
						+ screen.getWidth() + "-" 
						+ screen.getHeight();
				
				System.out.println(str);
				objectOutStream.writeUTF(str); // 문자열의 형태로 값 전달
				objectOutStream.flush();
				
				// 슬레이브에서 넘어온 값 만약 비회원으로 접속시에는 ip가 회원일경우 회원번호가 전달됨
				String data = (String)objectInStream.readObject();
				RegularExpression checkData = new RegularExpression(data,"ip");
				System.out.println(data);
				check = checkData.isRegex();
				setDBTable(check, data);	// 함수로 묶기
				
			
				while (true) {
					
					try { // 데이터 수신 도중에 EOFException 발생시 초기화를 해주고 break 걸어주어 다시 accept
						data = (String)objectInStream.readObject();
					}catch(SocketException e) { // 연결된 소켓이 강제 종료되었을 경우
						System.out.println("데이터 수신 중 SocketException 예외 발생");
						labelInit();
						break;
					}catch(EOFException e) { // EOFException - 읽을 데이터가 없어 발생하는 문제 현재는 상대가 직접 종료하여 데이터가 전송이 안될때
						System.out.println("EOFException 예외 발생");
						labelInit();
						break;
					}

					mouse = MouseInfo.getPointerInfo().getLocation();
					
					// 현재 마우스의 위치가 0이면서 chkPlace가 false일때 == 활동영역 생성
					// => chkPlace - false ==> 현재 활동영역이 생성되지 않음
					if (mouse.x == 0 && !chkPlace) {	
						new Robot().mouseMove((int) screen.getWidth(), mouse.y);
						placeJframe = new MasterExecutionJFrame();
						placeJframe.requestFocus();
						chkPlace = true;
					} 
					
					if(placeJframe != null &&placeJframe.isFrameState()) {
						placeJframe = null;
						chkPlace = false;
					}
					try {
						// 객체로 값 전달
						// placeJframe에서 리스너의 값이 true라면 해당 값을 전달
						if (placeJframe != null) {
							if (placeJframe.isKeyListen()) {
								objectOutStream.writeObject(placeJframe.getKey());
								objectOutStream.flush();
								placeJframe.setKeyListen(false);
								sleep(100);
							} else if (placeJframe.isMouseWheelListen()) {
								objectOutStream.writeObject(placeJframe.getMouseWheel());
								objectOutStream.flush();
								placeJframe.setMouseWheelListen(false);
								sleep(100);
							} else if (placeJframe.isMouseListen()) {
								objectOutStream.writeObject(placeJframe.getMouse());
								objectOutStream.flush();
								placeJframe.setMouseListen(false);
							} else {
								objectOutStream.writeObject(mouse);
								objectOutStream.flush();
							}
						} else {
							objectOutStream.writeObject(mouse);
							objectOutStream.flush();

						}
					} catch (SocketException e) { // 클라이언트가 강제 종료하였을 경우를 캐치
						System.out.println("SocketException 예외 발생");
						labelInit();
						break;
					}
					sleep(10); // 만약 마우스가 잘 안먹는다 싶으면 해당 부분을 더 낮추기
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException 발생\n"+e.getMessage());
			labelInit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectInStream != null) objectInStream.close();
				if (objectOutStream != null) objectOutStream.close();
				if (socket != null) socket.close();
				labelInit();
				System.out.println("소켓 닫기");
			} catch(SocketException e) { 
				System.out.println("종료 중 SocketException 예외 발생");
				labelInit();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// DB에 연결하여 값을 추가 수정 
	private void setDBTable(boolean check, String data) {
		if(check) {	// ip형태일 경우
			/*
			 * 처음으로 마스터와 연결된 것이라면 ConncetNonUserDAO에 추가
			 * 처음이 아니라면 ConncetNonUserDAO에서 번호를 가져옴
			 * UsageRecordNonDAO에서 가져온 번호와 맞는 레코드에서 
			 * recent는 지금 시간으로 수정
			 * count는 1 증가
			 * 연결 종료시
			 * records에는 [recent + 사용시간]을 누적
			 * */
			
			
			try {
				// 이전 연결 기록확인을 위한 연결정보 테이블 확인
				nonUserDAO = new ConnectNonUserDAO();
				nonUserDTO = nonUserDAO.connectSelect("where connect_master = "+userNo+" and connect_ip = '"+data+"'");
				int rowCount = nonUserDTO.size();	// 연결테이블 튜플의 개수
				
				// 현재 날짜/시간        
				LocalDateTime now = LocalDateTime.now();         
				// 현재 날짜/시간 출력        
				System.out.println(now);     
				// 포맷팅        
				String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")); 
				
				// 비었다면 테이블에 연결정보를 추가 
				if(nonUserDTO.isEmpty()) {	// 연결된 적이 없는 경우
					System.out.println(rowCount);
					
					String query = "insert into connect_nonuser values("
							+(++rowCount)+","
							+ userNo +",'"
							+ data + "','"
							+ formatedNow +"')";
					
					// query를 실행하여 테이블을 업데이트 이후 결과를 반환
					int resultConnect = nonUserDAO.connectNonUpdate(query);
					
					if(resultConnect != 0) {
						System.out.println("연결테이블에 등록에 성공");
					}else {
						System.out.println("연결테이블에 등록에 실패");
					}
					
					// 다시 가져오기
					nonUserDTO = nonUserDAO.connectSelect("where connect_master = "+userNo+" and connect_ip = '"+data+"'");
					System.out.println(nonUserDTO.get(0).getConnectNo());	
					
					// 연결번호
					connectNo = nonUserDTO.get(0).getConnectNo();
					
					int rowUsageCount = usageNonDAO.getConnectRow(""); 	// 사용기록 테이블 튜플의 개수
					
					// 사용기록 테이블에 추가
					String usageQuery = "insert into usage_record_non(usage_no,usage_connect,usage_count,usage_records) values("
							+ (3)+","
							+ connectNo +","
							+ 1 +",'"
							+ " / " +"')";
					
					// query를 실행하여 테이블을 업데이트 이후 결과를 반환
					int resultUsage = usageNonDAO.usageNonUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("사용기록테이블에 등록에 성공");
					}else {
						System.out.println("사용기록테이블에 등록에 실패");
						throw new Exception();	// 등록실패시 예외 발생시키기
					}
					
					usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
					
				}else {					// 연결된 적이 있는 경우
					// 연결번호
					connectNo = nonUserDTO.get(0).getConnectNo();
					
					// 연결번호에 맞는 사용기록 테이블을 호출
					usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
					usageNo = usageNonDTO.get(0).getUsageNo();
					
					int usageCount = usageNonDTO.get(0).getUsageCount();

					String usageQuery = "update usage_record_non "
							+ "set usage_count = " + ++usageCount
							+ " where usage_no = " + usageNo;
					
					// query를 실행하여 테이블을 업데이트 이후 결과를 반환
					int resultUsage = usageNonDAO.usageNonUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("사용기록 테이블에 업테이트에 성공");
					}else {
						System.out.println("사용기록테이블에 업데이트에 실패");
						throw new Exception();	// 등록실패시 예외 발생시키기
					}
					// usageNonDTO 업데이트
					usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
				}	
				

				
				record = usageNonDTO.get(0).getUsageRecord();
				String[] recordSplit = record.split("/");
				recordContent.setLayout(new GridLayout(0,1));
				for(int i = 0; i< recordSplit.length-2;i++) {
					recordContent.add(new JLabel((i+1) + ". "+recordSplit[i]));
				}
				
			}catch(Exception e1) {	// 메시지는 로그로 남을 것임 -> dao를 통해 검색하는 값[list]가 비어있기 때문 해당 처리를 위에서 해주었기 때문에 나와도 상관없음
				e1.printStackTrace();
			}
			
			
			
		}else { 					// 회원번호일 경우
			/*
			 * 처음으로 마스터와 연결된 것이라면 ConncetUserDAO에 추가
			 * 처음이 아니라면 ConncetUserDAO에서 번호를 가져옴
			 * UsageRecordDAO에서 가져온 번호와 맞는 레코드에서 
			 * recent는 지금 시간으로 수정
			 * count는 1 증가
			 * */
			
			try {
				// 이전 연결 기록확인을 위한 연결정보 테이블 확인
				userDAO = new ConnectUserDAO();
				userDTO = userDAO.connectSelect("where connect_master = "+userNo+" and connect_slave = "+data);
				int rowCount = userDAO.getConnectRow("");	// 연결테이블 튜플의 개수
				
				// 현재 날짜/시간        
				LocalDateTime now = LocalDateTime.now();         
				// 현재 날짜/시간 출력        
				System.out.println(now);     
				// 포맷팅        
				String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")); 
				
				// 비었다면 테이블에 연결정보를 추가 
				if(userDTO.isEmpty()) {	// 연결된 적이 없는 경우
					
					String query = "insert into connect_user values("
							+(++rowCount)+","
							+ userNo +","
							+ data + ",'"
							+ formatedNow +"')";
					
					// query를 실행하여 테이블을 업데이트 이후 결과를 반환
					int resultConnect = userDAO.connectUpdate(query);
					
					if(resultConnect != 0) {
						System.out.println("연결테이블에 등록에 성공");
					}else {
						System.out.println("연결테이블에 등록에 실패");
					}
					
					// 다시 가져오기
					userDTO = userDAO.connectSelect("where connect_master = "+userNo+" and connect_slave = "+data);
					System.out.println(userDTO.get(0).getConnectNo());	
					
					// 연결번호
					connectNo = userDTO.get(0).getConnectNo();
					
					// 연결번호에 맞는 사용기록 테이블을 호출
					usageDAO = new UsageRecordDAO();
					
					int rowUsageCount = usageDAO.getConnectRow(""); 	// 사용기록 테이블 튜플의 개수
					
					// 사용기록 테이블에 추가
					String usageQuery = "insert into usage_record(usage_no,usage_connect,usage_count,usage_records) values("
							+ (++rowUsageCount)+","
							+ connectNo +","
							+ 1 +",'"
							+ " / " +"')";
					
					// query를 실행하여 테이블을 업데이트 이후 결과를 반환
					int resultUsage = usageDAO.usageUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("사용기록테이블에 등록에 성공");
					}else {
						System.out.println("사용기록테이블에 등록에 실패");
						throw new Exception();	// 등록실패시 예외 발생시키기
					}
					
					usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
					
				}else {					// 연결된 적이 있는 경우
					// 연결번호
					connectNo = userDTO.get(0).getConnectNo();
					
					// 연결번호에 맞는 사용기록 테이블을 호출
					usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
					
					usageNo = usageDTO.get(0).getUsageNo();
					int usageCount = usageDTO.get(0).getUsageCount();

					String usageQuery = "update usage_record "
							+ "set usage_count = " + ++usageCount
							+ " where usage_no = " + usageNo;
					
					
					// query를 실행하여 테이블을 업데이트 이후 결과를 반환
					int resultUsage = usageNonDAO.usageNonUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("사용기록 테이블에 업테이트에 성공");
					}else {
						System.out.println("사용기록테이블에 업데이트에 실패");
						throw new Exception();	// 등록실패시 예외 발생시키기
					}
					
					// DTO 업데이트
					usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
				}	
				record = usageDTO.get(0).getUsageRecord();
				String[] recordSplit = record.split("/");
				recordContent.setLayout(new GridLayout(0,1));
				for(int i = 0; i< recordSplit.length-2;i++) {
					recordContent.add(new JLabel((i+1) + ". "+recordSplit[i]));
				}
		
			}catch(Exception e1) {	// 메시지는 로그로 남을 것임 -> dao를 통해 검색하는 값[list]가 비어있기 때문 해당 처리를 위에서 해주었기 때문에 나와도 상관없음
				e1.printStackTrace();
			}
			
		}
	}

}
