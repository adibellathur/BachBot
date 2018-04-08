import java.io.FileWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;

public class testMain {
	public static void main(String[] args) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<Song> songs = new ArrayList<Song>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://303.itpwebdev.com/wakugawa_CSCI201_FinalProject", "wakugawa_CSCI201", "wakugawa_CSCI201");
			st = conn.createStatement();
			rs = st.executeQuery("SELECT songs.title, songs.path, users.username,COUNT(*) AS favorites "
					+ "FROM songs "
					+ "JOIN users "
						+ "ON users.id=songs.user_id "
					+ "JOIN favorites "
						+ "ON favorites.song_id=songs.id "
					+ "GROUP BY songs.id;");
			while(rs.next()) {
				String title = rs.getString("title");
				String path = rs.getString("path");
				int favorites = rs.getInt("favorites");
				System.out.println(title + "\t" + path + "\t" + favorites);
				songs.add(new Song(title, path, favorites));
			}
	        
	        String json = new Gson().toJson(songs);

	        System.out.println(json);
			
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
