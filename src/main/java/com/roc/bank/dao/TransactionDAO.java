package com.roc.bank.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.roc.bank.models.Transaction;

public interface TransactionDAO {
	public int createTransaction(Transaction transaction, Connection connection) throws SQLException;
}
