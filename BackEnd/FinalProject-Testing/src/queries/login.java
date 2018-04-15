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

@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		String password = null;
		String username = null;
		String imageUrl = null;
		
		boolean success = false;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			st = conn.createStatement();
			String inputUsername = request.getParameter("username");
			rs = st.executeQuery("SELECT *\r\n" + 
					"	FROM users\r\n" + 
					"	WHERE users.username LIKE '" + inputUsername + "'");
			while(rs.next()) {
				password = rs.getString("password");
				if(password != null && password.equals(request.getParameter("password"))) {
					success = true;
					username = rs.getString("username");
					imageUrl = rs.getString("image_url");
				}
			}
			
			UserBool returnUser = new UserBool(success, new User(username, imageUrl));
			
	        response.setContentType("application/json");
	        response.setCharacterEncoding("UTF-8");
	        
	        String json = new Gson().toJson(returnUser);

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
