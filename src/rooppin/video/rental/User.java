package rooppin.video.rental;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
/**
 * User class.
 * @author VADIM&ORI&MATAN
 *{@link Person} {@link ActionListener}
 */
public class User extends Person  implements ActionListener {
	
	protected ApplicationWindow parent;
	protected JTextField[] userDetails;
	protected JPasswordField[] pases;
	private JLabel[] stars;
	protected JButton[] buttons;
	protected JCheckBox agree;
	int level;
	protected final String [] details= {"Id:","Name:","Telephone:","Email:","City:","Street:","House:","Entry:","Appartment:"};
	
	/**
	 * Default Constructor
	 * @param parent {@link ApplicationWindow}
	 */
	public User(ApplicationWindow parent)
	{
		this.parent = parent;
		this.level=0;
		init();
	}
	
	/**
	 * Parameter Constructor
	 * @param parent {@link ApplicationWindow}
	 * @param data {@link HashMap}
	 */
	public User(ApplicationWindow parent, HashMap<String,String> data)
	{
		super(data);
		this.parent = parent;
		this.level = Integer.parseInt(data.get("level"));
		init();		
	}
	
	/**
	 * init method set panels in the frame
	 */
	void init()
	{
        setLayout(new BorderLayout(5, 5));
        add(makeTopPanel(), BorderLayout.NORTH);
        add(makeLeftPanel(), BorderLayout.WEST);
        add(makeRightPanel(), BorderLayout.CENTER);
        add(makeBottomPanel(), BorderLayout.SOUTH);
	}
	
	/**
	 * makeTopPanel method create top panel
	 * @return JPanel {@link JPanel}
	 */
	protected JPanel makeTopPanel()
	{
		JPanel topPanel = new JPanel();
		JLabel topLabel = new JLabel("User panel");
		topLabel.setFont(new Font("Serif", Font.BOLD, 36));
		topPanel.add(topLabel);
		return topPanel;
	}
	
	/**
	 * makeBottomPanel method create bottom panel with controlls
	 * @return JPanel {@link JPanel}
	 */
	private JPanel makeBottomPanel()
	{
		String[] forBtn = {"Submit","Clear all"};
		JPanel bottomPanel = new JPanel();
		buttons = new JButton[forBtn.length];
		for(int i=0;i<forBtn.length;i++)
		{
			buttons[i] = new JButton(forBtn[i]);
			buttons[i].setPreferredSize(new Dimension(170, 35));
			buttons[i].setFont(new Font("Serif", Font.BOLD, 16));
			bottomPanel.add(buttons[i]);
			buttons[i].addActionListener(this);
		}
		return bottomPanel;
	}
	
	/**
	 * makeLeftPanel method create left panel
	 * with all user data
	 * 
	 * @return JPanel {@link JPanel}
	 */
	protected JPanel makeLeftPanel()
	{
		JPanel tmpPanel;
		userDetails = new JTextField[details.length];
		stars = new JLabel[details.length];
		JLabel tmpLbl;
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
		centralPanel.setBackground(new Color(235,235,235));
		for(int i=0;i<details.length;i++)
		{
			tmpPanel = new JPanel();
			
			tmpLbl = new JLabel(details[i], SwingConstants.RIGHT);
			tmpLbl.setPreferredSize(new Dimension(140, 25));
			tmpLbl.setFont(new Font("Serif", Font.BOLD, 14));
			
			userDetails[i] = new JTextField();
			userDetails[i].setPreferredSize(new Dimension(170, 25));
			
			stars[i] = new JLabel("*", SwingConstants.LEFT);
			stars[i].setFont(new Font("Serif", Font.BOLD, 20));
			stars[i].setForeground(new Color(235,235,235));
			
			tmpPanel.add(tmpLbl);
			tmpPanel.add(userDetails[i]);
			tmpPanel.add(stars[i]);
			centralPanel.add(tmpPanel);
		}
		centralPanel.add(Box.createVerticalStrut(200));
		
		return centralPanel;
	}
	
