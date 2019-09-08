package com.fxHelper.general.common;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

public class Logs {
	static Properties props = new Properties();
	static Logger log;

	static public synchronized void info(String details) {
		init();
		log.info(UtilityMethods.getConsoleFormatedText(details));
	}
	
	
	static public synchronized void error(String details) {
		init();
		log.error(UtilityMethods.getConsoleFormatedText(details));
	}
	
	static public synchronized void warn(String details) {
		init();
		log.warn(UtilityMethods.getConsoleFormatedText(details));
	}
	
	static public synchronized void fatal(String details) {
		init();
		log.fatal(UtilityMethods.getConsoleFormatedText(details));
	}

	private static void init() {
		if (log == null) {
			try {
				
				InputStream in = Logs.class.getClassLoader()
		                .getResourceAsStream("log4j.properties");
				PropertyConfigurator.configure(props);
				
				//source  - https://www.programcreek.com/java-api-examples/org.apache.log4j.FileAppender
				
				FileAppender fa = new FileAppender();
				
				fa.setName("FileLogger");
				fa.setFile(ExtentManager.getReportLocation()+"/Logs/main.log");
				fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
				fa.setThreshold(Level.INFO);
				fa.setAppend(true);
				fa.activateOptions();

				//add appender to any Logger (here is root)
				Logger.getRootLogger().addAppender(fa);
				
			log = Logger.getLogger("rootLogger");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}