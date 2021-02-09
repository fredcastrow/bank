package com.roc.bank.models;

public class Customer {
//	public static int cust_bank_id;
	private static int cust_bank_id;
	private static String cust_fname;
	private static String cust_lname;
	private static String cust_log_id;
	private static String cust_log_pw;
	private static String cust_email;
	
	public Customer() {
		super();
	}

	public static Customer initializeCustomer(int cust_bank_id, String cust_fname, String cust_lname, String cust_log_id, String cust_log_pw, String cust_email ) {
		Customer.setCust_bank_id(cust_bank_id);
		Customer.setCust_fname(cust_fname);
		Customer.setCust_lname(cust_lname);
		Customer.setCust_log_id(cust_log_id);
		Customer.setCust_log_pw(cust_log_pw);
		Customer.setCust_email(cust_email);

		Customer customer = new Customer();
		return customer;
	}

	public static int getCust_bank_id() {return cust_bank_id;}
	public static void setCust_bank_id(int cust_bank_id) {Customer.cust_bank_id = cust_bank_id;}

	public static String getCust_fname() {return cust_fname;}
	public static void setCust_fname(String cust_fname) {Customer.cust_fname = cust_fname;}

	public static String getCust_lname() {return cust_lname;}
	public static void setCust_lname(String cust_lname) {Customer.cust_lname = cust_lname;}

	public static String getCust_log_id() {return cust_log_id;}
	public static void setCust_log_id(String cust_log_id) {Customer.cust_log_id = cust_log_id;}

	public static String getCust_log_pw() {return cust_log_pw;}
	public static void setCust_log_pw(String cust_log_pw) {Customer.cust_log_pw = cust_log_pw;}

	public static String getCust_email() { return cust_email; }
	public static void setCust_email(String cust_email) { Customer.cust_email = cust_email; }
}
