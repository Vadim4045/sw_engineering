package rooppin.video.rental;

import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * JPanel for view film in admin mode
 */
public class AdminFilmView extends AdminView
{
/**
 * Constructor
 * @param parent
 * @param film
 */
	public AdminFilmView(ApplicationWindow parent, Film film) {
		super(parent);
		setContent(film);
		makeControls();
	}

	/**
	 * Action event handler
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		parent.deleteFilm(((Film)Content).tconst);
		
	}

	/**
	 * Add button for remove film from base
	 */
	@Override
	protected void makeControls() {
		JButton btn = new JButton("Delete film from base");
		btn.addActionListener(this);
		ControlPanel.add(btn);
	}

}
