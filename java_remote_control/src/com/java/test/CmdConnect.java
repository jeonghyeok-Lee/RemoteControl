package com.java.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdConnect {
	
	public String execCmd(String cmd) {
	    try {
	        Process process = Runtime.getRuntime().exec("cmd /c " + cmd);
	        System.out.println(cmd);
	        BufferedReader reader = new BufferedReader(
	                new InputStreamReader(process.getInputStream()));
	        String line = null;
	        StringBuffer sb = new StringBuffer();
	        sb.append(cmd);
	        System.out.println(sb);
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	            sb.append("\n");
	        }
	        System.out.println(sb);
	        return sb.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	public static void main(String[] args) {
		CmdConnect cmd = new CmdConnect();
		System.out.println(cmd.execCmd("echo JVAV_HOME "));
	}
}
