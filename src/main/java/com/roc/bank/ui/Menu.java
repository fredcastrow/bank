package com.roc.bank.ui;

//import com.roc.bank.util.ConnectionUtil;
//import com.roc.bank.exceptions.DatabaseConnectionException;

//import java.sql.Connection;
//import java.sql.SQLException;

//import com.roc.bank.main.Application;

public interface Menu {
//	public static Connection connection = ConnectionUtil.getConnection();

/*	
	Connection connection = new Connection();
	try {
		connection = ConnectionUtil.getConnection();
	} catch (DatabaseConnectionException e) {
		Menu.Log.info("DatabaseConnectionException");
	}
*/

//	public void display() throws SQLException;
	
	public void display();
	
	public void display(int int1, int int2);
}
