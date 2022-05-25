package com.java.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.java.db.DataBaseInfo;
import com.java.db.dto.UserDTO;

public class UserDAO {
	DataBaseInfo dbInfo = DataBaseInfo.getDataBaseInfo();
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	private String query = null;
	
	public UserDAO() {
		try {
			Class.forName(dbInfo.getDirver());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<UserDTO> userSelect(String where){
		query = "select * from user " + where;
		ArrayList<UserDTO> userDTO = new ArrayList<UserDTO>();
		try {
			conn = DriverManager.getConnection(dbInfo.getUrl(), dbInfo.getUid(), dbInfo.getPw());
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				int userNo = rs.getInt("user_no");
				String userId = rs.getString("user_id");
				String userPassword = rs.getString("user_password");
				String userName = rs.getString("user_name");
				String userEmail = rs.getString("user_email");
				String userDate = rs.getString("user_date");
				String userRank = rs.getString("user_rank");
				
				UserDTO dto = new UserDTO(userNo, userId, userPassword, userName, userEmail, userDate,userRank);
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
}
