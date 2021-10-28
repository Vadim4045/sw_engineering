package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;

/**
 * JPanel for search and get different data from database
 */
public class AdminPanel  extends JPanel implements ActionListener
{
	private ApplicationWindow parent;
	private JPanel topPanel, centralPanel;
	private JButton[] buttons;
	private JComboBox[] boxes;
	private JTextField input;
	
	@SuppressWarnings("unchecked")
	AdminPanel(ApplicationWindow parent)
	{
		this.parent=parent;
		JLabel label;
		setLayout(new BorderLayout());
		
		add(makeTopPanel(), BorderLayout.NORTH);
		add(makeCentralPanel(), BorderLayout.CENTER);
		setVisible(true);
	}
	
	/**
	 * Make JPanel with all search options
	 * @return TopPanel
	 */
	private Component makeTopPanel() {
		String[] forButtons = {"User search", "Order search", "Film search"};
		String[][] Options = {{"User by name", "Best users", "Worst users"},
				{"No closed", "No payed", "Largest"},
				{"Top used", "No used", "Ordered", "Out of stock"}};
		
		JPanel top = new JPanel();
		
		input = new JTextField(20);
		top.add(input);
		
		buttons = new JButton[forButtons.length];
		boxes = new JComboBox[forButtons.length];
		for(int i=0;i<forButtons.length;i++) {
			buttons[i] = new JButton(forButtons[i]);
			buttons[i].addActionListener(this);
			boxes[i] = new JComboBox(Options[i]);
			top.add(boxes[i]);
			top.add(buttons[i]);
			top.add(new JSeparator(SwingConstants.VERTICAL));
		}
		return top;
	}
	
	/**
	 * Make scrollpanel for view results of search
	 * @return ScrollPanel
	 */
	private Component makeCentralPanel() {
		centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollFrame = new JScrollPane(centralPanel);
		scrollFrame.setPreferredSize(new Dimension( 600,400));
		centralPanel.setAutoscrolls(true);
		scrollFrame.setAlignmentX(LEFT_ALIGNMENT);
		return scrollFrame;
	}
	
	/**
	 * Gets search results and arramge their on scroll panel
	 * @param result
	 */
	private void fillScrollContent(HashMap<String,String> result) {
		Thread thread = new Thread(){
		    public void run(){
		    	JLabel newL=null;
		    	centralPanel.removeAll();
		    	for (String i : result.keySet()){
		    		newL = new JLabel(result.get(i));
		    		newL.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                    showDetails(i);
		                }

						@Override
		                public void mouseEntered(MouseEvent e) {
		                	setCursor(new Cursor(Cursor.HAND_CURSOR));
		                }
		                
		                @Override
		                public void mouseExited(MouseEvent e) {
		                	setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		                }
		            });
		    		centralPanel.add(newL);
		    	}
		    	if(result.size()==0) newL = new JLabel("Nothing found");
		    	
		    	centralPanel.add(newL);
		    	centralPanel.revalidate();
		    	centralPanel.repaint();
		    }
		  };

		  thread.start();
	}

	/**
	 * Action event handler
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent curComponent = (JComponent) event.getSource();
		for(int i=0;i<buttons.length;i++) {
			if(curComponent==buttons[i]) {
				HashMap<String,String> result = parent.getAdminSearch(
						buttons[i].getText(),
						boxes[i].getSelectedItem().toString(),
						input.getText()
						);
				fillScrollContent(result);
			}
		}
	}
	
    /**
     * Gets string whith identificator of clicked item
     * and make new tab on TapPanel whith corresponding content
     * @param i String
     */
    private void showDetails(String i) {
    	String s;
		String[] set = i.split(":");
		switch(set[0]) {
		case "User search":
			  User user = new RegisteredUser(parent, parent.getUserById(set[1]));
			  s = user.getName();
			  parent.MakeNewTab(s, new AdminUserView(parent,user));
			break;
		case "Order search":
			OrderPanel order = new OrderPanel(parent, Integer.valueOf(set[1]));
			s = "Order: " + set[1];
			  parent.MakeNewTab(s, new AdminOrderView(parent,order));
			  order.showOrder();
			break;
		case "Film search":
			Film f = parent.MakeNewFilm(set[1]);
            if(f!=null) {
            	s = f.title.length()>15? (f.title.substring(0, 15) +"..."):f.title;
                parent.MakeNewTab(s, new AdminFilmView(parent,f));
            }
			break;
		default:
			break;
		}
	}
}
