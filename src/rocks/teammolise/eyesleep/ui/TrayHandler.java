package rocks.teammolise.eyesleep.ui;

import java.awt.AWTException;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rocks.teammolise.eyesleep.utils.Utils;

public class TrayHandler {
	private PopupMenu popup;
	private TrayIcon trayIcon;
	private SystemTray tray;
	
	public TrayHandler(final TrayCallback pCallback) {
		if (!isSupported())
            return;
		
        popup = new PopupMenu();
        trayIcon = new TrayIcon(Utils.loadImage("tray.png", "test"));
        trayIcon.setImageAutoSize(true);
        
        trayIcon = new TrayIcon(Utils.loadImage("tray.png", "test", (int)trayIcon.getSize().getWidth()));
        tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem labelItem = new MenuItem();
        labelItem.setEnabled(false);
        Menu actions = new Menu("Actions");
        MenuItem actionWatchMovie = new MenuItem("Watching a movie");
        MenuItem actionExit = new MenuItem("Exit");
        MenuItem aboutItem = new MenuItem("About");
       
        //Add components to pop-up menu
        popup.add(labelItem);
        popup.add(actions);
        actions.add(actionWatchMovie);
        actions.add(actionExit);
        popup.addSeparator();
        popup.add(aboutItem);
        
        actionExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pCallback.onExit();
			}
		});
        
        actionWatchMovie.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pCallback.onWatchingMovie();
			}
		});
        
        aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pCallback.onAbout();
			}
		});
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
        }
	}
	
	public static boolean isSupported() {
		return SystemTray.isSupported();
	}
	
	public void update(int pMinutes) {
		this.popup.getItem(0).setLabel(pMinutes + " minutes left");
	}
}
