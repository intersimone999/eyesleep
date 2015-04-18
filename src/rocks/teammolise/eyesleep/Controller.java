package rocks.teammolise.eyesleep;

import rocks.teammolise.eyesleep.ui.CoveringPanel;
import rocks.teammolise.eyesleep.ui.TrayHandler;
import rocks.teammolise.eyesleep.utils.Utils;

public class Controller {
	public int sleepTime; //Sleep time in seconds
	public int workTime; //Work time in minutes
	public int movieWorktime;
	public int skipWorktime; //Work time in minutes until next sleep (when "skip" is pressed)
	public int maxSkips; //Total number of available skips
	
	private int availableSkips;
	private int targetMinute;
	private TrayHandler tray;
	
	public Controller( int pSleepTime, int pWorkTime, int pMovieWorktime, int pSkipWorktime, int pMaxSkips) {
		this.sleepTime = pSleepTime;
		this.workTime = pWorkTime;
		this.movieWorktime = pMovieWorktime;
		this.skipWorktime = pSkipWorktime;
		this.maxSkips = pMaxSkips;
		
		this.availableSkips = pMaxSkips;
	}
	
	public void initialize(int pStyle) {
		this.setTray(pStyle);
		this.targetMinute = Utils.timeInMinutes() + this.workTime;
	}
	
	public boolean needRest() {
		int minutesLeft = getMinutesLeft();
		
		if (TrayHandler.isSupported())
			tray.update(minutesLeft > 0 ? minutesLeft : 0);
		
		if (minutesLeft <= 0)
			return true;
		
		return false;
	}
	
	public void sleep() throws InterruptedException {
		CoveringPanel panel = new CoveringPanel(this.sleepTime, availableSkips);
		
		Utils.playClip("alarm.wav");
		Thread.sleep(5000);
		
		panel.setVisible(true);
		panel.startSleeping();
		while (!panel.isDone()) {
			panel.update();
			Thread.sleep(100);
		}
		panel.setVisible(false);
		panel.dispose();
		
		if (panel.hasSkipped()) {
			this.availableSkips--;
			if (panel.isWatchingAMovie())
				this.targetMinute = Utils.timeInMinutes() + this.movieWorktime;
			else
				this.targetMinute = Utils.timeInMinutes() + this.skipWorktime;
		} else {
			Utils.playClip("ding.wav");
			this.availableSkips = this.maxSkips;
			this.targetMinute = Utils.timeInMinutes() + this.workTime;
		}
		
		this.tray.update(getMinutesLeft());
	}
	
	public void setTray(int pStyle) {
		if (TrayHandler.isSupported()) {
			if (this.tray != null)
				this.tray.dispose();
			this.tray = new TrayHandler(pStyle, new MyCallback(this));
			this.tray.update(getMinutesLeft());
		}	
	}
	
	public void watchMovie() {
		if (getMinutesLeft() <= 20) {
			targetMinute = Utils.timeInMinutes() + this.movieWorktime;
		
			if (TrayHandler.isSupported())
				tray.update(getMinutesLeft());
		}
	}
	
	private int getMinutesLeft() {
		return targetMinute - Utils.timeInMinutes();
	}
}
