/**
 * <Copyright information>
 * 
 * ALL WORKS � SHIVAJI VARMA[contact@shivajivarma.com]
 * 
 * PassengerDAO.java
 * 
 * This file contains PassengerDAO class which helps in accessing and
 * modifying Passenger table in database.
 * 
 * Version 1.0
 * 
 * Created on 3 SEP 2012
 * 
 * <Modification History>
 */

package com.brs.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.brs.entity.BusBean;
import com.brs.entity.RouteBean;
import com.brs.utility.exceptions.DBConnectException;

/**
 * Following class tries to access and modify AccountDetails table in database.
 * 
 * @version 1.0
 */
public class BusDAO {

	// Holds database connection object.
	Connection conn;

	/**
	 * Following constructor gets the connection object from DBConnection class
	 * and holds the connection.
	 */
	public BusDAO() {
		conn = DBConnection.getConnection();
	}
	
	


	/** 
	 * Provides the list of buses from selected origin to selected destination on given date 
	 * */
	public Collection<BusBean> find(String origin,String destination,String date)throws  DBConnectException {
		Collection<BusBean> bbs = new ArrayList<BusBean>();
		try {
			PreparedStatement pst = conn.prepareStatement("SELECT b.bid,r.origin,r.destination,b.ac,b.depttime,b.arrtime,b.fare,(select 40-count(*) FROM reserve re where re.bid=b.bid and dt=?)  AS AvailablityCount  "+
					"FROM bus b,route r  "+
					"WHERE b.rid=r.rid AND r.origin=? AND r.destination=?");
			
			pst.setString(1, date);
			pst.setString(2, origin);
			pst.setString(3, destination);
			
			
			ResultSet rs = pst.executeQuery(); 
			BusBean bb = null;
			RouteBean rob = null;
			while (rs.next()) {
				
				bb = new BusBean();
				bb.setBid(rs.getLong("BID"));
				
				rob = new RouteBean();
				rob.setOrigin(rs.getString("ORIGIN"));
				rob.setDestination(rs.getString("DESTINATION"));
				bb.setRouteBean(rob);
				
				if(rs.getInt("AC") == 1)
					bb.setAc(true);
				else
					bb.setAc(false);
				
				bb.setDeptime(rs.getString("DEPTTIME"));
				bb.setArrtime(rs.getString("ARRTIME"));
				bb.setFare(rs.getInt("FARE"));
				bb.setAvailablityCount(rs.getInt("AvailablityCount"));
				
				bbs.add(bb);
			} 
	
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return bbs;
	}
		
}
