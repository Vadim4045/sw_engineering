package rooppin.video.rental;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AdminUserView  extends AdminView
{

	public AdminUserView(ApplicationWindow parent, User user) {
		super(parent);
		setContent(user);
		makeControls();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		parent.deleteUser(((User)Content).getId());
	}

	@Override
	protected void makeControls() {
		JButton btn = new JButton("Delete user from base");
		btn.addActionListener(this);
		ControlPanel.add(btn);
	}

}
