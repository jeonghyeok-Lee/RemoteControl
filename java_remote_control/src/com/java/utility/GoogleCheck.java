package com.java.utility;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.java.db.MyGoogleData;

// 메일을 보낼 계정을 체크하기 위함
public class GoogleCheck extends Authenticator  {

	PasswordAuthentication passAuth = null;
	
	public GoogleCheck() {
		MyGoogleData googleData = new MyGoogleData();
        String id = googleData.getEmail();      	// 구글 ID
        String pw = googleData.getPassword();       // 구글 비밀번호

        passAuth = new PasswordAuthentication(id,pw);
	}
	
    public PasswordAuthentication getPasswordAuthentication() {
        return passAuth;
    }

}
