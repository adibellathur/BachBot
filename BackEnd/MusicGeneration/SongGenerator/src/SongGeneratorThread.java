import java.util.ArrayList;

public class SongGeneratorThread extends Thread{
	private String key;
	private String tempo;
	private String instrumentSoprano;
	private String instrumentAlto;
	private String instrumentTenor;
	private String instrumentBass;
	private ArrayList<Integer> givenChordProg;
	private String filename;
	
	public SongGeneratorThread(String key, String tempo, String instrumentSoprano, String instrumentAlto,
							  String instrumentTenor, String instrumentBass,
							  ArrayList<Integer> givenChordProg, String filename) {
		this.key = key;
		this.tempo = tempo;
		this.instrumentSoprano = instrumentSoprano;
		this.instrumentAlto = instrumentAlto;
		this.instrumentTenor = instrumentTenor;
		this.instrumentBass = instrumentBass;
		this.givenChordProg = givenChordProg;
		this.filename = filename;
		this.run();
	}
	
	public void run() {
		SongGenerator sg = new SongGenerator(key, tempo,
											instrumentSoprano, instrumentAlto, instrumentTenor, instrumentBass,   
											givenChordProg, filename);
	}
}