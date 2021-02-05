package com.roc.bank.models;

public class Customer {
//	public static int cust_bank_id;
	public static String cust_fname;
	public static String cust_lname;
	public static String cust_log_id;
	public static String cust_log_pw;
	
	public Customer() {
		super();
	}

	public Customer(String cust_fname, String cust_lname, String cust_log_id, String cust_log_pw ) {
//		this.cust_bank_id = cust_bank_id;
		this.cust_fname = cust_fname;
		this.cust_lname = cust_lname;
		this.cust_log_id = cust_log_id;
		this.cust_log_pw = cust_log_pw;
	}
}
