package com.java.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class IpAddress {
	private String hostName = ""
				, hostAddress = "";

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	
	public IpAddress() {
		try {
			InetAddress inad = InetAddress.getLocalHost();
			
			hostName = inad.getHostName();
			hostAddress = inad.getHostAddress();
			
		}catch (UnknownHostException hostError) {
			hostError.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
