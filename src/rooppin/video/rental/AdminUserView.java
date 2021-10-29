package rooppin.video.rental;

import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * JPanel for view user details in admin mode
 * @author VADIM&ORI&MATAN
 */
public class AdminUserView  extends AdminView
{

	/**
	 * Constructor
	 * @param parent
	 * @param user
	 */
	public AdminUserView(ApplicationWindow parent, User user) {
		super(parent);
		setContent(user);
		makeControls();
	}

	/**
	 * Action event handler
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		parent.deleteUser(((User)Content).getId());
	}

	/**
	 * Add button for remove user from base
	 */
	@Override
	protected void makeControls() {
		JButton btn = new JButton("Delete user from base");
		btn.addActionListener(this);
		ControlPanel.add(btn);
	}

}
