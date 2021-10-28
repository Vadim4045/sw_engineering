package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class MainAppFrame extends JFrame
{
	protected JTabbedPane tabPanel;

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
	
	void removeTabByName(String name) {
		int idx = findTabByName(name);
		if(idx>=0) tabPanel.remove(idx);
	}
	
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
	
	protected void setFocusToTab(String s)
	{
		int idx = findTabByName(s);
		tabPanel.setSelectedIndex(idx);
	}
	
	void infoMsgByPlace(JLabel label, String msg, int seconds) {
		Thread thread = new Thread(){
		    public void run(){
		    	new InfoMesegWithTimer(label, msg, seconds);
		    }
		 };
		 thread.start();
	}
	
	class InfoMesegWithTimer {
	    Timer timer;
	    String msg, oldMsg;
	    JLabel label;
	    
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
