package com.java.db.dto;

public class UsageRecordNonDTO {
	private int usageNo;
	private int usageConnect; 		// (마스터-ip)의 번호 - ConnectNonUserDTO에서 가져옴
	private String usageRecent; 	// 최근 사용기록
	private int usageCount; 		// 사용횟수
	private String usageRecord; 	// 사용기록
	
	public int getUsageNo() {
		return usageNo;
	}
	public void setUsageNo(int usageNo) {
		this.usageNo = usageNo;
	}
	public int getUsageConnect() {
		return usageConnect;
	}
	public void setUsageConnect(int usageConnect) {
		this.usageConnect = usageConnect;
	}
	public String getUsageRecent() {
		return usageRecent;
	}
	public void setUsageRecent(String usageRecent) {
		this.usageRecent = usageRecent;
	}
	public int getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}
	public String getUsageRecord() {
		return usageRecord;
	}
	public void setUageRecord(String usageRecord) {
		this.usageRecord = usageRecord;
	}
	
	public UsageRecordNonDTO(int usageNo, int usageConnect, String usageRecent, int usageCount, String usageRecord) {
		this.usageNo = usageNo;
		this.usageConnect = usageConnect;
		this.usageRecent = usageRecent;
		this.usageCount = usageCount;
		this.usageRecord = usageRecord;
	}
}
