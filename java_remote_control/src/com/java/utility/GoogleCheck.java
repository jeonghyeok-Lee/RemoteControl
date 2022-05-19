package com.java.utility;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.java.db.MyGoogleData;

// ������ ���� ������ üũ�ϱ� ����
public class GoogleCheck extends Authenticator  {

	PasswordAuthentication passAuth = null;
	
	public GoogleCheck() {
		MyGoogleData googleData = new MyGoogleData();
        String id = googleData.getEmail();      	// ���� ID
        String pw = googleData.getPassword();       // ���� ��й�ȣ

        passAuth = new PasswordAuthentication(id,pw);
	}
	
    public PasswordAuthentication getPasswordAuthentication() {
        return passAuth;
    }

}
