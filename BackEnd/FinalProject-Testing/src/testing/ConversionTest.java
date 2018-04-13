package testing;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ConversionTest {
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException {
		AudioInputStream format = AudioSystem.getAudioInputStream(new File("C:\\Users\\Russell Wakugawa\\Desktop\\Mii_Channel.mid"));
		System.out.println(format.toString());
		//System.out.println(System.getProperty("catalina.base"));
	}
}
