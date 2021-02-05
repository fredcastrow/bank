package com.roc.bank.services;

import java.sql.Connection;
import java.sql.SQLException;

import com.roc.bank.dao.AccountDAO;
import com.roc.bank.dao.AccountDAOImpl;
import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.models.Account;
import com.roc.bank.util.ConnectionUtil;

public class AccountService {
	
	public AccountDAO accountDAO;
	
	public AccountService() {
		accountDAO = new AccountDAOImpl();
	}
	
	public int createAccount(int cust_bank_id, int cust_id, float acct_initial_deposit_amt) {
		int count = 1;
		
		Account account = new Account(cust_bank_id, cust_id, acct_initial_deposit_amt);
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			count = accountDAO.createAccount(account, connection); // pass connection object into DAO operation
			
			// Let's say we have some more team manipulation going on here.
			// Then because these operations are wrapped between .setAutoCommit(false) and .commit()
			// We can treat all of these operations as a single transaction
			
			connection.commit(); // commit DAO operation changes here
		} catch (SQLException | DatabaseConnectionException e) {
			System.out.println(e.getMessage());
		}

		return count;
	}
}
