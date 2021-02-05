package com.roc.bank.ui;

import org.apache.log4j.Logger;
import java.sql.*;

import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.main.Application;
import com.roc.bank.util.ConnectionUtil;

public class EmployeeMenu implements Menu{

	@Override
	public void display(){}

	@Override
	public void display( int emp_id, int emp_bank_id ) {
		Logger Log = Logger.getLogger(Application.class);
		
		ResultSet rs;
		Connection connection = null;

		int choice = 0;
		do {
			Log.info("");
			Log.info("===============");
			Log.info("[EMPLOYEE MENU]");
			Log.info("===============");
			Log.info("1.) Back");
			Log.info("2.) List Customers");
			Log.info("3.) List Accounts for a Customer");
			Log.info("4.) Approve Account for a Customer");

			Log.info("5.) Apply for a new Account");
			Log.info("6.) View Account Balance");
			Log.info("7.) Make a Deposit");
			Log.info("8.) Make a Withdrawal");
			Log.info("9.) Transfer Money");
			Log.info("10.) Receive Money");
			Log.info("[Enter a choice between 1 and 10]");

			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
				Log.info("NumberFormatException: " + e.getMessage());
			}
			
			switch (choice) {
				case 1:
					break;
					
				case 2:
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Log.info("DatabaseConnectionException: " + e.getMessage());
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
								Log.info("");
								Log.info("===============");
								Log.info("[Customer List]");
								Log.info("===============");
							}
							cust_id = rs.getInt(1);
							cust_fname = rs.getString(2);
							cust_lname = rs.getString(3);
							Log.info("[" +cust_id + "] " + cust_fname + " " + cust_lname);
						}
						if( customer_count == 0 ) {
							Log.info("[No Customers found for bank [" + emp_bank_id + "]]");
						}
						
					} catch (SQLException e) {
						Log.info("SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
				case 3:
					int cust_id = 0, acct_id = 0;
					String cust_fname = "", cust_lname = "";
					float acct_initial_deposit_amt = 0;
					boolean acct_approved;
					int account_count = 0;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Log.info("DatabaseConnectionException: " + e.getMessage());
					}

					Log.info("Enter Customer ID:");
					cust_id = Integer.parseInt(sc.nextLine());

					try {
						String sql = "SELECT cust_id, cust_fname, cust_lname, acct_id, acct_initial_deposit_amt, acct_approved, acct_approved ";
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
								Log.info("");
								Log.info("=======================");
								Log.info("[Customer Account List]");
								Log.info("=======================");
							}
							
							cust_id = rs.getInt(1);
							cust_fname = rs.getString(2);
							cust_lname = rs.getString(3);
							acct_id = rs.getInt(4);
							acct_initial_deposit_amt = rs.getInt(5);
							acct_approved = rs.getBoolean(6);
							Log.info("[" +cust_id + "] " + cust_fname + " " + cust_lname + " [" + acct_id + "] " + acct_initial_deposit_amt + " (" + acct_approved + ")");
						}
						
						if( account_count == 0 ) {
							Log.info("[No Accounts found for Customer [" + cust_id + "]]");
						}
						
					} catch (SQLException e) {
						Log.info("SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				case 4:
					int acct_cust_own_id = 0;
					int acct_count = 0;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Log.info("DatabaseConnectionException: " + e.getMessage());
					}

					Log.info("Enter Customer ID:");
					acct_cust_own_id = Integer.parseInt(sc.nextLine());
					Log.info("Enter Customer Account ID:");
					acct_id = Integer.parseInt(sc.nextLine());
					
					try {
						String sql = "SELECT acct_id, acct_initial_deposit_amt ";
								sql += "FROM bank.account ";
								sql += "WHERE acct_id = ? AND acct_owner_type = 'C' AND acct_bank_id = ? AND acct_cust_own_id = ? AND acct_approved = FALSE";

						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setInt(1, acct_id);
						pstmt.setInt(2, emp_bank_id);
						pstmt.setInt(3, acct_cust_own_id);

						Log.info("before check for NOT approved: " + emp_id + " " + acct_id + " " + emp_bank_id + " " + acct_cust_own_id);

						rs = pstmt.executeQuery();
						
						rs.next();
						acct_count = rs.getInt(1);
						acct_initial_deposit_amt = rs.getFloat(2); 
						
						if( acct_count == 0 ) {
							Log.info( "[That is not an Account to be approved]");
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
							
							Log.info("before update: " + emp_id + " " + acct_id + " " + emp_bank_id + " " + acct_cust_own_id);
							
							acct_count = pstmt.executeUpdate();

/*							

// set autocommit off							
							sql = "UPDATE bank.account SET ";
							sql += "acct_approved = TRUE, ";
							sql += "acct_approving_emp_id = " + emp_id + ", ";
							sql += "acct_approval_date = current_timestamp(0), ";
							sql += "acct_current_bal = acct_initial_deposit_amt ";
							sql += "WHERE acct_id = " + acct_id + " AND acct_owner_type = 'C' AND acct_bank_id = " + emp_bank_id + " AND acct_cust_own_id = " + acct_cust_own_id + " AND acct_approved IS NULL";
							
							Statement statement = connection.createStatement();
							statement.executeUpdate(sql);
 */
							
// create a Transaction Model, Service, DAO and DALImpl
							
							sql = "INSERT INTO bank.transaction ( tran_acct_id, tran_bank_id, tran_date, tran_type, tran_amt) ";
							sql += "VALUES (?, ?, current_timestamp(0), 'O', ?)";
							pstmt = connection.prepareStatement(sql);
					// change this to use getter methods		
							pstmt.setInt( 1, acct_id );
							pstmt.setInt( 2, emp_bank_id );
							pstmt.setFloat(3, acct_initial_deposit_amt);

							Log.info("before insert: " + emp_id + " " + acct_id + " " + emp_bank_id + " " + acct_cust_own_id);
							account_count = pstmt.executeUpdate();
// commit
// set autocommit on?
						}
					} catch (SQLException e) {
						Log.info("SQLException: " + e.getMessage());
		//				throw new SQLException("An issue occurred when trying to connect to.
					}
					break;
					
				default:
					Log.info("[No valid choice entered, please try again]");

			}
		} while (choice != 1);
	}
}	