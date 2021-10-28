package rooppin.video.rental;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ApplicationWindow extends SearchAppFrame
{
	User user;
	
	protected MenuPanel menu;
	
	public ApplicationWindow()
	{
		user = new User(this);
		menu = new MenuPanel(this);
		add(menu, BorderLayout.NORTH);
		setVisible(true);
		MakeNewTab("Search panel", new SearchPanel(this));
	}
	
	public String getUserId() {
		if(user!=null) return user.getId();
		return null;
	}
	
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

	void register() {
		MakeNewTab("User panel", user);
	}

	void logOut() {
		if(user.getId().length()>0)dbService.logOut(user.getId());
		user=new User(this);
		removeTabByName("User panel");
		order=null;
		levelState();
	}

	private void levelState() {
		menu.state(user.level);
		revalidate();
		repaint();
	}

	void showUserData(boolean i) {
		RegisteredUser tmp = (RegisteredUser) user;
		tmp.detailsSetEditable(i);
		int idx = findTabByName("User panel");
		if(idx < 0)MakeNewTab("User panel", user);
		setFocusToTab("User panel");
	}
	
	/**
	 * 
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
	 * Returns inner inventory code of film
	 * 
	 * @param tconst imdb code
	 * 
	 * @return invconst inner inventory code
	 */
	public String getInventory(String tconst) {
		return dbService.getInventory(tconst);
	}
	
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

	boolean updatePassword(String[] s) {
		if(FileSystemService.updatePasswordInFile(s)) return true;
		return false;
	}
	
	boolean changeEmailInPassword(String oldEmail, String newEmail, String pass) {
		if(FileSystemService.updateEmailInFile(oldEmail, newEmail, pass))return true;
		else return false;				
	}
	
	void infoMsgTop(String msg, int seconds) {
		StringBuilder s = new StringBuilder();
		for(int i=0;i<50-msg.length()/2;i++) {
			s.append(" ");
		}
		s.append(msg);
		infoMsgByPlace(menu.infoLabel, s.toString(), seconds);
	}
	
	int exit() {
		dbService.logOut(user.getId());
		System.exit(0);
		return 0;
	}

	int getOrderNumber() {
		return dbService.newOrderNumber(user.getId());
	}

	boolean submitOrder(int orderNumber, HashMap<String, Film> orderedInStock) {
		if(dbService.submitOrder(orderNumber, orderedInStock))return true;
		return false;
	}

	boolean pay(int orderNumber) {
		if(PaymentService.pay() && dbService.setPayed(orderNumber)) return true;
		return false;
	}

	void newOrder() {
		removeTabByName("Order");
		order = new OrderPanel(this);
	}
	
	boolean removeOrderFilmFromDB(String inv) {
		if(dbService.removeOrderFilmFromDB(inv, user.getId())) return true;
		return false;
	}
	
	boolean addFilmToOrder(Film film) {
		String inv = dbService.orderFilm(film, user.getId());
		if(inv!= null && order.addFilmToOrder(film, inv)) return true;
		return false;
	}

	public HashMap<String,String> getAdminSearch(String mode, String type, String input) {
		if(user.level<2) return null;
		
		HashMap<String,String> result = dbService.getAdminSearch(mode, type, input);
		return result;
	}

	public void showAdminPanel() {
		removeTabByName("Admin panel");
		MakeNewTab("Admin panel", new AdminPanel(this));
	}
	
	public static void main(String[] args)
	{
		ApplicationWindow newApp = new ApplicationWindow();
	}

	public void deleteFilm(String tconst) {
		dbService.deleteFilm(tconst);
		
	}

	public void deleteUser(String id) {
		dbService.deleteUser(id);
	}

	public void closeOrder(int orderNumber) {
		dbService.closeOrder(orderNumber);
	}

	public HashMap<String, Film> getOrderedFilms(int orderNumber) {
		Film f = null;
		Vector<String> filmList = dbService.getOrderedFilms(orderNumber);
		HashMap<String,Film> orderedInStock = new HashMap<String,Film>();
		
		for(String s:filmList) {
			f=MakeNewFilm(s);
			if(f!=null) orderedInStock.put(s, f);
		}
		return orderedInStock;
	}

	public boolean getPayed(int orderNumber) {
		return dbService.getPayed(orderNumber);
	}

	public HashMap<String, String> getUserById(String id) {
		return dbService.getUserById(id);
	}
}
