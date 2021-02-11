package com.roc.bank.ui;

import com.roc.bank.main.Application;
import com.roc.bank.util.ConnectionUtil;
import com.roc.bank.exceptions.DatabaseConnectionException;

import java.sql.*;
import java.time.LocalDate;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.TimeZone;

public class EmployeeMenu implements Menu{

	@Override
	public void display(){}

	@Override
	public void display( int emp_id, int emp_bank_id ) {
		ResultSet rs = null;
		Connection connection = null;

		int account_count = 0;
		float acct_initial_deposit_amt = 0;

		int choice = 0;
		do {
			Application.Log.info("");
			Application.Log.info("===============");
			Application.Log.info("[EMPLOYEE MENU]");
			Application.Log.info("===============");
			Application.Log.info("1.) Back");
			Application.Log.info("2.) List Customers");
			Application.Log.info("3.) List Accounts for a Customer");
			Application.Log.info("4.) Approve Account for a Customer");
			Application.Log.info("5.) List Transaction Log");
/*
			Application.Log.info("5.) Apply for a new Account");
			Application.Log.info("6.) View Account Balance");
			Application.Log.info("7.) Make a Deposit");
			Application.Log.info("8.) Make a Withdrawal");
			Application.Log.info("9.) Transfer Money");
			Application.Log.info("10.) Receive Money");
*/			
			Application.Log.info("[Enter a choice between 1 and 5]");

			try {
				choice = Integer.parseInt(Application.sc.nextLine());
			} catch (NumberFormatException e) {
				Application.Log.info("NumberFormatException: " + e.getMessage());
			}
			
			switch (choice) {
				case 1:
					break;
					
				case 2:
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Application.Log.info("DatabaseConnectionException: " + e.getMessage());
					}

					try {
						String sql = "SELECT cust_id, cust_fname, cust_lname ";
						sql += "FROM bank.customer ";
						sql += "WHERE cust_bank_id = ?";
						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, emp_bank_id);
						rs = pstmt.executeQuery();
						int customer_count = 0;
						
						int cust_id = 0;
						String cust_fname = "", cust_lname = "";
						
						while( rs.next() ) {
							customer_count++;
							if( customer_count == 1 ) {
								Application.Log.info("");
								Application.Log.info("===============");
								Application.Log.info("[Customer List]");
								Application.Log.info("===============");
							}
							cust_id = rs.getInt(1);
							cust_fname = rs.getString(2);
							cust_lname = rs.getString(3);
							Application.Log.info("[" +cust_id + "] " + cust_fname + " " + cust_lname);
						}
						if( customer_count == 0 ) {
							Application.Log.info("[No Customers found for bank [" + emp_bank_id + "]]");
						}
						
					} catch (SQLException e) {
						Application.Log.info("[EmployeeMenu] SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
				case 3:
					int cust_id = 0, acct_id = 0;
					String cust_fname = "", cust_lname = "";
					boolean acct_approved;
					String acct_status, acct_type;
					account_count = 0;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Application.Log.info("DatabaseConnectionException: " + e.getMessage());
					}

					Application.Log.info("Enter Customer ID:");
					cust_id = Integer.parseInt(Application.sc.nextLine());

					try {
						String sql = "SELECT cust_id, cust_fname, cust_lname, acct_id, acct_initial_deposit_amt, acct_approved, acct_type ";
								sql += "FROM bank.customer, bank.account ";
								sql += "WHERE acct_cust_own_id = cust_id ";
								sql += "AND cust_bank_id = acct_bank_id AND acct_owner_type = 'C' ";
								sql += "AND cust_id = ? AND cust_bank_id = ?";
						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, cust_id);
						pstmt.setInt(2, emp_bank_id);
						rs = pstmt.executeQuery();
						
						while( rs.next() ) {
							account_count ++;
							if( account_count == 1 ) {
								Application.Log.info("");
								Application.Log.info("===========[Customer Account List]=========");
								Application.Log.info("CustID CustName AcctID AcctBal AcctApproved AcctType");
								Application.Log.info("-------------------------------------------");
							}
							
							cust_id = rs.getInt(1);
							cust_fname = rs.getString(2);
							cust_lname = rs.getString(3);
							acct_id = rs.getInt(4);
							acct_initial_deposit_amt = rs.getInt(5);
							acct_approved = rs.getBoolean(6);
							acct_type = rs.getString(7);

							if( acct_approved) {
								acct_status = "Approved";
							}else{
								acct_status = "NOT Approved";
							}
							
							Application.Log.info("[" +cust_id + "]    " + cust_fname + " " + cust_lname + "    [" + acct_id + "]   " + acct_initial_deposit_amt + "    [" + acct_status + "]" + "    [" + acct_type + "]");
						}
						Application.Log.info("===========================================");
						
						if( account_count == 0 ) {
							Application.Log.info("[No Accounts found for Customer [" + cust_id + "]]");
						}
						
					} catch (SQLException e) {
						Application.Log.info("[EmployeeMenu] SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				case 4:
					int acct_cust_own_id = 0;
					account_count = 0;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Application.Log.info("DatabaseConnectionException: " + e.getMessage());
					}

					Application.Log.info("Enter Customer ID:");
					acct_cust_own_id = Integer.parseInt(Application.sc.nextLine());
					Application.Log.info("Enter Customer Account ID:");
					acct_id = Integer.parseInt(Application.sc.nextLine());

					try {
						connection.setAutoCommit(false);
						String sql = "SELECT acct_initial_deposit_amt ";
								sql += "FROM bank.account ";
								sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = FALSE";
								
						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, acct_id);
						pstmt.setInt(2, emp_bank_id);
						pstmt.setInt(3, acct_cust_own_id);

						rs = pstmt.executeQuery();
						while( rs.next() ) {
							account_count ++; 
							acct_initial_deposit_amt = rs.getFloat(1); 
						}
						
						if( account_count == 0 ) {
							Application.Log.info( "[That is not an Account to be approved]");
							break;
						} else {
							sql = "UPDATE bank.account SET ";
							sql += "acct_approved = TRUE, ";
							sql += "acct_approving_emp_id = ?, ";
							sql += "acct_approval_date = current_timestamp(0), ";
							sql += "acct_current_bal = acct_initial_deposit_amt ";
							sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = FALSE";
							
							pstmt = connection.prepareStatement(sql);
							pstmt.setInt(1, emp_id);
							pstmt.setInt(2, acct_id);
							pstmt.setInt(3, emp_bank_id);
							pstmt.setInt(4, acct_cust_own_id);
							
							account_count = pstmt.executeUpdate();

// create a Transaction Model, Service, DAO and DALImpl
							
							sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_amt) ";
							sql += "VALUES (?, ?, current_timestamp(0), 'O', ?)";
							pstmt = connection.prepareStatement(sql);
					// change this to use getter methods		
							pstmt.setInt( 1, acct_id );
							pstmt.setInt( 2, emp_bank_id );
							pstmt.setFloat(3, acct_initial_deposit_amt);

							account_count = pstmt.executeUpdate();
							
							connection.commit();

							Application.Log.info( "[Account Approved]");

							break;
						}
					} catch (SQLException e) {
						Application.Log.info("{EmployeeMenu] SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					try {
						connection.rollback();
					} catch (SQLException e) {
						Application.Log.info("{EmployeeMenu, rollback] SQLException: " + e.getMessage());
					}
					break;
					
				case 5:
					int transaction_count = 0;
					int tran_id = 0;
					int tran_acct_id = 0;
//					LocalDateTime tran_date;
					String tran_type;
					int tran_transfer_from_cust_id, tran_transfer_to_cust_id;
					int tran_transfer_from_acct_id, tran_transfer_to_acct_id;
					float tran_amt;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Application.Log.info("DatabaseConnectionException: " + e.getMessage());
					}
/*
					Application.Log.info("Enter Customer ID:");
					cust_id = Integer.parseInt(Application.sc.nextLine());
*/					

