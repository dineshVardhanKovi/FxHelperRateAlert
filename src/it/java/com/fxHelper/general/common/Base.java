package com.fxHelper.general.common;

import static com.fxHelper.general.common.UtilityMethods.getEnvforReport;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.galenframework.reports.GalenTestInfo;
import com.relevantcodes.extentreports.LogStatus;
import com.fxHelper.general.utils.PortFinder;

import groovy.transform.Synchronized;
import io.restassured.RestAssured;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;

/**
 * This class is used to create instance of reports and manage logs for html
 * reports.
 * 
 * @author Krishna.Gandavaram
 *
 */
@Listeners(com.fxHelper.general.common.Listener.class)
public class Base {

	private static ThreadLocal<HashMap<String, Object>> driverff;
	private static ThreadLocal<HashMap<String, Object>> driverchrome;
	private static ThreadLocal<HashMap<String, Object>> driverie;
	private static ThreadLocal<HashMap<String, Object>> driverchromeheadless;
	private static ThreadLocal<HashMap<String, Object>> driverffheadless;
	private static ThreadLocal<HashMap<String, Object>> driveredge;
	private static ThreadLocal<HashMap<String, Object>> driverThread;

	public static ArrayList<WebDriver> drivers = new ArrayList<>();
	public static ReadProperties prop;

	public static List<GalenTestInfo> tests;
	public String reportName = ExtentManager.getReportLocation() + "/Galen_Reports";
	public static WebDriver driver;

	public static int passCount = 0;
	public static int failCount = 0;
	public static int skippedCount = 0;

	public static HashMap<String, String> TestcaseDetails = new HashMap<String, String>();
	public static String TCaseResult = "";
	public static String TCaseName = "";
	static int TestCasesCount = 0;
	protected String report_name = "";

	public static String url;
	public String browser;
	public String jenkinsExecution;
	public String breakPoint;
	public String targetDevice;
	public String reportFileNme;
	public static String mednetCookie = "";
	public static String browserMob;
	public static String targetNode;
	public static String useGrid;
	public static String hubHost;
	public static String hubPort;
	public static String sendReportsEmail;
	public static String recipientList;
	public static String takeScreenShotOnPass;
	public static String userName;
	public static String password;
	public ITestContext ctx;
	public static Map<String, String> headers;
	public static Map<String, String> getCookie;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite(ITestContext ctx) throws Throwable {
		
	    
		driverff = new ThreadLocal<>();
		driverchrome = new ThreadLocal<>();
		driverie = new ThreadLocal<>();
		driverchromeheadless = new ThreadLocal<>();
		driverffheadless = new ThreadLocal<>();
		driverThread = new ThreadLocal<>();
		getCookie = new HashMap<>();
		this.ctx = ctx;
		reportFileNme = (new Date()).toString().replace(":", "_").replace(" ", "_");
		prop = new ReadProperties();
		tests = new LinkedList<GalenTestInfo>();
		setProjectExecutionVariables(ctx);
		prop.loadConfig("profiles/" + getEnvforReport().replace(".", "").toLowerCase() + "-config.properties");
		String urlReport = UtilityMethods.getEnvforReport();
		Logs.info("urlironment : " + urlReport);
		ExtentManager.getReporter().addSystemInfo("urlironment", urlReport);
		headers = new HashMap<String, String>();
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
		RestAssured.useRelaxedHTTPSValidation();
		
		
	}

	@BeforeMethod(alwaysRun = true)
	public void setProjectExecutionVariable(ITestContext ctx) {
		this.ctx = ctx;
	}

