package rocks.teammolise.eyesleep.ui;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rocks.teammolise.eyesleep.utils.Utils;

public class CoveringPanel extends JFrame {
	private static final long serialVersionUID = 7318202214125494619L;
	
	private boolean skipped;
	private int secondsToLive;
	private int targetSecond;
	
	private JPanel panel;
	private JButton skip;
	private JLabel label;
	private JLabel warningLabel;

	public CoveringPanel(int pTimeToLive, int skipsLeft) {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.setAlwaysOnTop(true);
		this.setAutoRequestFocus(true);
		this.setUndecorated(true);
		this.setResizable(false);
		
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height*4);
		
		this.secondsToLive = pTimeToLive;
		this.skipped = false;
		
		initGraphics(skipsLeft);
		
		if (skipsLeft > 0)
			this.skip.addActionListener(new ButtonListerer(this));
	}
	
	private void initGraphics(int skipsLeft) {
		this.panel = new JPanel();
		
		SensibleListener listener = new SensibleListener(this);
		this.panel.addMouseListener(listener);
		this.panel.addKeyListener(listener);
		this.panel.addMouseMotionListener(listener);
		this.add(panel);
		
		this.warningLabel = new JLabel();
		this.warningLabel.setSize(this.getSize().width, this.warningLabel.getSize().height);
		this.warningLabel.setLocation(0, 0);
		panel.add(warningLabel);
		
		this.label = new JLabel(this.secondsToLive + " seconds...");
		this.label.setSize(this.getSize().width, this.label.getSize().height);
		this.label.setLocation(0, 0);
		panel.add(label);
		
		this.skip = new JButton("Skip (" + skipsLeft + " left)");
		this.skip.setLocation(0, 0);
		panel.add(skip);
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
	
	public void update() {
		this.setLocation(new Point(0, 0));
		this.updateText(getSecondsToLive());
		this.requestFocus();
		
		if (getSecondsToLive() < this.secondsToLive - 3)
			this.warningLabel.setText("");
	}
	
	private void updateText(int seconds) {
		this.label.setText(seconds + " seconds...");
	}
	
	private int getSecondsToLive() {
		return this.targetSecond - Utils.timeInSeconds();
	}
}

class ButtonListerer implements ActionListener {
	private CoveringPanel coveringPanel;

	public ButtonListerer(CoveringPanel pPanel) {
		this.coveringPanel = pPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
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