					try {
						String sql = "SELECT tran_id, tran_acct_id, tran_date, tran_type, tran_xfer_from_cust_id, tran_xfer_from_acct_id, tran_xfer_to_cust_id, tran_xfer_to_acct_id, tran_amt  ";
								sql += "FROM bank.transaction ";
								sql += "ORDER BY tran_id ";
						PreparedStatement pstmt = connection.prepareStatement(sql);
						rs = pstmt.executeQuery();
						
						while( rs.next() ) {
							transaction_count ++;
							if( transaction_count == 1 ) {
								Application.Log.info("");
								Application.Log.info("=========================================[Transaction List]============================================");
								Application.Log.info("TranID AcctID          TranDate          TranType       XferFrom(user, acct) XferTo(user, acct) TranAmt");
								Application.Log.info("-------------------------------------------------------------------------------------------------------");
							}
							
							tran_id = rs.getInt(1);
							tran_acct_id = rs.getInt(2);
							
							Timestamp tran_date;
							tran_date = rs.getTimestamp(3);

//							DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//							LocalDateTime localDateTime = rs.getObject(3, LocalDateTime.class);
//							localDateTime.parse( localDateTime., localDateTime));
							
							tran_type = rs.getString(4);
							tran_transfer_from_cust_id = rs.getInt(5);
							tran_transfer_from_acct_id = rs.getInt(6);
							tran_transfer_to_cust_id = rs.getInt(7);
							tran_transfer_to_acct_id = rs.getInt(8);
							tran_amt = rs.getFloat(9);

/*							
							TimeZone tz = TimeZone.getTimeZone("UTC");
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); // Quoted "Z" to indicate UTC, no timezone offset
							df.setTimeZone(tz);
							String nowAsISO = df.format(tran_date);
*/

/*							
							java.util.Date aDate = new java.util.Date();
							java.util.Date aTime = new java.util.Date();
							
//							LocalDateTime myDateObj = LocalDateTime.now();
							LocalDateTime myDateObj = LocalDateTime.of(tran_date);
							DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//						    DateFormat myDateObj = new DateTimeFormatter();
							String formattedDate = myDateObj.format(myFormatObj);
*/							
							Application.Log.info(" [" +tran_id + "]      " + tran_acct_id + "     " + tran_date + "    [" + tran_type + "]             " + tran_transfer_from_cust_id + " " + tran_transfer_from_acct_id + "                  " + tran_transfer_to_cust_id + " " + tran_transfer_to_acct_id + "             " + tran_amt);
						}
						Application.Log.info("=======================================================================================================");
						
						if( transaction_count == 0 ) {
							Application.Log.info("[No Transactions found]");
						}
						
					} catch (SQLException e) {
						Application.Log.info("[EmployeeMenu] SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				default:
					Application.Log.info("[No valid choice entered, please try again]");

			}
		} while (choice != 1);
	}
}	
