package rocks.teammolise.eyesleep.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class ConfigManager {
	private Properties properties;
	public ConfigManager() throws IOException {
		properties = new Properties();
		
		properties.load(new FileInputStream(new File("./config.properties")));
	}
	
	public int getSleepTime() {
		return Integer.parseInt(properties.getProperty("sleeptime"));
	}

	public void setSleepTime(int sleepTime) throws IOException {
		properties.setProperty("sleepTime", String.valueOf(sleepTime));
		properties.store(new FileWriter("config.properties"), "");
	}

	public int getWorkTime() {
		return Integer.parseInt(properties.getProperty("worktime"));
	}

	public void setWorkTime(int workTime) throws IOException {
		properties.setProperty("worktime", String.valueOf(workTime));
		properties.store(new FileWriter("config.properties"), "");
	}

	public int getMovieWorktime() {
		return Integer.parseInt(properties.getProperty("movie.worktime"));
	}

	public void setMovieWorktime(int movieWorktime) throws IOException {
		properties.setProperty("movie.worktime", String.valueOf(movieWorktime));
		properties.store(new FileWriter("config.properties"), "");
	}

	public int getSkipWorktime() {
		return Integer.parseInt(properties.getProperty("skiptime"));
	}

	public void setSkipWorktime(int skipWorktime) throws IOException {
		properties.setProperty("skiptime", String.valueOf(skipWorktime));
		properties.store(new FileWriter("config.properties"), "");
	}

	public int getMaxSkips() {
		return Integer.parseInt(properties.getProperty("maxskips"));
	}

	public void setMaxSkips(int maxSkips) throws IOException {
		properties.setProperty("maxskips", String.valueOf(maxSkips));
		properties.store(new FileWriter("config.properties"), "");
	}

	public int getStyle() {
		return Integer.parseInt(properties.getProperty("style"));
	}

	public void setStyle(int style) throws IOException {
		properties.setProperty("style", String.valueOf(style));
		properties.store(new FileWriter("config.properties"), "");
	}
}
