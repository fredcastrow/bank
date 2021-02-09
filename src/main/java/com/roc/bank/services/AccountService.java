package com.roc.bank.services;

import java.sql.Connection;

import com.roc.bank.main.Application;
import com.roc.bank.dao.AccountDAO;
import com.roc.bank.dao.AccountDAOImpl;
import com.roc.bank.models.Account;
import com.roc.bank.util.ConnectionUtil;
import java.sql.SQLException;
import com.roc.bank.exceptions.DatabaseConnectionException;

public class AccountService {
	
	public AccountDAO accountDAO;
	
	public AccountService() {
		accountDAO = new AccountDAOImpl();
	}
	
	public Account createAccount(int cust_bank_id, int cust_id, float acct_initial_deposit_amt, String acct_type) {
		Account account = new Account();
		account = Account.initializeAccount(cust_bank_id, cust_id, acct_initial_deposit_amt, acct_type);
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			accountDAO.createAccount(account, connection); // pass connection object into DAO operation
			
			connection.commit(); // commit DAO operation changes here
		} catch (SQLException | DatabaseConnectionException e) {
			Application.Log.info("[AcctService]:" +e.getMessage());
		}

		return account;
	}


	public Account depositAccount(int acct_id, int cust_bank_id, float acct_deposit_amt) {
		Account account = new Account();
		account = Account.initializeDepositAccount(acct_id, cust_bank_id, acct_deposit_amt);
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			accountDAO.depositAccount(account, connection); // pass connection object into DAO operation
			
			connection.commit(); // commit DAO operation changes here
		} catch (SQLException | DatabaseConnectionException e) {
			Application.Log.info("[AcctService]:" +e.getMessage());
		}

		return account;
	}

	public Account withdrawAccount(int acct_id, int cust_bank_id, float acct_withdraw_amt) {
		Account account = new Account();
		account = Account.initializeWithdrawAccount(acct_id, cust_bank_id, acct_withdraw_amt);
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			accountDAO.withdrawAccount(account, connection); // pass connection object into DAO operation
			
			connection.commit(); // commit DAO operation changes here
		} catch (SQLException | DatabaseConnectionException e) {
			Application.Log.info("[AcctService]:" +e.getMessage());
		}

		return account;
	}
}
