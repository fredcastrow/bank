package com.roc.bank.dao;

import com.roc.bank.main.Application;
import com.roc.bank.models.Transaction;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAOImpl implements TransactionDAO{
	
	public TransactionDAOImpl() {
		super();
	}

	@Override
	public int createTransaction(Transaction transaction, Connection connection) throws SQLException {
		int count = 0;

		String sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_xfer_from_cust_id, tran_xfer_from_acct_id, ";
		sql += "tran_xfer_to_cust_id, tran_xfer_to_acct_id, tran_amt ) ";
		sql += "VALUES ";
//		sql += "( ?, ?, current_timestamp(0), ?, ?, ?, ?, ?, ?)";
		sql += "( ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, Transaction.getTran_acct_id());
			pstmt.setInt(2, Transaction.getTran_bank_id());

//			pstmt.setDate(3, (Date) Transaction.getTran_date());
//			pstmt.setDate(3, Transaction.getTran_date());
			
			pstmt.setString(3, Transaction.getTran_type());
			pstmt.setInt(4, Transaction.getTran_xfer_from_cust_id());
			pstmt.setInt(5, Transaction.getTran_xfer_from_acct_id());
			pstmt.setInt(6, Transaction.getTran_xfer_to_cust_id());
			pstmt.setInt(7, Transaction.getTran_xfer_to_acct_id());
			pstmt.setFloat(8, Transaction.getTran_amt());
			
			count = pstmt.executeUpdate();
		} catch (SQLException e) {

// FIX THIS
// tran_acct_id = 0
			Application.Log.info("tran_acct_id: " + Transaction.getTran_acct_id());
			Application.Log.info("tran_type: " + Transaction.getTran_type());
			
			Application.Log.info("[TransactionDAOImpl] SQLException: " + e.getMessage());
		}

		return count;
	}
}
