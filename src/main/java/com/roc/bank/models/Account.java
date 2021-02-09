package com.roc.bank.models;

public class Account {
	private static int acct_id;
	private static int cust_bank_id;
	private static int cust_id;
	private static float acct_initial_deposit_amt;
	private static float acct_deposit_amt;
	private static float acct_withdraw_amt;
	private static String acct_type;
	
	public Account() {
		super();
	}

	public static Account initializeAccount(int cust_bank_id, int cust_id, float acct_initial_deposit_amt, String acct_type  ) {
		Account.setAcct_bank_id(cust_bank_id);
		Account.setCust_id(cust_id);
		Account.setAcct_initial_deposit_amt(acct_initial_deposit_amt);
		Account.setAcct_type(acct_type);
		
		Account account = new Account();
		return account;
	}
	
	public static Account initializeDepositAccount(int acct_id, int cust_bank_id, float acct_deposit_amt ) {
		Account.setAcct_id(acct_id);
		Account.setAcct_bank_id(cust_bank_id);
		Account.setAcct_deposit_amt(acct_deposit_amt);

		Account account = new Account();
		return account;
	}
	
	public static Account initializeWithdrawAccount(int acct_id, int cust_bank_id, float acct_withdraw_amt  ) {
		Account.setAcct_id(acct_id);
		Account.setAcct_bank_id(cust_bank_id);
		Account.setAcct_withdraw_amt(acct_withdraw_amt);

		Account account = new Account();
		return account;
	}
	
	public static float getAcct_withdraw_amt() { return acct_withdraw_amt; }
	public static void setAcct_withdraw_amt(float acct_withdraw_amt) { Account.acct_withdraw_amt = acct_withdraw_amt; }

	public static int getAcct_id() { return acct_id; }
	public static void setAcct_id(int acct_id) { Account.acct_id = acct_id; }

	public static float getAcct_deposit_amt() { return acct_deposit_amt; }
	public static void setAcct_deposit_amt(float acct_deposit_amt) { Account.acct_deposit_amt = acct_deposit_amt; }

	public static int getAcct_bank_id() { return cust_bank_id; }
	public static void setAcct_bank_id(int cust_bank_id) { Account.cust_bank_id = cust_bank_id; }

	public static int getCust_id() { return cust_id; }
	public static void setCust_id(int cust_id) { Account.cust_id = cust_id; }

	public static float getAcct_initial_deposit_amt() { return acct_initial_deposit_amt; }
	public static void setAcct_initial_deposit_amt(float acct_initial_deposit_amt) { Account.acct_initial_deposit_amt = acct_initial_deposit_amt; }

	public static String getAcct_type() { return acct_type; }
	public static void setAcct_type(String acct_type) { Account.acct_type = acct_type; }
}
