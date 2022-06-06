package com.java.db.dto;

// 슬레이브 - 마스터 
public class ConnectUserDTO {

	private int connectNo;
	private int connectMaster;
	private int connectSlave;
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
	public int getConnectSlave() {
		return connectSlave;
	}
	public void setConnectSlave(int connectSlave) {
		this.connectSlave = connectSlave;
	}
	public String getConnectDate() {
		return connectDate;
	}
	public void setConnectDate(String connectDate) {
		this.connectDate = connectDate;
	}
	
	public ConnectUserDTO(int connectNo, int connectMaster , int connectSlave , String connectDate) {
		this.connectNo = connectNo;
		this.connectMaster = connectMaster;
		this.connectSlave = connectSlave;
		this.connectDate = connectDate;
	}
}
