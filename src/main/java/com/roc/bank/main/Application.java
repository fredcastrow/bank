package com.roc.bank.main;

import org.apache.log4j.Logger;

//import com.roc.bank.ui.MainMenu;
import com.roc.bank.ui.LoginMenu;
import com.roc.bank.ui.Menu;

public class Application {

	public static Logger Log=Logger.getLogger(Application.class);
	
	public static void main(String[] args) {
		Log.info("================================");
		Log.info("Welcome to the Bank Application!");
		Log.info("================================");
		
//		Invoke our MainMenu's display method
//		Menu mainMenu = new MainMenu();
//		mainMenu.display();
		
		Menu loginMenu = (Menu) new LoginMenu();
//		loginMenu.display();
		int int1=0, int2=-0;
		loginMenu.display(int1, int2);
		
		Menu.sc.close();
		Log.info("Goodbye!");
	}
}
