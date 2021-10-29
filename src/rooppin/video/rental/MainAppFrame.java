package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The basic version of the main window, 
 * implements the basic functions of the graphical arrangement of elements 
 * and displaying messages for a given period of time
 * @author VADIM&ORI&MATAN
 */
@SuppressWarnings("serial")
public class MainAppFrame extends JFrame
{
	protected JTabbedPane tabPanel;

	/**
	 * Constructor
	 */
	public MainAppFrame()
	{
		super("ProVideoRentals");
		setSize(900, 600);
		this.setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));
        tabPanel = new JTabbedPane();
		add(tabPanel,BorderLayout.CENTER);
	}
	
	/**
	 * Gets JPanel and String for naming new Tab
	 * Make new Tab on TabbetPane whith given content
	 * @param s
	 * @param panel
	 */
	void MakeNewTab(String s, JPanel panel)
	{
		removeTabByName(s);
		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		
		JLabel lblTitle = new JLabel(s);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		pnlTab.add(lblTitle,gbc);
		
		if(!(panel instanceof SearchPanel)) {
			JButton btnClose = new JButton("    X");
			btnClose.setBorder(BorderFactory.createEmptyBorder());
			btnClose.setContentAreaFilled(false);
			btnClose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					int idx = tabPanel.indexOfTab(s);
					tabPanel.remove(idx);
				}
			});
			
			gbc.gridx++;
			gbc.weightx = 0;
			pnlTab.add(btnClose,gbc);
		}
		
		tabPanel.add(s,panel);
		tabPanel.setTabComponentAt(tabPanel.indexOfTab(s), pnlTab);
		int idx = findTabByName(s);
		switch(s)
		{
			case "Search panel":
				tabPanel.setBackgroundAt(idx,new Color(100,100,250));
			break;
			case "User panel":
				tabPanel.setBackgroundAt(idx,new Color(150,150,250));
			break;
			default:
			break;
		}
		setFocusToTab(s);
	}
	
	/**
	 * Gets String of tab name, invoke search function and remove finded tab
	 * @param name
	 */
	void removeTabByName(String name) {
		int idx = findTabByName(name);
		if(idx>=0) tabPanel.remove(idx);
	}
	
	/**
	 * Gets tab name and find tab if exists
	 * @param title String of tab name
	 * @return finded tab
	 */
	protected int findTabByName(String title)  
	{
	  int tabCount = tabPanel.getTabCount();
	  for (int i=0; i < tabCount; i++) 
	  {
	    String tabTitle = tabPanel.getTitleAt(i);
	    if (tabTitle.equals(title)) return i;
	  }
	  return -1;
	}
	
	/**
	 * Gets tab name , invoke tab search function and set focus on finded tab
	 * @param s tab name
	 */
	protected void setFocusToTab(String s)
	{
		int idx = findTabByName(s);
		if(idx>=0) tabPanel.setSelectedIndex(idx);
	}
	
	/**
	 * Gets target(JLabel) to show message,
	 * String whith given message and period of time
	 * Invoke InfoMesegWithTimer class for start in enather thread
	 * 
	 * @param label
	 * @param msg
	 * @param seconds
	 */
	void infoMsgByPlace(JLabel label, String msg, int seconds) {
		Thread thread = new Thread(){
		    public void run(){
		    	new InfoMesegWithTimer(label, msg, seconds);
		    }
		 };
		 thread.start();
	}
	
	/**
	 * Class for show temporary message on given target
	 * in different thread
	 */
	class InfoMesegWithTimer {
	    Timer timer;
	    String msg, oldMsg;
	    JLabel label;
	    
	    /**
	     * Show given message on given target by given period time
	     * 
	     * @param label
	     * @param msg
	     * @param seconds
	     */
	    InfoMesegWithTimer(JLabel label, String msg, int seconds) {
	    	this.msg=msg;
	    	this.label=label;
	        timer = new Timer();
	        oldMsg = label.getText();
	        label.setText(msg);
	        label.revalidate();
	        label.repaint();
	        timer.schedule(new RemindTask(), seconds*1000);
		}

	    /**
	     * Scheduler for return old message on target after given period time
	     */
	    class RemindTask extends TimerTask {
	        public void run() {
	        	 label.setText(oldMsg);
	 	        label.revalidate();
	 	        label.repaint();
	            timer.cancel();
	        }
	    }
	}
}