	public void browserLaunch(ITestContext ctx) {
		WebDriver dd = null;
		HashMap<String, Object> driverServer = new HashMap<>();
		try {
			UtilityMethods um = new UtilityMethods();
			setProjectExecutionVariables(ctx);
			DesiredCapabilities capabilities = new DesiredCapabilities();
			driverServer.put("breakPoint", breakPoint);
			Logs.info("Browser Name :" + browser);
			Logs.info("Break Point :" + breakPoint);
			if (targetDevice.toLowerCase().equalsIgnoreCase("web")) {

				if (useGrid.equalsIgnoreCase("true")) {
					Logs.info("Scripts will be executed in Gird");
					Proxy seleniumProxy = null;
					if (browserMob.equalsIgnoreCase("true")) {}
					Logs.info("System Property of Use grid is " + useGrid);
					dd = um.webGridBrowserLaunch(driver, seleniumProxy, browser, hubHost, hubPort, breakPoint,
							browserMob);

				} else {
					
					dd = um.webBrowserLaunch(driver, capabilities, browser, breakPoint);
				}

				drivers.add(dd);
				driverServer.put("driver", dd);
				switch (browser.toLowerCase()) {
				case "ff":
				case "firefox":

					driverff.set(driverServer);

					break;
				case "chrome":

					driverchrome.set(driverServer);

					break;
				case "ie":
					driverie.set(driverServer);

					break;
				case "chromeheadless":
					driverchromeheadless.set(driverServer);
					break;
				case "ffheadless":
				case "firefoxheadless":
					driverffheadless.set(driverServer);
					break;
				case "edge":
				case "EDGE":
				case "Edge":
					driveredge.set(driverServer);
					break;
				default:
					driverThread.set(driverServer);
					break;
				}

			} else if (targetDevice.equalsIgnoreCase("android")) {
				Proxy seleniumProxy = null;
				dd = um.androidLaunch(driver, capabilities);
				drivers.add(dd);
				driverServer.put("driver", dd);
				driverThread.set(driverServer);
			}else{
				Proxy seleniumProxy = null;
				dd = um.iOSWebLanunch(driver,capabilities, prop);
				drivers.add(dd);
				driverServer.put("driver", dd);
				driverThread.set(driverServer);
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, UtilityMethods.getException(e));
		}

	}

	public WebDriver screenShotDriver() {
		setProjectExecutionVariables(ctx);
		switch (browser.toLowerCase()) {
		case "ff":
		case "firefox":
			return returnScreenShotDriver(driverff);

		case "chrome":
			return returnScreenShotDriver(driverchrome);

		case "ie":
			return returnScreenShotDriver(driverie);

		case "chromeheadless":
			return returnScreenShotDriver(driverchromeheadless);

		case "ffheadless":
		case "firefoxheadless":
			return returnScreenShotDriver(driverffheadless);
		case "edge":
		case "EDGE":
		case "Edge":
			return returnScreenShotDriver(driveredge);
		default:
			try {
				return (WebDriver) driverThread.get().get("driver");
			} catch (Exception e) {
				return null;
			}

		}

	}

	public WebDriver getDriver() {
		setProjectExecutionVariables(ctx);
		switch (browser.toLowerCase()) {
		case "ff":
		case "firefox":
			return returnDriver(driverff);

		case "chrome":
			return returnDriver(driverchrome);

		case "ie":
			return returnDriver(driverie);

		case "chromeheadless":
			return returnDriver(driverchromeheadless);

		case "ffheadless":
		case "firefoxheadless":
			return returnDriver(driverffheadless);

		default:
			if (driverThread.get() == null) {
				browserLaunch(ctx);
				return (WebDriver) driverThread.get().get("driver");
			}

			return (WebDriver) driverThread.get().get("driver");

		}

	}

	private WebDriver returnDriver(ThreadLocal<HashMap<String, Object>> threadObject) {
		if (threadObject.get() == null) {
			browserLaunch(ctx);
			return (WebDriver) threadObject.get().get("driver");
		} else if (threadObject.get().get("driver").toString().contains("null")
				|| threadObject.get().get("driver") == null) {
			browserLaunch(ctx);
			return (WebDriver) threadObject.get().get("driver");
		}

		if (!threadObject.get().get("breakPoint").equals(breakPoint)) {
			browserLaunch(ctx);
			return (WebDriver) threadObject.get().get("driver");
		}

		return (WebDriver) threadObject.get().get("driver");
	}

