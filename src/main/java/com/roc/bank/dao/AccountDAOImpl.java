package com.roc.bank.dao;

//import java.sql.Connection;
//import java.sql.SQLException;
import java.sql.*;

import com.roc.bank.models.Account;

public class AccountDAOImpl implements AccountDAO{
	
	public AccountDAOImpl() {
		super();
	}

	@Override
	public int createAccount(Account account, Connection connection) throws SQLException {
		int count = 0;
		
		String sql = "INSERT INTO bank.account( acct_owner_type, acct_bank_id, acct_cust_own_id, acct_type, acct_initial_deposit_amt ) VALUES ('C', ?, ?, 'C', ?)";
		
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, Account.cust_bank_id);
		pstmt.setInt(2, Account.cust_id);
		pstmt.setFloat(3, Account.acct_initial_deposit_amt);
		
		count = pstmt.executeUpdate();

		return count;
	}
}
