package com.roc.bank.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.roc.bank.dao.CustomerDAO;
import com.roc.bank.dao.CustomerDAOImpl;
import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.models.Customer;
import com.roc.bank.util.ConnectionUtil;

public class CustomerService {
	
	public CustomerDAO customerDAO;
	
	public CustomerService() {
		customerDAO = new CustomerDAOImpl();
	}
	
	public int createCustomer(int cust_bank_id, String cust_fname, String cust_lname, String cust_log_id, String cust_log_pw) {
		int count = 1;
		
		Customer customer = new Customer(cust_bank_id, cust_fname, cust_lname, cust_log_id, cust_log_pw);
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			count = customerDAO.createCustomer(customer, connection); // pass connection object into DAO operation
			
			connection.commit(); // commit DAO operation changes here
		} catch (SQLException | DatabaseConnectionException e) {
			System.out.println(e.getMessage());
		}

		return count;
	}
}
