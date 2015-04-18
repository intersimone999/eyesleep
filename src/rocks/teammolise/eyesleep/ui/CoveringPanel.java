package rocks.teammolise.eyesleep.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rocks.teammolise.eyesleep.utils.Utils;

public class CoveringPanel extends JFrame {
	private static final long serialVersionUID = 7318202214125494619L;

	private static final Color BG = new Color(0f, 0f, 0f, .3f);

	private static final Color FG_TEXT = new Color(.9f, .9f, .9f, 1f);

	private static final Font MSG_FONT = new Font("Helvetica", Font.PLAIN, 20);
	
	private boolean skipped;
	private boolean watchingMovie;

	private int secondsToLive;
	private int targetSecond;
	
	private JPanel centeredPanel;
	private JPanel rootPanel;
	private JPanel shadowPanel;
	
	private JButton skipButton;
	private JButton watchingMovieButton;
	private JButton exitButton;
	
	private JLabel countdownLabel;
	private JLabel warningLabel;

	public CoveringPanel(int pTimeToLive, int skipsLeft) {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.setAlwaysOnTop(true);
		this.setAutoRequestFocus(true);
		this.setUndecorated(true);
		this.setResizable(false);
		
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		this.setLocationRelativeTo(null);
		
		this.secondsToLive = pTimeToLive;
		this.skipped = false;
		
		initGraphics(skipsLeft);
		
		if (skipsLeft > 0)
			this.skipButton.addActionListener(new ButtonListener(this, false));
		else
			this.skipButton.setEnabled(false);
		
		this.watchingMovieButton.addActionListener(new ButtonListener(this, true));
		
		this.exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}
	
