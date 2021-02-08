package com.roc.bank.dao;

import com.roc.bank.main.Application;
import com.roc.bank.models.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.sql.*;

public class AccountDAOImpl implements AccountDAO{
	
	public AccountDAOImpl() {
		super();
	}

	@Override
	public int createAccount(Account account, Connection connection) throws SQLException {
		int count = 0;
		
		String sql = "INSERT INTO bank.account( acct_owner_type, acct_bank_id, acct_cust_own_id, acct_type, acct_initial_deposit_amt ) VALUES ('C', ?, ?, 'C', ?)";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, Account.getAcct_bank_id());
			pstmt.setInt(2, Account.getCust_id());
			pstmt.setFloat(3, Account.getAcct_initial_deposit_amt());
			
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			Application.Log.info("SQLException: " + e.getMessage());
		}

		return count;
	}
}
