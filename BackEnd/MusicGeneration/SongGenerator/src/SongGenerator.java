import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Note;

public class SongGenerator {
	//USER INPUT
	private String key;
	private String tempo;
	private String instrumentSoprano;
	private String instrumentAlto;
	private String instrumentTenor;
	private String instrumentBass;
	private ArrayList<String> validChords;
	private ArrayList<Integer> givenChordProg;
	private String melody; //null if none given
	private String filename;
	
	//OTHER
	private ArrayList<ArrayList<Note> > existingNotes;
	
	public SongGenerator()  { //default configurations
		ArrayList<Integer> test = generateChordProgression();
		for (int j = 0; j < test.size(); j++) System.out.print(test.get(j) + " ");
		System.out.println();
	}
	
	public SongGenerator(String key, String tempo, String instrumentSoprano, String instrumentAlto,
						String instrumentTenor, String instrumentBass, ArrayList<String> validChords,
						ArrayList<Integer> givenChordProg, String melody, String filename) {
		this.key = key;
		this.tempo = tempo;
		this.instrumentSoprano = instrumentSoprano;
		this.instrumentAlto = instrumentAlto;
		this.instrumentTenor = instrumentTenor;
		this.instrumentBass = instrumentBass;
		this.validChords = new ArrayList<String>(validChords);
		this.givenChordProg = new ArrayList<Integer>(givenChordProg);
		this.melody = melody;
		this.filename = filename;
		this.existingNotes = new ArrayList<ArrayList<Note> >();
		
		ArrayList<Integer> chordNums = generateChordProgression();
		String cpstring = null;
		if (key.contains("i")) { //minor
			cpstring = toCPString(chordNums, false);
		} else {
			cpstring = toCPString(chordNums, true);
		}
		//PRINT
		for (int j = 0; j < chordNums.size(); j++) System.out.print(chordNums.get(j) + " ");
		System.out.println();
		
		ChordProgression cp = new ChordProgression(cpstring);
		cp.setKey(key);
		Player player = new Player();
		player.play(cp);
		
		
		/*String bass = generateBass();
		String soprano = generateSoprano();
		String alto = generateAlto();
		String tenor = generateTenor();
		
		
		String song = soprano + alto + tenor + bass;
		
		Pattern pattern = new Pattern(song);
		try {
			MidiFileManager.savePatternToMidi(pattern, new File(filename));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}*/
	}
	
	private String generateSoprano() {
		if (this.melody == null) { //randomly generate
			
		} else { //parse melody for errors, if none return it
			
		}
		return "";
	}
	
	//THIS WILL BE REPLACED WITH NEW METHOD
	private String generateAlto() {
		return "";
	}
	
	private String generateTenor() {
		return "";
	}
	
	private String generateBass() {
		return "";
	}
	
	private String addNonharmonic() { //called by each voice, after each note is added
		return "";
	}
	
	private ArrayList<Integer> generateChordProgression() { //TODO: generate entire chord progression
		ArrayList<Integer> songChords = new ArrayList<Integer>(); //contains numbers representing chords
		if (givenChordProg.size() == 0) { //randomly generate
			
		} else { //use given chordProgs
			//MEASURE 1
			if (givenChordProg.get(0) == 1) { //use given CP if available
				songChords.addAll(givenChordProg);
			} else { //otherwise give a premade one
				songChords.add(1);
				double random = Math.random();
				if (random < .25) {
					songChords.add(4);
					songChords.add(5);
					songChords.add(1);
				} else if (random < .50) {
					songChords.add(6);
					songChords.add(4);
					songChords.add(5);
				} else if (random < .75) {
					songChords.add(3);
					songChords.add(6);
					songChords.add(2);
				} else {
					songChords.add(2);
					songChords.add(7);
					songChords.add(1);
				}
			}

			//MEASURE 2
			songChords.addAll(randomChordProgression(songChords.get(songChords.size()-1)));
			
			//MEASURE 3
			songChords.addAll(randomChordProgression(songChords.get(songChords.size()-1)));

			//MEASURE 4
			if (songChords.get(songChords.size()-1) == 3) { //finish circle progression
				double random = Math.random();
				if (random < .5) {
					songChords.add(6);
					songChords.add(2);
					songChords.add(5);
				} else {
					songChords.add(6);
					songChords.add(4);
					songChords.add(5);
				}
			}
			else {
				double random = Math.random();
				if (random < .5) {
					songChords.add(1);
					songChords.add(2);
					songChords.add(5);
				} else {
					songChords.add(1);
					songChords.add(4);
					songChords.add(5);
				}
			}
			
			//MEASURE 5
			if (Math.random() < .8) {
				songChords.addAll(givenChordProg);
			} else {
				songChords.addAll(randomChordProgression(songChords.get(songChords.size()-1)));
			}
			
			//MEASURE 6
			songChords.addAll(randomChordProgression(songChords.get(songChords.size()-1)));
			
			//MEASURE 7
			songChords.addAll(randomChordProgression(songChords.get(songChords.size()-1)));
			
			//MEASURE 8
			double random8 = Math.random();
			if (random8 < .375) {
				songChords.add(2);
				songChords.add(5);
				songChords.add(1);
			} else if (random8 < .75) {
				songChords.add(4);
				songChords.add(5);
				songChords.add(1);
			} else if (random8 < .875) {
				songChords.add(2);
				songChords.add(7);
				songChords.add(1);
			} else {
				songChords.add(4);
				songChords.add(7);
				songChords.add(1);
			}
		}
		return songChords;
	}
	
