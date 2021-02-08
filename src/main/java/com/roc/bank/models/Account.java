package com.roc.bank.models;

public class Account {
	private static int cust_bank_id;
	private static int cust_id;
	private static float acct_initial_deposit_amt;
	
	public Account() {
		super();
	}

	public static Account initializeAccount(int cust_bank_id, int cust_id, float acct_initial_deposit_amt  ) {
		Account.setAcct_bank_id(cust_bank_id);
		Account.setCust_id(cust_id);
		Account.setAcct_initial_deposit_amt(acct_initial_deposit_amt);
		
		Account account = new Account();
		return account;
	}
	
	public static int getAcct_bank_id() { return cust_bank_id; }
	public static void setAcct_bank_id(int cust_bank_id) { Account.cust_bank_id = cust_bank_id; }

	public static int getCust_id() { return cust_id; }
	public static void setCust_id(int cust_id) { Account.cust_id = cust_id; }

	public static float getAcct_initial_deposit_amt() { return acct_initial_deposit_amt; }
	public static void setAcct_initial_deposit_amt(float acct_initial_deposit_amt) { Account.acct_initial_deposit_amt = acct_initial_deposit_amt; }

}
