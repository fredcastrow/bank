package com.roc.bank.ui;

import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.main.Application;
import com.roc.bank.services.AccountService;
import com.roc.bank.services.CustomerService;
import com.roc.bank.util.ConnectionUtil;

import java.sql.*;

import org.apache.log4j.Logger;

public class CustomerMenu implements Menu {
	@Override
	public void display() {}

	public static Logger Log = Logger.getLogger(Application.class);

	public CustomerService customerService;
	public AccountService accountService;

	public CustomerMenu() {
		customerService = new CustomerService();
		accountService = new AccountService();
	}

//	@Override
	public void display(int cust_bank_id, int cust_id) {
		int choice = 0;
		do {
			Log.info("");
			Log.info("===============");
			Log.info("[CUSTOMER MENU]");
			Log.info("===============");
			Log.info("1.) Back");
			Log.info("2.) Apply for a new Account");
			Log.info("3.) View Approved Account Balances");
			Log.info("4.) Make a Deposit");
			Log.info("5.) Make a Withdrawal");
			Log.info("6.) Transfer Money");
			Log.info("7.) Receive Money");
			Log.info("[Enter a choice between 1 and 7]");

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			
			switch (choice) {
				case 1:
					break;
					
				case 2:
//					String account = getCreateAccountInput();
					
					Log.info("Enter Initial Deposit Amount:");
					float acct_initial_deposit_amt = Float.parseFloat(sc.nextLine());
					
					accountService.createAccount(cust_bank_id, cust_id, acct_initial_deposit_amt);
					
					Log.info(cust_bank_id);
					Log.info(cust_id);
					Log.info(acct_initial_deposit_amt);

					Log.info("[Account Request created:  Check back later to for approval status]");
					Log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

					break;
					
				case 3:
					ResultSet rs;
					Connection connection = null;
					int account_count = 0;

					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Log.info("DatabaseConnectionException");
					}

					try {
						String sql = "SELECT Count(*) as AccountCount ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_approved = TRUE";
						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						rs = pstmt.executeQuery();

						rs.next();
						account_count = rs.getInt(1);
						if( account_count == 0 ) {
							Log.info("[No approved accounts found]");
							break;
						}else {
							account_count = rs.getInt(1);
						}
						
						sql = "SELECT acct_id, acct_initial_deposit_amt ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						rs = pstmt.executeQuery();
						
						int acct_id = 0;
						acct_initial_deposit_amt = 0;

						while( rs.next() ){
							account_count++;
							if( account_count == 1) {
								Log.info("");
								Log.info("======================");
								Log.info("[Approved Account IDs]");
								Log.info("======================");
							}
							acct_id = rs.getInt(1);
							acct_initial_deposit_amt = rs.getFloat(2); 
							Log.info("Account ID: " + acct_id + " Balance = " + acct_initial_deposit_amt);
						}
						if( account_count == 0) {
							Log.info("[No Approved Accounts found]");
						}
					} catch (SQLException e) {
						Log.info("SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				case 4:
					Log.info("Deposit under construction.");
					break;
					
				case 5:
					Log.info("Withdrawal under construction.");
					break;
					
				case 6:
					Log.info("Transfer under construction.");
					break;
					
				case 7:
					Log.info("Receive under construction.");
					break;
					
				default:
					Log.info("[No valid choice entered, please try again]");
					break;
			}
		} while (choice != 1);
/*
	public String getCreateAccountInput() {
//		Log.info("Enter a team name: ");
//		String teamName = Menu.sc.nextLine();

//		return teamName;
		return "getCreateAccountInput";
	}
*/	
	}
}	