	/**
	 * makeRightPanel method create right panel
	 * with passwords and LICENSE AGREEMENT
	 * 
	 * @return JPanel {@link JPanel}
	 */
	private JPanel makeRightPanel()
	{
		String[] details = {"Enter password", "Confirm password"};
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
		
		JPanel tmpPanel;
		JLabel tmpLbl;
		pases = new JPasswordField[details.length];
		for(int i=0;i<details.length;i++)
		{
			tmpPanel = new JPanel();
			tmpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			tmpLbl = new JLabel(details[i], SwingConstants.RIGHT);
			tmpLbl.setPreferredSize(new Dimension(140, 25));
			tmpLbl.setFont(new Font("Serif", Font.BOLD, 14));
			
			pases[i] = new JPasswordField();
			pases[i].setPreferredSize(new Dimension(170, 25));
			if(level>0) pases[i].setEditable(false);
			tmpPanel.add(tmpLbl);
			tmpPanel.add(pases[i]);
			centralPanel.add(tmpPanel);
		}
		centralPanel.add(Box.createVerticalGlue());
		centralPanel.add(makeLicensePanel());
		return centralPanel;
	}

	/**
	 * makeLicensePanel method create license panel
	 * @return Component {@link Component}
	 */
	protected Component makeLicensePanel() {
		String[] content = {"LIMITED SINGLE-USE VIDEO LICENSE AGREEMENT\r\n"
				, "\r\n"
				, "By checking the “Acceptance” Box on the next page, you are accepting this License Agreement and you acknowledge that you have read, understand, and agree to be bound by the terms below. \r\n"
				, "\r\n"
				, "1 Smarter Every Day (“SED”) Videos are copyrighted by Smarter Every Day, LLC and may not be reproduced, disseminated, adapted, changed, or modified in any way. \r\n"
				, "\r\n"
				, "2  You have no ownership rights in the Videos; you have a license to the limited use of the Videos on the terms and for the period specified herein: \r\n"
				, "\r\n"
				, "3  Neither SED nor any of its directors, officers, employees, or representatives shall be held liable for any harm to you or any third party resulting directly or indirectly from your use of the Videos. You shall hold harmless and indemnify SED and its agents from and against any liability for any such claim; \r\n"
				, "\r\n"
				, "4  SED grants to you a non-exclusive, limited license to use the Videos and portions thereof, provided that: \r\n"
				, "\r\n"
				, "a.  You shall stream the video directly from the SED YouTube channel;\r\n"
				, "\r\n"
				, "b.  You shall not repost, sell, license, adapt, change, or modify the Videos or any portion of the Videos;\r\n"
				, "\r\n"
				, "c.  You shall not charge for the use of the Videos; \r\n"
				, "\r\n"
				, "d.  You shall not redistribute, share, repackage or include in any online or offline archive, video collection, website, DVD, or other media (for example, uploading event presentation footage to the internet);\r\n"
				, "\r\n"
				, "e.  You shall not attempt to transfer or sublicense the Videos;\r\n"
				, "\r\n"
				, "f.  You shall not attempt to register a copyright or trademark for any part of the video. \r\n"
				, "\r\n"
				, "5  No portion of this limited license shall be interpreted to convey any copyrights to you or to limit SED’s rights in the Videos. SED may terminate this limited license at any time at its discretion, at which time you must immediately discontinue use of the Videos;\r\n"
				, "\r\n"
				, "6  This license is valid for sixty days once the fee has been paid and a signed and dated copy of this agreement is received at business@smartereveryday.com. This license covers one presentation of the Videos (including, but not limited to, training seminars, company meetings, etc) within this sixty-day period.\r\n"
				, "\r\n"
				, "Copyright infringement is a violation of federal law subject to criminal and civil penalties.\r\n"
				, "\r\n"
				, "Once you have completed your purchase, you will receive an email with a link to download a copy of this license agreement to sign, date and return. "};
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
		
		JScrollPane scrollFrame = new JScrollPane(centralPanel);
		scrollFrame.setPreferredSize(new Dimension( 330,320));
		centralPanel.setAutoscrolls(true);
		scrollFrame.setAlignmentX(LEFT_ALIGNMENT);
		fillScrollContent(centralPanel, content);
		return scrollFrame;
	}
	
