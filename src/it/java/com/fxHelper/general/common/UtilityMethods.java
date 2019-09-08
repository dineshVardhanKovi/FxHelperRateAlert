package com.fxHelper.general.common;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import net.lightbody.bmp.proxy.CaptureType;

public class UtilityMethods {
	private final static String JS_GET_VIEWPORT_WIDTH = "var width = undefined; if (window.innerWidth) {width = window.innerWidth;} else if (document.documentElement && document.documentElement.clientWidth) {width = document.documentElement.clientWidth;} else { var b = document.getElementsByTagName('body')[0]; if (b.clientWidth) {width = b.clientWidth;}};return width;";
	private final static String JS_GET_VIEWPORT_HEIGHT = "var height = undefined;  if (window.innerHeight) {height = window.innerHeight;}  else if (document.documentElement && document.documentElement.clientHeight) {height = document.documentElement.clientHeight;}  else { var b = document.getElementsByTagName('body')[0]; if (b.clientHeight) {height = b.clientHeight;}};return height;";

	public static String getParamValue(ITestContext ctx, String param) {
		String value = System.getProperty(param);
		
		if (value!=null) {
			return value;
		}
		try {
			return (ctx.getCurrentXmlTest().getParameter(param)).toString();

		} catch (Exception e) {
			try {
				//new ReadProperties();
				return new ReadProperties().getProperty(param);
			} catch (Throwable e1) {
			}
		}
		return "";
	}

	public  static String getBrowserName(ITestResult testResult) {
		String browserName = UtilityMethods.getParamValue(testResult.getTestContext(), "browser");
		String breakPoint = UtilityMethods.getParamValue(testResult.getTestContext(), "breakPoint");

		return browserName + "> BP " + breakPoint;
	}

	
	
