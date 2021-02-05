package com.roc.bank.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.roc.bank.models.Customer;

public interface CustomerDAO {
	public int createCustomer(Customer customer, Connection connection) throws SQLException;
}
