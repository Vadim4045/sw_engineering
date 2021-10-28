package rooppin.video.rental;

import java.awt.image.*;
import java.util.*;

/**
 * A logical continuation of the MainAppFrame class.
 * Implements the functions of searching for films in the database 
 * and loading a poster in a separate thread
 */
public class SearchAppFrame extends MainAppFrame
{
	protected UserDbService dbService = null;
	OrderPanel order;
	
	/**
	 * Constructor
	 */
	public SearchAppFrame() {
		dbService = new UserDbService((ApplicationWindow) this);
		
	}

	/**
	 * Invoke getFreeSearch function in UserDbService class
	 * 
	 * @param input
	 * @return
	 */
	HashMap<String,String> getFreeSearch(String input)
	{
		return dbService.getFreeSearch(input);
	}
	
	void makeTemporaryTable(String[] param)
	{
		Thread thread = new Thread(){
		    public void run(){
		    	dbService.makeTemporaryTable(param);
		    }
		  };

		  thread.start();
	}
	
	/**
	 * Invoke getInitialValues function in UserDbService class
	 * 
	 * @param num
	 * @return
	 */
	String[] getInitialValues(int num)
	{
		final String[] years = {"All", "1900-1950", "1950-1980", "1980-1990", "1990-2000", "2000-2010", "2010-2020", "2020-2022"};
		return num==2? years:dbService.getInitialValues(num);
	}
	
	/**
	 * Gets poster image by URL.
	 * If image file not exists in local storage
	 * invoke WebService to giv file from WEB
	 * and store its local for repeated use
	 * 
	 * @param uri
	 * @param name
	 * @return
	 */
	public BufferedImage getPoster(final String uri, final String name)
	{
		BufferedImage image=null;
		if(uri!=null && !uri.equals("N") && !uri.equals("N/A")) {
			String fileName = dbService.getPosterFileName(uri);
			image = FileSystemService.getImageFromFile(fileName);
			if(image==null)
			{
				final BufferedImage webImage = WebService.getPoster(uri);

				Thread thread = new Thread(){
				    public void run(){
						if(FileSystemService.saveImage(webImage, name))
								dbService.updateImageFile(name, uri);
				    }
				 };
				 thread.start();
				 if(webImage!=null) image = webImage;
			}
		}
		
		if(image==null)image = FileSystemService.getImageFromFile("no_poster");
		return image;
	}
	
	/**
	 * Set focus on search panel
	 */
	void getSearch() {
		tabPanel.setSelectedIndex(findTabByName("Search panel"));
	}

	/**
	 * Repaint main window
	 */
	void refresh() {
		revalidate();
		repaint();
	}

	
}
