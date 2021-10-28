package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public abstract class AdminView extends JPanel implements ActionListener
{
	protected ApplicationWindow parent;
	protected JPanel Content, ControlPanel;
	
	public AdminView(ApplicationWindow parent) {
		this.parent=parent;
		
		setLayout(new BorderLayout());
		ControlPanel = new JPanel();
		add(ControlPanel,BorderLayout.SOUTH);
	}

	public void setContent(JPanel content) {
		Content = content;
		add(Content, BorderLayout.CENTER);
	}
	protected abstract void makeControls();
}
