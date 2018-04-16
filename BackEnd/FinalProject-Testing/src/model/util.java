package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class util {
	public static int getUserId(String username, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		int id = 0;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT *\r\n" + 
					"	FROM users\r\n" + 
					"   WHERE users.username LIKE '" + username + "';");
			while(rs.next()) {
				id = rs.getInt("id"); 
			}
			return id;
	        
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(st != null) {st.close();}
			} catch (SQLException sqle) {
				System.out.println("sqle:" + sqle.getMessage());
			}
		}
		return id;
	}
}
