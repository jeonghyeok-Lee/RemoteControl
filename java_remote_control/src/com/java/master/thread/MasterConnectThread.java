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
	private boolean chkPlace = false;							// �������� Ȱ�������� �����ִ����ľ� true -> Ȱ������ Ȱ��ȭ ����
	private Screen screen = new Screen(); 						// ���� ���÷����� ũ�⸦ �ľ��ϱ����� ScreenŬ����
	private TimerClass timer = null;							// ����ð��� ����ϴ� Ŭ����
	private JLabel labelTimer = null; 							// ����ð��� ǥ���� ���̺�
	private MasterExecutionJFrame placeJframe = null; 			// �����Ͱ� Ȱ���� ������ ������.
	private int userNo = 0;										// �������̺��� �ڽ��� �α����� ������ ������ȣ
	// ��ȸ������ ���� ���̺�
	private ConnectNonUserDAO nonUserDAO = new ConnectNonUserDAO();				// �������̺� ���� DAO
	private ArrayList<ConncetNonUserDTO> nonUserDTO = null;						// �������̺� ���� Ʃ���� ������� ����Ʈ
	private UsageRecordNonDAO usageNonDAO = new UsageRecordNonDAO();			// ��������̺� ���� DAO
	private ArrayList<UsageRecordNonDTO> usageNonDTO = null; 					// ��������̺� ���� Ʃ���� ������� ����Ʈ
	
	// ȸ������ ���� ���̺� 
	private ConnectUserDAO userDAO = new ConnectUserDAO();
	private ArrayList<ConnectUserDTO> userDTO = null;
	private UsageRecordDAO usageDAO = new UsageRecordDAO();
	private ArrayList<UsageRecordDTO> usageDTO = null;

	private int connectNo = 0;									// �������̺��� ����� ���� �ڽŰ��� ��Ī��ȣ
	private int usageNo = 0;									// ��������̺��� ��Ϲ�ȣ
	
	private String record = "";									// ��� ����� ��� ���ڵ�

	private boolean check = false; 								// ip���� Ȯ���� ���� boolean true ->  ip
	
	private JPanel recordContent = null;

	// ������ (�������� , �����ڸ� ������ ������, �α����� ȸ�� ��ȣ)
	public MasterConnectThread(ServerSocket server, MasterJFrame myJFrame, int userNo) {
		this.server = server;
		this.myJFrame = myJFrame;
		this.userNo = userNo;
		labelIP = myJFrame.getLabelIP();
		labelHost =  myJFrame.getLabelHost();
		labelTimer = myJFrame.getLabelTimer();
		recordContent = myJFrame.getRecordContent();
	}

	// ���� ����
	public void run() {
		run2();
	}
	
	// �ʱ�ȭ
	private void labelInit() {	
		if(placeJframe != null) {
			placeJframe.setSlaveEnd(true);
			placeJframe = null;
			chkPlace = false;
		}
		
		try {
			// ������ ���� �� �� usage_records�� [���� ��¥+labelTimer(���ð�)]�� ����
			if (check) { // ��ȸ������ ������ ���
				// �����ȣ�� �´� ����� ���̺��� ȣ��
				usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
				usageNo = usageNonDTO.get(0).getUsageNo();
				
				// dto�� �ٽ� ����
				usageNonDTO = usageNonDAO.recordSelect("where usage_no = " + usageNo);
				System.out.println(usageNonDTO.get(0).getUsageNo());
				String record = usageNonDTO.get(0).getUsageRecord();
				// record�� �߰��� ��
				String value = usageNonDTO.get(0).getUsageRecent() + " " + labelTimer.getText() + " / "
						+ record;
				System.out.println(value);

				String query = "update usage_record_non" 
						+ " set usage_records = '" + value + "'" 
						+ "where usage_no = " + usageNo;
				
				// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
				int resultUsage = usageNonDAO.usageNonUpdate(query);

				if (resultUsage != 0) {
					System.out.println("����� ���ڵ� ���� �Ϸ�");
				} else {
					System.out.println("����� ���ڵ� ���� ����");
					throw new Exception();
				}
			}else { 	// ȸ�� ������ ���
				// �����ȣ�� �´� ����� ���̺��� ȣ��
				usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
				usageNo = usageDTO.get(0).getUsageNo();
				
				usageDTO = usageDAO.recordSelect("where usage_no = " + usageNo);
				String record = usageDTO.get(0).getUsageRecord();
				// record�� �߰��� ��
				String value = usageDTO.get(0).getUsageRecent() + " " + labelTimer.getText() + " / "
						+ record;
				System.out.println(value);

				String query = "update usage_record" 
						+ " set usage_records = '" + value + "'" 
						+ "where usage_no = " + usageNo;
				
				// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
				int resultUsage = usageNonDAO.usageNonUpdate(query);

				if (resultUsage != 0) {
					System.out.println("����� ���ڵ� ���� �Ϸ�");
				} else {
					System.out.println("����� ���ڵ� ���� ����");
					throw new Exception();
				
				}
			}
		}catch(SocketException e) { 
			System.out.println("�ʱ�ȭ ���� SocketException ���� �߻�");
		}catch(EOFException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		recordContent.removeAll(); // ��� �ڽ� ����
		recordContent.revalidate();
		recordContent.repaint();
		
		labelIP.setText("000.000.000.000");
		labelHost.setText("Unknown");
		if(timer!=null) timer.getTimer().cancel();
		labelTimer.setText("00:00:00");
		
	}

	public void run2() {
		InetAddress myNet = null, inet = null; 		// ip/hostName���� �˾Ƴ��� ���� ����
		ObjectInputStream objectInStream = null;		// ���� �б� ���� InputStream
		ObjectOutputStream objectOutStream = null; 	// ���� ��ü�� �����ϱ� ���� ObjectOutputStream
		String str = ""; 							// ������ ���ڿ��� �����ϱ� ���� str����
		Point mouse = null;
		Socket socket = null;
		try {
			myNet = InetAddress.getLocalHost();

			// �������� ������ �����ϱ� ���� while��
			while (true) {
				// ��ư���� ó���� ��ư�� ������ ������ ����ϴµ��� ��� ���������Ƿ� ���⼭ ó��
				System.out.println("���� ���");
				try {
					socket = server.accept();
					
					// �����̺꿡 �� ������ ���� ��Ʈ�� ���� / ���� �ޱ� ���� ��Ʈ�� ����
					objectOutStream = new ObjectOutputStream(socket.getOutputStream());
					objectInStream = new ObjectInputStream(socket.getInputStream());
				}catch(SocketException e) { 
					System.out.println("���� ������ ��Ʈ�� ���� �� SocketException ���� �߻�");
					labelInit();
				}
				
				System.out.println(socket);
				System.out.println("���� ����");
				timer = new TimerClass(labelTimer); // Ÿ�̸� ����
				
				// ���� ������ ip�� ������ IP/Host�� ����
				inet = socket.getInetAddress();
				String hostName = socket.getInetAddress().getLocalHost().getHostName().toString();
				labelIP.setText(inet.getHostAddress().toString());
				labelHost.setText(hostName);

				// �����̺꿡�� ���� �� ����
				str = myNet.getHostAddress().toString() + "-" 
						+ myNet.getHostName().toString() + "-"
						+ screen.getWidth() + "-" 
						+ screen.getHeight();
				
				System.out.println(str);
				objectOutStream.writeUTF(str); // ���ڿ��� ���·� �� ����
				objectOutStream.flush();
				
				// �����̺꿡�� �Ѿ�� �� ���� ��ȸ������ ���ӽÿ��� ip�� ȸ���ϰ�� ȸ����ȣ�� ���޵�
				String data = (String)objectInStream.readObject();
				RegularExpression checkData = new RegularExpression(data,"ip");
				System.out.println(data);
				check = checkData.isRegex();
				setDBTable(check, data);	// �Լ��� ����
				
			
				while (true) {
					
					try { // ������ ���� ���߿� EOFException �߻��� �ʱ�ȭ�� ���ְ� break �ɾ��־� �ٽ� accept
						data = (String)objectInStream.readObject();
					}catch(SocketException e) { // ����� ������ ���� ����Ǿ��� ���
						System.out.println("������ ���� �� SocketException ���� �߻�");
						labelInit();
						break;
					}catch(EOFException e) { // EOFException - ���� �����Ͱ� ���� �߻��ϴ� ���� ����� ��밡 ���� �����Ͽ� �����Ͱ� ������ �ȵɶ�
						System.out.println("EOFException ���� �߻�");
						labelInit();
						break;
					}

					mouse = MouseInfo.getPointerInfo().getLocation();
					
					// ���� ���콺�� ��ġ�� 0�̸鼭 chkPlace�� false�϶� == Ȱ������ ����
					// => chkPlace - false ==> ���� Ȱ�������� �������� ����
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
						// ��ü�� �� ����
						// placeJframe���� �������� ���� true��� �ش� ���� ����
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
					} catch (SocketException e) { // Ŭ���̾�Ʈ�� ���� �����Ͽ��� ��츦 ĳġ
						System.out.println("SocketException ���� �߻�");
						labelInit();
						break;
					}
					sleep(10); // ���� ���콺�� �� �ȸԴ´� ������ �ش� �κ��� �� ���߱�
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException �߻�\n"+e.getMessage());
			labelInit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (objectInStream != null) objectInStream.close();
				if (objectOutStream != null) objectOutStream.close();
				if (socket != null) socket.close();
				labelInit();
				System.out.println("���� �ݱ�");
			} catch(SocketException e) { 
				System.out.println("���� �� SocketException ���� �߻�");
				labelInit();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// DB�� �����Ͽ� ���� �߰� ���� 
	private void setDBTable(boolean check, String data) {
		if(check) {	// ip������ ���
			/*
			 * ó������ �����Ϳ� ����� ���̶�� ConncetNonUserDAO�� �߰�
			 * ó���� �ƴ϶�� ConncetNonUserDAO���� ��ȣ�� ������
			 * UsageRecordNonDAO���� ������ ��ȣ�� �´� ���ڵ忡�� 
			 * recent�� ���� �ð����� ����
			 * count�� 1 ����
			 * ���� �����
			 * records���� [recent + ���ð�]�� ����
			 * */
			
			
			try {
				// ���� ���� ���Ȯ���� ���� �������� ���̺� Ȯ��
				nonUserDAO = new ConnectNonUserDAO();
				nonUserDTO = nonUserDAO.connectSelect("where connect_master = "+userNo+" and connect_ip = '"+data+"'");
				int rowCount = nonUserDTO.size();	// �������̺� Ʃ���� ����
				
				// ���� ��¥/�ð�        
				LocalDateTime now = LocalDateTime.now();         
				// ���� ��¥/�ð� ���        
				System.out.println(now);     
				// ������        
				String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy�� MM�� dd�� HH�� mm�� ss��")); 
				
				// ����ٸ� ���̺� ���������� �߰� 
				if(nonUserDTO.isEmpty()) {	// ����� ���� ���� ���
					System.out.println(rowCount);
					
					String query = "insert into connect_nonuser values("
							+(++rowCount)+","
							+ userNo +",'"
							+ data + "','"
							+ formatedNow +"')";
					
					// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
					int resultConnect = nonUserDAO.connectNonUpdate(query);
					
					if(resultConnect != 0) {
						System.out.println("�������̺� ��Ͽ� ����");
					}else {
						System.out.println("�������̺� ��Ͽ� ����");
					}
					
					// �ٽ� ��������
					nonUserDTO = nonUserDAO.connectSelect("where connect_master = "+userNo+" and connect_ip = '"+data+"'");
					System.out.println(nonUserDTO.get(0).getConnectNo());	
					
					// �����ȣ
					connectNo = nonUserDTO.get(0).getConnectNo();
					
					int rowUsageCount = usageNonDAO.getConnectRow(""); 	// ����� ���̺� Ʃ���� ����
					
					// ����� ���̺� �߰�
					String usageQuery = "insert into usage_record_non(usage_no,usage_connect,usage_count,usage_records) values("
							+ (3)+","
							+ connectNo +","
							+ 1 +",'"
							+ " / " +"')";
					
					// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
					int resultUsage = usageNonDAO.usageNonUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("��������̺� ��Ͽ� ����");
					}else {
						System.out.println("��������̺� ��Ͽ� ����");
						throw new Exception();	// ��Ͻ��н� ���� �߻���Ű��
					}
					
					usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
					
				}else {					// ����� ���� �ִ� ���
					// �����ȣ
					connectNo = nonUserDTO.get(0).getConnectNo();
					
					// �����ȣ�� �´� ����� ���̺��� ȣ��
					usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
					usageNo = usageNonDTO.get(0).getUsageNo();
					
					int usageCount = usageNonDTO.get(0).getUsageCount();

					String usageQuery = "update usage_record_non "
							+ "set usage_count = " + ++usageCount
							+ " where usage_no = " + usageNo;
					
					// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
					int resultUsage = usageNonDAO.usageNonUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("����� ���̺� ������Ʈ�� ����");
					}else {
						System.out.println("��������̺� ������Ʈ�� ����");
						throw new Exception();	// ��Ͻ��н� ���� �߻���Ű��
					}
					// usageNonDTO ������Ʈ
					usageNonDTO = usageNonDAO.recordSelect("where usage_connect = "+ connectNo);
				}	
				

				
				record = usageNonDTO.get(0).getUsageRecord();
				String[] recordSplit = record.split("/");
				recordContent.setLayout(new GridLayout(0,1));
				for(int i = 0; i< recordSplit.length-2;i++) {
					recordContent.add(new JLabel((i+1) + ". "+recordSplit[i]));
				}
				
			}catch(Exception e1) {	// �޽����� �α׷� ���� ���� -> dao�� ���� �˻��ϴ� ��[list]�� ����ֱ� ���� �ش� ó���� ������ ���־��� ������ ���͵� �������
				e1.printStackTrace();
			}
			
			
			
		}else { 					// ȸ����ȣ�� ���
			/*
			 * ó������ �����Ϳ� ����� ���̶�� ConncetUserDAO�� �߰�
			 * ó���� �ƴ϶�� ConncetUserDAO���� ��ȣ�� ������
			 * UsageRecordDAO���� ������ ��ȣ�� �´� ���ڵ忡�� 
			 * recent�� ���� �ð����� ����
			 * count�� 1 ����
			 * */
			
			try {
				// ���� ���� ���Ȯ���� ���� �������� ���̺� Ȯ��
				userDAO = new ConnectUserDAO();
				userDTO = userDAO.connectSelect("where connect_master = "+userNo+" and connect_slave = "+data);
				int rowCount = userDAO.getConnectRow("");	// �������̺� Ʃ���� ����
				
				// ���� ��¥/�ð�        
				LocalDateTime now = LocalDateTime.now();         
				// ���� ��¥/�ð� ���        
				System.out.println(now);     
				// ������        
				String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy�� MM�� dd�� HH�� mm�� ss��")); 
				
				// ����ٸ� ���̺� ���������� �߰� 
				if(userDTO.isEmpty()) {	// ����� ���� ���� ���
					
					String query = "insert into connect_user values("
							+(++rowCount)+","
							+ userNo +","
							+ data + ",'"
							+ formatedNow +"')";
					
					// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
					int resultConnect = userDAO.connectUpdate(query);
					
					if(resultConnect != 0) {
						System.out.println("�������̺� ��Ͽ� ����");
					}else {
						System.out.println("�������̺� ��Ͽ� ����");
					}
					
					// �ٽ� ��������
					userDTO = userDAO.connectSelect("where connect_master = "+userNo+" and connect_slave = "+data);
					System.out.println(userDTO.get(0).getConnectNo());	
					
					// �����ȣ
					connectNo = userDTO.get(0).getConnectNo();
					
					// �����ȣ�� �´� ����� ���̺��� ȣ��
					usageDAO = new UsageRecordDAO();
					
					int rowUsageCount = usageDAO.getConnectRow(""); 	// ����� ���̺� Ʃ���� ����
					
					// ����� ���̺� �߰�
					String usageQuery = "insert into usage_record(usage_no,usage_connect,usage_count,usage_records) values("
							+ (++rowUsageCount)+","
							+ connectNo +","
							+ 1 +",'"
							+ " / " +"')";
					
					// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
					int resultUsage = usageDAO.usageUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("��������̺� ��Ͽ� ����");
					}else {
						System.out.println("��������̺� ��Ͽ� ����");
						throw new Exception();	// ��Ͻ��н� ���� �߻���Ű��
					}
					
					usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
					
				}else {					// ����� ���� �ִ� ���
					// �����ȣ
					connectNo = userDTO.get(0).getConnectNo();
					
					// �����ȣ�� �´� ����� ���̺��� ȣ��
					usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
					
					usageNo = usageDTO.get(0).getUsageNo();
					int usageCount = usageDTO.get(0).getUsageCount();

					String usageQuery = "update usage_record "
							+ "set usage_count = " + ++usageCount
							+ " where usage_no = " + usageNo;
					
					
					// query�� �����Ͽ� ���̺��� ������Ʈ ���� ����� ��ȯ
					int resultUsage = usageNonDAO.usageNonUpdate(usageQuery);
					
					if(resultUsage != 0) {
						System.out.println("����� ���̺� ������Ʈ�� ����");
					}else {
						System.out.println("��������̺� ������Ʈ�� ����");
						throw new Exception();	// ��Ͻ��н� ���� �߻���Ű��
					}
					
					// DTO ������Ʈ
					usageDTO = usageDAO.recordSelect("where usage_connect = "+ connectNo);
				}	
				record = usageDTO.get(0).getUsageRecord();
				String[] recordSplit = record.split("/");
				recordContent.setLayout(new GridLayout(0,1));
				for(int i = 0; i< recordSplit.length-2;i++) {
					recordContent.add(new JLabel((i+1) + ". "+recordSplit[i]));
				}
		
			}catch(Exception e1) {	// �޽����� �α׷� ���� ���� -> dao�� ���� �˻��ϴ� ��[list]�� ����ֱ� ���� �ش� ó���� ������ ���־��� ������ ���͵� �������
				e1.printStackTrace();
			}
			
		}
	}

}
