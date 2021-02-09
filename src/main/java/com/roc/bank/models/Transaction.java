package com.roc.bank.models;

//import java.util.Date;

public class Transaction {
	private static int tran_acct_id;
	private static int tran_bank_id;
//	private static Date tran_date;
	private static String tran_type;
	private static int tran_xfer_from_cust_id;
	private static int tran_xfer_from_acct_id;
	private static int tran_xfer_to_cust_id;
	private static int tran_xfer_to_acct_id;
	private static float tran_amt;
	
	public Transaction() {
		super();
	}

	public static Transaction initializeTransaction( int tran_acct_id, int tran_bank_id, String tran_type, int tran_xfer_from_cust_id, int tran_xfer_from_acct_id, int tran_xfer_to_cust_id, int tran_xfer_to_acct_id, float tran_amt ) {
		Transaction.setTran_acct_id(tran_acct_id);
		Transaction.setTran_bank_id(tran_bank_id);
		Transaction.setTran_type(tran_type);
		Transaction.setTran_xfer_from_cust_id(tran_xfer_from_cust_id);
		Transaction.setTran_xfer_from_acct_id(tran_xfer_from_acct_id);
		Transaction.setTran_xfer_to_cust_id(tran_xfer_to_cust_id);
		Transaction.setTran_xfer_to_acct_id(tran_xfer_to_acct_id);
		Transaction.setTran_bank_id(tran_bank_id);
		Transaction.setTran_amt(tran_amt);
		
		Transaction transaction = new Transaction();
		return transaction;
	}
	
	public static int getTran_acct_id() { return tran_acct_id; }
	public static void setTran_acct_id(int tran_acct_id) { Transaction.tran_acct_id = tran_acct_id; }

	public static int getTran_bank_id() { return tran_bank_id; }
	public static void setTran_bank_id(int tran_bank_id) { Transaction.tran_bank_id = tran_bank_id; }

//	public static Date getTran_date() { return tran_date; }
//	public static void setTran_date(Date tran_date) { Transaction.tran_date = tran_date; }

	public static String getTran_type() { return tran_type; }
	public static void setTran_type(String tran_type) { Transaction.tran_type = tran_type; }

	public static int getTran_xfer_from_cust_id() { return tran_xfer_from_cust_id; }
	public static void setTran_xfer_from_cust_id(int tran_xfer_from_cust_id) { Transaction.tran_xfer_from_cust_id = tran_xfer_from_cust_id; }

	public static int getTran_xfer_from_acct_id() { return tran_xfer_from_acct_id; }
	public static void setTran_xfer_from_acct_id(int tran_xfer_from_acct_id) { Transaction.tran_xfer_from_acct_id = tran_xfer_from_acct_id; }

	public static int getTran_xfer_to_cust_id() { return tran_xfer_to_cust_id; }
	public static void setTran_xfer_to_cust_id(int tran_xfer_to_cust_id) { Transaction.tran_xfer_to_cust_id = tran_xfer_to_cust_id; }

	public static int getTran_xfer_to_acct_id() { return tran_xfer_to_acct_id; }
	public static void setTran_xfer_to_acct_id(int tran_xfer_to_acct_id) { Transaction.tran_xfer_to_acct_id = tran_xfer_to_acct_id; }
	
	public static float getTran_amt() { return tran_amt; }
	public static void setTran_amt(float tran_amt) { Transaction.tran_amt = tran_amt; }
}
