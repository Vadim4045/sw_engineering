package rooppin.video.rental;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
class RegisteredUser extends User
{
	public RegisteredUser(ApplicationWindow parent, HashMap<String,String> data)
	{
		super(parent, data);
		init();		
	}
	
	@Override
	void init() {
		setLayout(new BorderLayout(5, 5));
        add(makeTopPanel(), BorderLayout.NORTH);
        add(makeLeftPanel(), BorderLayout.WEST);
        add(makeRightPanel(), BorderLayout.CENTER);
        add(makeBottomPanel(), BorderLayout.SOUTH);
		agree.setSelected(true);
		agree.setEnabled(false);
		feelTextFields();
		detailsSetEditable(false);
	}
	
	private JPanel makeBottomPanel()
	{
		String[] forBtn = {"Edit my data","Change password","Submit","Discard changes","Order"};
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
	
	private JPanel makeRightPanel()
	{
		String[] details = {"Enter old password", "Enter new password:", "Confirm new password"};
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
		
		JPanel tmpPanel;
		JLabel tmpLbl;
		int start = level==0? 1:0;
		pases = new JPasswordField[details.length-start];
		for(int i=0;i<details.length-start;i++)
		{
			tmpPanel = new JPanel();
			tmpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			tmpLbl = new JLabel(details[i+start], SwingConstants.RIGHT);
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
	
	String getFieldByName(String field)
	{
		switch(field) {
		case "Id":
			return "'" + getId() + "'";
		case "Name":
			return "'" + getName() + "'";	
		case "Telephone":
			return "'" + getTelephone() + "'";
		case "Email":
			return "'" + getEmail() + "'";
		case "City":
			return "'" + getCity() + "'";
		case "Street":
			return "'" + getStreet() + "'";
		case "House":
			return String.valueOf(getHouse());
		case "Entry":
			return String.valueOf(getEntry());
		case "Appartment":
			return String.valueOf(getAppartment());
		case "level":
			return String.valueOf(level);
		default:
			return null;
		}
	}
	
	private void feelTextFields()
	{
		if(getId().equals("")) return;
		
		userDetails[0].setText(getId());
		userDetails[1].setText(getName());
		userDetails[2].setText(getTelephone());
		userDetails[3].setText(getEmail());
		userDetails[4].setText(getCity());
		userDetails[5].setText(getStreet());
		userDetails[6].setText(getHouse()+"");
		userDetails[7].setText(getEntry()+"");
		userDetails[8].setText(getAppartment()+"");
		for(JPasswordField p:pases) p.setText("");
	}
	
	void detailsSetEditable(boolean state)
	{
		int start = (level>0 && state)? 1:0;
		for(int i=start;i<userDetails.length;i++) userDetails[i].setEditable(state);
	}
	
	void pasesSetEditable(boolean state)
	{
		for(JTextField t:pases) t.setEditable(state);
	}
		
	@Override
	public void actionPerformed(ActionEvent event) {
		JComponent curComponent = (JComponent) event.getSource();
		
		if(curComponent == buttons[0])
		{
			detailsSetEditable(true);
			pasesSetEditable(false);
		}
		
		if(curComponent == buttons[1])
		{
			pasesSetEditable(true);
			detailsSetEditable(false);
		}
		
		if(curComponent == buttons[2])
		{
			if(userDetails[1].isEditable()==true && commit()) {
				HashMap<String,String> set = new HashMap<>();
				for(int i=0;i<details.length;i++) {
					set.put(details[i].substring(0,details[i].length()-1), userDetails[i].getText().trim());
				}
					set.put("pass", new String(pases[pases.length-1].getPassword()).trim());
					set.put("level", String.valueOf(level));
					if(!getEmail().equals(userDetails[3].getText())) {
						String pass = JOptionPane.showInputDialog(this, "Input current password", "Confirm change");
						if(parent.changeEmailInPassword(getEmail(), userDetails[3].getText(), pass))
							parent.updateUserRegistration(set);
						else JOptionPane.showMessageDialog(this, "Error! Data was not updated.", "Warning",JOptionPane.WARNING_MESSAGE);
					} else parent.updateUserRegistration(set);
					
				}
				
			if(pases[0].isEditable()==true && passCheck()) {
				String[] s = new String[pases.length];
				s[0] = getEmail();
				for(int i=1;i<pases.length;i++) s[i] = new String(pases[i-1].getPassword());
				String msg;
				if(parent.updatePassword(s)) msg = "Successful";
				else msg = "Error! The password was not updated.";
				JOptionPane.showMessageDialog(this, msg, "Warning",JOptionPane.WARNING_MESSAGE);
			}
			
		}
		
		if(curComponent == buttons[3])
		{
			detailsSetEditable(false);
			pasesSetEditable(false);
			feelTextFields();
		}
		
		if(curComponent == buttons[4])
		{
			parent.showCurrentOrder();
		}
	}
}
