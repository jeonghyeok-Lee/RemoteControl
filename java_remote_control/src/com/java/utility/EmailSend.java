package com.java.utility;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

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
		// 시스템 정보를 가져오기 위한 Properties 선언
		// System.getProperties();를 통해 현재 시스템의 정보를 가져옴
		
        Properties p = System.getProperties();
        p.put("mail.smtp.starttls.enable", "true");     // 키가 "mail.smtp.starttls.enable" 이고 값이 "true"인 것을 p에 저장
        //  "mail.smtp.host" 속성은 이메일 발송을 처리해줄 STMP 서버를 의미
        p.put("mail.smtp.host", "smtp.gmail.com");  
       
        p.put("mail.smtp.auth","true");
        p.put("mail.smtp.port", "587");    
        
        // 구글 인증을 위한 객체 생성
        Authenticator auth = new GoogleCheck();  
        
        Session session = Session.getDefaultInstance(p, auth);
        // 전송하고자하는 메일 메시지 생성을 위한 MimeMessage객체 생성
        MimeMessage msg = new MimeMessage(session);
        String fromName = "발신자 닉네임";
        String charSet = "UTF-8";
        
        try{
            // 편지보낸시간 설정
            msg.setSentDate(new Date());
              
            // 송신자 설정
            InternetAddress from = new InternetAddress() ;
            from = new InternetAddress(new String(fromName.getBytes(charSet), "8859_1") + "<devilljh1@kyungmin.ac.kr>");
            msg.setFrom(from);
              
            // 수신자 설정
            InternetAddress to = new InternetAddress(user);
            msg.setRecipient(Message.RecipientType.TO, to);
             
            // 제목 설정
            msg.setSubject(title, "UTF-8");
            //내용 설정
            msg.setText(content, "UTF-8");  
             
            // 메일 송신
            Transport.send(msg);   
             
            System.out.println("메일 발송을 완료하였습니다.");
        }catch (AddressException addr_e) {  //예외처리 주소를 입력하지 않을 경우
            JOptionPane.showMessageDialog(null, "메일을 입력해주세요", "메일주소입력", JOptionPane.ERROR_MESSAGE);
            addr_e.printStackTrace();
        }catch (MessagingException msg_e) { //메시지에 이상이 있을 경우
            JOptionPane.showMessageDialog(null, "메일을 제대로 입력해주세요.", "오류발생", JOptionPane.ERROR_MESSAGE);
            msg_e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
}
