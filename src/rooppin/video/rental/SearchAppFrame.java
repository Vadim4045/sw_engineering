package rooppin.video.rental;

import java.awt.image.*;
import java.util.*;

@SuppressWarnings("serial")
public class SearchAppFrame extends MainAppFrame
{
	protected UserDbService dbService = null;
	OrderPanel order;
	
	public SearchAppFrame() {
		dbService = new UserDbService((ApplicationWindow) this);
		
	}
	
	
	
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
		
	String[] getInitialValues(int num)
	{
		final String[] years = {"All", "1900-1950", "1950-1980", "1980-1990", "1990-2000", "2000-2010", "2010-2020", "2020-2022"};
		return num==2? years:dbService.getInitialValues(num);
	}
	
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
	
	void getSearch() {
		tabPanel.setSelectedIndex(findTabByName("Search panel"));
	}

	void refresh() {
		revalidate();
		repaint();
	}

	
}
