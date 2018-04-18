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
	
	public static int getSongId(String title, String composer, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		int id = 0;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT songs.id\r\n" + 
					"	FROM songs\r\n" + 
					"	LEFT JOIN users\r\n" + 
					"		ON songs.user_id=users.id\r\n" + 
					"	WHERE songs.title LIKE '" + title + "'\r\n" + 
					"	AND users.username LIKE '" + composer + "';");
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
	
	public static boolean alreadyFollowing(int userId, int followingId, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		boolean returnBool = false;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT *\r\n" + 
					"	FROM following\r\n" + 
					"    WHERE following.user_id=" + userId + "\r\n" + 
					"    AND following.following_id=" + followingId + ";");
			while(rs.next()) {
				returnBool = true;
			}
	        
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
		
		return returnBool;
	}
	
	public static boolean alreadyFavorite(int userId, int songId, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		boolean returnBool = false;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT *\r\n" + 
					"	FROM favorites\r\n" + 
					"	WHERE favorites.user_id=" + userId + "\r\n" + 
					"	AND favorites.song_id=" + songId + ";");
			while(rs.next()) {
				returnBool = true;
			}
	        
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
		
		return returnBool;
	}

	public static boolean alreadySaved(int userId, String title, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		boolean returnBool = false;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT *\r\n" + 
					"	FROM songs\r\n" + 
					"	WHERE songs.title='" + title + "'\r\n" + 
					"    AND songs.user_id=" + userId + ";");
			while(rs.next()) {
				returnBool = true;
			}
	        
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
		
		return returnBool;
	}
}
