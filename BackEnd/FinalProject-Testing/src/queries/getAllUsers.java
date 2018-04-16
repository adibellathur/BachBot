package queries;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.*;

@WebServlet("/getAllUsers")
public class getAllUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<User> users = new ArrayList<User>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT users.*, COUNT(followers.user_id) AS num_followers\r\n" + 
					"	FROM users\r\n" + 
					"	LEFT JOIN followers\r\n" + 
					"		ON followers.user_id=users.id\r\n" + 
					"	GROUP BY users.id\r\n" + 
					"	ORDER BY num_followers DESC;");
			while(rs.next()) {
				String username = rs.getString("username");
				String imageUrl = rs.getString("image_url");
				int numFollowers = rs.getInt("num_followers");
				users.add(new User(username, imageUrl, numFollowers));
			}
			
			String json = new Gson().toJson(users);

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