package com.roc.bank.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.roc.bank.dao.CustomerDAO;
import com.roc.bank.dao.CustomerDAOImpl;
import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.main.Application;
import com.roc.bank.models.Customer;
import com.roc.bank.util.ConnectionUtil;

public class CustomerService {
	
	public CustomerDAO customerDAO;
	
	public CustomerService() {
		customerDAO = new CustomerDAOImpl();
	}
	
	public Customer createCustomer(int cust_bank_id, String cust_fname, String cust_lname, String cust_log_id, String cust_log_pw, String cust_email) {
		Customer customer = new Customer();
		customer = Customer.initializeCustomer(cust_bank_id, cust_fname, cust_lname, cust_log_id, cust_log_pw, cust_email);
		
		try (Connection connection = ConnectionUtil.getConnection()) {
// transaction control not in calling method and not needed as this is a single SQL INSER5T use case			
			connection.setAutoCommit(false);
			
			customerDAO.createCustomer(customer, connection); // pass connection object into DAO operation
			
			connection.commit(); // commit DAO operation changes here
		} catch (SQLException | DatabaseConnectionException e) {
			Application.Log.info("[CustomerService]:" +e.getMessage());
		}

		return customer;
	}
}
