package rooppin.video.rental;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.concurrent.*;



@SuppressWarnings("serial")
class Film extends JPanel implements ActionListener
{
	String tconst;
	String title;
	ApplicationWindow parent;
	private ExecutorService executor;
	JPanel lPanel, rPanel;
	JLabel informationLabel;
	
	Film(ApplicationWindow parent, final HashMap<String,String> set)
	{
		this.parent=parent;
		String[] titles = {"Title", "Type", "Year", "Country", "Production", "Released",
				"Director", "Writer", "Actors", "Runtime", "imdbRating", "imdbVotes"};
		Thread thread = new Thread(){
		    public void run(){
		    	executor = Executors.newSingleThreadExecutor();
		    	Future<BufferedImage> img = executor.submit(new FuturePoster(parent, set));
		    	getFuturePoster(img);
		    }
		 };
		 thread.start();
		 
	    tconst = set.get("imdbID");
		title = set.get("Title");
		
		setLayout(new GridLayout());
		lPanel = new JPanel();
		lPanel.setLayout(new BoxLayout(lPanel, BoxLayout.PAGE_AXIS));
		lPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		lPanel.add(Box.createVerticalGlue());
		
		for(int i=0;i<titles.length;i++) {
			String curStr = set.get(titles[i]);
			if(curStr!=null &&!curStr.equals("null") && !curStr.equals("N")
													&& !curStr.equals("N/A")) {
				JLabel l = new JLabel(titles[i] + ": " + curStr);
				l.setFont(new Font("Serif", Font.PLAIN,18));
				lPanel.add(l);
			}
		}
		String curStr = set.get("Plot");
		if(curStr!=null && curStr.length()>3) fillContent(lPanel, curStr, 60);
		
		lPanel.add(Box.createVerticalGlue());
		
		if(parent.user.level<2) {
			JButton btn = new JButton("Add to order");
			btn.setFont(new Font("Serif", Font.PLAIN,16));
			btn.addActionListener(this);
			lPanel.add(btn);
			
			informationLabel = new JLabel();
			informationLabel.setFont(new Font("Serif", Font.PLAIN,18));
			lPanel.add(informationLabel);
			
			lPanel.add(Box.createVerticalGlue());
		}
		
		
		rPanel = new JPanel();
		
		add(lPanel);
		add(rPanel);

	}

	private void fillContent(JPanel scroll, String content, int maxLen) {
			String[] tempStr = content.split(" ");
			StringBuilder str = new StringBuilder();
			for(int i=0;i<tempStr.length;) {
				int len=0;
				while(true) {
					if(len<maxLen && i<tempStr.length){
						str.append(tempStr[i]+" ");
						len+=tempStr[i++].length()+1;
					}else break;
				}
				JLabel tmp = new JLabel("  " + str.toString(),SwingConstants.LEFT);
				tmp.setFont(new Font("Serif", Font.PLAIN,16));
				str.setLength(0);
				scroll.add(tmp);
			}
	}
	
	private void getFuturePoster(Future<BufferedImage> img)
	{
		try {
		 	while(!img.isDone()) {
			 	Thread.sleep(100);	
		 	}
		 	Image poster = img.get();
		 	if(poster!=null)
		    {

		 		ImageIcon icon = new ImageIcon(poster);
		 		JLabel picLabel = new JLabel(icon);
		 		rPanel.add(picLabel);
		 		this.revalidate();
		 		this.repaint();
		    }
		 
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(parent.order==null) {
			parent.infoMsgByPlace(informationLabel, "For registered users only!", 10);
		}else {
			 if(parent.addFilmToOrder(this))
				 parent.infoMsgByPlace(informationLabel, "Added succefull", 5);
			 else parent.infoMsgByPlace(informationLabel, "Temporarily out of stock", 5);
		}
		
	}

	class FuturePoster implements Callable<BufferedImage>
	{
		HashMap<String,String> set;
		SearchAppFrame parent;
	
		FuturePoster(SearchAppFrame parent, final HashMap<String,String> set)
		{
			this.set=set;
			this.parent=parent;
		}
	
		@Override
		public BufferedImage call() throws Exception {
			if(set.get("Poster").length()>5) return parent.getPoster(set.get("Poster"), set.get("imdbID"));
			else return null;
		}
	
	}
}