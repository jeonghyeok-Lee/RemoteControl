package com.java.db.dto;

public class ConncetNonUserDTO {
	private int connectNo;
	private int connectMaster;
	private String connectIP;
	private String connectDate;
	public int getConnectNo() {
		return connectNo;
	}
	public void setConnectNo(int connectNo) {
		this.connectNo = connectNo;
	}
	public int getConnectMaster() {
		return connectMaster;
	}
	public void setConnectMaster(int connectMaster) {
		this.connectMaster = connectMaster;
	}
	public String getConnectIP() {
		return connectIP;
	}
	public void setConnectIP(String connectIP) {
		this.connectIP = connectIP;
	}
	public String getConnectDate() {
		return connectDate;
	}
	public void setConnectDate(String connectDate) {
		this.connectDate = connectDate;
	}
	public ConncetNonUserDTO(int connectNo, int connectMaster, String connectIP,String connectDate) {
		super();
		this.connectNo = connectNo;
		this.connectMaster = connectMaster;
		this.connectIP = connectIP;
		this.connectDate = connectDate;
	}
	
	

	
}
