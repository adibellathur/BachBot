package queries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.*;
import musicGen.*;

@WebServlet("/generateSongs")
public class generateSongs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Song> songs = new ArrayList<Song>();
		
		String username = request.getParameter("username");
		String title = request.getParameter("title");
		
		String fileName = username + "_" + title;
		
		String key = request.getParameter("key");
		System.out.println(key);
		String tempo = request.getParameter("tempo");
		String soprano = request.getParameter("instrument1");
		String alto = request.getParameter("instrument2");
		String tenor = request.getParameter("instrument3");
		String bass = request.getParameter("instrument4");
		
		String temp = request.getParameter("chordProgression");
		System.out.println(temp);
		//parse temp into arraylist of integeres
		String[] parts = temp.split(" ");
		ArrayList<Integer> chords = new ArrayList<Integer>();
		for(int n = 0; n < parts.length; n++) {
		   chords.add(Integer.parseInt(parts[n]));
		}
		
		ExecutorService executors = Executors.newCachedThreadPool();
		
		for(int i = 0 ; i < 6; i++) {
			String tempFilePath = getServletContext().getRealPath("") + "/" + fileName + i;
			String tempFileName = fileName + i;
			executors.execute(new SongGeneratorThread(key, tempo, soprano, alto, tenor, bass, chords, tempFilePath));
			songs.add(new Song(title, tempFileName, username, 0));
		}
		executors.shutdown();
		while(!executors.isTerminated()) {
			Thread.yield();
		}
		
		String json = new Gson().toJson(songs);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
	}
}


