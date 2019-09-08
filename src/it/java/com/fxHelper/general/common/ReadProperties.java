package com.fxHelper.general.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import static com.fxHelper.general.common.UtilityMethods.getEnvforReport;
public class ReadProperties {

	HashMap<String, String> props = new HashMap<String, String>();
	public static Properties pro ;
	FileInputStream fis;
	public static String sender = null;
	public static String receiver = null;
	public static String smtp = null;
	public static String port = null;
	public static String subject = null;
	public static String url = null;
	public static String uname = null;
	public static String password = null;
	public static String sendMail = null;
	public static String BreakPoint = null;
	public static String browser = null;
	public static String DEVICE_NAME = null;
	public static String PLATFORM_VERSION = null;
	public static String targetDevice = null;
	public static String projectLocation = "src/it/resources/";


	public  String getProperty(String key) {
		try {
			return pro.getProperty(key);
		} catch (Exception e) {
			return "";
		}
	}
	
	
	
	public  ReadProperties() throws Throwable  {
		//loading data from framework config
		if(pro!=null){
			return;
		}
		pro= new Properties();
		InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("Config.properties");
		
		pro.load(in);
		
		//Override values with user file
		File userConfig = new File(projectLocation + "/Config.properties");
		
		if (userConfig.exists()) {
			fis = new FileInputStream(userConfig);
			pro.load(fis);
		}
		
		// reading properties form .properties file

		sendMail = pro.getProperty("sendReportsEmail");
		url = pro.getProperty("url");
		sender = pro.getProperty("Sender");
		receiver = pro.getProperty("Receiver");
		smtp = pro.getProperty("smtp");
		port = pro.getProperty("port");
		subject = pro.getProperty("Subject");
		BreakPoint = pro.getProperty("breakPoint");
		DEVICE_NAME = pro.getProperty("DEVICE_NAME");
		PLATFORM_VERSION = pro.getProperty("PLATFORM_VERSION");
		browser = pro.getProperty("browser");
		targetDevice = pro.getProperty("targetDevice");

		// Credentials

		uname = pro.getProperty("username");
		password = pro.getProperty("password");
		
	}
	
	public void	loadConfig(String filePath) throws IOException{
		
		File loadProfile = new File(filePath);
		
		if (loadProfile.exists()) {
			Logs.info("Loading properties file :"+filePath);
			fis = new FileInputStream(loadProfile);
			pro.load(fis);
		}else{
			Logs.error("File does not exists : "+filePath);
		}
	}

}
