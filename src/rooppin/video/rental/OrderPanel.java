package rooppin.video.rental;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class OrderPanel extends JPanel implements ActionListener
{
	private ApplicationWindow parent;
	int orderNumber;
	HashMap<String,Film> orderedInStock;
	private boolean payed;
	private JPanel orderedPanel, btnPanel;
	private JLabel[] labels;
	private JButton[] btns;
	
	OrderPanel(ApplicationWindow parent) {
		this.parent = parent;
		orderNumber = parent.getOrderNumber();
		orderedInStock = new HashMap<>();
		payed = false;
		init();		
	}
	
	OrderPanel(ApplicationWindow parent, int orderNumber){
		this.orderNumber = orderNumber;
		orderedInStock = parent.getOrderedFilms(orderNumber);
		payed = parent.getPayed(orderNumber);
		init();
	}
	
	private void init() 
	{
		String[] forLabels = {"Order number: " + String.valueOf(orderNumber), "Ordered films:"};
		
		setLayout(new BorderLayout(5, 5));
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
	
		labels = new JLabel[forLabels.length];
		for(int i=0;i<forLabels.length;i++) {
			labels[i] = new JLabel(forLabels[i]);
			labels[i].setFont(new Font("Serif", Font.BOLD,18));
			top.add(labels[i]);
		}
		add(top, BorderLayout.NORTH);
		
		orderedPanel = new JPanel();
		orderedPanel.setAlignmentX(LEFT_ALIGNMENT);
		orderedPanel.setLayout(new BoxLayout(orderedPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollFrame = new JScrollPane(orderedPanel);
		orderedPanel.setAutoscrolls(true);
		scrollFrame.setAlignmentX(LEFT_ALIGNMENT);
		add(scrollFrame, BorderLayout.CENTER);
		
		btnPanel = getButtons();
		add(btnPanel, BorderLayout.SOUTH);
	}
	
	void showOrder() {
		JPanel tmp;
		JButton btn;
		orderedPanel.removeAll();
		int len = orderedInStock.size();
		if(len>0) {
			Set<String> films = orderedInStock.keySet();
			for(String s:films) {
				tmp = new JPanel();
				tmp.setAlignmentX(Component.LEFT_ALIGNMENT);
				JLabel l = new JLabel(orderedInStock.get(s).title + "  (" + s + ")");
				l.setFont(new Font("Serif", Font.BOLD,16));
				tmp.add(l);
				btn = new JButton("Remove");
				btn.setActionCommand(s);
				btn.addActionListener(this);
				tmp.add(btn);
				orderedPanel.add(tmp);
			}
			orderedPanel.add(Box.createVerticalGlue());
			orderedPanel.revalidate();
			orderedPanel.repaint();
		}
		btns[0].setEnabled(orderedInStock.size()>0);
		btns[2].setEnabled(!payed);
		repaint();
	}
	
	JPanel getButtons() {
		String[] forBtn = {"Sabmit", "Clear all","Pay"};
		JPanel btnPanel = new JPanel();
		btns = new JButton[forBtn.length];
		for(int i=0;i<btns.length;i++) {
			btns[i] = new JButton(forBtn[i]);
			btns[i].setFont(new Font("Serif", Font.PLAIN,16));
			btns[i].addActionListener(this);
			btns[i].setActionCommand(forBtn[i]);
			btnPanel.add(btns[i]);
		}
		return btnPanel;
	}
	
	boolean addFilmToOrder(Film film, String inv) {
		orderedInStock.put(inv, film);
		if(orderedInStock.containsKey(inv))return true;
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()) {
		case "Sabmit":
			if(parent.submitOrder(orderNumber, orderedInStock)) {
				orderedInStock.clear();
				showOrder();
				parent.removeTabByName("Order");
			} else parent.infoMsgTop("Error. Not submited.", 5);
			
			break;
		case "Clear all":
			orderedInStock.clear();
			showOrder();
			parent.removeTabByName("Order");
			break;
		case "Pay":
			if(parent.pay(orderNumber)) parent.newOrder();
			break;
			default:
				if(parent.removeOrderFilmFromDB(event.getActionCommand()))
							orderedInStock.remove(event.getActionCommand());
				showOrder();
			break;
		}
	}
}