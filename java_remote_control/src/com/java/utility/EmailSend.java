package com.java.utility;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSend {
	String user = null;
	String title = null;
	String content = null;

	public EmailSend(String user, String title, String content) {
		this.user = user;
		this.title = title;
		this.content = content;
	}

	public void setSend() {

		GoogleCheck auth = new GoogleCheck();
		
		// 1. 발신자의 메일 계정과 비밀번호 설정
		final String myMail = auth.getId();
		final String myPassword = auth.getPw();

		// 2. Property에 SMTP 서버 정보 설정
		Properties prop = new Properties();

		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 3. SMTP 서버정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스 생성
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myMail, myPassword);
			}
		});

		// 4. Message 클래스의 객체를 사용하여 수신자와 내용, 제목의 메시지를 작성한다.
		// 5. Transport 클래스를 사용하여 작성한 메세지를 전달한다.
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(myMail));

			// 수신자 메일 주소
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user));

			// Subject
			message.setSubject(title);

			// Text
			message.setText(content);

			Transport.send(message); // send message
			
			System.out.println("message send successfully...");

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
