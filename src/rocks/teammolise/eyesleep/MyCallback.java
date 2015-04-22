package rocks.teammolise.eyesleep;

import java.io.IOException;

import javax.swing.JOptionPane;

import rocks.teammolise.eyesleep.config.ConfigManager;
import rocks.teammolise.eyesleep.ui.TrayCallback;

public class MyCallback implements TrayCallback {
	private Controller controller;
	
	public MyCallback(Controller pController) {
		this.controller = pController;
	}
	
	@Override
	public void onExit() {
		System.exit(0);
	}

	@Override
	public void onWatchingMovie() {
		this.controller.watchMovie();
	}

	@Override
	public void onAbout() {
		JOptionPane.showMessageDialog(null, "From Molise with ❤️", "Eyesleep", JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void onChangeStyle(int pNewStyle) {
		try {
			ConfigManager config = new ConfigManager();
			config.setStyle(pNewStyle);
		} catch (IOException e) {
			System.err.println("Unable to store config file: " + e.getMessage());
		}
		
		this.controller.setTray(pNewStyle);
	}
}