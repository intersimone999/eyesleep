package rocks.teammolise.eyesleep.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public class Utils {
	public static int timeInSeconds() {
		return (int) (Calendar.getInstance().getTimeInMillis() / 1000);
	}

	public static int timeInMinutes() {
		return timeInSeconds() / 60;
	}

	public static void playClip(String pName) {
		try {
			URL stream = Utils.class.getResource("resources/" + pName);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(stream);
			// Open audio clip and load samples from the audio input stream.
			AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip)AudioSystem.getLine(info);
            clip.open(audioIn);
            clip.start();

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public static Image loadImage(String path, String description) {
        URL imageURL = Utils.class.getResource("resources/"+path);
         
        return (new ImageIcon(imageURL, description)).getImage();
    }
	
	public static Image loadImage(String path, String description, int pWidth) {
		try {
	        URL imageURL = Utils.class.getResource("resources/"+path);
	        BufferedImage trayIconImage = ImageIO.read(imageURL);
	        
	        return trayIconImage.getScaledInstance(pWidth, -1, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			return null;
		}
    }
}
