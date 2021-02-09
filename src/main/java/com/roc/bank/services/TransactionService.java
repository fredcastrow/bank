package com.roc.bank.services;

import java.sql.Connection;
import java.sql.SQLException;
//import java.util.Date;

import com.roc.bank.dao.TransactionDAO;
import com.roc.bank.dao.TransactionDAOImpl;
import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.main.Application;
import com.roc.bank.models.Transaction;
import com.roc.bank.util.ConnectionUtil;

import jdk.internal.org.jline.utils.Log;

public class TransactionService {
	public TransactionDAO transactionDAO;
	
	public TransactionService() {
		transactionDAO = new TransactionDAOImpl();
	}
	
	public Transaction createTransaction( int tran_acct_id, int tran_bank_id, String tran_type, int tran_xfer_from_cust_id, int tran_xfer_from_acct_id, int tran_xfer_to_cust_id, int tran_xfer_to_acct_id, float tran_amt, String xfer_type ) {

		Transaction transaction = new Transaction();
			transaction = Transaction.initializeTransaction( tran_acct_id, tran_bank_id, tran_type, tran_xfer_from_cust_id, tran_xfer_from_acct_id, tran_xfer_to_cust_id, tran_xfer_to_acct_id, tran_amt, xfer_type );
			
			try (Connection connection = ConnectionUtil.getConnection()) {
				
//				if ( (tran_type.equals("W") && xfer_type.equals("XX")) || (tran_type.equals("D") && xfer_type.equals("XX") || tran_type.equals(xfer_type)) ) {
				if ( xfer_type.equals("XX") || tran_type.equals(xfer_type) ) {
					connection.setAutoCommit(false);
					transactionDAO.createTransaction(transaction, connection); // pass connection object into DAO operation
					connection.commit(); // commit DAO operation changes here
				}
				
			} catch (SQLException | DatabaseConnectionException e) {
				Application.Log.info("[TransactionService]:" +e.getMessage());
			}
		return transaction;
	}
}