	public  BrowserMobProxyServer startBrowserMobProxy(BrowserMobProxyServer server, DesiredCapabilities capabilities) throws UnknownHostException {
		server = new BrowserMobProxyServer();
		server.setTrustAllServers(true);
		server.setMitmManager(ImpersonatingMitmManager.builder().trustAllServers(true).build());
		//server.setConnectTimeout(20, TimeUnit.SECONDS);
		server.setIdleConnectionTimeout(120, TimeUnit.SECONDS);
		server.setRequestTimeout(120, TimeUnit.SECONDS);
		server.start();
		Logs.info("BrowserMob Proxy server started");

		//server.blacklistRequests("https?://.*(dsr.t402.livefyre)+.*", 404);
		//server.blacklistRequests("https?://.*(tagsrvcs)+.*", 404);
		//server.blacklistRequests("http?://.*(everesttech)+.*", 404);
		//server.blacklistRequests("http?://.*(turn)+.*", 404);
		//server.blacklistRequests("https://.*(medscape.com/medpulseservice/lists/statuses)+.*", 404);
		//https://api.qa00.medscape.com/medpulseservice/lists/statuses.json
		//server.blacklistRequests("http?://*(api.qa02.medscape)+.*", 404);

		Proxy proxy = ClientUtil.createSeleniumProxy(server);
		proxy.setHttpProxy("localhost:" + server.getPort());
		Logs.info("BrowserMob Proxy server port : "+server.getPort());
		capabilities.setAcceptInsecureCerts(true);
		//capabilities.setCapability("acceptInsecureCerts", true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT,
				CaptureType.REQUEST_HEADERS);
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		
		return server;

	}
	
	public  BrowserMobProxyServer getBrowserMobProxy(BrowserMobProxyServer server, DesiredCapabilities capabilities) {
		server = new BrowserMobProxyServer();
		server.setTrustAllServers(true);
		server.setMitmManager(ImpersonatingMitmManager.builder().trustAllServers(true).build());

		
		server.start();
		Logs.info("BrowserMob Proxy server started");
;

		server.blacklistRequests("https?://.*(dsr.t402.livefyre)+.*", 404);
		server.blacklistRequests("https?://.*(tagsrvcs)+.*", 404);
		server.blacklistRequests("http?://.*(everesttech)+.*", 404);
		server.blacklistRequests("http?://.*(turn)+.*", 404);
		//server.blacklistRequests("http?://*(api.qa02.medscape)+.*", 404);

		capabilities.setAcceptInsecureCerts(true);
		//capabilities.setCapability("acceptInsecureCerts", true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		server.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT,
				CaptureType.REQUEST_HEADERS);
		
		Proxy proxy = ClientUtil.createSeleniumProxy(server);
		proxy.setHttpProxy("localhost:" + server.getPort());
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		
		return server;

	}

	public  WebDriver androidLaunch(WebDriver driver, DesiredCapabilities capabilities) {
		// capabilities.setCapability("--command-timeout", 10000);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		// TODO add app apk path, app activity
		capabilities.setCapability("app", "???");
		capabilities.setCapability("appActivity", "???");
		capabilities.setCapability("noReset", "true");
	    capabilities.setCapability("deviceOrientation", "portrait");
		capabilities.setCapability("platformVersion", ReadProperties.PLATFORM_VERSION);
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ReadProperties.DEVICE_NAME);
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, ReadProperties.PLATFORM_VERSION);
	    capabilities.setCapability("appiumVersion", "1.5.3");

		try {
			return new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			//AndroidDriver<>(new URL(URL), capabilities);
		} catch (MalformedURLException e1) {
			return null;
		}
	}

	public DesiredCapabilities getPltaform(DesiredCapabilities capability) {
		String sysEnv = "";
		try {
			sysEnv = System.getenv("OS").toString();
		} catch (Exception e) {
			try {
				sysEnv = System.getProperty("OS").toString();
			} catch (Exception e1) {

			}
		}

		try {
			if (sysEnv.equalsIgnoreCase("mac")) {
				capability.setPlatform(Platform.MAC);
				 capability.setCapability("nodeEnv", Base.targetNode);
			} else if (sysEnv.equalsIgnoreCase("linux")) {
				capability.setPlatform(Platform.LINUX);
				 capability.setCapability("nodeEnv", Base.targetNode);
			} else {
				capability.setPlatform(Platform.WINDOWS);
				 capability.setCapability("nodeEnv", Base.targetNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return capability;
	}



	
	public  WebDriver webGridBrowserLaunch(WebDriver driver, Proxy proxy, String browser,String hubHost,String hubPort,
			String breakPoint, String browserMob) throws Error {
		//capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		try {
			String url = "http://" + hubHost + ":" + hubPort + "/wd/hub";
			Logs.info("Grid URL :"+url);
			Logs.info("Launching browser :"+browser);
			DesiredCapabilities capability =null;
			final ChromeOptions chromeOptions = new ChromeOptions();
			
			switch (browser.toLowerCase()) {

			case "firefox":
			case "ff":
				System.out.println("Browser ff is being executed");
				capability = DesiredCapabilities.firefox();
				getPltaform(capability);
				capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				if (browserMob.equalsIgnoreCase("true")) {
					capability.setCapability(CapabilityType.PROXY, proxy);
				}
				driver = getRemoteDriver(url, capability);
				
				break;
			case "ie":
				capability = DesiredCapabilities.internetExplorer();
				capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capability.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				capability.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "accept");
				capability.setJavascriptEnabled(true);
				getPltaform(capability);
				if (browserMob.equalsIgnoreCase("true")) {
					capability.setCapability(CapabilityType.PROXY, proxy);
				}
				driver = getRemoteDriver(url, capability);
				break;
			case "chrome":
				capability =DesiredCapabilities.chrome();
				getPltaform(capability);
				setChromeUserAgents(breakPoint, chromeOptions);
				setChromeOptions(proxy, browserMob, capability, chromeOptions);
				driver = getRemoteDriver(url, capability);
				break;

			case "chromeheadless":
				capability = DesiredCapabilities.chrome();
				setChromeUserAgents(breakPoint, chromeOptions);
				setChromeOptions(proxy, browserMob, capability, chromeOptions);
				chromeOptions.addArguments("--headless");
				driver = getRemoteDriver(url, capability);
				
				break;
			default:
				throw new Error("Browser Not available:" + browser);
			}
			// Resize browser
			setBrowserSize(driver, breakPoint);
		
		} catch (Exception e) {
				ExtentTestManager.getTest().log(LogStatus.INFO, getException(e));
				System.out.println(getException(e));
		}
		return driver;
	}

	private void setChromeOptions(Proxy proxy, String browserMob, DesiredCapabilities capability,
			final ChromeOptions chromeOptions) {
		setPageLoadStategy(chromeOptions);
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		chromeOptions.addArguments("-allow-running-insecure-content");
		chromeOptions.addArguments("--ignore-certificate-errors");
		chromeOptions.addArguments("disable-infobars");
		chromeOptions.addArguments("--disable-gpu");
		if (browserMob.equalsIgnoreCase("true")) {
			chromeOptions.setCapability(CapabilityType.PROXY, proxy);
		}
		capability.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		capability.setBrowserName(BrowserType.CHROME);
		capability.setPlatform(Platform.ANY);
	}

	private void setPageLoadStategy(final ChromeOptions chromeOptions) {
		String strategy = Base.prop.getProperty("PageLoadStrategy").toString().toLowerCase();
		if (strategy.equals("none")) {
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
		}else if(strategy.contains("normal")){
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		}else{
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		}
	}

	private WebDriver getRemoteDriver(String url, DesiredCapabilities capability) throws MalformedURLException {
		WebDriver driver;
		try {
			Logs.info("Launching Browser with below capabilities:<br>"+capability.asMap());
			driver = new RemoteWebDriver(new URL(url), capability);
			Logs.info("Launched Browser :"+capability);
		} catch (Exception e) {
			Logs.info("Launched Browser failed , retrying to launch");
			driver = new RemoteWebDriver(new URL(url), capability);
			Logs.info("Launched Browser :"+capability);
		}
		return driver;
	}
	
	@SuppressWarnings("deprecation")
	public  WebDriver webBrowserLaunch(WebDriver driver, DesiredCapabilities capabilities, String browser,
			String breakPoint) throws Error {
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		try {
			Logs.info("Launching browser : " + browser);
			switch (browser.toLowerCase()) {

			case "firefox":
			case "ff":
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				FirefoxOptions ffOptions = new FirefoxOptions();
				switch (breakPoint) {
				case "1":
					
						firefoxProfile.setPreference("general.useragent.override",
								getBP1UserAgent());
					break;
				case "2":
					firefoxProfile.setPreference("general.useragent.override",
							getBP2UserAgent());
					break;
				}

				ffOptions.setProfile(firefoxProfile);
				capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, ffOptions);
				System.setProperty("webdriver.gecko.driver",
						ReadProperties.projectLocation + "Drivers/"+getDriversLocation()+"/geckodriver"+getDriverExtension());
				try {
					Logs.info("Launching Browser with below capabilities:<br>"+capabilities.asMap());
					driver = new FirefoxDriver(capabilities);
				} catch (Exception e) {
					driver = new FirefoxDriver(capabilities);
				}
				break;
			case "ie":
				InternetExplorerOptions ieOption = new InternetExplorerOptions();
				ieOption.setCapability("acceptInsecureCerts", false);
				capabilities.setCapability("se:ieOptions", ieOption);
				File file = new File(ReadProperties.projectLocation + "Drivers/"+getDriversLocation()+"/IEDriverServer"+getDriverExtension());
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				capabilities.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "accept");
				capabilities.setJavascriptEnabled(true);
				try {
					Logs.info("Launching Browser with below capabilities:<br>"+capabilities.asMap());
					driver = new InternetExplorerDriver(capabilities);
				} catch (Exception e) {
					driver = new InternetExplorerDriver(capabilities);
				}
				break;
			case "chrome":
				System.setProperty("webdriver.chrome.driver",
						ReadProperties.projectLocation + "Drivers/"+getDriversLocation()+"/chromedriver"+getDriverExtension());
				ChromeOptions options = new ChromeOptions();
				setPageLoadStategy(options);
				options.addArguments("-allow-running-insecure-content");
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("disable-infobars");
				//Linux machine causing some issue, so added fix it
				if (isUnix()) {
					options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--incognito", "--single-process");
				}
				setChromeUserAgents(breakPoint, options);
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				Logs.info(capabilities.toString());
				try {
					Logs.info("Launching Browser with below capabilities:<br>"+capabilities.asMap());
					driver = new ChromeDriver(capabilities);
				} catch (Exception e) {
					driver = new ChromeDriver(capabilities);
				}
				break;

			case "chromeheadless":
				driver = getChromeHeadLess(driver, capabilities,breakPoint);
				break;
			case "firefoxheadless":
			case "ffheadless":
				FirefoxBinary firefoxBinary = new FirefoxBinary();
				firefoxBinary.addCommandLineOptions("--headless");
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setBinary(firefoxBinary);
				FirefoxProfile firefoxProfile1 = new FirefoxProfile();
				switch (breakPoint) {
				case "1":
					firefoxProfile1.setPreference("general.useragent.override",
							getBP1UserAgent());
					break;
				case "2":
					firefoxProfile1.setPreference("general.useragent.override",
							getBP2UserAgent());
					break;
				}
				firefoxOptions.setProfile(firefoxProfile1);
				System.setProperty("webdriver.gecko.driver",
						ReadProperties.projectLocation + "Drivers/"+getDriversLocation()+"/geckodriver"+getDriverExtension());
				capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
				try {
					Logs.info("Launching Browser with below capabilities:<br>"+capabilities.asMap());
					driver = new FirefoxDriver(capabilities);
				} catch (Exception e) {
					driver = new FirefoxDriver(capabilities);
				}
				break;
				
			case "edge":
			case "Edge":
			case "EDGE":
				System.setProperty("webdriver.edge.driver", ReadProperties.projectLocation + "Drivers/"+getDriversLocation()+"/MicrosoftWebDriver.exe");
				driver = new EdgeDriver(capabilities);
				break;
			default:
				throw new Error("Browser Not available:" + browser);

			}
			if (!(browser.contains("chrome")&&breakPoint.contains("1"))) {
				// Resize browser
				setBrowserSize(driver, breakPoint);
			}
			Logs.info("Launched browser : " + browser);
			return driver;
		}

		catch (Exception e) {
			Logs.info("Failed to launch browser : " + browser+"<br>"+UtilityMethods.getException(e));
			return null;
		}
	}

	private void setChromeUserAgents(String breakPoint, ChromeOptions options) {
		switch (breakPoint) {
		case "1":
			Map<String, Object> deviceMetrics = new HashMap<>();
			deviceMetrics.put("width", Constantns.BREAK_POINT_1_BROWSER_WIDTH);
			deviceMetrics.put("height", Constantns.BROWSER_HEIGHT);
			Map<String, Object> mobileEmulation = new HashMap<>();
			mobileEmulation.put("deviceMetrics", deviceMetrics);
			mobileEmulation.put("userAgent", getBP1UserAgent());
			options.setExperimentalOption("mobileEmulation", mobileEmulation);

			break;
		case "2":
			options.addArguments(
					"--user-agent="+getBP2UserAgent());
			break;
		}
	}

	public  WebDriver getChromeHeadLess(WebDriver driver, DesiredCapabilities capabilities, String breakPoint) {
		ChromeOptions chromeOptions = new ChromeOptions();
		setChromeUserAgents(breakPoint, chromeOptions);
		System.setProperty("webdriver.chrome.driver", ReadProperties.projectLocation + "Drivers/"+getDriversLocation()+"/chromedriver"+getDriverExtension());
		chromeOptions.addArguments("-allow-running-insecure-content");
		setPageLoadStategy(chromeOptions);
		chromeOptions.addArguments("--ignore-certificate-errors");
		chromeOptions.addArguments("disable-infobars");
		//Linux machine causing some issue, so add fix it
		if (isUnix()) {
			chromeOptions.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--incognito", "--single-process");
		}else{
			chromeOptions.addArguments("--headless");
			chromeOptions.addArguments("--disable-gpu", "--no-sandbox");
		}
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		Logs.info(capabilities.toString());
		try {
			Logs.info("Launching Browser with below capabilities:<br>"+capabilities.asMap());
			driver = new ChromeDriver(capabilities);
		} catch (Exception e) {
			driver = new ChromeDriver(capabilities);
		}
		return driver;

	}

	public static void setBrowserSize(WebDriver driver, String breakPoint) {
		
		switch (breakPoint) {
		case "1":
			driver.manage().window()
					.setSize(new Dimension(Constantns.BREAK_POINT_1_BROWSER_WIDTH, Constantns.BROWSER_HEIGHT));

			break;
		case "2":
			driver.manage().window()
					.setSize(new Dimension(Constantns.BREAK_POINT_2_BROWSER_WIDTH, Constantns.BROWSER_HEIGHT));

			break;
		case "3":
			driver.manage().window()
					.setSize(new Dimension(Constantns.BREAK_POINT_3_BROWSER_WIDTH, Constantns.BROWSER_HEIGHT));
			break;
		case "4":
			driver.manage().window()
					.setSize(new Dimension(Constantns.BREAK_POINT_4_BROWSER_WIDTH, Constantns.BROWSER_HEIGHT));
			break;
		default:
			driver.manage().window().maximize();
			break;
		}
		Dimension browserSize = driver.manage().window().getSize();
		Dimension actualViewportSize = getViewportSize(driver);
		driver.manage().window().setSize(new Dimension(2*browserSize.width - actualViewportSize.getWidth(),
				2 * browserSize.height - actualViewportSize.getHeight()));
		Logs.info("Resized browser Width x Height  to "+browserSize.width+" x "+browserSize.height);
	}

	// Browser resizing method to viewport
	public static Dimension getViewportSize(WebDriver driver) {
		int width = extractViewportWidth(driver);
		int height = extractViewportHeight(driver);
		return new Dimension(width, height);
	}

	static int extractViewportWidth(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int viewportWidth = Integer.parseInt(js.executeScript(JS_GET_VIEWPORT_WIDTH, new Object[0]).toString());
		return viewportWidth;
	}

	static int extractViewportHeight(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int result = Integer.parseInt(js.executeScript(JS_GET_VIEWPORT_HEIGHT, new Object[0]).toString());
		return result;
	}

	public static String getException(Exception e) {
		try {
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			e.printStackTrace();
			return "<pre><br>"+sw.toString()+"</pre>";
			
		} catch (Exception e1) {
			//e1.printStackTrace();
			return "<br>";
		}
	}

	public static String getException(Throwable e) {
		String traceException = "";
		try {
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			e.printStackTrace();
			return  "<pre><br>"+sw.toString()+"</pre>";

		} catch (Exception e1) {
			traceException = "";
		}

		return traceException;
	}

	public String getDriversLocation() {

		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "WinOS";
		} else if (os.contains("mac")) {
			return "MacOS";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
			return "Linux";
		} else {
			throw new Error("Drivers not configured for this OS" + os);
		}
	}

	public String getDriverExtension() {

		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return ".exe";
		}
		return "";
	}

	
	public static String getBP1UserAgent() {
		try {
			String s = new ReadProperties().getProperty("MobileView_Bp1");
			return s;
		} catch (Throwable e) {
			return "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_4 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G35 Safari/601.1";
			
		}
	}
	
	public static String getBP2UserAgent() {
		try {
			return new ReadProperties().getProperty("IpadView_Bp2");
		} catch (Throwable e) {
			return "Mozilla/5.0 (iPad; CPU OS 10_1_1 like Mac OS X) AppleWebKit/602.2.14 (KHTML, like Gecko) Version/10.0 Mobile/14B100 Safari/602.1";
			
		}
	}
	
	public static boolean isUnix() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
	
	public static String getConsoleFormatedText(String text){
		
		return text.replace("<br>", "\n\t\t").replace("<b>", "").replace("</b>", "").replace("<pre>", "").replace("</pre>", "").replace("</br>", "\n\t");
	}
	
	/**
	 * Returns PROD if env is blank , other wise return same in upper case
	 * @return
	 */
	public static String getEnvforReport() {
		String envReport;
		try {
			envReport = Base.url.toUpperCase().trim();
			if(envReport.length()==0){
				envReport = "PROD";
			}
			return envReport;
		} catch (Exception e) {
			Logs.warn("Environment : <br>"+UtilityMethods.getException(e));
			return e.getMessage();
		}
				
	}
	
	/**
	 * It converts string to UTF8-8
	 * 
	 * @param str
	 * @return
	 */
	public static String toUTF8(String str) {
		try {
			return new String(str.toString().getBytes(), "UTF-8");
		} catch (Exception e) {
			return str;
		}

	}

	public IOSDriver iOSWebLanunch(WebDriver driver, DesiredCapabilities capabilities, ReadProperties prop) throws MalformedURLException {			
			capabilities.setCapability("platformName" ,"iOS"); 
			capabilities.setCapability("--command-timeout", 60000);
		    capabilities.setCapability(CapabilityType.BROWSER_NAME, "Safari");
			capabilities.setCapability(MobileCapabilityType.UDID, prop.getProperty("UDID"));
			capabilities.setCapability("automationName", "XCUITest");
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, prop.getProperty("IOS_PLATFORM_VERSION"));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,prop.getProperty("IOS_device_name"));
			capabilities.setCapability("startIWDP", true);
			capabilities.setCapability("showXcodeLog", true);
			capabilities.setCapability("xcodeSigningId", "iPhone Developer");
			capabilities.setCapability("xcodeOrgId", "Narasimha K(Personal Team)");
			capabilities.setCapability("safariIgnoreFraudWarning", true);
			capabilities.setCapability("safariAllowPopups", "true");
			capabilities.setCapability("unhandledPromptBehavior", "true");
			capabilities.setCapability("noReset", true);  // for iOS only
			capabilities.setCapability("safariOpenLinksInBackground",true);
			capabilities.setCapability("nativeWebTap",true);
			capabilities.setCapability("safariIngoreFraudWarning",true);
			capabilities.setCapability("autoDismissAlerts",false);
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,360);
			
				return new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
			
			
			
		
	}
}
