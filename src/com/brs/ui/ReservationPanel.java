package com.brs.ui;

import java.awt.Color;
import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

import com.brs.application.Passenger;
import com.brs.entity.RouteBean;
import com.brs.utility.RouteDAO;

public class ReservationPanel extends JPanel{

	private static final long serialVersionUID = -723858658158409045L;
	
	JLabel lOrigin,lDestination,lDate;
	JButton btnSubmit;

	JComboBox<String> cbOrigin, cbDestination, cbMonth;
	JComboBox<Integer> cbDate, cbYear;
	int x=200,y=40;
	ReservationPanel self;
	
	public ReservationPanel(){
		self = this;
		setLayout(null);
		setBackground(Color.white);
		setBounds(0,0,1024,500);
			
		
		lOrigin=new JLabel("Origin");
		lOrigin.setBounds(x+0,y+0,70,30);
		lOrigin.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		cbOrigin=new JComboBox<String>();
		Iterator<RouteBean> originIterator = new RouteDAO().findOrigins().iterator();
		do{
			RouteBean rb = originIterator.next();
			cbOrigin.addItem(rb.getOrigin());
		}while(originIterator.hasNext());
		cbOrigin.setBounds(x+0,y+50,200,30);
		cbOrigin.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		lDestination=new JLabel("Destination");
		lDestination.setBounds(x+300,y+0,200,30);
		lDestination.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		cbDestination=new JComboBox<String>();
		Iterator<RouteBean> destinationIteration = new RouteDAO().findDestinations(cbOrigin.getSelectedItem().toString()).iterator();
		do{
			RouteBean rb = destinationIteration.next();
			cbDestination.addItem(rb.getDestination());
		}while(destinationIteration.hasNext());
		cbDestination.setBounds(x+300,y+50,200,30);
		cbDestination.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		lDate=new JLabel("Select your journey date");
		lDate.setBounds(x+0,y+180,240,30);
		lDate.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		cbDate=new JComboBox<Integer>();
		cbDate.setBounds(x+480,y+180,100,30);
		cbDate.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		cbMonth=new JComboBox<String>();
		cbMonth.setBounds(x+310,y+180,150,30);
		cbMonth.addItem("Select a month");
		cbMonth.addItem("January");
		cbMonth.addItem("February");
		cbMonth.addItem("March");
		cbMonth.addItem("April");
		cbMonth.addItem("May");
		cbMonth.addItem("June");
		cbMonth.addItem("July");
		cbMonth.addItem("August");
		cbMonth.addItem("September");
		cbMonth.addItem("October");
		cbMonth.addItem("November");
		cbMonth.addItem("December");
		cbMonth.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		cbYear=new JComboBox<Integer>();
		Integer yr = new Integer(StylesAndHelperMethods.currentYear());
		cbYear.addItem(yr);
		yr = new Integer(StylesAndHelperMethods.currentYear()+1);
		cbYear.addItem(yr);
		cbYear.setBounds(x+220,y+180,70,30);
		cbYear.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		
		btnSubmit=new JButton("Submit");
		btnSubmit.setBounds(x+370,y+300,100,40);
		btnSubmit.setFont(StylesAndHelperMethods.FONT_NORMAL);
		
		add(lOrigin);
		add(cbOrigin);
		add(lDestination);
		add(cbDestination);
		add(lDate);
		add(cbMonth);
		add(cbYear);
	
		cbOrigin.addItemListener(new ItemAdapter(){
			
			public void itemStateChanged(ItemEvent e){
				
				if(e.getStateChange()==1){
					cbDestination.removeAllItems();
					Iterator<RouteBean> destinationIteration = new RouteDAO().findDestinations(cbOrigin.getSelectedItem().toString()).iterator();
					do{
						RouteBean rb = destinationIteration.next();
						cbDestination.addItem(rb.getDestination());
					}while(destinationIteration.hasNext());
				}
				
			}
			
		});
		
		cbMonth.addItemListener(new ItemAdapter(){
			
			public void itemStateChanged(ItemEvent e){
				
					cbDate.removeAllItems();
					if((e.getItem().equals("January"))||(e.getItem().equals("March"))||(e.getItem().equals("May"))||(e.getItem().equals("July"))||(e.getItem().equals("August"))||(e.getItem().equals("October"))||(e.getItem().equals("December"))){
						for(int i=1;i<=31;i++){
							cbDate.addItem(new Integer(i));
						}
					}
					else if((e.getItem().equals("April"))||(e.getItem().equals("June"))||(e.getItem().equals("September"))||(e.getItem().equals("November"))){
						for(int i=1;i<=30;i++){
							cbDate.addItem(new Integer(i));
						}
					}
					else if(e.getItem().equals("February")){
						for(int i=1;i<=29;i++){
							cbDate.addItem(new Integer(i));
						}
					}
					
					add(btnSubmit);
					add(cbDate);	
			}
			
		});
		
		btnSubmit.addActionListener(new ActionAdapter() {
			
			public void actionPerformed(ActionEvent ae) {
				if(cbMonth.getSelectedIndex()==0){
					StylesAndHelperMethods.errorMessage("Select month.");
				}
				else if(Integer.parseInt(cbYear.getSelectedItem().toString()) > StylesAndHelperMethods.currentYear()
						|| cbMonth.getSelectedIndex()>StylesAndHelperMethods.currentMonth() 
						|| ( cbMonth.getSelectedIndex()==StylesAndHelperMethods.currentMonth() && cbDate.getSelectedIndex() >= StylesAndHelperMethods.currentDay())
						){	
					
					String date = cbDate.getSelectedItem()+"-"+cbMonth.getSelectedItem()+"-"+cbYear.getSelectedItem();
					MainFrame.brs.availableBusesPage(Passenger.session.availableBuses(cbOrigin.getSelectedItem().toString(), cbDestination.getSelectedItem().toString(),date), date);
				}
				else {
					StylesAndHelperMethods.errorMessage("Select a future date.");
				}
			}
			
		});
		
	}
	
}

