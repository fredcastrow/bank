package com.roc.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.roc.bank.models.Customer;

import jdk.internal.org.jline.utils.Log;

public class CustomerDAOImpl implements CustomerDAO{
	
	public CustomerDAOImpl() {
		super();
	}

	@Override
	public int createCustomer(Customer customer, Connection connection) throws SQLException {
		int count = 0;
			
		String sql = "INSERT INTO bank.customer ( cust_bank_id, cust_fname, cust_lname, cust_log_id, cust_log_pw)";
				sql += "VALUES (1, ?, ?, ?, ?)";
		
		PreparedStatement pstmt = connection.prepareStatement(sql);
// change this to use getter methods		
//		pstmt.setString(1, customer.getCustomerFname());
		pstmt.setString(1, customer.cust_fname);
		pstmt.setString(2, customer.cust_lname);
		pstmt.setString(3, customer.cust_log_id);
		pstmt.setString(4, customer.cust_log_pw);
		
		count = pstmt.executeUpdate();
		
		return count;
	}
}