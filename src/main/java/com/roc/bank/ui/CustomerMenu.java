package com.roc.bank.ui;

import com.roc.bank.main.Application;

import com.roc.bank.services.AccountService;
import com.roc.bank.services.CustomerService;
import com.roc.bank.util.ConnectionUtil;

//import jdk.internal.org.jline.utils.Log;

import com.roc.bank.exceptions.DatabaseConnectionException;
import java.sql.*;

public class CustomerMenu implements Menu {
	@Override
	public void display() {}

	public CustomerService customerService;
	public AccountService accountService;

	public CustomerMenu() {
		customerService = new CustomerService();
		accountService = new AccountService();
	}

//	@Override
	public void display(int cust_bank_id, int cust_id) {
		int acct_id = 0;
		ResultSet rs;

		Connection connection = null;
		try {
			connection = ConnectionUtil.getConnection();
		} catch (DatabaseConnectionException e) {
		}
		
		int account_count = 0;
		float withdrawal_amt = 0;
		float deposit_amt = 0;

		String sql;
		PreparedStatement pstmt;

		int choice = 0;
		do {
			Application.Log.info("");
			Application.Log.info("===============");
			Application.Log.info("[CUSTOMER MENU]");
			Application.Log.info("===============");
			Application.Log.info("1.) Back");
			Application.Log.info("2.) Apply for a new Account");
			Application.Log.info("3.) View Approved Account Balances");
			Application.Log.info("4.) Make a Deposit");
			Application.Log.info("5.) Make a Withdrawal");
			Application.Log.info("6.) Transfer Money");
			Application.Log.info("7.) Receive Money");
			Application.Log.info("[Enter a choice between 1 and 7]");

			try {
				choice = Integer.parseInt(Application.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			
			switch (choice) {
				case 1:
					break;
					
				case 2:
//					String account = getCreateAccountInput();
					
					Application.Log.info("Enter Initial Deposit Amount:");
					float acct_initial_deposit_amt = Float.parseFloat(Application.sc.nextLine());
					
					accountService.createAccount(cust_bank_id, cust_id, acct_initial_deposit_amt);
					
					Application.Log.info("[Account Request created:  Check back later to for approval status]");
					Application.Log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

					break;
					
				case 3:
					try {
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
							Application.Log.info("[No approved accounts found]");
							break;
						}else {
							account_count = rs.getInt(1);
						}
						
						sql = "SELECT acct_id, acct_current_bal ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						rs = pstmt.executeQuery();
						
						acct_initial_deposit_amt = 0;
						
						account_count = 0;
						while( rs.next() ){
							account_count++;
							if( account_count == 1) {
								Application.Log.info("");
								Application.Log.info("======================");
								Application.Log.info("[Approved Account IDs]");
								Application.Log.info("======================");
							}
							acct_id = rs.getInt(1);
							acct_initial_deposit_amt = rs.getFloat(2); 
							Application.Log.info("Account ID: " + acct_id + " Balance = " + acct_initial_deposit_amt);
						}
						if( account_count == 0) {
							Application.Log.info("[No Approved Accounts found]");
						}
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				case 4:
					try {
						Application.Log.info("Enter Account ID to Deposit to:");
						try {acct_id = Integer.parseInt(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
						Application.Log.info("Enter Amount to Deposit:");
						try {deposit_amt = Float.parseFloat(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
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
							Application.Log.info("[Deposit Rejected.  Account " + "(" + acct_id + ") is not an approved account]");
							break;
						}
						
						sql = "UPDATE bank.account SET ";
						sql += "acct_current_bal = acct_current_bal + ? ";
						sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = TRUE";
						
						pstmt = connection.prepareStatement(sql);
						pstmt.setFloat( 1, deposit_amt);
						pstmt.setInt( 2, acct_id);
						pstmt.setInt( 3, cust_bank_id );
						pstmt.setInt( 4, cust_id);
						account_count = pstmt.executeUpdate();
						
						sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_amt) ";
						sql += "VALUES (?, ?, current_timestamp(0), 'D', ?)";
						pstmt = connection.prepareStatement(sql);
				// change this to use getter methods		
						pstmt.setInt( 1, acct_id );
						pstmt.setInt( 2, cust_bank_id );
						pstmt.setFloat(3, deposit_amt);
						account_count = pstmt.executeUpdate();
						
						connection.commit();
						
						break;
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
					}
					
					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
					}
					break;
					
				case 5:
					try {
						Application.Log.info("Enter Account ID to Withdraw from:");
						try {acct_id = Integer.parseInt(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
						Application.Log.info("Enter Amount to Withdraw:");
						try {withdrawal_amt = Float.parseFloat(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
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
							Application.Log.info("[Withddrawal Rejected.  Account " + "(" + acct_id + ") is not an approved account]");
							break;
						}
						
						sql = "UPDATE bank.account SET ";
						sql += "acct_current_bal = acct_current_bal - ? ";
						sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = TRUE";
						
						pstmt = connection.prepareStatement(sql);
						pstmt.setFloat( 1, withdrawal_amt);
						pstmt.setInt( 2, acct_id);
						pstmt.setInt( 3, cust_bank_id );
						pstmt.setInt( 4, cust_id);
						
						account_count = pstmt.executeUpdate();
						
						sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_amt) ";
						sql += "VALUES (?, ?, current_timestamp(0), 'W', ?, ?, ?, ?, ?)";
						pstmt = connection.prepareStatement(sql);
				// change this to use getter methods		
						pstmt.setInt( 1, acct_id );
						pstmt.setInt( 2, cust_bank_id );
						
						pstmt.setFloat(3, withdrawal_amt);
	
						account_count = pstmt.executeUpdate();
						connection.commit();
						break;
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
					}

					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
					}
					break;
					
				case 6:
					int acct_id_xfer_from = 0, acct_id_xfer_to = 0;
					int cust_id_to = 0;
					float xfer_amt = 0;
					
					try {
						Application.Log.info("Enter Account ID to Transfer FROM:");
						try {acct_id_xfer_from = Integer.parseInt(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
						Application.Log.info("Enter Customer ID to Transfer TO:");
						try {cust_id_to = Integer.parseInt(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
						Application.Log.info("Enter Account ID to Transfer TO:");
						try {acct_id_xfer_to = Integer.parseInt(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}
						
						Application.Log.info("Enter Amount to Transfer:");
						try {xfer_amt = Float.parseFloat(Application.sc.nextLine());
						} catch (NumberFormatException e) {Application.Log.info("parseInt exception: " + e.getMessage());}

						connection.setAutoCommit(false);
						
						sql = "SELECT count(*) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_id = ? AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id);
						pstmt.setInt(3, acct_id_xfer_from);
						rs = pstmt.executeQuery();

						rs.next();
						account_count = rs.getInt(1);
						if( account_count == 0) {
							Application.Log.info("[Transfer Rejected.  Account " + "(" + acct_id + ") is not an approved account to transfer from]");
							connection.rollback();
							break;
						}
						
						sql = "UPDATE bank.account SET ";
						sql += "acct_current_bal = acct_current_bal - ? ";
						sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = TRUE";
						
						pstmt = connection.prepareStatement(sql);
						pstmt.setFloat( 1, xfer_amt);
						pstmt.setInt( 2, acct_id_xfer_from);
						pstmt.setInt( 3, cust_bank_id );
						pstmt.setInt( 4, cust_id);
						
						account_count = pstmt.executeUpdate();
						
						sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_xfer_from_cust_id, tran_xfer_from_acct_id, tran_xfer_to_cust_id, tran_xfer_to_acct_id, tran_amt ) ";
						sql += "VALUES (?, ?, current_timestamp(0), 'TF', ?, ?, ?, ?, ?)";
						pstmt = connection.prepareStatement(sql);
				// change this to use getter methods		
						pstmt.setInt( 1, acct_id_xfer_from );
						pstmt.setInt( 2, cust_bank_id );
						pstmt.setInt( 3, cust_id );
						pstmt.setInt( 4, acct_id_xfer_from );
						pstmt.setInt( 5, cust_id_to );
						pstmt.setInt( 6, acct_id_xfer_to );
						pstmt.setFloat(7, xfer_amt);
	
						account_count = pstmt.executeUpdate();
						
						sql = "SELECT count(*) ";
						sql += "FROM bank.account ";
						sql += "WHERE acct_bank_id = ? AND acct_cust_own_id = ? AND acct_owner_type = 'C' AND acct_id = ? AND acct_approved = TRUE";
						pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_bank_id);
						pstmt.setInt(2, cust_id_to);
						pstmt.setInt(3, acct_id_xfer_to);
						rs = pstmt.executeQuery();

						rs.next();
						account_count = rs.getInt(1);
						if( account_count == 0) {
							Application.Log.info("[Transfer Rejected.  Account " + "(" + acct_id + ") is not an approved account to transfer to]");
							connection.rollback();
							break;
						}
						
						sql = "UPDATE bank.account SET ";
						sql += "acct_current_bal = acct_current_bal + ? ";
						sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = TRUE";
						
						pstmt = connection.prepareStatement(sql);
						pstmt.setFloat( 1, xfer_amt);
						pstmt.setInt( 2, acct_id_xfer_to);
						pstmt.setInt( 3, cust_bank_id );
						pstmt.setInt( 4, cust_id_to);
						
						
						sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_xfer_from_cust_id, tran_xfer_from_acct_id, tran_xfer_to_cust_id, tran_xfer_to_acct_id, tran_amt ) ";
						sql += "VALUES (?, ?, current_timestamp(0), 'TT', ?, ?, ?, ?, ? )";
						pstmt = connection.prepareStatement(sql);
				// change this to use getter methods		
						pstmt.setInt( 1, acct_id_xfer_to );
						pstmt.setInt( 2, cust_bank_id );
						pstmt.setInt( 3, cust_id );
						pstmt.setInt( 4, acct_id_xfer_from );
						pstmt.setInt( 5, cust_id_to );
						pstmt.setInt( 6, acct_id_xfer_to );
						pstmt.setFloat(7, xfer_amt);
	
						account_count = pstmt.executeUpdate();

						connection.commit();

						break;
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
					}
					
					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.info("SQLException: " + e.getMessage());
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
