package com.roc.bank.ui;

import java.util.Scanner;
import java.sql.SQLException;

public interface Menu {
	public static Scanner sc = new Scanner(System.in);
	
//	public void display() throws SQLException;
	public void display();
	
	public void display(int int1, int int2);
}
