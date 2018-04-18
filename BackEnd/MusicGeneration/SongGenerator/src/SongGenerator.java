import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Intervals;
import org.jfugue.theory.Note;

public class SongGenerator {
	//USER INPUT
	
	private String instrumentSoprano;
	private String instrumentAlto;
	private String instrumentTenor;
	private String instrumentBass;
	private ArrayList<Integer> givenChordProg; //JUST 4 CHORDS
	
	/* Not needed at global scope, just used in constructor
	private String key;
	private String tempo;
	private String filename;
	private boolean allowFirstInversion;
	private boolean allowSecondInversion;
	*/
	
	//OTHER
	private ArrayList<String> comboCollection = new ArrayList<String>();
	private ArrayList<ArrayList<Integer> > comboCollectionNums = new ArrayList<ArrayList<Integer> >();
	private HashMap<String, Integer> noteToQuantity = new HashMap<String, Integer>();
	
	public SongGenerator(String key, String tempo, String instrumentSoprano, String instrumentAlto,
						String instrumentTenor, String instrumentBass,
						ArrayList<Integer> givenChordProg, String filename,
						boolean allowFirstInversion, boolean allowSecondInversion) {
		this.instrumentSoprano = instrumentSoprano;
		this.instrumentAlto = instrumentAlto;
		this.instrumentTenor = instrumentTenor;
		this.instrumentBass = instrumentBass;
		this.givenChordProg = new ArrayList<Integer>(givenChordProg);
		ChordProgression scale = new ChordProgression("I II III IV V VI VII");
		scale.setKey(key);
		Chord[] scaleChords = scale.getChords();
		for (int i = 1; i <= scaleChords.length; i++) {
			noteToQuantity.put(scaleChords[i-1].getRoot().toString(), i);
		}
		
		ArrayList<Integer> chordNums = generateChordProgression();
		String cpstring = null;
		if (key.contains("i")) { //minor
			cpstring = toCPString(chordNums, false);
		} else {
			cpstring = toCPString(chordNums, true);
		}
		
		int[] beat1 = {0,4,8,12,15,19,23,27};
		
		Player player = new Player(); 
		Pattern p = new Pattern();
		ChordProgression cp = new ChordProgression(cpstring);
		cp.setKey(key);
		Chord[] chords = cp.getChords();
		String lastIntervals = null;
		for (int i = 0; i < chords.length; i++) {
			String next = findBestIntervals(lastIntervals, chords[i]);
			Chord chord = new Chord(new Note(chords[i].getRoot()), new Intervals(next));
			if (key.contains("Ab") || key.contains("A") || key.contains("Bb") || key.contains("B")) { //transpose down to sound better
				chord.setBassNote(chord.getRoot().changeValue(-24));
			} else {
				chord.setBassNote(chord.getRoot().changeValue(-12));
			}
			double random = Math.random();
			if (allowFirstInversion && random < .3) {
				chord.setInversion(1);
			} else if (allowSecondInversion && random < .45 && random >= .3){
				chord.setInversion(2);
			}
			lastIntervals = next;
			boolean useStrChord = false; //for strange bug purposes
			String strChord = chord.toNoteString();
			if (i == 14 || i == 29) { //convert to half note
				strChord += "h";
				useStrChord = true;
			} 
			for (int j = 0; j < beat1.length; j++) {
				if (i == beat1[j]) { // add note velocity
					strChord += "a75d100";
					useStrChord = true;
					break;
				}
			}
			if (useStrChord) p.add(strChord);
			else p.add(chord);
		}
		ArrayList<Pattern> voices = set_instruments(p, beat1);

		Pattern finalSong = voices.get(0).add(voices.get(1)).add(voices.get(2)).add(voices.get(3));
		finalSong.setTempo(Integer.parseInt(tempo));
		player.play(finalSong); 
		
		try {
			MidiFileManager.savePatternToMidi(p, new File(filename + ".midi"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	private ArrayList<Pattern> set_instruments(Pattern p, int[] beat1) { //called by each voice, after each note is added
		ArrayList<Pattern> voices = new ArrayList<Pattern>();
		voices.add(new Pattern().setVoice(0).setInstrument(instrumentSoprano));
		voices.add(new Pattern().setVoice(1).setInstrument(instrumentAlto));
		voices.add(new Pattern().setVoice(2).setInstrument(instrumentTenor));
		voices.add(new Pattern().setVoice(3).setInstrument(instrumentBass));
		String song = p.toString();
		String[] tokens = song.split(" ");
		String soprano = "";
		String alto = "";
		String tenor = "";
		String bass = "";
		for (int i = 0; i < tokens.length; i++) { //bass up to soprano
			boolean addedBeatOne = false;
			boolean adjustForHalfnote = (i == 14 || i == 29);
			for (int j = 0; j < beat1.length; j++) { //check if beat 1, special case
				if (i == beat1[j]) {
					tokens[i] = tokens[i].substring(1);
					String[] notes = tokens[i].split("\\+");
					int indexOfCloseParen = notes[3].indexOf(")");
					String noteInfo = notes[3].substring(indexOfCloseParen + 1);
					notes[3] = notes[3].substring(0, indexOfCloseParen); //now notes array is ready
					bass += notes[0] + noteInfo + " ";
					tenor += notes[1] + noteInfo + " ";
					alto += notes[2] + noteInfo + " ";
					soprano += notes[3] + noteInfo + " ";
					addedBeatOne = true;
					break;
				}
			} //else, not beat 1
			if (addedBeatOne == false) {
				String[] notes = tokens[i].split("\\+");
				bass += notes[0] + " ";
				tenor += notes[1] + " ";
				alto += notes[2] + " ";
				soprano += notes[3] + " ";
			}
			if (adjustForHalfnote) {
				soprano = soprano.replace("(","");
				soprano = soprano.replace(")","");
				soprano = soprano.replace("h","");
				alto = alto.replace("(","");
				alto = alto.replace(")","");
				alto = alto.replace("h","");
				tenor = tenor.replace("(","");
				tenor = tenor.replace(")","");
				tenor = tenor.replace("h","");
				bass = bass.replace("(","");
				bass = bass.replace(")","");
				bass = bass.replace("h","");
				voices.get(0).add("(" + soprano + ")h");
				voices.get(1).add("(" + alto + ")h");
				voices.get(2).add("(" + tenor + ")h");
				voices.get(3).add("(" + bass + ")h");
			} else {
				voices.get(0).add(soprano);
				voices.get(1).add(alto);
				voices.get(2).add(tenor);
				voices.get(3).add(bass);
			}
			soprano = "";
			alto = "";
			tenor = "";
			bass = "";
		}
		return voices;
	}
	
	private ArrayList<Integer> generateChordProgression() {
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
	
	private String toCPString(ArrayList<Integer> chordNums, boolean isMajor) {
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
	
	private String findBestIntervals(String lastIntervals, Chord c) { //c is current chord to be generated
		 ArrayList<Integer> validNums = new ArrayList<Integer>();
		 for (int x = 3; x <= 12; x++) {
			 if (x%7  == 1 || x%7  == 3 || x%7  == 5) {
				 validNums.add(x);
			 }
		 }
		 genCombosAndOrder(validNums, 3); //THIS GIVES THE LAST 3 NUMS OF INTERVAL
		 String interval = "";
		 if (c.isMajor()) { //determine the best of the possible intervals
			 if (lastIntervals == null) {
				 if (Math.random() < .5) {
					 interval = "1 5 8 10";
				 } else {
					 interval = "1 3 8 12";
				 }
			 }
			 else {
				 int randIndex = (int) (Math.random()*(comboCollection.size()-1));
				 interval = "1 " + comboCollection.get(randIndex);				 
			 }
		 } else if (c.isMinor()) {
			 if (lastIntervals == null) {
				 if (Math.random() < .5) {
					 interval = "1 5 8 b10";
				 } else {
					 interval = "1 b3 8 12";
				 }
			 } else {
				 int randIndex = (int) (Math.random()*(comboCollection.size()-1));
				 interval = "1 " + comboCollection.get(randIndex);
				 interval = interval.replaceAll("3", "b3");
				 interval = interval.replaceAll("10", "b10");
			 }

		 } else { //for now, must be diminished
			 if (lastIntervals == null) {
				 if (Math.random() < .5) {
					 interval = "1 b5 8 b10";
				 } else {
					 interval = "1 b3 8 b12";
				 }
			 } else {
				 int randIndex = (int) (Math.random()*(comboCollection.size()-1));
				 interval = " 1 " + comboCollection.get(randIndex);
				 interval = interval.replaceAll(" 5", " b5");
				 interval = interval.replaceAll(" 12", " b12");
				 interval = interval.replaceAll(" 3", " b3");
				 interval = interval.replaceAll(" 10", " b10");
				 interval = interval.trim();
			 }
		 }
		 return interval.trim();
	 }
	
	private void genCombosAndOrder(ArrayList<Integer> set, int r) {
		 helper(set, new ArrayList<Integer>(), r, 0); //fills combos
	 }
	private void helper(ArrayList<Integer> set, ArrayList<Integer> currCombo, int r, int start) {
		 if (currCombo.size() == r) { //BASE CASE: valid combo, add to list
			 String x = "";
			 boolean one = false;
			 boolean three = false;
			 boolean five = false;
			 for (int i = 0; i < r; i++) { //chord must contain at least one of each from {1,3,5}
				 int element = currCombo.get(i);
				 if (element == 8 || element == 15) one = true;
				 else if (element == 3 || element == 10) three = true;
				 else if (element == 5 || element == 12) five = true;
				 x += element + " ";
			 }
			 if (one && three && five) {
				 comboCollection.add(x.trim());
				 comboCollectionNums.add(currCombo);
			 } //else don't add it
		 } else {
			 for (int i = start; i < set.size(); i++) {
				 currCombo.add(set.get(i));
				 helper(set, currCombo, r, ++start);
				 currCombo.remove(currCombo.size()-1);
			 }
		 }
	 }
	
	public static void main(String[] args) {
		ArrayList<Integer> givencp = new ArrayList<Integer>();
		givencp.add(4);
		givencp.add(2);
		givencp.add(5);
		givencp.add(1);
		
		@SuppressWarnings("unused")
		SongGenerator sg = new SongGenerator("E", "110", //key & tempo
											"PIANO","PIANO","PIANO","PIANO",   //instruments
											givencp, "mySong", //given cp & filename
											true, true); //first and second inversions
		
	}
}