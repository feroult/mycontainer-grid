package com.googlecode.mycontainer.grid.server.setup;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4JSetup {

	public void run() {
		System.out.println("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
		Logger root = Logger.getRootLogger();
		root.setLevel(Level.INFO);
		ConsoleAppender appender = new ConsoleAppender(new PatternLayout("%-4r %-5p [%t] [%c{1}] %m%n"));
		root.addAppender(appender);		
	}
	
}
