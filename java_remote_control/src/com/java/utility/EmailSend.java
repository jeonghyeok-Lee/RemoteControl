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
		
		// 1. �߽����� ���� ������ ��й�ȣ ����
		final String myMail = auth.getId();
		final String myPassword = auth.getPw();

		// 2. Property�� SMTP ���� ���� ����
		Properties prop = new Properties();

		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		// 3. SMTP ���������� ����� ������ ������� Session Ŭ������ �ν��Ͻ� ����
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myMail, myPassword);
			}
		});

		// 4. Message Ŭ������ ��ü�� ����Ͽ� �����ڿ� ����, ������ �޽����� �ۼ��Ѵ�.
		// 5. Transport Ŭ������ ����Ͽ� �ۼ��� �޼����� �����Ѵ�.
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(myMail));

			// ������ ���� �ּ�
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