	private WebDriver returnScreenShotDriver(ThreadLocal<HashMap<String, Object>> threadObject) {

		try {
			return (WebDriver) threadObject.get().get("driver");
		} catch (Exception e) {

		}
		return null;
	}


	@AfterSuite(alwaysRun = true)
	public void afterSuite(ITestContext context) {
		closeBrowser();
		

		try {
			Logs.info("************************************************************************************\n");
			if (!(ExtentManager.emailReports.length() > 0)) {
				Logs.info("Extent Reports Location : " + System.getProperty("user.dir") + "/" + ExtentManager.filePath
						+ "\n");
			} else {
				Logs.info("Extent Reports Location : " + ExtentManager.emailReports + "\n");
			}
			Logs.info("************************************************************************************\n");
		} catch (Exception e) {
			Logs.info(UtilityMethods.getException(e));
		}
	}

	private void closeBrowser() {
		Logs.info("Closing all current session browsers");
		try {
			for (WebDriver webDriver : drivers) {

				if (webDriver != null) {
					try {
						webDriver.quit();
					} catch (Exception e) {

					}
				}

			}
		} catch (Exception e) {
		}
		Logs.info("Closed all current session browsers");
	}

	public void setProjectExecutionVariables(ITestContext ctx) {

		browser = getParamValue(ctx, "browser");
		targetDevice = getParamValue(ctx, "targetDevice");
		jenkinsExecution = getParamValue(ctx, "jenkinsExecution");

		if (jenkinsExecution.equalsIgnoreCase("true")) {
			setupJenkinsParams(ctx);

		} else {
			url = getParamValue(ctx, "url").trim();
		}

		breakPoint = getParamValue(ctx, "breakPoint");
		browserMob = getParamValue(ctx, "browserMob");
		useGrid = getParamValue(ctx, "useGrid");
		hubHost = getParamValue(ctx, "hubHost");
		hubPort = getParamValue(ctx, "hubPort");
		sendReportsEmail = getParamValue(ctx, "sendReportsEmail");
		recipientList = getParamValue(ctx, "recipientList");
		takeScreenShotOnPass = getParamValue(ctx, "takeScreenShotOnPass");
		userName = getParamValue(ctx, "username");
		password = getParamValue(ctx, "password");

	}

	/**
	 * Setup jenkins parameters
	 * 
	 * @param ctx
	 */
	public void setupJenkinsParams(ITestContext ctx) {

		try {
			String nodeType = "";
			String urlDigit = "";

			nodeType = getParamValue(ctx, "url").trim();
			if (nodeType.toUpperCase().contains("QA")) {
				targetNode = "QAxx";
				if (nodeType.length() > 2) {
					urlDigit = nodeType.substring(2, 4);
					url = "qa" + urlDigit + ".";
				} else
					url = "qa02.";
			} else if (nodeType.toUpperCase().contains("DEV")) {
				targetNode = "DEVxx";
				if (nodeType.length() > 3) {
					urlDigit = nodeType.substring(3, 5);
					url = "dev" + urlDigit + ".";
				} else
					url = "dev03.";
			} else if (nodeType.toUpperCase().contains("PROD") || nodeType.toUpperCase().length() == 0) {
				targetNode = "PROD";
				url = "";
			} else if (nodeType.toUpperCase().contains("STAGING")) {
				targetNode = "PROD";
				url = "staging.";

			} else {
				throw new InvalidBrowserException("Please select valid urlt while passing from Jenkins / Maven");
			}
		}

		catch (InvalidBrowserException e) {
			Logs.info(UtilityMethods.getException(e));
		}
	}

	public class InvalidBrowserException extends Exception {
		String error = "";

		InvalidBrowserException(String str) {

			error = str;

		}
	}

	/**
	 * Get value from testng.xml and properties file
	 * 
	 */
	public String getParamValue(ITestContext ctx, String param) {

		String value = System.getProperty(param);

		if (value != null) {
			return value;
		}
		try {

			return (ctx.getCurrentXmlTest().getParameter(param)).toString();

		} catch (Exception e) {
			return prop.getProperty(param);
		}
	}

}