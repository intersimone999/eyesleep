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
	private int secondsToLive;
	private int targetSecond;
	
	private JPanel centeredPanel;
	private JPanel rootPanel;
	private JPanel shadowPanel;
	private JButton skip;
	private JLabel label;
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
			this.skip.addActionListener(new ButtonListerer(this));
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
		this.centeredPanel.addMouseListener(listener);
		this.centeredPanel.addKeyListener(listener);
		this.centeredPanel.addMouseMotionListener(listener);
		
		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		textPanel.setOpaque(false);
		this.warningLabel = new JLabel();
		this.warningLabel.setFont(MSG_FONT);
		this.warningLabel.setForeground(CoveringPanel.FG_TEXT);
		this.warningLabel.setOpaque(false);
		this.warningLabel.setSize(this.getSize().width, this.warningLabel.getSize().height);
		this.warningLabel.setLocation(0, 0);
		textPanel.add(this.warningLabel);
		
		this.label = new JLabel(this.secondsToLive + " seconds...");
		this.label.setFont(MSG_FONT);
		this.label.setForeground(CoveringPanel.FG_TEXT);
		this.label.setOpaque(false);
		this.label.setSize(this.getSize().width, this.label.getSize().height);
		this.label.setLocation(0, 0);
		textPanel.add(this.label);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setOpaque(false);
		
		this.skip = Utils.createFlatButton("Skip (" + skipsLeft + " left)");
		this.skip.setForeground(CoveringPanel.FG_TEXT);
		this.skip.setOpaque(false);
		this.skip.setLocation(0, 0);
		buttonsPanel.add(this.skip);
		
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
	
	public void update() {
		this.setLocation(new Point(0, 0));
		this.updateText(getSecondsToLive());
		this.requestFocus();
		
		if (getSecondsToLive() < this.secondsToLive - 3)
			this.warningLabel.setText("");
	}
	
	private void updateText(int seconds) {
		this.label.setText(seconds + " seconds...");
		this.repaint();
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