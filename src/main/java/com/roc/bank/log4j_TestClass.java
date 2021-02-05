package com.roc.bank;

import org.apache.log4j.Logger;

public class log4j_TestClass {

	public static Logger Log=Logger.getLogger(log4j_TestClass.class);
	public static void main(String[] args) {
		
		Log.trace("Hello. It's my first logging output!");
/*		
		Log.debug("Hello. It's my first logging output!");
		Log.info("Hello. It's my first logging output!");
		Log.warn("Hello. It's my first logging output!");
		Log.error("Hello. It's my first logging output!");
		Log.fatal("Hello. It's my first logging output!");
*/		
		
	}
}
