package com.java.jframe;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;
import com.java.master.MasterJFrame;
import com.java.slave.SlaveJFrame;

public class LogInJFrame extends JFrame {
	private JTextField txtID = null;
	private JPasswordField txtPW = null;
	private JLabel lbID = null, lbPW = null;
	private boolean checkProgram = false;
	
	private UserDAO dao = null;
	private ArrayList<UserDTO> dto = null;
	
	private DefaultJFrame jframe = null;
	
	// checkProgram - �����̺����� �ƴ��� ���� �ľǿ� true-�����̺� false - ������
	public LogInJFrame(boolean checkProgram) {
		this.checkProgram = checkProgram;
		setUI();
	}
	
	private void setUI() {
		jframe = new DefaultJFrame("�α��� ��",450,220);
		jframe.setPanel();
		
		dao = new UserDAO();
		JPanel center = jframe.getCenterPanel();
		
		center.add(setCenter());
		
		jframe.addContain();
		
	}
	
	private JPanel setCenter() {
		JPanel centerContent = new JPanel(new GridLayout(0,2,10,10));
		JPanel btnContentLeft = new JPanel(new GridLayout(0,2));
		JPanel btnContentRight = new JPanel(new GridLayout(0,2,10,10));
		
		
		JButton btnLogin = new JButton("�α���");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(!isStringEmpty(txtID.getText().toString()) && !isStringEmpty(txtPW.getText().toString())) {	// �Է¶��� ��������ʴٸ�
					String where = "";
					if(checkProgram) {	
						where = "where user_id = '" + txtID.getText() +"' and user_password = '"+txtPW.getText()+"' and user_rank = '�����̺�'";
					}else {
						where = "where user_id = '" + txtID.getText() +"' and user_password = '"+txtPW.getText()+"' and user_rank = '������'";
					}
					
					try {
						dto = dao.userSelect(where);
						if(dto.isEmpty()) {
							String msgText = "���̵� Ȥ�� ��й�ȣ�� �߸��Է��ϼ̽��ϴ�.";
							JLabel[] msg = new JLabel[] {new JLabel(msgText) };
							JButton btnOk = new JButton("Ȯ��");
							DefaultJFrame error = new DefaultJFrame("���", 300, 120, msg, btnOk, false);
							btnOk.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									error.dispose(); // ���� â�ݱ�
								}
							});
							error.addContaionEmpty();
						}
					}catch(Exception e1) {	// �޽����� �α׷� ���� ���� -> dao�� ���� �˻��ϴ� ��[list]�� ����ֱ� ���� �ش� ó���� ������ ���־��� ������ ���͵� �������
						e1.getStackTrace(); 
					}finally {						
						txtID.setText("");
						txtPW.setText("");
					}
					dto = dao.userSelect(where);
					System.out.println(where);
					System.out.println("ID : " + dto.get(0).getUserId() + " PW : " + dto.get(0).getUserPassword());
					
					if(checkProgram) {				// �����̺��ʿ��� �α��� �õ� �� 
						new SlaveJFrame(dto, dao);		// ȸ������ �α���
						jframe.dispose();
						
					}else {							// �������ʿ��� �α��� �õ� ��
						new MasterJFrame(dto,dao);
						jframe.dispose();
					}
						
				}else {
					String msgText = "���̵� Ȥ�� ��й�ȣ�� ��� �Է����ּ���.";
					JLabel[] msg = new JLabel[] {new JLabel(msgText) };
					JButton btnOk = new JButton("Ȯ��");
					DefaultJFrame error = new DefaultJFrame("���", 300, 120, msg, btnOk, false);
					btnOk.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							error.dispose(); // ���� â�ݱ�
						}
					});
					error.addContaionEmpty();
					txtID.setText("");
					txtPW.setText("");
				}	
			}
		});
		
		JButton btnAccount = new JButton("ȸ������");
		btnAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new AccountSignUp(checkProgram);
			}
			
		});
		
		centerContent.add(lbID = new JLabel("ID : "));
		lbID.setHorizontalAlignment(JLabel.CENTER);
		centerContent.add(txtID = new JTextField(15));
		centerContent.add(lbPW = new JLabel("PW : "));
		lbPW.setHorizontalAlignment(JLabel.CENTER);
		centerContent.add(txtPW = new JPasswordField(15));
		btnContentLeft.add(new JLabel());
		btnContentLeft.add(btnLogin);
		
		if(checkProgram) {
			JButton btnNonLogin = new JButton("��α���");
			btnNonLogin.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new SlaveJFrame();	// ��ȸ������ �α���
					dispose();
				}
			});
			btnContentRight.add(btnNonLogin);
		}else {
			btnContentRight.add(new JLabel());
		}
		btnContentRight.add(btnAccount);
		
		centerContent.add(btnContentLeft);
		centerContent.add(btnContentRight);
		
		return centerContent;
		
	}
	
	private boolean isStringEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}
	
}
