package com.java.test;

import java.util.ArrayList;

import com.java.db.dao.UserDAO;
import com.java.db.dto.UserDTO;

public class TestClass {

	public static void main(String[] args) {
//		new ThreadTest();
		UserDAO dao = new UserDAO();
		ArrayList<UserDTO> dto = dao.userSelect("where user_id = 'root'");
		System.out.println("No\tID\tPW\tName\tEmail\tRank");
		System.out.println("==============================================");
		for (int i = 0; i < dto.size(); i++) {
			System.out.print(dto.get(i).getUserNo() 
					+ "\t" + dto.get(i).getUserId() 
					+ "\t" + dto.get(i).getUserPassword()
					+ "\t" + dto.get(i).getUserName() 
					+ "\t" + dto.get(i).getUserEmail()
					+ "\t" +dto.get(i).getUserRank());
		}

	}

}
