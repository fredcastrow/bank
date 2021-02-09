package com.roc.bank.dao;

import com.roc.bank.main.Application;
import com.roc.bank.models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDAOImpl implements CustomerDAO{
	
	public CustomerDAOImpl() {
		super();
	}

	@Override
	public int createCustomer(Customer customer, Connection connection) throws SQLException {
		int count = 0;
			
		String sql = "INSERT INTO bank.customer ( cust_bank_id, cust_fname, cust_lname, cust_log_id, cust_log_pw)";
				sql += "VALUES (?, ?, ?, ?, ?)";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, Customer.getCust_bank_id());
			pstmt.setString(2, Customer.getCust_fname());
			pstmt.setString(3, Customer.getCust_lname());
			pstmt.setString(4, Customer.getCust_log_id());
			pstmt.setString(5, Customer.getCust_log_pw());
			
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			Application.Log.info("[CustomerDAOImpl] SQLException: " + e.getMessage());
		}
		
		count = 1;
		return count;
	}
}
