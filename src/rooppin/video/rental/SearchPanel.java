package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * User interface to extended search films in base
 */
public class SearchPanel extends JPanel
{
	private ApplicationWindow parent;
	private String[] forLables = {"Type:", "Genre:", "Years:", "Contains a word:"};
	private JPanel centralPanel;
	private JComboBox[] boxes;
	private JTextField input;

	
	/**
	 * Constructor
	 * @param searchAppFrame
	 */
	SearchPanel(ApplicationWindow searchAppFrame)
	{
		this.parent=searchAppFrame;
		JLabel label;
		setLayout(new FlowLayout());
		JPanel tmpPanel;
		JPanel leftPanel = new JPanel();		

		JLabel tmpLbl;
		
		boxes = new JComboBox[forLables.length-1];
		for(int i=0;i<3;i++)
		{
			tmpPanel = new JPanel();

			tmpLbl = new JLabel(forLables[i]);
			tmpPanel.add(tmpLbl);
			
			boxes[i] = new JComboBox(searchAppFrame.getInitialValues(i));
			boxes[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					searchAppFrame.makeTemporaryTable(makeParamString());
				}
			});
			tmpPanel.add(boxes[i]);
			leftPanel.add(tmpPanel);
		}
		
		searchAppFrame.makeTemporaryTable(makeParamString());
		
		label = new JLabel(forLables[forLables.length-1]);
		leftPanel.add(label);
		
		input = new JTextField(20);
		leftPanel.add(input);
		
		JButton goBtn = new JButton("Search");
		goBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				getFreeSearch();
			}
		});
		leftPanel.add(goBtn);
		
		centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
		
		JScrollPane scrollFrame = new JScrollPane(centralPanel);
		scrollFrame.setPreferredSize(new Dimension( 850,450));
		centralPanel.setAutoscrolls(true);
		scrollFrame.setAlignmentX(LEFT_ALIGNMENT);
		
		
		add(leftPanel);
		add(scrollFrame);
		
		setVisible(true);
	}
	
	/**
	 * Launches search procedure by given parameters
	 */
	private void getFreeSearch()
	{
		Thread thread = new Thread(){
		    public void run(){
		    	JLabel newL=null;
		    	centralPanel.removeAll();
		    	HashMap<String,String> result = parent.getFreeSearch(input.getText());
		    	for (String i : result.keySet()){
		    		String tmp = result.get(i).length()>40 ? result.get(i).substring(0, 40) + "...":result.get(i);
		    		newL = new JLabel(tmp);
		    		newL.addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {
		                    Film f = parent.MakeNewFilm(i);
		                    if(f!=null) {
		                    	String s = f.title.length()>15? (f.title.substring(0, 15) +"..."):f.title;
			                    parent.MakeNewTab(s, f);
		                    } 
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
	 * Collects search parameters into array
	 * @return
	 */
	private String[] makeParamString()
	{
		String[] res = new String[4];
		for(int i=0;i<boxes.length;i++)
		{
			if(i<2) res[i] = boxes[i].getSelectedItem().toString();
			else
			{
				String tmp = boxes[i].getSelectedItem().toString();
				if(tmp.equals("All"))
				{
					res[2] = "1900";
					res[3] = "2021";
				}else
				{
					String[] tmp2 = tmp.split("-");
					res[2] = tmp2[0].trim();
					res[3] = tmp2[1].trim();
				}
			}
		}
		return res;
	}
}