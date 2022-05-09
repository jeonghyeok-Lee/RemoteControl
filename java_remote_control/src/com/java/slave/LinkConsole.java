package com.java.slave;

import java.net.Socket;
import java.util.Scanner;

public class LinkConsole {	
	public void checkLinkConsole() {
		Scanner scan = new Scanner(System.in);
		try {
			System.out.print("접속 IP : " );
			String ip = scan.next();
			System.out.print("Port 번호 : ");
			int port = scan.nextInt();
			Socket socket = new Socket(ip, port);
//			Socket socket = new Socket("192.168.0.3",9095);
			System.out.println("서버와 접속이 되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				scan.close();
			}catch(Exception e) {
				e.getMessage();
			}
		}
	}
}
