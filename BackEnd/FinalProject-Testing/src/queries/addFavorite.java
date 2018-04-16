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

import model.util;

@WebServlet("/addFavorite")
public class addFavorite extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement st = null;
		
		String username = request.getParameter("username");
		String title = request.getParameter("title");
		String composer = request.getParameter("composer");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			
			int songId = util.getSongId(title, composer, conn);
			int userId = util.getUserId(username, conn);
			
			boolean favoriteExists = util.alreadyFavorite(userId, songId, conn);
			
			if(!favoriteExists) {
				st = conn.createStatement();
				st.executeUpdate("INSERT INTO favorites(user_id, song_id)\r\n" + 
						"	VALUES(" + userId + ", " + songId + ");");
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
