package queries;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/generateSongs")
public class generateSongs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String title = request.getParameter("title");
		
		String fileName = username + "_" + title;
		
		for(int i = 0 ; i < 6; i++) {
			
		}
		
		
	}
	
	public void addSong() {
		
	}
}


