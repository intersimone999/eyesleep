package rocks.teammolise.eyesleep.ui;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rocks.teammolise.eyesleep.utils.Utils;

public class TrayHandler {
	public static final int STYLE_LIGHT = 0;
	public static final int STYLE_DARK = 1;
	public static final int STYLE_CLASSIC = 2;
	
	private PopupMenu popup;
	private TrayIcon trayIcon;
	private SystemTray tray;
	
	public TrayHandler(int pStyle, final TrayCallback pCallback) {
		if (!isSupported())
            return;
		
		String imagePath;
		switch (pStyle) {
		case STYLE_LIGHT:
			imagePath = "trayTransparentLight.png";
			break;
		case STYLE_DARK:
			imagePath = "trayTransparentDark.png";
			break;
		case STYLE_CLASSIC:
			imagePath = "tray.png";
			break;
		default:
			imagePath = "tray.png";
		}
		
        popup = new PopupMenu();
        trayIcon = new TrayIcon(Utils.loadImage(imagePath, "test"));
        trayIcon.setImageAutoSize(true);
        
        trayIcon = new TrayIcon(Utils.loadImage(imagePath, "test", (int)trayIcon.getSize().getWidth()));
        tray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        MenuItem labelItem = new MenuItem();
        labelItem.setEnabled(false);
        Menu actions = new Menu("Actions");
        Menu style = new Menu("Theme");
        MenuItem actionWatchMovie = new MenuItem("Watching a movie");
        MenuItem actionExit = new MenuItem("Exit");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem styleLight = new MenuItem("Light");
        MenuItem styleDark = new MenuItem("Dark");
        MenuItem styleClassic = new MenuItem("Classic");
        
        switch (pStyle) {
        case STYLE_LIGHT:
        	styleLight.setEnabled(false);
        	break;
        case STYLE_DARK:
        	styleDark.setEnabled(false);
        	break;
        case STYLE_CLASSIC:
        	styleClassic.setEnabled(false);
        	break;
        }
       
        //Add components to pop-up menu
        popup.add(labelItem);
        popup.add(actions);
        actions.add(actionWatchMovie);
        actions.add(actionExit);
        popup.add(style);
        style.add(styleLight);
        style.add(styleDark);
        style.add(styleClassic);
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
        
        styleLight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pCallback.onChangeStyle(STYLE_LIGHT);
			}
		});
        
        styleDark.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pCallback.onChangeStyle(STYLE_DARK);
			}
		});
        
        styleClassic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pCallback.onChangeStyle(STYLE_CLASSIC);
				
			}
		});
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
        }
	}
	
	public void dispose() {
		tray.remove(trayIcon);
	}
	
	public static boolean isSupported() {
		return SystemTray.isSupported();
	}
	
	public void update(int pMinutes) {
		if (!TrayHandler.isSupported())
			return;
		
		String minutesLeft = pMinutes + " minutes left";
		this.trayIcon.setToolTip(minutesLeft);
		this.popup.getItem(0).setLabel(minutesLeft);
	}
}
