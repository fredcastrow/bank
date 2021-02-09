package com.roc.bank.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.roc.bank.models.Account;

public interface AccountDAO {
	public int createAccount(Account account, Connection connection) throws SQLException;
	public int depositAccount(Account account, Connection connection) throws SQLException;
	public int withdrawAccount(Account account, Connection connection) throws SQLException;
}
