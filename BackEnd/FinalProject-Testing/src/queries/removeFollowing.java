package queries;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.util;

@WebServlet("/removeFollowing")
public class removeFollowing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement st = null;
		
		int userId = Integer.parseInt(request.getParameter("userId"));
		String target = request.getParameter("target");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			st = conn.createStatement();
			
			int targetId = util.getUserId(target, conn);
			
			st.executeUpdate("DELETE FROM following\r\n" + 
					"	WHERE following.user_id=" + userId + "\r\n" + 
					"	AND following.following_id=" + targetId + ";");
			
			st.executeUpdate("DELETE FROM followers\r\n" + 
					"	WHERE followers.user_id=" + targetId + "\r\n" + 
					"	AND followers.follower_id=" + userId + ";");
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			try {
				if(st != null) {st.close();}
				if(conn != null) {conn.close();}
			} catch (SQLException sqle) {
				System.out.println("sqle:" + sqle.getMessage());
			}
		}
	}
}
