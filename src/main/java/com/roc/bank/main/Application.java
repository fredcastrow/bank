package com.roc.bank.main;

import org.apache.log4j.Logger;
//import org.postgresql.Driver;

import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.util.Scanner;

import com.roc.bank.util.ConnectionUtil;
import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.ui.LoginMenu;
import com.roc.bank.ui.Menu;

public class Application {
	
	public static Scanner sc = new Scanner(System.in);
	public static Logger Log=Logger.getLogger(Application.class);

	public static void main(String[] args) {
		Log.info("================================");
		Log.info("Welcome to the Bank Application!");
		Log.info("================================");
		
		Menu loginMenu = new LoginMenu();

		int int1=0, int2=-0;
		loginMenu.display(int1, int2);
		
		Log.info("Goodbye!");
		
	}
}
