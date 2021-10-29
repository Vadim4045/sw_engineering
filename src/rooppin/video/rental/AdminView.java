package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Abstract class for extends to all admin views
 * @author VADIM&ORI&MATAN
 */
public abstract class AdminView extends JPanel implements ActionListener
{
	protected ApplicationWindow parent;
	protected JPanel Content, ControlPanel;
	
	/**
	 * Constructor
	 * @param parent
	 */
	public AdminView(ApplicationWindow parent) {
		this.parent=parent;
		setLayout(new BorderLayout());
		ControlPanel = new JPanel();
		add(ControlPanel,BorderLayout.SOUTH);
	}

	/**
	 * Gets content panel and put it on mine center
	 * @param content (Film, User or Order)
	 */
	public void setContent(JPanel content) {
		Content = content;
		add(Content, BorderLayout.CENTER);
	}
	
	/**
	 * abstract function to add additional controls
	 */
	protected abstract void makeControls();
}
