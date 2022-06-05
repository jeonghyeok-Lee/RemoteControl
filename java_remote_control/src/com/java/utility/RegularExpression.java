package com.java.utility;

import java.util.regex.Pattern;

// Á¤±Ô½Ä
public class RegularExpression {
	
	private String pattern = "";
	private boolean regex = false;
	
	public boolean isRegex() {
		return regex;
	}

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
