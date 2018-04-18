package musicGen;

import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

public class SongGeneratorThread extends RecursiveAction {
	private String key;
	private String tempo;
	private String instrumentSoprano;
	private String instrumentAlto;
	private String instrumentTenor;
	private String instrumentBass;
	private ArrayList<Integer> givenChordProg;
	private String filename;
	private boolean firstInversionAllowed;
	private boolean secondInversionAllowed;
	
	public SongGeneratorThread(String key, String tempo, String instrumentSoprano, String instrumentAlto,
							  String instrumentTenor, String instrumentBass,
							  ArrayList<Integer> givenChordProg, String filename, 
							  boolean firstInversionAllowed, boolean secondInversionAllowed) {
		this.key = key;
		this.tempo = tempo;
		this.instrumentSoprano = instrumentSoprano;
		this.instrumentAlto = instrumentAlto;
		this.instrumentTenor = instrumentTenor;
		this.instrumentBass = instrumentBass;
		this.givenChordProg = givenChordProg;
		this.filename = filename;
		this.firstInversionAllowed = firstInversionAllowed;
		this.secondInversionAllowed = secondInversionAllowed;
	}
	
	public void compute() {
		SongGenerator sg = new SongGenerator(key, tempo,
											instrumentSoprano, instrumentAlto, instrumentTenor, instrumentBass,   
											givenChordProg, filename,
											firstInversionAllowed, secondInversionAllowed);
		System.out.println("constructor finished");
	}
}