package com.java.utility;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.java.db.MyGoogleData;

// ������ ���� ������ üũ�ϱ� ����
public class GoogleCheck extends Authenticator  {

	private String id = null;
	private String pw = null;
	PasswordAuthentication passAuth = null;
	
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public PasswordAuthentication getPassAuth() {
		return passAuth;
	}

	public void setPassAuth(PasswordAuthentication passAuth) {
		this.passAuth = passAuth;
	}
	
    public GoogleCheck() {
    	MyGoogleData googleData = new MyGoogleData();
    	id = googleData.getEmail();      	// ���� ID
    	pw = googleData.getPassword();       // ���� ��й�ȣ
    	
    	passAuth = new PasswordAuthentication(id,pw);
    }
    
}
