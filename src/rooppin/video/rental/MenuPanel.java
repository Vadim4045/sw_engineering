package rooppin.video.rental;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
class MenuPanel extends JMenuBar implements ActionListener
{
	ApplicationWindow parent = null;
	JMenu mainMenu, userMenu, adminMenu;
	JLabel infoLabel;
	MenuPanel(ApplicationWindow parent)
	{
		this.parent=parent;
		mainMenu=mainMenu();
		userMenu = getUserMenu();
		adminMenu=getAdminMenu();
		infoLabel = new JLabel();
		infoLabel.setFont(new Font("Serif", Font.PLAIN,20));
		state(0);
	}
	
	void state(int level) {
		topTitle("Hellow " + parent.user.getName());
		this.removeAll();
		switch(level) {
		case 0:
			this.add(mainMenu);
			this.add(infoLabel);
			break;
		case 1:
			this.add(mainMenu);
			this.add(userMenu);
			this.add(infoLabel);
			break;
		case 2:
			this.add(mainMenu);
			this.add(userMenu);
			this.add(adminMenu);
			this.add(infoLabel);
			break;
		}
		this.revalidate();
		this.repaint();
	}
	
	private JMenu getUserMenu(){
		if(userMenu==null)userMenu=userMenu();
		return userMenu;
	}
	
	private JMenu getAdminMenu() {
		if(adminMenu==null)adminMenu=adminMenu();
		return adminMenu;
	}
	
	private JMenu adminMenu()
	{
		JMenuItem menuItem;

		JMenu menu = new JMenu("Admin menu");
		menu.setMnemonic(KeyEvent.VK_A);
		
		menuItem = new JMenuItem("Show admin panel");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("admin");
		menu.add(menuItem);
		return menu;
	}
	
	private JMenu userMenu()
	{
		JMenuItem menuItem;

		JMenu menu = new JMenu("User menu");
		menu.setMnemonic(KeyEvent.VK_U);
		
		menuItem = new JMenuItem("Show my details");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("show");
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Edit data");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("edit");
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Order");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("order");
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Log out");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("logout");
		menu.add(menuItem);
		return menu;
	}
	
	private JMenu mainMenu()
	{
		JMenuItem menuItem;

		JMenu menu = new JMenu("Main menu");
		menu.setMnemonic(KeyEvent.VK_M);

		menuItem = new JMenuItem("Log in");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("login");
		menu.add(menuItem);

		if(parent.user.level==0) {
		menuItem = new JMenuItem("Register");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("register");
		menu.add(menuItem);
		}
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Search");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("search");
		menu.add(menuItem);
		

		menu.addSeparator();
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("exit");
		menu.add(menuItem);
		return menu;
	}
	
	void topTitle(String msg) {
		StringBuilder s = new StringBuilder();
		for(int i=0;i<50-msg.length()/2;i++) {
			s.append(" ");
		}
		s.append(msg);
		infoLabel.setText(s.toString());
		infoLabel.revalidate();
		infoLabel.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand())
		{
		case "login":
			parent.logIn();
			break;
		case "register":
			parent.register();
			break;
		case "logout":
			parent.logOut();
			break;
		case "search":
			parent.getSearch();
			break;
		case "show":
			parent.showUserData(false);
			break;
		case "edit":
			parent.showUserData(true);
			break;
		case "order":
			parent.showCurrentOrder();
			break;
		case "admin":
			parent.showAdminPanel();
			break;
		case "exit":
			parent.exit();
			break;
		default:
			break;
		}
		
	}

}