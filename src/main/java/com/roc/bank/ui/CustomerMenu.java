package com.roc.bank.ui;

import com.roc.bank.main.Application;

import com.roc.bank.services.AccountService;
import com.roc.bank.services.CustomerService;
import com.roc.bank.util.ConnectionUtil;
import com.roc.bank.services.TransactionService;

import com.roc.bank.exceptions.DatabaseConnectionException;
import java.sql.*;
//import java.util.Date;

public class CustomerMenu implements Menu {
	@Override
	public void display() {}

	public CustomerService customerService;
	public AccountService accountService;
	public TransactionService transactionService;

	public CustomerMenu() {
		customerService = new CustomerService();
		accountService = new AccountService();
		transactionService = new TransactionService();
	}

//	@Override
	public void display(int cust_bank_id, int cust_id) {
		ResultSet rs;
	
		Connection connection = null;
		try {
			connection = ConnectionUtil.getConnection();
		} catch (DatabaseConnectionException e) {
		}
		
		int account_count = 0;
		int acct_id = 0;
		float withdraw_amt = 0;
		float deposit_amt = 0;
		String acct_type = "C";

		String sql;
		PreparedStatement pstmt;

		int choice = 0;
		do {
			choice = 0;
			Application.Log.info("");
			Application.Log.info("===============");
			Application.Log.info("[CUSTOMER MENU]");
			Application.Log.info("===============");
			Application.Log.info("1.) Back");
			Application.Log.info("2.) Apply for a new Account");
			Application.Log.info("3.) List Approved Account Balances");
			Application.Log.info("4.) Make a Deposit");
			Application.Log.info("5.) Make a Withdrawal");
			Application.Log.info("6.) Transfer Money");
//			Application.Log.info("7.) Receive Money");
			Application.Log.info("[Enter a choice between 1 and 6]");

			try {
				choice = Integer.parseInt(Application.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			
			switch (choice) {
				case 1:
					break;
					
				case 2:
//					String account = getCreateAccountInput();
					Float acct_initial_deposit_amt = 0f;
					
					Application.Log.info("Enter Account Type (C = Checking, S = Savings)");
					acct_type = Application.sc.nextLine();
					acct_type = acct_type.toUpperCase();

					Application.Log.info("Enter Initial Deposit Amount:");

//					acct_initial_deposit_amt = Float.parseFloat(Application.sc.nextLine());
					
					try {
						acct_initial_deposit_amt = Float.parseFloat(Application.sc.nextLine());
					}catch (NumberFormatException e) {
						if( e.getMessage().contains("For input string:")) {
							Application.Log.warn("[ERROR] [CustomerMenu]: Initial Deposit Amount must be a number]");
						}else {
							Application.Log.error("[CustomerMenu]: " + e.getMessage());
						}
						break;
					}
					

					accountService.createAccount(cust_bank_id, cust_id, acct_initial_deposit_amt, acct_type);

					try {
						sql = "SELECT Max(acct_id) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C'" ;
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						rs = pstmt.executeQuery();
						
						while( rs.next() ){ acct_id = rs.getInt(1); }

						transactionService.createTransaction( acct_id, cust_bank_id, "O", 0, 0, 0, 0, acct_initial_deposit_amt, "" );
						
					} catch (SQLException e) {
						Application.Log.error("[CustomerMenu] SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
						break;
					}
					break;
					
				case 3:
					try {
/*						
						sql = "SELECT Count(*) as AccountCount ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_approved = TRUE AND acct_type = ?";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						pstmt.setString(3, acct_type);
						rs = pstmt.executeQuery();
*/
						sql = "SELECT Count(*) as AccountCount ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						rs = pstmt.executeQuery();

						rs.next();
						account_count = rs.getInt(1);
						if( account_count == 0 ) {
							Application.Log.warn("[No approved accounts found]");
							break;
						}else {
							account_count = rs.getInt(1);
						}
						
						sql = "SELECT acct_id, acct_current_bal, acct_type ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_approved = TRUE " ;
						sql += "ORDER BY acct_id ASC";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						rs = pstmt.executeQuery();
						
						acct_initial_deposit_amt = 0f;
						
						account_count = 0;
						while( rs.next() ){
							account_count++;
							if( account_count == 1) {
								Application.Log.info("");
								Application.Log.info("================================================");
								Application.Log.info("              [Approved Account IDs]");
								Application.Log.info("================================================");
							}
							acct_id = rs.getInt(1);
							acct_initial_deposit_amt = rs.getFloat(2);
							acct_type = rs.getString(3);

							String acct_type_desc = "Unknown";
							if( acct_type.equals("C")) {
								acct_type_desc = "Checking";
							}else if( acct_type.equals("S")) {
								acct_type_desc = "Savings ";
							}
							
							Application.Log.info( "Account ID: " + acct_id + "  Type: " + acct_type_desc + "  Balance = " + acct_initial_deposit_amt );
						}
						if( account_count == 0) {
							Application.Log.warn("[CustomerMenu] [No Approved Accounts found]");
						}
					} catch (SQLException e) {
						Application.Log.error("[CustomerMenu] SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				case 4:
					try {

						try {
							Application.Log.info("Enter Account ID to Deposit to:");
							acct_id = Integer.parseInt(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) { 
								Application.Log.warn("[ERROR] [CustomerMenu]: Account ID must be a number");
							}else { 
								Application.Log.error("[CustomerMenu]: " + e.getMessage()); }
							break;
						}
							
						try {
							Application.Log.info("Enter Amount to Deposit:");
							deposit_amt = Float.parseFloat(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) {
								Application.Log.warn("[ERROR] [CustomerMenu]: Deposit Amount must be a number");
							}else {
								Application.Log.error("[CustomerMenu]: " + e.getMessage());
							}
							break;
						}
						
						connection.setAutoCommit(false);

						sql = "SELECT count(*) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_id = ? AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						pstmt.setInt(3, acct_id);
						rs = pstmt.executeQuery();

						rs.next();
						account_count = rs.getInt(1);
						if( account_count == 0) {
							Application.Log.warn("[Deposit Rejected.  Account " + "(" + acct_id + ") is not an approved account]");
							break;
						}

// depositAccount creates a transaction
						accountService.depositAccount(acct_id, cust_bank_id, deposit_amt, "XX");
						
						connection.commit();
						
						break;
					} catch (SQLException e) {
						Application.Log.error("[CustomerMenu] SQLException: " + e.getMessage());
					}
					
					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.error("[CustomerMenu. rollback] SQLException: " + e.getMessage());
						break;
					}
					Application.Log.info("[Deposit Complete]");
					Application.Log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

					break;
					
				case 5:
					try {
						
						try {
							Application.Log.info("Enter Account ID to Withdraw from:");
							acct_id = Integer.parseInt(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) {
								Application.Log.warn("[ERROR] [CustomerMenu]: Account ID must be a number");
							}else {
								Application.Log.error("[CustomerMenu]: " + e.getMessage());
							}
							break;
						}
							
						try {
							Application.Log.info("Enter Amount to Withdraw:");
							withdraw_amt = Float.parseFloat(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) {
								Application.Log.warn("[ERROR] [CustomerMenu]: Deposit Amount must be a number");
							}else {
								Application.Log.error("[CustomerMenu]: " + e.getMessage());
							}
							break;
						}
						
						
						connection.setAutoCommit(false);
						sql = "SELECT count(*) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_id = ? AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						pstmt.setInt(3, acct_id);
						rs = pstmt.executeQuery();

						rs.next();
						account_count = rs.getInt(1);
						if( account_count == 0) {
							Application.Log.warn("[Withddrawal Rejected.  Account " + "(" + acct_id + ") is not an approved account]");
							break;
						}

// withdrawAccount creates a transaction
						accountService.withdrawAccount(acct_id, cust_bank_id, withdraw_amt, "XX");

						connection.commit();
						break;
					} catch (SQLException e) {
						Application.Log.error("{CustomerMenu] SQLException: " + e.getMessage());
					}

					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.error("{CustomerMenu, rollback] SQLException: " + e.getMessage());
					}
					break;
					
				case 6:
					int acct_id_xfer_from = 0, acct_id_xfer_to = 0;
					int cust_id_to = 0;
					float xfer_amt = 0;
					
					try {

						try {
							Application.Log.info("Enter Account ID to Transfer FROM:");
							acct_id_xfer_from = Integer.parseInt(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) { 
								Application.Log.warn("[ERROR] [CustomerMenu]: Transfer FROM Account ID must be a number");
							}else { 
								Application.Log.error("[CustomerMenu]: " + e.getMessage()); }
							break;
						}
						
						try {
							Application.Log.info("Enter Customer ID to Transfer TO:");
							cust_id_to = Integer.parseInt(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) { 
								Application.Log.warn("[ERROR] [CustomerMenu]: Transfer TO Customer ID must be a number");
							}else { 
								Application.Log.error("[CustomerMenu]: " + e.getMessage()); }
							break;
						}
						
						try {
							Application.Log.info("Enter Account ID to Transfer TO:");
							acct_id_xfer_to = Integer.parseInt(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) { 
								Application.Log.warn("[ERROR] [CustomerMenu]: Transfer TO Account ID must be a number");
							}else { 
								Application.Log.error("[CustomerMenu]: " + e.getMessage()); }
							break;
						}
						
						try {
							Application.Log.info("Enter Amount to Transfer:");
							xfer_amt = Float.parseFloat(Application.sc.nextLine());
						}catch (NumberFormatException e) {
							if( e.getMessage().contains("For input string:")) { 
								Application.Log.warn("[ERROR] [CustomerMenu]: Transfer Amount must be a number");
							}else { 
								Application.Log.error("[CustomerMenu]: " + e.getMessage()); }
							break;
						}
						
						connection.setAutoCommit(false);
						
						sql = "SELECT count(*) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_id = ? AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						pstmt.setInt(3, acct_id_xfer_from);
						rs = pstmt.executeQuery();

						account_count = 0;
						
						rs.next();
						account_count = rs.getInt(1);
						
						if( account_count == 0) {
							Application.Log.warn("[Transfer Rejected.  Account " + "(" + acct_id + ") is not an approved account to Transfer FROM]");
							connection.rollback();
							break;
						}

						accountService.withdrawAccount(acct_id_xfer_from, cust_bank_id, xfer_amt, "TF");
						transactionService.createTransaction( acct_id_xfer_from, cust_bank_id, "TF", cust_id, acct_id_xfer_from, cust_id_to, acct_id_xfer_to, xfer_amt, "TF" );
						
						sql = "SELECT count(*) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_id = ? AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id_to);
						pstmt.setInt(3, acct_id_xfer_to);
						rs = pstmt.executeQuery();

						account_count = 0;

						rs.next();
						account_count = rs.getInt(1);

						if( account_count == 0) {
							Application.Log.warn("[Transfer Rejected.  Account " + "(" + acct_id + ") is not an approved account to transfer TO]");
							connection.rollback();
							break;
						}
						accountService.depositAccount(acct_id_xfer_to, cust_bank_id, xfer_amt, "TT");
						transactionService.createTransaction( acct_id_xfer_to, cust_bank_id, "TT", cust_id, acct_id_xfer_from, cust_id_to, acct_id_xfer_to, xfer_amt, "TT" );

						connection.commit();

						break;
					} catch (SQLException e) {
						Application.Log.error("[CustomerMenu] SQLException: " + e.getMessage());
					}
					
					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.error("[CustomerMenu] SQLException: " + e.getMessage());
					}
					break;
					
				case 7:
					Application.Log.info("Receive under construction.");
					break;
					
				default:
					Application.Log.info("[No valid choice entered, please try again]");
					break;
			}
		} while (choice != 1);
/*
	public String getCreateAccountInput() {
//		Application.Log.info("Enter a team name: ");
//		String teamName = Application.sc.nextLine();

//		return teamName;
		return "getCreateAccountInput";
	}
*/	
	}
}	
