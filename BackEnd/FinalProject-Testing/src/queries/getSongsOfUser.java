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

import model.ProfileDetails;
import model.Song;

@WebServlet("/getSongsOfUser")
public class getSongsOfUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<Song> songs = new ArrayList<Song>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT songs.title, users.username, songs.path, COUNT(favorites.song_id) AS favorites\r\n" + 
					"	FROM songs\r\n" + 
					"	JOIN users\r\n" + 
					"		ON users.id=songs.user_id\r\n" + 
					"	LEFT JOIN favorites\r\n" + 
					"		ON favorites.song_id=songs.id\r\n" + 
					"	WHERE users.username LIKE '" + request.getParameter("username") + "'\r\n" + 
					"	GROUP BY songs.title\r\n" + 
					"   ORDER BY favorites DESC;");
			while(rs.next()) {
				String title = rs.getString("title");
				String path = rs.getString("path");
				String username = rs.getString("username");
				int favorites = rs.getInt("favorites");
				songs.add(new Song(title, path, username, favorites));
			}
			
			String json = new Gson().toJson(songs);

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
