package rocks.teammolise.eyesleep.utils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
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
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.jhlabs.image.GaussianFilter;

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
			Clip clip = (Clip) AudioSystem.getLine(info);
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
		URL imageURL = Utils.class.getResource("resources/" + path);

		return (new ImageIcon(imageURL, description)).getImage();
	}

	public static Image loadImage(String path, String description, int pWidth) {
		try {
			URL imageURL = Utils.class.getResource("resources/" + path);
			BufferedImage trayIconImage = ImageIO.read(imageURL);

			return trayIconImage.getScaledInstance(pWidth, -1,
					Image.SCALE_SMOOTH);
		} catch (IOException e) {
			return null;
		}
	}
	
	public static BufferedImage scale(BufferedImage before, double sx, double sy) {
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(sx, sy);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp.filter(before, after);
	}

	public static BufferedImage getScreenshot() throws AWTException {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize());
		BufferedImage capture = new Robot().createScreenCapture(screenRect);
		return capture;
	}

	public static BufferedImage blurImage(BufferedImage image, int radius) {
		GaussianFilter gf = new GaussianFilter(radius);
		
		return gf.filter(image, null);
	}

	public static void printGaussianBlurFilter(int radius) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius must be >= 1");
		}

		int size = radius * 2 + 1;
		float[] data = new float[size * size];

		float sigma = radius / 3.0f;
		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		int index = 0;
		for (int y = -radius; y <= radius; y++) {
			for (int x = -radius; x <= radius; x++) {
				float distance = x * x + y * y;
				data[index] = (float) Math.exp(-distance / twoSigmaSquare)
						/ sigmaRoot;
				total += data[index];
				System.out.printf("%.3f\t", data[index]);
				index++;
			}
			System.out.println("");
		}
	}

	private static ConvolveOp getGaussianBlurFilter(int radius,
			boolean horizontal) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius must be >= 1");
		}

		int size = radius * 2 + 1;
		float[] data = new float[size];

		float sigma = radius / 3.0f;
		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		for (int i = -radius; i <= radius; i++) {
			float distance = i * i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance / twoSigmaSquare)
					/ sigmaRoot;
			total += data[index];
		}

		for (int i = 0; i < data.length; i++) {
			data[i] /= total;
		}

		Kernel kernel = null;
		if (horizontal) {
			kernel = new Kernel(size, 1, data);
		} else {
			kernel = new Kernel(1, size, data);
		}
		return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	}

	public static JButton createFlatButton(String text) {
		JButton button = new JButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		button.setFont(button.getFont().deriveFont(Font.BOLD));
		Border line = new LineBorder(Color.WHITE, 2);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		button.setBorder(compound);
		return button;
	}
}