	private void initGraphics(int skipsLeft) {
		double ratio = 1d/2d;
		Dimension shadowPanelDimension = new Dimension();
		shadowPanelDimension.width = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * ratio);
		shadowPanelDimension.height = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * ratio);
		BufferedImage screenshot;
		try {
			screenshot = Utils.blurImage(Utils.getScreenshot(), 100);
			this.rootPanel = new BackgroundPanel(screenshot);
		} catch (AWTException e) {
			e.printStackTrace();
			this.rootPanel = new JPanel();
		}
		this.rootPanel.setLayout(new GridBagLayout());
		this.shadowPanel = new JPanel(new GridBagLayout());
		this.shadowPanel.setSize(shadowPanelDimension);
		this.shadowPanel.setPreferredSize(shadowPanelDimension);
		this.shadowPanel.setBackground(CoveringPanel.BG);
		
		this.centeredPanel = new JPanel();
		VerticalFlowLayout verticalFlowLayout = new VerticalFlowLayout(20, FlowLayout.CENTER);
		this.centeredPanel.setLayout(verticalFlowLayout);
		this.centeredPanel.setBackground(CoveringPanel.BG);
		Dimension centeredPanelDimension = new Dimension((int) (shadowPanelDimension.width*ratio), (int) (shadowPanelDimension.height*ratio));
		this.centeredPanel.setPreferredSize(centeredPanelDimension);
		this.centeredPanel.setOpaque(false);
		
		SensibleListener listener = new SensibleListener(this);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
		this.addMouseMotionListener(listener);
		
		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		textPanel.setOpaque(false);
		this.warningLabel = new JLabel();
		this.warningLabel.setFont(MSG_FONT);
		this.warningLabel.setForeground(CoveringPanel.FG_TEXT);
		this.warningLabel.setOpaque(false);
		this.warningLabel.setSize(this.getSize().width, this.warningLabel.getSize().height);
		this.warningLabel.setLocation(0, 0);
		textPanel.add(this.warningLabel);
		
		this.countdownLabel = new JLabel(this.secondsToLive + " seconds...");
		this.countdownLabel.setFont(MSG_FONT);
		this.countdownLabel.setForeground(CoveringPanel.FG_TEXT);
		this.countdownLabel.setOpaque(false);
		this.countdownLabel.setSize(this.getSize().width, this.countdownLabel.getSize().height);
		this.countdownLabel.setLocation(0, 0);
		textPanel.add(this.countdownLabel);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setOpaque(false);
		
		
		this.watchingMovieButton = Utils.createFlatButton("Watching a movie");
		this.watchingMovieButton.setForeground(CoveringPanel.FG_TEXT);
		this.watchingMovieButton.setOpaque(false);
		this.watchingMovieButton.setLocation(0,0);
		buttonsPanel.add(this.watchingMovieButton);
		
		this.skipButton = Utils.createFlatButton("Skip (" + skipsLeft + " left)");
		this.skipButton.setForeground(CoveringPanel.FG_TEXT);
		this.skipButton.setOpaque(false);
		this.skipButton.setLocation(0, 0);
		buttonsPanel.add(this.skipButton);
		
		this.exitButton = Utils.createFlatButton("Exit");
		this.exitButton.setForeground(CoveringPanel.FG_TEXT);
		this.exitButton.setOpaque(false);
		this.exitButton.setLocation(0,0);
		buttonsPanel.add(this.exitButton);
		
		JPanel aPanel = new JPanel(new GridBagLayout());
		aPanel.setOpaque(false);
		aPanel.setPreferredSize(new Dimension(centeredPanelDimension.width, 100));
		aPanel.add(textPanel);
		centeredPanel.add(aPanel);
		JPanel anotherPanel = new JPanel(new GridBagLayout());
		anotherPanel.add(buttonsPanel);
		anotherPanel.setPreferredSize(new Dimension(centeredPanelDimension.width, 100));
		anotherPanel.setOpaque(false);
		centeredPanel.add(anotherPanel);
		
		this.shadowPanel.add(this.centeredPanel);
		this.rootPanel.add(this.shadowPanel);
		this.add(this.rootPanel);
	}
	
	public void startSleeping() {
		this.targetSecond = Utils.timeInSeconds() + this.secondsToLive;
	}
	
	public void moved() {
		if (Utils.timeInSeconds() + this.secondsToLive == this.targetSecond)
			return;
		
		this.warningLabel.setText("Hey, don't be brave! Just wait");
		this.startSleeping();
	}
	
	public boolean isDone() {
		return getSecondsToLive() <= 0;
	}
	
	public boolean hasSkipped() {
		return this.skipped;
	}
	
	public void skip() {
		this.skipped = true;
		this.targetSecond = 0; //ensures isDone returns always true
	}
	
	public void watchMovie() {
		this.skipped = true;
		this.watchingMovie = true;
		this.targetSecond = 0; //ensures isDone returns always true
	}
	
	public void update() {
		this.setLocation(new Point(0, 0));
		this.updateText(getSecondsToLive());
		this.requestFocus();
		
		if (getSecondsToLive() < this.secondsToLive - 3)
			this.warningLabel.setText("");
	}
	
	public boolean isWatchingAMovie() {
		return this.watchingMovie;
	}
	
	private void updateText(int seconds) {
		this.countdownLabel.setText(seconds + " seconds...");
		this.repaint();
	}
	
	private int getSecondsToLive() {
		return this.targetSecond - Utils.timeInSeconds();
	}
}

class ButtonListener implements ActionListener {
	private CoveringPanel coveringPanel;
	private boolean watchingMovie;

	public ButtonListener(CoveringPanel pPanel, boolean pWatchingMovie) {
		this.coveringPanel = pPanel;
		this.watchingMovie = pWatchingMovie;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.watchingMovie)
			this.coveringPanel.watchMovie();
		else
			this.coveringPanel.skip();
	}
}

class SensibleListener implements KeyListener, MouseListener, MouseMotionListener {
	private CoveringPanel panel;

	public SensibleListener(CoveringPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.panel.moved();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.panel.moved();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		this.panel.moved();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.panel.moved();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.panel.moved();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.panel.moved();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.panel.moved();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		this.panel.moved();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		this.panel.moved();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.panel.moved();
	}
}