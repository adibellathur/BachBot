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

import com.google.gson.Gson;

import model.*;

@WebServlet("/addFollowing")
public class addFollowing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = Integer.parseInt(request.getParameter("userId"));
		String following = request.getParameter("following");
		
		Connection conn = null;
		Statement st = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			
			int followingId = util.getUserId(following, conn);
			
			boolean alreadyExists = util.alreadyFollowing(userId, followingId, conn);
			
			if(!alreadyExists) {
				st = conn.createStatement();
				st.executeUpdate("INSERT INTO following (user_id, following_id) \r\n" + 
						"	VALUES(" + userId + ", " + followingId + ");");
				st.executeUpdate("INSERT INTO followers (user_id, follower_id) \r\n" + 
						"	VALUES(" + followingId + ", " + userId + ");");
				response.getWriter().write(new Gson().toJson(true));
			} else {
				response.getWriter().write(new Gson().toJson(false));
			}
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