	/**
	 * fillScrollContent method for agreement user using page.
	 * @param scroll {@link JPanel}
	 * @param content {@link String}
	 */
	private void fillScrollContent(JPanel scroll, String[] content) {
		
		int maxLen=90;
		for(String s:content) {
			String[] tempStr = s.split(" ");
			StringBuilder str = new StringBuilder();
			for(int i=0;i<tempStr.length;) {
				int len=0;
				while(true) {
					if(len<maxLen && i<tempStr.length){
						str.append(tempStr[i]+" ");
						len+=tempStr[i++].length()+1;
					}else break;
				}
				JLabel tmp = new JLabel("  " + str.toString(),SwingConstants.LEFT);
				tmp.setFont(new Font("Serif", Font.PLAIN,12));
				str.setLength(0);
				scroll.add(tmp);
			}
		}
		agree = new JCheckBox("I agree with the conditions");
		agree.setFont(new Font("Serif", Font.PLAIN,12));
		if(level>0) {
			
		}
		scroll.add(agree);
	}
	
	/**
	 * commit method check if the fields are correct.
	 * @return boolean 
	 */
	protected boolean commit()
	{
		Color col = new Color(235,235,235);
		String[] matches = {"[0-9]{9}","[A-Z]?[a-z]+([’'\\- ]?[A-Z]?[a-z]+){0,2}","[0-9]{3}-?[0-9]{3}-?[0-9]{2}-?[0-9]{2}","[A-Za-z]+(.)?[A-Za-z]+@[a-z]+(.[a-z]{2,3}){0,2}","[A-Z]+\\w+","[A-Z]+\\w+","[0-9]+","[0-9]+","[0-9]+"};
		for(int i=0;i<userDetails.length-3;i++)
		{
			if(userDetails[i].getText().length()>0 && userDetails[i].getText().matches(matches[i]))
			{
				stars[i].setForeground(col);
			}
			else
			{
				stars[i].setForeground(Color.RED);
				return false;
			}
		}

		return true;
	}
	
	/**
	 * passCheck method check if the password field is correct.
	 * @return boolean 
	 */
	protected boolean passCheck() {
		for(int i=0;i<pases.length;i++)
			if(pases[i].getPassword().length==0) return false;
		if(new String(pases[pases.length-1].getPassword())
				.equals(new String(pases[pases.length-2].getPassword())))
					return true;
		else return false;
		
	}
	
	@Override
	/**
	 * actionPerformed method manage evented handlers 
	 * @param event {@link ActionEvent}
	 */
	public void actionPerformed(ActionEvent event) {
		JComponent curComponent = (JComponent) event.getSource();
		if(curComponent == buttons[0])
		{
			HashMap<String,String> set = new HashMap<>();
			if(commit() && agree.isSelected() && passCheck()) {
				for(int i=0;i<details.length;i++) {
					set.put(details[i].substring(0,details[i].length()-1), userDetails[i].getText().trim());
				}
					set.put("pass", new String(pases[pases.length-1].getPassword()).trim());
					set.put("level", "1");
					parent.newUserRegistration(set);
				}else JOptionPane.showMessageDialog(this, "Fill all fields and check license agree.", "Warning",
			        JOptionPane.ERROR_MESSAGE);
		}
		if(curComponent == buttons[1])
		{
			for(JTextField f:userDetails) f.setText("");
			for(JTextField f:pases) f.setText("");
		}
	}
}
