package com.java.utility;

import java.util.regex.Pattern;

// 정규식
public class RegularExpression {
	
	private String pattern = "";
	private boolean regex = false;
	
	public boolean isRegex() {
		return regex;
	}

	// 체크할 문자열 - str / 체크할 문자열의 파트부분 - part[id / pw / email]
	public RegularExpression(String str, String part) {
		if(part == "id")
			pattern = "^[\\w]*{8,20}$";
		else if(part == "pw")
			pattern = "^[a-zA-Z\\\\d`~!@#$%^&*()-_=+]{8,20}$";
		else if(part == "email")
			pattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
		
		regex = Pattern.matches(pattern, str);

	}
}
