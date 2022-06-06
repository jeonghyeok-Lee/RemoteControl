package com.java.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.java.db.DataBaseInfo;
import com.java.db.dto.UsageRecordDTO;

public class UsageRecordDAO {
	DataBaseInfo dbInfo = DataBaseInfo.getDataBaseInfo();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	private String query = null;
	
	public UsageRecordDAO() {
		try {
			Class.forName(dbInfo.getDirver());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<UsageRecordDTO> userSelect(String where){
		query = "select * from usage_record " + where;
		ArrayList<UsageRecordDTO> userDTO = new ArrayList<UsageRecordDTO>();
		try {
			conn = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUid(), dbInfo.getPw());
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				int usageNo = rs.getInt("usage_no");
				int uageConnect = rs.getInt("usage_connect"); 
				String uageRecent = rs.getString("usage_recent");
				int uageCount = rs.getInt("usage_count"); 		
				String uageRecord = rs.getString("usage_record"); 	
				
				UsageRecordDTO dto = new UsageRecordDTO(usageNo,uageConnect,uageRecent,uageCount,uageRecord);
				userDTO.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return userDTO;
	}
	
	// usage_record 테이블 수정
	public int usageUpdate(String query) {
		this.query = query;
		int result = 0;
		try {
			conn = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUid(), dbInfo.getPw());
			stmt = conn.createStatement();
			
			result = stmt.executeUpdate(query);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
