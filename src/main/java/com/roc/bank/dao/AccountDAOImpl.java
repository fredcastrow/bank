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
		
		String sql = "INSERT INTO bank.account( acct_owner_type, acct_bank_id, acct_cust_own_id, acct_type, acct_initial_deposit_amt ) VALUES ('C', ?, ?, ?, ?)";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, Account.getAcct_bank_id());
			pstmt.setInt(2, Account.getCust_id());
			pstmt.setString(3, Account.getAcct_type());
			pstmt.setFloat(4, Account.getAcct_initial_deposit_amt());
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			if( e.getMessage().contains("ERROR: new row for relation \"account\" violates check constraint \"account_acct_initial_deposit_amt_check\"")){
				Application.Log.error("[AcctountDAOImpl] Initial Balance can not be negative.");
			}else {
				Application.Log.error("[AcctountDAOImpl] SQLException: " + e.getMessage());
			}
			return 0;
		}
		
		Application.Log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		Application.Log.info("[Account Request created:  Check back later to for approval status]");
		Application.Log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

		return count;
	}

	public int depositAccount(Account account, Connection connection) throws SQLException {
		int count = 0;

		String sql = "UPDATE bank.account SET ";
		sql += "acct_current_bal = acct_current_bal + ? ";
		sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_approved = TRUE";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setFloat(1, Account.getAcct_deposit_amt());
			pstmt.setInt(2, Account.getAcct_id());
			pstmt.setInt(3, Account.getAcct_bank_id());
			
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			if( e.getMessage().contains("ERROR: new row for relation \"account\" violates check constraint \"account_acct_current_bal_check\"")) {
				Application.Log.error("[AcctountDAOImpl] Account Balance can not be negative");
			}else {
				Application.Log.error("[AcctountDAOImpl] SQLException: " + e.getMessage());
			}
		}
		
		return count;
	}

	public int withdrawAccount(Account account, Connection connection) throws SQLException {
		int count = 0;

		String sql = "UPDATE bank.account SET ";
		sql += "acct_current_bal = acct_current_bal - ? ";
		sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_approved = TRUE";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setFloat(1, Account.getAcct_withdraw_amt());
			pstmt.setInt(2, Account.getAcct_id());
			pstmt.setInt(3, Account.getAcct_bank_id());
			
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			if( e.getMessage().contains("ERROR: new row for relation \"account\" violates check constraint \"account_acct_current_bal_check\"")) {
				Application.Log.error("[AcctountDAOImpl] Account Balance can not be negative");
			}else {
				Application.Log.error("[AcctountDAOImpl] SQLException: " + e.getMessage());
			}
		}
		
		return count;
	}
}
