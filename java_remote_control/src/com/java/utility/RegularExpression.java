package com.java.utility;

import java.util.regex.Pattern;

// ���Խ�
public class RegularExpression {
	
	private String pattern = "";
	private boolean regex = false;
	
	public boolean isRegex() {
		return regex;
	}

	// üũ�� ���ڿ� - str / üũ�� ���ڿ��� ��Ʈ�κ� - part[id / pw / email]
	public RegularExpression(String str, String part) {
		if(part == "id")
			pattern = "^[\\w]*{8,20}$";
		else if(part == "pw")
			pattern = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{8,20}$";
		else if(part == "email")
			pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
		else if(part == "ip")
			pattern = "((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])([.](?!$)|$)){4}";
		regex = Pattern.matches(pattern, str);

	}
	
//	public static void main(String[] args) {
//		String ip ="192.168.0.3";
//		RegularExpression c = new RegularExpression(ip,"ip");
//		System.out.println(c.isRegex());
//	}
}
