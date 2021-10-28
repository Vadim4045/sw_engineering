package rooppin.video.rental;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.*;
import javax.swing.*;

/**
 * A logical continuation of the SearchAppFrame class.
 * Implements all the functionality of links
 * 
 * @author _GoLeM_
 *
 */
public class ApplicationWindow extends SearchAppFrame
{
	User user;
	protected MenuPanel menu;
	
	/**
	 * Constructor
	 */
	public ApplicationWindow()
	{
		user = new User(this);
		menu = new MenuPanel(this);
		add(menu, BorderLayout.NORTH);
		setVisible(true);
		MakeNewTab("Search panel", new SearchPanel(this));
	}
	
	/**
	 * Return Id of current user
	 * @return
	 */
	public String getUserId() {
		if(user!=null) return user.getId();
		return null;
	}
	
	/**
	 * Gets all film data from database and make instance of Film in different thread
	 * If data not exists, invoke WebService and save data in database
	 * for repeated use
	 * 
	 * @param tconst
	 * @return
	 */
	Film MakeNewFilm(String tconst)
	{
		HashMap<String, String> set=null;
		try {
			 final HashMap<String, String> dbSet = dbService.getFullFilmData(tconst);

			if(dbSet.size()==0)
			{
				final HashMap<String, String> webSet = WebService.getFilmData(tconst);
				set = webSet;
				if(webSet==null || webSet.size()==0 || webSet.get("Errordb")!=null) 
					throw new Exception("Parsing ERROR");
				Thread thread = new Thread(){
				    public void run(){
				    	dbService.updateFullFilmTable(webSet);
				    }
				 };
				 thread.start();
			}else set=dbSet;
			
			return new Film(this, set);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Sorry, information is temporarily unavailable.", "Warning",
			        JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	/**
	 * Launches the authorization procedure
	 */
	void logIn() {
		String s = loginDialog(this);
		if(s!=null) {
			user = new RegisteredUser(this, dbService.getUserByEmail(s));
			int idx = findTabByName("User panel");
			if(idx>=0) tabPanel.remove(idx);
			order=new OrderPanel(this);
			levelState();
			infoMsgTop("Log in successful",5);
		}
	}

	/**
	 * Open form for user registration
	 */
	void register() {
		if(user.level==0) MakeNewTab("User panel", user);
	}

	/**
	 * Launches the unauthorization procedure
	 */
	void logOut() {
		if(user.getId().length()>0)dbService.logOut(user.getId());
		user=new User(this);
		removeTabByName("User panel");
		order=null;
		levelState();
	}

	/**
	 * Check access level of current user and repaint window according to it
	 */
	private void levelState() {
		menu.state(user.level);
		revalidate();
		repaint();
	}

	/**
	 * Show form with all data of current user
	 * boolean parameter switches to edit mode
	 * 
	 * @param i
	 */
	void showUserData(boolean i) {
		RegisteredUser tmp = (RegisteredUser) user;
		tmp.detailsSetEditable(i);
		int idx = findTabByName("User panel");
		if(idx < 0)MakeNewTab("User panel", user);
		setFocusToTab("User panel");
	}
	
	/**
	 * Show current no submited order if exists
	 */
	void showCurrentOrder() {
		if(order==null || order.orderedInStock.size()==0) {
			infoMsgTop("No order toview",5);
			return;
		}
		order.showOrder();
		MakeNewTab("Order", order);
	}
	
	/**
	 * Invoke DbServise to book film by IMDB code
	 * Returns specific inner inventory code of booked film
	 * 
	 * @param tconst imdb code
	 * @return invconst inner inventory code
	 */
	/*public String getInventory(String tconst) {
		return dbService.getInventory(tconst);
	}*/
	
	/**
	 * Open login dialog and invoke FileSystemService
	 *  to check inputed data
	 * 
	 * @param frame
	 * @return registered user email if done
	 */
	static String loginDialog(JFrame frame) {
		
	    JPanel panel = new JPanel(new BorderLayout(5, 5));
	    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("E-Mail", SwingConstants.RIGHT));
	    label.add(new JLabel("Password", SwingConstants.RIGHT));
	    panel.add(label, BorderLayout.WEST);
	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    JTextField username = new JTextField();
	    controls.add(username);
	    JPasswordField password = new JPasswordField();
	    controls.add(password);
	    panel.add(controls, BorderLayout.CENTER);
	    int n = JOptionPane.showConfirmDialog(frame, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
	    String a = username.getText().trim();
	    String b = new String(password.getPassword()).trim();
	    if (n == JOptionPane.YES_OPTION && a.length()>0 && b.length()>0) {
	    	if(FileSystemService.getPassFromFile(FileSystemService.encode(a,b))!=-1) return a;
	    }
	    return null;
	}
	
	/**
	 * Gets all update data of registered user 
	 * and completes update procedure
	 * 
	 * @param set
	 * @return true if done
	 */
	boolean updateUserRegistration(HashMap<String, String> set) {
		RegisteredUser u = new RegisteredUser(this,set);
		 if(dbService.updateUser(u)) {
			 this.user = u;
			 removeTabByName("User panel");
			 MakeNewTab("User panel", this.user);
			 levelState();
			 return true;
		 }else return false;
	}
	
	/**
	 * Gets all data from registration form
	 * and completes registration procedure
	 * includes store password
	 * 
	 * @param set
	 * @return true if done
	 */
	void newUserRegistration(HashMap<String, String> set) {
		Thread thread = new Thread(){
		    public void run(){
		    	FileSystemService.storePassToFile(FileSystemService.encode(set.get("Email"),
		    			set.get("pass")),-1);
		    }
		 };
		 thread.start();
		 RegisteredUser u = new RegisteredUser(this,set);
		 if(dbService.addNewUser(u)) {
			 this.user = u;
			 this.order = new OrderPanel(this);
			 removeTabByName("User panel");
			 MakeNewTab("User panel", this.user);
			 levelState();
		 }
	}

	/**
	 * Invoke FileSystemService to change user password
	 * 
	 * @param s
	 * @return
	 */
	boolean updatePassword(String[] s) {
		if(FileSystemService.updatePasswordInFile(s)) return true;
		return false;
	}
	
	/**
	 *  Invoke FileSystemService to change user email in password storage
	 *  
	 * @param oldEmail
	 * @param newEmail
	 * @param pass
	 * @return
	 */
	boolean changeEmailInPassword(String oldEmail, String newEmail, String pass) {
		if(FileSystemService.updateEmailInFile(oldEmail, newEmail, pass))return true;
		else return false;				
	}
	
	/**
	 * Invoke infoMsgByPlace function for temporary show given message on top of window
	 * 
	 * @param msg
	 * @param seconds
	 */
	void infoMsgTop(String msg, int seconds) {
		StringBuilder s = new StringBuilder();
		for(int i=0;i<50-msg.length()/2;i++) {
			s.append(" ");
		}
		s.append(msg);
		infoMsgByPlace(menu.infoLabel, s.toString(), seconds);
	}
	
	/**
	 * Launches exit procedure
	 * 
	 * @return
	 */
	int exit() {
		dbService.logOut(user.getId());
		System.exit(0);
		return 0;
	}

	/**
	 * Book new order number in database and return its
	 * @return
	 */
	int getOrderNumber() {
		return dbService.newOrderNumber(user.getId());
	}

	/**
	 * Submit order in database
	 * 
	 * @param orderNumber
	 * @param orderedInStock
	 * @return
	 */
	boolean submitOrder(int orderNumber, HashMap<String, Film> orderedInStock) {
		if(dbService.submitOrder(orderNumber, orderedInStock))return true;
		return false;
	}

	/**
	 * Invoke pay procedure(not implements in this project) 
	 * and set order as payed if done.
	 * 
	 * @param orderNumber
	 * @return
	 */
	boolean pay(int orderNumber) {
		if(PaymentService.pay() && dbService.setPayed(orderNumber)) return true;
		return false;
	}

	/**
	 * Make new instance of OrderPanel
	 */
	void newOrder() {
		removeTabByName("Order");
		order = new OrderPanel(this);
	}
	
	/**
	 * Cancellation of booking film in database
	 * 
	 * @param inv
	 * @return
	 */
	boolean removeOrderFilmFromDB(String inv) {
		if(dbService.removeOrderFilmFromDB(inv, user.getId())) return true;
		return false;
	}
	
	/**
	 * Booking film to user in database
	 * @param film
	 * @return
	 */
	boolean addFilmToOrder(Film film) {
		String inv = dbService.orderFilm(film, user.getId());
		if(inv!= null && order.addFilmToOrder(film, inv)) return true;
		return false;
	}

	/**
	 * Invoke getAdminSearch in DbSerice for extended search data
	 * in admin mode
	 * 
	 * @param mode
	 * @param type
	 * @param input
	 * @return
	 */
	public HashMap<String,String> getAdminSearch(String mode, String type, String input) {
		if(user.level<2) return null;
		
		HashMap<String,String> result = dbService.getAdminSearch(mode, type, input);
		return result;
	}

	/**
	 * Open admin search panel
	 */
	public void showAdminPanel() {
		removeTabByName("Admin panel");
		MakeNewTab("Admin panel", new AdminPanel(this));
	}

	/**
	 * Delete film from base(admin mode)
	 * 
	 * @param tconst
	 */
	public void deleteFilm(String tconst) {
		if(user.level<2) return;
		dbService.deleteFilm(tconst);
		
	}

	/**
	 * Delete user from base(admin mode)
	 * 
	 * @param id
	 */
	public void deleteUser(String id) {
		if(user.level<2) return;
		dbService.deleteUser(id);
	}

	/**
	 * Mark all ordered films as returned(admin mode)
	 * 
	 * @param orderNumber
	 */
	public void closeOrder(int orderNumber) {
		if(user.level<2) return;
		dbService.closeOrder(orderNumber);
	}

	/**
	 * Get all ordered films from base by order number
	 * (admin mode)
	 * 
	 * @param orderNumber
	 * @return
	 */
	public HashMap<String, Film> getOrderedFilms(int orderNumber) {
		if(user.level<2) return null;
		Film f = null;
		Vector<String> filmList = dbService.getOrderedFilms(orderNumber);
		HashMap<String,Film> orderedInStock = new HashMap<String,Film>();
		
		for(String s:filmList) {
			f=MakeNewFilm(s);
			if(f!=null) orderedInStock.put(s, f);
		}
		return orderedInStock;
	}

	/**
	 * Get pay status of cpecific order
	 * (admin mode)
	 * 
	 * @param orderNumber
	 * @return
	 */
	public boolean getPayed(int orderNumber) {
		if(user.level<2) return false;
		return dbService.getPayed(orderNumber);
	}

	/**
	 * Get all registered user details by user ID
	 * (admin mode)
	 * 
	 * @param id
	 * @return
	 */
	public HashMap<String, String> getUserById(String id) {
		if(user.level<2) return null;
		return dbService.getUserById(id);
	}
	
	/**
	 * Application entry function
	 * @param args
	 */
	public static void main(String[] args)
	{
		ApplicationWindow newApp = new ApplicationWindow();
	}
}
