package com.roc.bank.models;

public class Account {
	public static int cust_bank_id;
	public static int cust_id;
	public static float acct_initial_deposit_amt;
	
	public Account() {
		super();
	}

	public Account(int cust_bank_id, int cust_id, float acct_initial_deposit_amt) {
		this.cust_bank_id = cust_bank_id;
		this.cust_id = cust_id;
		this.acct_initial_deposit_amt = acct_initial_deposit_amt;
	}

}
