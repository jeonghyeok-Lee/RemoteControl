package com.java.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.java.db.DataBaseInfo;
import com.java.db.dto.ConnectUserDTO;

public class ConnectUserDAO {
	DataBaseInfo dbInfo = DataBaseInfo.getDataBaseInfo();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	private String query = null;
	
	public ConnectUserDAO() {
		try {
			Class.forName(dbInfo.getDirver());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 테이블에서 모든 레코드를 반환
	public ArrayList<ConnectUserDTO> connectSelect(String where){
		query = "select * from connect_user " + where;
		ArrayList<ConnectUserDTO> userDTO = new ArrayList<ConnectUserDTO>();
		try {
			conn = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUid(), dbInfo.getPw());
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				int connectNo = rs.getInt("conncet_no");
				int connectMaster= rs.getInt("conncet_master");
				int connectSlave= rs.getInt("conncet_slave");
				String connectDate = rs.getString("conncet_date");
				
				ConnectUserDTO dto = new ConnectUserDTO(connectNo,connectMaster,connectSlave,connectDate);
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
	
	// 쿼리에 따른 connect_user테이블 수정
	public int connectUpdate(String query) {
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
