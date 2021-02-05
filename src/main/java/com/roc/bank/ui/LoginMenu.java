package com.roc.bank.ui;

import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.roc.bank.exceptions.DatabaseConnectionException;
import com.roc.bank.main.Application;
import com.roc.bank.util.ConnectionUtil;

import java.sql.*;

import com.roc.bank.services.CustomerService;

public class LoginMenu implements Menu{
	public void display(){}
	
	public static Logger Log=Logger.getLogger(Application.class);

	public CustomerService customerService;
	public LoginMenu() {
		customerService = new CustomerService();
	}
	
//	@Override
	public void display(int int1, int int2){
		int choice = 0;

		int cust_bank_id = 0;
		int cust_id = 0;
		int emp_bank_id = 0;
		int emp_id = 0;

		do {
			Log.info("");
			Log.info("=================");
			Log.info("[BANK LOGIN MENU]");
			Log.info("=================");
			Log.info("1.) Exit Application");
			Log.info("2.) Customer Login");
			Log.info("3.) Employee Login");
			Log.info("4.) Create Customer Login");
			Log.info("[Enter a choice between 1 and 4]");
			
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			
			switch (choice) {
				case 1:
					break;

				case 2:
					String cust_log_id = new String();
					String cust_log_pw = new String();
					
					ResultSet rs;
					Connection connection = null;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Log.info("DatabaseConnectionException: " + e.getMessage());
					}
					
					String cust_fname = new String();
					String cust_lname = new String();

					try {
						Log.info("Enter Login ID:");
						cust_log_id = Menu.sc.nextLine();
						Log.info("Enter Login Password:");
						cust_log_pw = Menu.sc.nextLine();
						
						String sql = "SELECT cust_fname, cust_lname, cust_id, cust_bank_id ";
						sql += "FROM bank.customer ";
						sql += "WHERE cust_log_id = ? AND cust_log_pw = ?";
						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setString(1, cust_log_id);
						pstmt.setString(2, cust_log_pw);
						rs = pstmt.executeQuery();
						
						if( !rs.next() ) {
							Log.info("[Invalid ID or Password]");
							break;
						} else {
							cust_fname = rs.getString(1);
							cust_lname = rs.getString(2);
							cust_id = rs.getInt(3);
							cust_bank_id = rs.getInt(4);
							
							Log.info("[Hello customer " + cust_fname + " " + cust_lname + "]");

							Menu customerMenu = new CustomerMenu();
							customerMenu.display( cust_bank_id, cust_id );
						}
					} catch (SQLException e) {
						Log.info("SQLException: " + e.getMessage());
//						throw new SQLException("An issue occurred when trying to connect to the database");
					}
					break;
				case 3:
					boolean employee_found = false;

					String emp_log_id = new String();
					String emp_log_pw = new String();
					connection = null;
					
					try {
						connection = ConnectionUtil.getConnection();
					} catch (DatabaseConnectionException e) {
						Log.info("DatabaseConnectionException: " + e.getMessage());
					}
					
					String emp_fname = new String();
					String emp_lname = new String();

					try {
						Log.info("Enter Login ID:");
						emp_log_id = (String) Menu.sc.nextLine();
						Log.info("Enter Login Password:");
						emp_log_pw = (String) Menu.sc.nextLine();

						String sql = "SELECT emp_fname, emp_lname, emp_id, emp_bank_id ";
						sql += "FROM bank.employee ";
						sql += "WHERE emp_log_id = ? AND emp_log_pw = ?";
						PreparedStatement pstmt = connection.prepareStatement(sql);
						pstmt.setString(1, emp_log_id);
						pstmt.setString(2, emp_log_pw);
						rs = pstmt.executeQuery();
						
						employee_found = rs.next();
						if( !employee_found) {
							Log.info("[Invalid ID or Password]");
							break;
						}
						
						emp_fname = rs.getString(1);
						emp_lname = rs.getString(2);
						emp_id = rs.getInt(3);
						emp_bank_id = rs.getInt(4);
						
						Log.info("[Hello employee " + emp_fname + " " + emp_lname + "]");
					} catch (SQLException e) {
						Log.info("SQLException: " + e.getMessage());
//						throw new SQLException("An issue occurred when trying to connect to the database");
					}

					if( employee_found ) {
						Menu employeeMenu = new EmployeeMenu();
						employeeMenu.display( emp_id, emp_bank_id );
					} else {
						Log.info("[Invalid ID or Password]");
					}
					break;

				case 4:
//					String cust_fname, cust_lname, cust_log_id, cust_log_pw;
					
					Log.info("");
					Log.info("==============");
					Log.info("[NEW Customer]");
					Log.info("==============");
					Log.info("Enter Customer First Name:");
					cust_fname = sc.nextLine();
					Log.info("Enter Customer Last Name:");
					cust_lname = sc.nextLine();
					Log.info("Enter Customer Log ID:");
					cust_log_id = sc.nextLine();
					Log.info("Enter Customer Log Password:");
					cust_log_pw = sc.nextLine();
					
					customerService.createCustomer(cust_fname, cust_lname, cust_log_id, cust_log_pw);

					Log.info("[Customer " + cust_fname + " " + cust_lname +" created]");
					Log.info("++++++++++++++++++++++++++++++");
					break;
					
				default:
					Log.info("[No valid choice entered, please try again]");
			}
		} while (choice != 1);
	}
}
