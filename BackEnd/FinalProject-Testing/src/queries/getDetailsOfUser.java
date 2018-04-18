package queries;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.*;

@WebServlet("/getDetailsOfUser")
public class getDetailsOfUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		int userId = Integer.parseInt(request.getParameter("userId"));
		String target = request.getParameter("target");
		
		int numFollowing = 0;
		int numFollowers = 0;
		int numSongs = 0;
		String imageUrl = null;
		boolean isFollowing = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT users.username, COUNT(*) AS num_following\r\n" + 
					"	FROM following\r\n" + 
					"	LEFT JOIN users\r\n" + 
					"		ON users.id=following.user_id\r\n" + 
					"	WHERE users.id=" + userId + ";");
			while(rs.next()) {
				numFollowing = rs.getInt("num_following");
			}
			rs.close();
			
			rs = st.executeQuery("SELECT users.username, COUNT(*) AS num_followers\r\n" + 
					"	FROM followers\r\n" + 
					"	LEFT JOIN users\r\n" + 
					"		ON users.id=followers.user_id\r\n" + 
					"	WHERE users.id=" + userId + ";");
			while(rs.next()) {
				numFollowers = rs.getInt("num_followers");
			}
			rs.close();
			
			rs = st.executeQuery("SELECT users.username, COUNT(*) AS num_songs\r\n" + 
					"	FROM songs\r\n" + 
					"	LEFT JOIN users\r\n" + 
					"		ON users.id=songs.user_id\r\n" + 
					"	WHERE users.id=" + userId + ";");
			while(rs.next()) {
				numSongs = rs.getInt("num_songs");
			}
			rs.close();
			
			int followingId = util.getUserId(target, conn);
			rs = st.executeQuery("SELECT *\r\n" + 
					"	FROM following\r\n" + 
					"	WHERE following.user_id=" + userId + "\r\n" + 
					"    AND following.following_id=" + followingId + ";");
			while(rs.next()) {
				isFollowing = true;
			}
			
			
			
			String json = new Gson().toJson(new ProfileDetails(numFollowers, numFollowing, numSongs, imageUrl, isFollowing));
			System.out.println(json);

	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        response.getWriter().write(json);
	        
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if(rs != null) {rs.close();}
				if(st != null) {st.close();}
				if(conn != null) {conn.close();}
			} catch (SQLException sqle) {
				System.out.println("sqle:" + sqle.getMessage());
			}
		}
	}
}
