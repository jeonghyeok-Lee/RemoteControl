package com.java.utility;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.java.db.MyGoogleData;

// 메일을 보낼 계정을 체크하기 위함
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
    	id = googleData.getEmail();      	// 구글 ID
    	pw = googleData.getPassword();       // 구글 비밀번호
    	
    	passAuth = new PasswordAuthentication(id,pw);
    }
    
}
