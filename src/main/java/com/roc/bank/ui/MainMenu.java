// NOT USED
// LOGIN MENU IS THE TOP LEVEL MENU NOW
/*
package com.roc.bank.ui;

import org.apache.log4j.Logger;

import com.roc.bank.main.Application;

public class MainMenu implements Menu{
	
	public static Logger Log=Logger.getLogger(Application.class);
	
	public MainMenu() {
		super();
	}

	@Override
	public void display() {
		int choice = 0;
		do {
			Log.info("BANK MAIN MENU");
			Log.info("==============");
			Log.info("1.) Exit Application");
			Log.info("2.) Customer Menu");
			Log.info("3.) Employee Menu");
			Log.info("Enter a choice between 1 and 3");
			
			try {
				choice = Integer.parseInt(Menu.sc.nextLine());
			} catch (NumberFormatException e) {
			}
			
			switch (choice) {
				case 1:
					break;
				case 2:
					Menu customerMenu = new CustomerMenu();
					customerMenu.display();
					break;
				case 3:
//					Menu employeeMenu = new EmployeeMenu();
//					employeeMenu.display();
					break;
				default:
					Log.info("No valid choice entered, please try again");
			}
			
		} while (choice != 1);
	}
	
	public void display(int cust_bank_id, int cust_id) {}
}
*/