	private ArrayList<Integer> randomChordProgression(int start) { //4 random chords based on given previous
		ArrayList<Integer> randProg = new ArrayList<Integer>();
		boolean forward;
		if (Math.random() < .5) forward = true;
		else forward = false;
		for (int i = 0; i < 4; i++) {
			if (forward) {
				if (Math.random() < .2) { //revert
					start -= 4;
					if (start < 1) start += 7;
					randProg.add(start);
				} else {
					start += 4;
					if (start > 7) start -= 7;
					randProg.add(start);
				}
			} else {
				if (Math.random() < .2) { //revert
					start += 4;
					if (start > 7) start -= 7;
					randProg.add(start);
				} else {
					start -= 4;
					if (start < 1) start += 7;
					randProg.add(start);
				}
			}
		}
		return randProg;
	}
	
	private String toCPString(ArrayList<Integer> chordNums, boolean isMajor) { //TODO: 7th chords
		String stacatto = "";
		for (int i = 0; i < chordNums.size(); i++) {
			if (isMajor) {
				if (chordNums.get(i) == 1) {
					stacatto += "I ";
				} else if (chordNums.get(i) == 2) {
					stacatto += "ii ";
				} else if (chordNums.get(i) == 3) {
					stacatto += "iii ";
				} else if (chordNums.get(i) == 4) {
					stacatto += "IV ";
				} else if (chordNums.get(i) == 5) {
					stacatto += "V ";
				} else if (chordNums.get(i) == 6) {
					stacatto += "vi ";
				} else {
					stacatto += "viio ";
				}
			} else {
				if (chordNums.get(i) == 1) {
					stacatto += "i ";
				} else if (chordNums.get(i) == 2) {
					stacatto += "iio ";
				} else if (chordNums.get(i) == 3) {
					stacatto += "III ";
				} else if (chordNums.get(i) == 4) {
					stacatto += "iv ";
				} else if (chordNums.get(i) == 5) {
					stacatto += "V ";
				} else if (chordNums.get(i) == 6) {
					stacatto += "VI ";
				} else {
					stacatto += "VII ";
				}
			}
			
		}
		return stacatto.trim();
	}
	
	
	
	public static void main(String[] args) {
		ArrayList<Integer> givencp = new ArrayList<Integer>();
		givencp.add(1);
		givencp.add(4);
		givencp.add(5);
		givencp.add(1);
		
		ArrayList<String> vchords = new ArrayList<String>();
		
		SongGenerator sg = new SongGenerator("Bmin", "100",
											"","","","", vchords,
											givencp, null, null);
		
	}
}
/*
 * General procedure: have 4 methods, generateSoprano(), generateAlto(), etc. that each return a String which is
 * the Stacatto representation of that voice. In the constructor, call all 4 of these methods and append them.
 * Then send this back to the front-end.
 * The voice methods may need to take in parameters as constraints, however most data is global.
 * 
 * For post-configurations, write methods that handle each task, which take in a Stacatto String and return
 * the resulting Stacatto String
 */
