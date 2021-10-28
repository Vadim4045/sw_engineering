package rooppin.video.rental;

import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * JPanel for view order on admin mode
 */
public class AdminOrderView  extends AdminView
{

	/**
	 * Constructor
	 * @param parent
	 * @param order
	 */
	public AdminOrderView(ApplicationWindow parent, OrderPanel order) {
		super(parent);
		setContent(order);
		makeControls();
	}

	/**
	 * Action event header
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		parent.closeOrder(((OrderPanel)Content).orderNumber);
		
	}

	/**
	 * Add button for close order(all videos returned)
	 */
	@Override
	protected void makeControls() {
		JButton btn = new JButton("Close order(mark all videos as returned)");
		btn.addActionListener(this);
		ControlPanel.add(btn);
	}

}
