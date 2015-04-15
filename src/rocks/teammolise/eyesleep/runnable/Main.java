package rocks.teammolise.eyesleep.runnable;

import javax.swing.JOptionPane;

import rocks.teammolise.eyesleep.ui.CoveringPanel;
import rocks.teammolise.eyesleep.ui.TrayCallback;
import rocks.teammolise.eyesleep.ui.TrayHandler;
import rocks.teammolise.eyesleep.utils.Utils;

public class Main {
	public static final int SLEEP_TIME = 20; //Sleep time in seconds
	public static final int WORK_TIME = 20; //Work time in minutes
	public static final int MOVIE_WORKTIME = 200;
	public static final int SKIP_WORKTIME = 1; //Work time in minutes until next sleep (when "skip" is pressed)
	public static final int MAX_SKIPS = 3; //Total number of available skips
	
	protected static int availableSkips = MAX_SKIPS;
	protected static int targetMinute;
	protected static TrayHandler tray;
	public static void main(String[] args) {
		if (TrayHandler.isSupported()) {
			tray = new TrayHandler(new MyCallback());
			tray.update(WORK_TIME);
		}
		
		targetMinute = Utils.timeInMinutes() + WORK_TIME;

		try {
			while (true) {
				if (TrayHandler.isSupported())
					tray.update(targetMinute - Utils.timeInMinutes());
				
				if (targetMinute - Utils.timeInMinutes() <= 0)
					targetMinute = Utils.timeInMinutes() + sleep();
				
				Thread.sleep(20000);
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted!");
		}
	}
	
	public static int sleep() throws InterruptedException {
		CoveringPanel panel = new CoveringPanel(SLEEP_TIME, availableSkips);
		panel.setVisible(true);
		panel.startSleeping();
		while (!panel.isDone()) {
			panel.update();
			
			Thread.sleep(100);
		}
		
		boolean skipped = panel.hasSkipped();
		
		panel.setVisible(false);
		panel.dispose();
		
		if (skipped) {
			availableSkips--;
			return getSkipNewWorktime();
		} else {
			Utils.playClip("ding.wav");
			availableSkips = MAX_SKIPS;
			return WORK_TIME;
		}
	}
	
	public static int getSkipNewWorktime() {
		String[] answers = new String[] {
				"Working hard",
				"Watching a movie",
				"Want to exit"
		};
		
		int answer = JOptionPane.showOptionDialog(null, 
				"Why are you skipping?", 
				"Skipped", 
				JOptionPane.OK_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null,
				answers,
				answers[0]);
		
		if (answer == 2)
			System.exit(1);
		
		if (answer == 0)
			return SKIP_WORKTIME;
		else
			return MOVIE_WORKTIME;
	}
	
	public static void watchMovie() {
		if (targetMinute < 20)
			targetMinute = Utils.timeInMinutes() + MOVIE_WORKTIME;
		
		if (TrayHandler.isSupported())
			tray.update(MOVIE_WORKTIME);
	}
}

class MyCallback implements TrayCallback {
	@Override
	public void onExit() {
		System.exit(0);
	}

	@Override
	public void onWatchingMovie() {
		Main.watchMovie();
	}

	@Override
	public void onAbout() {
		JOptionPane.showMessageDialog(null, "From Molise with ❤️", "Eyesleep", JOptionPane.INFORMATION_MESSAGE);
	}
	
}
