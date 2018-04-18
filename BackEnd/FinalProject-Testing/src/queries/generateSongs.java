package queries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

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
		
		boolean firstInversion = Boolean.parseBoolean(request.getParameter("firstInversion"));
		boolean secondInversion = Boolean.parseBoolean(request.getParameter("secondInversion"));
		
		String temp = request.getParameter("chordProgression");
		System.out.println(temp);
		//parse temp into arraylist of integeres
		String[] parts = temp.split(" ");
		ArrayList<Integer> chords = new ArrayList<Integer>();
		for(int n = 0; n < parts.length; n++) {
		   chords.add(Integer.parseInt(parts[n]));
		}
		
		System.out.println(username + "\t" + title + "\t" + key + "\t" + tempo + "\t" + soprano + "\t" + alto + "\t" + tenor + "\t" + bass + "\t" + temp);
		ForkJoinPool pool = new ForkJoinPool();
		//ExecutorService executor = Executors.newCachedThreadPool();
		
		long before = System.currentTimeMillis();
		
		for(int i = 0 ; i < 6; i++) {
			String tempFilePath = getServletContext().getRealPath("") + "/" + fileName + i;
			String tempFileName = fileName + i + ".midi";
			String titleSpec = title + i;
			pool.execute(new SongGeneratorThread(key, tempo, soprano, alto, tenor, bass, chords, tempFilePath, firstInversion, secondInversion));
			//executor.execute(new SongGeneratorThread(key, tempo, soprano, alto, tenor, bass, chords, tempFilePath));)
			songs.add(new Song(titleSpec, tempFileName, username, 0));
		}
		pool.shutdown();
		while(!pool.isTerminated()) {
			Thread.yield();
		}
		long after = System.currentTimeMillis();
		
		System.out.println("it took: " + (after-before) + " milliseconds");
		
		String json = new Gson().toJson(songs);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
	}
}


