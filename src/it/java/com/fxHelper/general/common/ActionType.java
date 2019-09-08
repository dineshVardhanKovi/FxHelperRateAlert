package com.fxHelper.general.common;

import static com.fxHelper.general.common.UtilityMethods.getException;
import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationResult;
import com.relevantcodes.extentreports.LogStatus;
import com.fxHelper.general.objectrepo.LoginPageObjects;
import com.fxHelper.general.utils.AESCryptor;
import com.fxHelper.general.utils.PortFinder;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ActionType extends Base {

	public WebDriverWait wait;
	// c20 value

	private String LEFTBOLDTAG = "<b>";
	private String RIGHTBOLDTAG = "</b>";

	static boolean b = true;

	/**
	 * Scroll to the given locator
	 * 
	 * @param by
	 * @param locator
	 */
	@SuppressWarnings("finally")
	public boolean scrollToObject(By by, String locator) {
		Exception e1 = null;
		boolean flag = false;
		try {
			JavascriptExecutor js = (JavascriptExecutor) getDriver();
			int yPosition = getDriver().findElement(by).getLocation().getY();
			js.executeScript("window.scroll(300,"
					+ (yPosition - (getDriver().manage().window().getSize().getHeight() / 2)) + ")");

			flag = true;
		} catch (Exception e) {
			e1 = e;
			flag = false;
		} finally {
			generateReport(flag, "Object Scroll : " + locator + " <br>" + getException(e1));
			return flag;
		}
	}

	

	/**
	 * It generates skip statement in report and takes screenshot if
	 * takeScreenShotOnPass is true
	 * 
	 * @param string
	 */
	public void generateSkipReport(String string) {
		try {
			ExtentTestManager.getTest().log(LogStatus.SKIP, string);
			if (prop.getProperty(Constantns.TAKE_SCREENSHOT_ON_PASS).equals("true")) {
				if (screenShotDriver() != null) {
					ExtentTestManager.getTest().log(LogStatus.INFO,
							ExtentTestManager.getTest().addScreenCapture(getScreenshot(screenShotDriver(), string)));
				}
			}
		} catch (Exception e) {

		}
	}


	/**
	 * It returns value from property file based on key value
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {

		if (prop == null) {
			try {
				prop = new ReadProperties();
			} catch (Throwable e) {
				// e.printStackTrace();
			}
		}
		return prop.getProperty(key);
	}

	/**
	 * It wait for element for 10 seconds if the object is not located
	 * 
	 * @param by
	 * @return
	 */
	public boolean waitForElement(By by) {
		return waitForElement(by,true);
	}

	/**
	 * 
	 * @param by
	 * @param scrollRequired
	 * @return
	 */
	public boolean waitForElement(By by, boolean scrollRequired) {
		Logs.info("	Waiting for element ... " + by.toString());
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), Integer.parseInt(getProperty("waitForElementTimeOut").trim()));
			WebElement element = wait.until(
			        ExpectedConditions.visibilityOfElementLocated(by));
			viewPort(by, scrollRequired);
			return element.isDisplayed();
		} catch (Exception e) {
			if (getProperty("PageLoadStrategy").toUpperCase().equals("NONE")) {
				waitForPageLoad(120);
				try {
					WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
					viewPort(by, scrollRequired);
					return element.isDisplayed();
				} catch (Exception e2) {

				} 
			}
			return false;
		}
	}

	/**
	 * It will wait till the object is located within the given time
	 * 
	 * @param by
	 *            :example By.xpath("//")
	 * @param seconds
	 *            : example 5
	 * @return true or false
	 */
	public boolean waitForElement(By by, int seconds) {

		return waitForElement(by, seconds,true);
	}

	public boolean waitForElement(By by, int seconds, boolean scrollRequired) {
		generateInfoReport("	Waiting for element ... " + by.toString());
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), seconds);
			WebElement element = wait.until(
			        ExpectedConditions.visibilityOfElementLocated(by));
			viewPort(by, scrollRequired);
			generateInfoReport("	Element located ... " + by.toString());
			return element.isDisplayed();
		} catch (Exception e) {
			generateInfoReport("	Element not located ... " + by.toString());
			return false;
		}
		
	}

	public boolean waitTillElementInVisible(By by, int seconds){
		try {
			generateInfoReport("Waiting for element to be disappeared :"+by);
			return new WebDriverWait(getDriver(), seconds).until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (Exception e) {
			generateInfoReport("Element is appeared :"+by);
			return true;
		}
	}
	private void viewPort(By by, boolean scrollRequired) {
		try {
			if (scrollRequired) {
				if (!isVisibleInViewport(getDriver().findElement(by))) {
					JavascriptExecutor js = (JavascriptExecutor) getDriver();
					int yPosition = getDriver().findElement(by).getLocation().getY();
					js.executeScript("window.scroll(300,"
							+ (yPosition - (getDriver().manage().window().getSize().getHeight() / 2)) + ")");
					StaticWait(1);
				}
			}
			
		} catch (Exception e) {

		}
	}

	/**
	 * It wait for element for 10 seconds if the object is not located
	 * 
	 * @param by
	 * @return
	 */
	public boolean waitForElement(WebElement element) {

		boolean flag = false;
		int nooftimes = 0;
		while (!flag) {
			try {
				if (nooftimes >= 10) {
					return false;
				}
				getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				try {
					JavascriptExecutor js = (JavascriptExecutor) getDriver();
					int yPosition = element.getLocation().getY();
					js.executeScript("window.scroll(300,"
							+ (yPosition - (getDriver().manage().window().getSize().getHeight() / 2)) + ")");
					// Thread.sleep(1000);
				} catch (Exception e) {

				}
				flag = element.isDisplayed();
				if (flag) {
					return true;
				} else {
					nooftimes++;
					StaticWait(1);
				}
			} catch (Exception e) {
				nooftimes++;
				StaticWait(1);
			}
		}
		return flag;
	}

	/**
	 * Generates pass or fail report based on status flag
	 * 
	 * @param status
	 * @param passText
	 * @param failText
	 */
	public void generateReport(boolean status, String passText, String failText) {
		if (status) {

			generatePassReport(passText);
		} else {

			generateFailReport(failText);
		}
	}





	
	/**
	 * Thread.sleep
	 * 
	 * @param i
	 *            :no of seconds like 10 for 10 seconds
	 */
	public void StaticWait(int i) {
		try {
			Thread.sleep(i * 1000);
		} catch (Exception e) {

		}

	}

	/**
	 * Generate info statement in reports
	 * 
	 * @param infoText
	 */
	public void generateInfoReport(String infoText) {
		ExtentTestManager.getTest().log(LogStatus.INFO, infoText);
		Logs.info(infoText.replace(LEFTBOLDTAG, "").replace(RIGHTBOLDTAG, "").replace("<br>", "\n\t"));
		// Logs.info("Passed: " + passText);

	}

	/**
	 * Generate warning statement in reports
	 * 
	 * @param infoText
	 */
	public void generateWarningReport(String infoText) {
		ExtentTestManager.getTest().log(LogStatus.WARNING, infoText);
		Logs.error(infoText.replace(LEFTBOLDTAG, "").replace(RIGHTBOLDTAG, "").replace("<br>", "\n\t"));
		// Logs.info("Passed: " + passText);

	}

	/**
	 * Generate pass statement in reports
	 * 
	 * @param infoText
	 */
	public void generatePassReportWithNoScreenShot(String passText) {
		ExtentTestManager.getTest().log(LogStatus.PASS, passText);
		Logs.info(passText);
	}

	/**
	 * Returns bold text
	 * 
	 * @param data
	 * @return
	 */
	public String getBoldText(String data) {
		return LEFTBOLDTAG + data + RIGHTBOLDTAG;
	}
	/**
	 * It generates the key pair values fist row columns as keys and rest as
	 * values for single row
	 * 
	 * @param arrary
	 * @param rowNum
	 * @return
	 */

	public Hashtable<String, String> getHashMapData(String arrary[][], int rowNum) {

		Hashtable<String, String> ht = new Hashtable<>();
		for (int i = 0; i < arrary[0].length; i++) {
			ht.put(arrary[0][i], arrary[rowNum][i]);
		}

		return ht;

	}

	/**
	 * If element not present it return true, otherwise returns false and
	 * generates report
	 * 
	 * @param by
	 * @param locatorName
	 * @return
	 */
	public boolean isElementNotPresent(By by, String locatorName) {
		boolean flag = false;
		try {
			getDriver().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			flag = getDriver().findElement(by).isDisplayed();
		} catch (Exception e) {
			flag = false;
		} finally {
			if (flag) {
				generateFailReport(locatorName + " is present in the page but should not visible<br>Locator: "+by);

			} else {
				generatePassReport(locatorName + " is not present on the page as expected<br>Locator: "+by);

			}
		}
		return flag;
	}

	/**
	 * returns true when element is displayed on screen Generates report
	 * 
	 * @param by
	 * @param locatorName
	 * @return
	 * @throws Throwable
	 */
	public boolean isElementPresent(By by, String locatorName) {
		boolean flag = false;
		try {
			flag = waitForElement(by);
			generateReport(flag, "Looking for element :" + locatorName + "<br>Locator :"+by+"<br>Expected:" + locatorName
					+ " should be displayed<br>Actual: " + flag);
		} catch (Exception e) {
			return flag;
		}
		return flag;
	}

	/**
	 * returns true when element is displayed on screen Generates report
	 * 
	 * @param element
	 * @param locatorName
	 * @return
	 */
	public boolean isElementPresent(WebElement element, String locatorName) {
		boolean flag = false;
		try {
			flag = waitForElement(element);
			generateReport(flag, "Looking for element :" + locatorName + "<br>Expected:" + locatorName
					+ " should be displayed<br>Actual: " + flag);
		} catch (Exception e) {
			return flag;
		}
		return flag;
	}

	/**
	 * Returns text on element if exists, otherwise returns empty string
	 * 
	 * @param by
	 * @param locator
	 * @return
	 */
	public String getText(By by, String locator) {

		return getText(by, locator,true);
	}

	private String getText(By by, String locator, boolean scrollRequired) {
		try {
			waitForElement(by,scrollRequired);
			String text =getDriver().findElement(by).getText().trim();
			generateInfoReport("Text on "+ locator +":"+text);
			return text;
		} catch (Exception e) {
			generateInfoReport("Text on "+locator +" :");
			return "";
		}
	}

	/**
	 * Validated page title
	 * 
	 * @param expectedPageTitle
	 * @param pageName
	 *            : this is sake of report
	 * @return true or false
	 */
	public boolean isTitleMatched(String expectedPageTitle, String pageName) {

		try {
			String text = getWindowTitle();

			generateReport(text.equals(expectedPageTitle.trim()), "Page Title verification for " + pageName
					+ "<br>Actual:" + text + "<br>Expected:" + expectedPageTitle);
			return text.equals(expectedPageTitle.trim());
		} catch (Exception e) {
			return false;
		}

		
	}
	
	public String getWindowTitle(){
		String title = getDriver().getTitle().trim();
		int  i=0;
		while (title.length()==0) {
			title = getDriver().getTitle().trim();
			i++;
			if(i==120){
				break;
			}else{
				StaticWait(1);
			}
		}
		generateInfoReport("Window title :"+title);
		return title;
	}
	
	

	/**
	 * This method used type value in to text box or text area
	 * 
	 * @param locator
	 *            : Action to be performed on element (Get it from Object
	 *            repository)
	 * 
	 * @param testdata
	 *            : Value wish to type in text box / text area
	 * 
	 * @param locatorName
	 *            : Meaningful name to the element (Ex:Textbox,Text Area etc..)
	 * 
	 * 
	 */
	public boolean type(By locator, String testdata, String locatorName) {
		String exception = "";
		boolean flag = false;
		try {
			Logs.info("Entering data " + testdata + " into " + locatorName + "  ... " + locator);
			waitForElement(locator);
			getDriver().findElement(locator).clear();
			getDriver().findElement(locator).sendKeys(testdata);
			StaticWait(1);
			flag = true;
		} catch (Exception e) {
			exception = "<br>Exception:" + getException(e);
			flag = false;
		} finally {
			generateReport(flag, "Enter data :" + testdata + " into " + locatorName+"<br>Locator :"+locator + exception);
		}
		return flag;
	}

	/**
	 * This method used type value in to text box or text area
	 * 
	 * @param locator
	 *            : Action to be performed on element (Get it from Object
	 *            repository)
	 * 
	 * @param Key
	 *            : input keyboard actions
	 * 
	 * @param locatorName
	 *            : Meaningful name to the element (Ex:Textbox,Text Area etc..)
	 * 
	 * 
	 */
	public boolean typeKeys(By locator, Keys key, String locatorName) {
		String exception = "";
		boolean flag = false;
		try {
			Logs.info("Entering data " + key + " into " + locatorName + "  ... " + locator);
			waitForElement(locator);
			getDriver().findElement(locator).clear();
			getDriver().findElement(locator).sendKeys(Keys.TAB);
			flag = true;
		} catch (Exception e) {
			exception = "<br>Exception:" + getException(e);
			flag = false;
		} finally {
			generateReport(flag, "Enter data :" + key + " into " + locatorName + exception);
		}
		return flag;
	}

	
	/**
	 * 
	 * @param locator : Locator of drop down
	 * @param dropDownName : Drop down Name
	 * @param valueToSelect : Value to select from Drop down
	 * @return
	 */
	public boolean selectItemFromDropDown(By locator, String dropDownName, String valueToSelect) {
		try {
			
			Select dropDown = new Select(driver.findElement(locator));
			dropDown.deselectByValue(valueToSelect);
			StaticWait(1);
			generateInfoReport(valueToSelect+" has been selected from "+dropDownName);
			return true;
		}catch(Exception e) {
			generateInfoReport("Exception while selecting the value "+valueToSelect+" from "+dropDownName);
			return false;
		}
	}

	/**
	 * Performs mouse over
	 * 
	 * @param locator
	 * @param locatorName
	 * @return
	 */
	public boolean mouseover(By locator, String locatorName) {
		String exception = "";
		boolean flag = false;
		try {
			WebElement mo = getDriver().findElement(locator);
			//
			new Actions(getDriver()).moveToElement(mo).build().perform();
			ExtentTestManager.getTest().log(LogStatus.PASS, "Mouse over perfomed on " + locatorName);
			flag = true;
		} catch (Exception e) {
			exception = ", Exception:" + e.getMessage();
			flag = false;
		} finally {
			generateReport(flag, "Mouse over on " + locatorName + exception);
		}
		return flag;
	}

	/**
	 * Wait for an ElementPresent
	 * 
	 * @param locator
	 *            : Action to be performed on element (Get it from Object
	 *            repository)
	 * 
	 * @return Whether or not the element is displayed
	 */
	public boolean waitForElementPresent(By by, String locator) throws Throwable {
		boolean flag = false;
		try {
			for (int i = 0; i < 600; i++) {
				if (getDriver().findElement(by).isDisplayed()) {
					flag = true;
					return true;

				} else {
					Thread.sleep(100);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (!flag) {
				Assert.fail("Falied to locate element " + locator);
			} else if (b && flag) {
				Assert.assertTrue(true, "Successfullly located element " + locator);
			}
		}

		return flag;

	}

	/**
	 * Select a value from Dropdown using send keys
	 * 
	 * @param locator
	 *            : Action to be performed on element (Get it from Object
	 *            repository)
	 * 
	 * @param value
	 *            : Value wish type in dropdown list
	 * 
	 * @param locatorName
	 *            : Meaningful name to the element (Ex:Year Dropdown, items
	 *            Listbox etc..)
	 * 
	 */
	public boolean selectBySendkeys(By locator, String value, String locatorName) throws Throwable {

		boolean flag = false;
		try {
			getDriver().findElement(locator).sendKeys(value);
			flag = true;
		} catch (Exception e) {

		} finally {
			generateReport(flag, "Select - " + value + " in " + locatorName);
		}
		return flag;
	}

	public static int getBreakPoint(int breakPoint) {
		switch (breakPoint) {
		case 1:
			return Constantns.BREAK_POINT_1_BROWSER_WIDTH;
		case 2:
			return Constantns.BREAK_POINT_2_BROWSER_WIDTH;
		case 3:
			return Constantns.BREAK_POINT_3_BROWSER_WIDTH;
		case 4:
			return Constantns.BREAK_POINT_4_BROWSER_WIDTH;
		}
		return 4;

	}

	/**
	 * Generates galen report
	 * 
	 * @param specFileLocation
	 *            : exclude src/it/resources/ in path
	 * @param fieldName
	 * @return
	 */
	public LayoutReport call_Gallen(String specFileLocation, String fieldName) {
		
		generateInfoReport(("Galen Spec execution started ......<br>Spec :"+ReadProperties.projectLocation+specFileLocation));
		String breakPoint1 = breakPoint;
		switch (breakPoint1) {
		case "1":
			breakPoint1 = "360x700";

			break;
		case "2":
			breakPoint1 = "768x700";

			break;
		case "3":
			breakPoint1 = "1024x700";

			break;
		case "4":
			breakPoint1 = "1280x700";

			break;

		default:
			break;
		}
		LayoutReport layoutReport = null;
		generateInfoReport("	Galen info title: " + browser.toUpperCase() + "_" + fieldName + "_" + breakPoint1);
		GalenTestInfo test = GalenTestInfo.fromString(browser.toUpperCase() + "_" + fieldName + "_" + breakPoint1);
		try {
			try {
				waitForPageLoad(60);
				getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
				generateInfoReport("	Executing galen spec......");
				layoutReport = Galen.checkLayout(getDriver(), ReadProperties.projectLocation + specFileLocation,
						asList(breakPoint1));
				
				List<ValidationResult> list = layoutReport.getValidationErrorResults();

				String errors = "";

				try {
					Logs.info("	Checking galen failures ....");
					for (ValidationResult validationResult : list) {
						// System.out.println(validationResult);
						List<String> v = validationResult.getError().getMessages();
						for (int i = 0; i < v.size(); i++) {
							// System.out.println(v.get(i));
							errors = errors.concat(v.get(i) + ",</br>");
						}
					}
				} catch (Exception e) {
					generateInfoReport(getException(e));
				}

				if (errors.length() > 0) {
					generateInfoReport("Find below galen validations<br>" + errors);
				} 
				generateInfoReport("	Spec Erros count is : " + layoutReport.errors());

				String reportLink = "<a target='_blank' href=\"./Galen_Reports/report.html\">"
						+ "<b>Click here for galen report<b>" + "</a>";

				if (!layoutReport.getObjects().isEmpty()) {
					if (layoutReport.errors() > 0) {
						ExtentTestManager.getTest().log(LogStatus.WARNING, "Galen erros count:" + layoutReport.errors()
								+ "<br>Report location:" + getBoldText(reportName));
					} else {
						generatePassReportWithNoScreenShot("No Galen spec failures" + "<br>Report location:" + reportName);
					}
				}else{
					generateFailReport("No specifications are passed,  please check heat map or screenshot for more details ");
				}
				
				addScreenShotToReport(true);
				generateInfoReport(reportLink);
				// Add current url to report
				String currentURl = getDriver().getCurrentUrl();
				test.getReport().layout(layoutReport, fieldName + "	-	Current URL - " + currentURl);
				tests.add(test);
				Logs.info("	Generating galen reports .....");
				new HtmlReportBuilder().build(tests, reportName);
				// GalenPageDump dump = new GalenPageDump("Home Page");
				// dump.dumpPage(getDriver(), ReadProperties.projectLocation
				// +specFileLocation, ReadProperties.projectLocation + "Dump/");
				Logs.info("	Galen report location : " + reportName);
			} catch (Throwable e) {
				Logs.info("<br> Report location:" + reportName + "<br>Exception:<br>" + getException(e));
				ExtentTestManager.getTest().log(LogStatus.FAIL,
						"<br> Report location:" + reportName + "<br>Exception:<br>" + getException(e));

				test.getReport().error(getException(e));
				tests.add(test);
				Logs.info("	Generating galen reports .....");
				new HtmlReportBuilder().build(tests, reportName);
				Logs.info("	Galen report location : " + reportName);
			}

		} catch (Exception e) {
			generateInfoReport(getException(e));
		}
		return layoutReport;

	}

	public String get_Content_type(String url) throws IOException, InterruptedException {
		Document doc;

		String content_type = null;

		String pageSource = Jsoup.connect(url + "?faf=1&uac=79142PK").get().html();

		doc = Jsoup.parse(pageSource);
		Thread.sleep(5000);
		// get page title
		String title = doc.title();
		Logs.info("Title : " + title);

		// get all links
		try {
			Elements links = doc.select("meta[name=xml-type]");
			for (Element link : links) {

				// get the value from href attribute
				Logs.info("\n Content Type for :" + url + " is : " + link.attr("content"));
				content_type = link.attr("content");
				// Logs.info("text : " + link.text());

			}
		} catch (Exception e) {
			Logs.info(getException(e));
		}

		return content_type;

	}

	// getDriver() and screenshotName
	public String getScreenshot(WebDriver driver, String screenshotName) {
		if(driver==null){
			return "";
		}
		screenshotName = toUTF8(screenshotName);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = null;
		try {
			// below line is just to append the date format with the screenshot
			// name to avoid duplicate names
			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

			destination = "Screenshots/" + screenshotName + dateName + ".png";

			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			File finalDestination = new File(ExtentManager.getReportLocation() + "/" + destination);

			FileUtils.copyFile(source, finalDestination);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return destination;
	}

	// getDriver() and screenshotName
	public String getScreenshot(WebDriver driver, String screenshotName, boolean takeFullScreenShot) {
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = null;
		if (screenShotDriver()==null || screenShotDriver().toString().contains("null")) {
			return "";
		}
		screenshotName = toUTF8(screenshotName);
		try {
			// below line is just to append the date format with the screenshot
			// name to avoid duplicate names

			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			destination = "Screenshots/" + screenshotName + dateName + ".png";

			try {
				File screens = new File(ExtentManager.getReportLocation() + "/Screenshots");
				if (!(screens.exists() && screens.isDirectory())) {
					screens.mkdirs();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			File finalDestination = new File(ExtentManager.getReportLocation() + "/" + destination);

			Screenshot fpScreenshot = null;
			if (takeFullScreenShot) {
				fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
						.takeScreenshot(driver);
			} else {
				fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.simple()).takeScreenshot(driver);
			}

			ImageIO.write(fpScreenshot.getImage(), "PNG", finalDestination);

			System.out.println();

		} catch (Exception e) {
			// Logs.info(getException(e));

		}
		return destination;
	}

	public void generateReport(boolean status, String text) {
		generateReport(status, text,Boolean.parseBoolean(prop.getProperty("takeFullScreenShotOnFail").toLowerCase()));
	}

	private void generateReport(boolean status, String text, boolean takeFullScreenShot) {
		if (status) {
			ExtentTestManager.getTest().log(LogStatus.PASS, "Passed: " + text);
			Logs.info("Passed: " + text);
			if (prop.getProperty("takeScreenShotOnPass").equalsIgnoreCase("true")) {
				if (screenShotDriver() != null) {
					ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest()
							.addScreenCapture(getScreenshot(screenShotDriver(), String.valueOf(System.nanoTime()),takeFullScreenShot)));
				}
			}
		} else {
			Logs.error("Failed: " + text);
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Failed: " + text);
			if (screenShotDriver() != null && getProperty("takeScreenShotOnFail").equalsIgnoreCase("true")) {
				ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest()
						.addScreenCapture(getScreenshot(screenShotDriver(), String.valueOf(System.nanoTime()), takeFullScreenShot)));
			}
		}
	}

	public void generateBoldReport(String text) {
		ExtentTestManager.getTest().log(LogStatus.INFO,
				"<b style=\"background: seagreen;color:  white;\">" + text + "</b>");
		Logs.info(text);
	}

	public void setDescription(String text) {

		try {
			ExtentTestManager.getTest().setDescription(text);
		} catch (Exception e) {

			// e.printStackTrace();
		}
	}

	public void generateFailReport(String text) {
		Logs.error("Failed: " + text);
		ExtentTestManager.getTest().log(LogStatus.FAIL, "Failed -" + text);
		if (screenShotDriver() != null) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, ExtentTestManager.getTest()
					.addScreenCapture(getScreenshot(screenShotDriver(), String.valueOf(System.nanoTime()), Boolean.parseBoolean(prop.getProperty("takeFullScreenShotOnFail").toLowerCase()))));
		}
	}

	public void addScreenShotToReport() {
		ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest()
				.addScreenCapture(getScreenshot(screenShotDriver(), String.valueOf(System.nanoTime()))));
	}

	public void addScreenShotToReport(boolean isFullScreen) {
		ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest()
				.addScreenCapture(getScreenshot(screenShotDriver(), String.valueOf(System.nanoTime()), isFullScreen)));
	}

	/**
	 * generates pass or fail report based on flag status
	 * 
	 * @param text
	 */
	public void generatePassReport(String text) {
		Logs.info("Passed: " + text);
		ExtentTestManager.getTest().log(LogStatus.PASS, text);

		if (prop.getProperty("takeScreenShotOnPass").equalsIgnoreCase("true")) {
			if (screenShotDriver() != null) {
				ExtentTestManager.getTest().log(LogStatus.INFO, ExtentTestManager.getTest()
						.addScreenCapture(getScreenshot(screenShotDriver(), String.valueOf(System.nanoTime()))));
			}
		}

	}

	/**
	 * Return true if the object is visible in viewport
	 * 
	 * @param element
	 * @return
	 */
	public boolean isVisibleInViewport(WebElement element) {

		try {
			boolean b = (Boolean) ((JavascriptExecutor) ((RemoteWebElement) element).getWrappedDriver())
					.executeScript("var elem = arguments[0], " + "box = elem.getBoundingClientRect(),"
							+ "cx = box.left + box.width / 2, " + "cy = box.top + box.height / 2, "
							+ "e = document.elementFromPoint(cx, cy); " + "for (; e; e = e.parentElement) { "
							+ "if (e === elem)" + "return true; " + "}" + "return false;", element);

			return b;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Switch to frame using By element
	 * 
	 * @param by
	 * @param locator
	 */
	public void switchToFrame(By by, String locator) {
		try {
			getDriver().switchTo().frame(getDriver().findElement(by));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	/**
	 * It converts string to UTF8-8
	 * 
	 * @param str
	 * @return
	 */
	public String toUTF8(String str) {
		try {
			return new String(str.toString().getBytes(), "UTF-8");
		} catch (Exception e) {
			return str;
		}

	}

	/**
	 * This method throws error on test case fail
	 * 
	 * @throws Error
	 */
	public void throwErrorOnTestFailure() throws Error {
		String status = ExtentTestManager.getTest().getRunStatus().toString().toLowerCase();
		if (status.equals("fail")) {
			addScreenShotToReport();
			throw new Error("Test Case Failed, please go through the above test steps for more information");
		}
	}

	/**
	 * Returns rest assured base url
	 * 
	 * @return
	 */
	private String getbaseURL() {
		String s = RestAssured.baseURI;
		if (s.contains("localhost")) {
			return "";
		}
		return "<br>	baseURL : " + s;
	}

	/**
	 * Validates status code 200
	 * 
	 * @param endPoint
	 * @return
	 */
	public Response apiCallGET(String endPoint) {

		return apiCallGET(endPoint, 0);

	}

	/**
	 * Validate URL with expected status code
	 * 
	 * @param endPoint
	 *            Example :
	 *            http://contentmetadataservice-app-qa01.prf.iad1.medscape.com:8080/contentmetadataservice/getArticle?legacyId=3600842
	 *            Example : /getArticle?legacyId=3600842 , applicable when
	 *            baseURI is set at class level
	 * @param expectedStatusCode
	 * @return
	 */
	public Response apiCallGET(String endPoint, int expectedStatusCode) {

		return apiCallGET(endPoint, expectedStatusCode, "");

	}

	/**
	 * Validates status code 200 and given expectedResponseNodes
	 * 
	 * @param endPoint
	 * @param expectedResponseNodes
	 * @return
	 */
	public Response apiCallGET(String endPoint, String... expectedResponseNodes) {

		return apiCallGET(endPoint, 0, expectedResponseNodes);

	}

	/**
	 * Validates status code 200 and given expectedResponseNodes
	 * <p>
	 * 
	 * <pre>
	 * <code>
	 * String exp[] =  {"related.contentId","related.pubName|Medscape Noticias Médicas|Medscape Noticias Médicas" };	
	   apiCallGET("/widgetLinks/related/5901638", 200,exp);<code>
	 * </pre>
	 * <p>
	 * 
	 * @param endPoint
	 *            <code>{@link http://contentmetadataservice-app-qa02.prf.iad1.medscape.com:8080/contentmetadataservice/widgetLinks/related/5901638}<code>
	 * @param expectedStatusCode
	 *            <code>200<code>
	 * @param expectedResponseNodes
	 *            <code>String exp[] =
	 *            {"related.contentId","related.pubName|Medscape Noticias
	 *            Médicas|Medscape Noticias Médicas" };<code>
	 * @return
	 *         <p>
	 *         Response object
	 *         <p>
	 */
	public Response apiCallGET(String endPoint, int expectedStatusCode, String... expectedResponseNodes) {

		return apiValidateGETandPost(null, endPoint,null, "GET", null, null, expectedStatusCode, expectedResponseNodes);

	}

	/**
	 * Validates status code 200 and given expectedResponseNodes
	 * 
	 * @param responseObject
	 * @param expectedStatusCode
	 * @param expectedResponseNodes
	 * @return
	 */
	public Response apiCallGET(Response responseObject, int expectedStatusCode, String... expectedResponseNodes) {

		return apiValidateGETandPost(responseObject, null,null, "GET", null, null, expectedStatusCode,
				expectedResponseNodes);

	}

	/**
	 * Validates given expectedResponseNodes
	 * 
	 * @param responseObject
	 * @param expectedResponseNodes
	 * @return
	 */
	public Response apiCallGET(Response responseObject, String... expectedResponseNodes) {

		return apiCallGET(responseObject, 0, expectedResponseNodes);
	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, HashMap<String, Object> params) {

		return apiCallPOST(endPointURL, params, 0);

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, HashMap<String, Object> params, int expectedStatusCode) {

		return apiCallPOST(endPointURL, params, expectedStatusCode, "");

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, HashMap<String, Object> params, String... expectedResponseNodes) {

		return apiValidateGETandPost(null, endPointURL,null, "POST", params, null, 0, expectedResponseNodes);

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, HashMap<String, Object> params, int expectedStatusCode,
			String... expectedResponseNodes) {

		return apiValidateGETandPost(null, endPointURL, null,"POST", params, null, expectedStatusCode,
				expectedResponseNodes);

	}


	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(Response responseObject, int expectedStatusCode) {

		return apiCallPOST(responseObject, expectedStatusCode,  "");

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(Response responseObject ,int expectedStatusCode,
			String... expectedResponseNodes) {

		return apiValidateGETandPost(responseObject, null, null,"POST", null, null, expectedStatusCode, expectedResponseNodes);

	}

	

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, String requestBody) {

		return apiCallPOST(endPointURL, requestBody, 0);

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, String requestBody, int expectedStatusCode) {

		return apiCallPOST(endPointURL, requestBody, expectedStatusCode, "");

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, String requestBody, String... expectedResponseNodes) {

		return apiCallPOST(endPointURL, requestBody, 0, expectedResponseNodes);

	}

	/**
	 * 
	 * @param params
	 * @param endPointURL
	 * @return
	 */
	public Response apiCallPOST(String endPointURL, String requestBody, int expectedStatusCode,
			String... expectedResponseNodes) {

		return apiValidateGETandPost(null, endPointURL,null, "POST", null, requestBody, expectedStatusCode,
				expectedResponseNodes);

	}

	

	/**
	 * 
	 * @param responseObject
	 * @param endPoint
	 * @param expectedStatusCode
	 * @param expectedResponseNodes
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private Response apiValidateGETandPost(Response responseObject, String endPoint, Map<String, Object> headers, String requestType,
			HashMap<String, Object> params, String requestBody, int expectedStatusCode,
			String... expectedResponseNodes) {

		Response resp = null;
		String baseURI = getbaseURL();
		
		if (headers==null) {
			headers = new HashMap<>(Base.headers);
		}
		try {
			if (responseObject != null) {
				resp = responseObject;
			} else {
				generateInfoReport("Sending request ... : " + endPoint + baseURI + "<br>	Headers :" + headers+"<br>Params :"+params+"<br>RequestBody :"+requestBody);
				if (requestType.equalsIgnoreCase("GET")) {
					resp = given().headers(headers).when().get(endPoint).then().extract().response();
				} else if (requestType.equalsIgnoreCase("POST")) {
					if (params != null) {
						resp = given().headers(headers).parameters(params).when().post(endPoint);
					} else if(requestBody!=null){
						resp = given().headers(headers).body(requestBody).when().post(endPoint);
					}
				}
			}
		} catch (Exception e2) {
			generateFailReport(getException(e2));
			return resp;
		}

		if (expectedStatusCode != 0) {
			generateReport(resp.statusCode() == expectedStatusCode,
					"Validating status code<br>Expected :" + expectedStatusCode + "<br>Actual :" + resp.statusCode());
		}

		String responseBody = getResponseAsString(resp);

		// generateInfoReport("Response data :<br><br><pre>" +
		// responseBody+"</pre>");

		apiValidateResponseBody(responseBody, expectedResponseNodes);

		return resp;

	}

	/**
	 * Fetch response value based on parseNodeKey
	 * 
	 * @param responseObject
	 * @param parseNodeKey
	 * @return
	 */
	public String extractXmlApiValue(Response responseObject, String parseNodeKey) {

		return extractXmlApiValue(responseObject, null, parseNodeKey);

	}

	/**
	 * Fetch response value based on parseNodeKey
	 * 
	 * @param responseObject
	 * @param parseNodeKey
	 * @return
	 */
	public List<Object> apiExtractValue(Response responseObject, String parseNodeKey) {

		return extractJsonApiValue(responseObject, null, parseNodeKey);

	}

	/**
	 * Fetches value from Json body response text
	 * 
	 * @param responseBodyAsString
	 * @param parseNodeKey
	 * @return
	 */
	public List<Object> apiExtractValue(String responseBodyAsString, String parseNodeKey) {

		return extractJsonApiValue(null, responseBodyAsString, parseNodeKey);

	}

	/**
	 * Extract api response node value
	 * 
	 * @param responseObject
	 * @param responseBodyAsString
	 * @param parseNodeKey
	 * @return
	 */
	private List<Object> extractJsonApiValue(Response responseObject, String responseBodyAsString, String parseNodeKey) {
		List<Object> value =new ArrayList<>();
		try {
			JsonPath jsonPath = null;
			if (responseObject != null) {
				jsonPath = new JsonPath(getRawStringToJsonFormat(getResponseAsString(responseObject)));
			} else {
				jsonPath = new JsonPath(getRawStringToJsonFormat(responseBodyAsString));
			}
			// jsonPath.
			Logs.info("Retrieving value for :" + parseNodeKey);
			value =extractListFromJson(jsonPath, parseNodeKey);
			generateInfoReport("Retrieved value for " + getBoldText(parseNodeKey) + "<br>	Value: " + value);
			return value;
		} catch (Exception e) {
			Logs.info("Could not find :" + parseNodeKey + "<br><br>" + getResponseAsString(responseObject));
			Logs.info(getException(e));
			generateInfoReport("Retrieved value for " + getBoldText(parseNodeKey) + "<br>	Value: " + value);
		}

		return value;
	}

	/**
	 * Extract api response node value
	 * 
	 * @param responseObject
	 * @param responseBodyAsString
	 * @param parseNodeKey
	 * @return
	 */
	private String extractXmlApiValue(Response responseObject, String responseBodyAsString, String parseNodeKey) {
		String value = "";
		try {
			XmlPath xmlPath = null;
			if (responseObject != null) {
				xmlPath = new XmlPath(getResponseAsString(responseObject));
			} else {
				xmlPath = new XmlPath(responseBodyAsString);
			}
			// jsonPath.
			Logs.info("Fetching value for :" + parseNodeKey);
			value = xmlPath.getString(parseNodeKey).toString();
			generateInfoReport("Fetched value for " + parseNodeKey + "<br>	Value: " + value);
			Logs.info(parseNodeKey + " :" + value);
			return value;
		} catch (Exception e) {
			Logs.info("Could not find :" + parseNodeKey + "<br><br>" + getResponseAsString(responseObject));
			Logs.info(getException(e));
			generateInfoReport("Fetched value for " + parseNodeKey + "<br>	Value: " + value);
		}

		return value;
	}

	/**
	 * Validates Response values
	 * 
	 * @param response
	 * @param expectedResponseNodes
	 * @return
	 */
	public JsonPath apiValidateResponseBody(Response response, String... expectedResponseNodes) {

		return apiValidateResponseBody(response, null, expectedResponseNodes);
	}

	/**
	 * Validates Response values and takes Response body as String
	 * 
	 * @param responseBodyAsString
	 * @param expectedResponseNodes
	 * @return
	 */
	public JsonPath apiValidateResponseBody(String responseBodyAsString, String... expectedResponseNodes) {

		return apiValidateResponseBody(null, responseBodyAsString, expectedResponseNodes);
	}

	/**
	 * Validates Response values and takes Response body as String
	 * 
	 * @param response
	 * @param responseBodyAsString
	 * @param expectedResponseNodes
	 * @return JsonPath
	 */
	private JsonPath apiValidateResponseBody(Response response, String responseBodyAsString,
			String... expectedResponseNodes) {

		JsonPath jsonPath = null;

		if (expectedResponseNodes[0].length() > 0) {
			if (response != null) {
				responseBodyAsString = getResponseAsString(response);
			}
			generateInfoReport("Validating response data for ...<br><br>	<pre>" + responseBodyAsString + "</pre>");
			String formatedJSON =getRawStringToJsonFormat(responseBodyAsString);
			jsonPath = new JsonPath(formatedJSON);
			for (int i = 0; i < expectedResponseNodes.length; i++) {
				String currentValue = expectedResponseNodes[i];
				String data[] = currentValue.split("\\|");
				String parseNodeKey = "";
				try {
					parseNodeKey = data[0];
				} catch (Exception e1) {
					Logs.info(
							"Use | symbol to pass values parseNodeKey and parseNodeValue like parseNodeKey|parseNodeValue");
					Logs.info(getException(e1));
				}
				if (currentValue.indexOf("|") == -1) {
					List<Object> listOfFecthedValues = new ArrayList<>();
					listOfFecthedValues = extractListFromJson(jsonPath, parseNodeKey);
					boolean b = isAllNulls(listOfFecthedValues);
					
					generateReport(!b&&listOfFecthedValues.size()>0, "Validating node :" + parseNodeKey
							+ "<br>Expected :Value should not be null<br>Actual :" + listOfFecthedValues);
					continue;
				}
				String expValue = currentValue.substring(currentValue.indexOf("|") + 1);
				String[] parseNodeValue = expValue.split("\\|");
				//ArrayList expNodeValues = new ArrayList<>(parseNodeValue);
				List<Object> listOfFecthedValues = new ArrayList<>();
				listOfFecthedValues = extractListFromJson(jsonPath, parseNodeKey);

					List<Object> matchedValues = new ArrayList<>();
					List<Object> nonMatchedValues = new ArrayList<>();
					for (int j = 0; j < parseNodeValue.length; j++) {
						String cValue = parseNodeValue[j];
						if (!listOfFecthedValues.contains(cValue)) {
							nonMatchedValues.add(cValue);
						} else {
							matchedValues.add(cValue);
						}
					}
					generateReport(nonMatchedValues.size()==0, "Validating node :" + getBoldText(parseNodeKey) + "<br>Retrieved Values :"
							+ listOfFecthedValues + "<br>Matched values :" + matchedValues, "Validating node :" + getBoldText(parseNodeKey) + "<br>Retrieved Values :"
							+ listOfFecthedValues + "<br>Matched values :" + matchedValues + "<br>Not matched values :"
							+ nonMatchedValues);
				
			}
		}

		return jsonPath;
	}

	public List<Object> extractListFromJson(JsonPath jsonPath, String parseNodeKey) {
		List<Object> listOfFecthedValues = new ArrayList<>();
		try {
			listOfFecthedValues = jsonPath.get(parseNodeKey);
			
			List<Object> listOfFecthedValues_local = new ArrayList<>(listOfFecthedValues.size());
			
			for (Object object : listOfFecthedValues) {
				listOfFecthedValues_local.add(object != null ? object.toString() : null);
			}
			
			listOfFecthedValues =listOfFecthedValues_local;
			System.out.println();
		} catch (Exception e) {
			try {
				listOfFecthedValues = new ArrayList<>();
				Object value = jsonPath.get(parseNodeKey);
				if (value!=null) {
					listOfFecthedValues.add(String.valueOf(value));
				}else{
					listOfFecthedValues.add(value);
				}
			} catch (Exception e1) {
				Logs.info(getException(e1));
			}
		}
		return listOfFecthedValues;
	}

	
	private boolean isAllNulls(Iterable<?> array) {
		if (array!=null) {
			for (Object element : array)
				if (element != null) {
					return false;
				}
			return true;
		}else{
			return true;
		}
	}
	/**
	 * Returns formatted json string
	 * 
	 * @param responseBodyAsString
	 * @return
	 */
	public String getRawStringToJsonFormat(String responseBodyAsString) {
		try {
			String formatedString = "";
			int firstIndex = responseBodyAsString.indexOf("{");
			// System.out.println(s.indexOf("{"));
			int lastIndex = responseBodyAsString.lastIndexOf("}");

			if (firstIndex == -1) {
				return responseBodyAsString;
			}
			formatedString = responseBodyAsString.substring(firstIndex, lastIndex + 1);

			if (responseBodyAsString.length() != formatedString.length()) {
				generateInfoReport("Formatted json ...<br>	" + formatedString);
			}
			return formatedString;

		} catch (Exception e) {
			return responseBodyAsString;
		}
	}

	/**
	 * Returns response body as String
	 * 
	 * @param responseObject
	 * @return
	 */
	public String getResponseAsString(Response responseObject) {
		try {
			String s =responseObject.body().prettyPrint();
			//Logs.info(s);
			return s;
		} catch (Exception e) {
			return getException(e);
		}
	}

	/**
	 * Example : http://" + host + "-app-" + env +
	 * "prf.iad1.medscape.com:8080/contentmetadataservice Example : http://" +
	 * host + "-app-" + env + "prf.iad1.medscape.com:8080
	 * 
	 * @param string
	 */
	public void setBaseURI(String string) {
		RestAssured.baseURI = string;

	}

	public void setApiHeader(String key, String value) {
		headers.put(key, value);
	}

	public void setApiCookie(String cookie) {
		Logs.info("Setting cookie :" + cookie);
		setApiHeader("Cookie", cookie);
	}
	
	public void setContentType(String contentType) {
		Logs.info("Setting Content-Type :" + contentType);
		setApiHeader("Content-Type", contentType);
	}

	public void removeApiHeader(String key) {
		headers.remove(key);
	}

	public void removeApiAllHeader() {
		Iterator<String> entriesIterator = headers.keySet().iterator();
		Logs.info("Removing headers ..." + headers);
		try {
			while (entriesIterator.hasNext()) {
				headers.remove(entriesIterator.next().toString());
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public void removeApiAllHeadersAndRetainAccept() {
		removeApiAllHeader();
		headers.put("Accept", "application/json");
		Logs.info("Added default api header " + headers);
	}

	/**
	 * Takes flat file as input and returns string
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public String getFlatFileContentAsString(String filePath) throws IOException {
		Logs.info("Converting Flat file to String ...<br>"+filePath);
		if(filePath.contains("src/it/resources")){
			return new String(Files.readAllBytes(Paths.get(filePath)));
		}else{
			return new String(Files.readAllBytes(Paths.get(ReadProperties.projectLocation+filePath)));
		}
		
		
	}
	/**
	 * Click with coordinates
	 * @param by
	 * @param xoffset
	 * @param yOffSet
	 */
	public  void clickCordinates(By by, int xoffset, int yOffSet) {
		try {
			new Actions(getDriver()).moveToElement(getDriver().findElement(by), xoffset, yOffSet).click().build().perform();
			generatePassReport("Perfomed click on :"+by+" with coordinates x:"+xoffset+" | y:"+yOffSet);
		} catch (Exception e) {
			generatePassReport("Failed  click on :"+by+" with coordinates x:"+xoffset+" | y:"+yOffSet);
		}
	}
	
	public void dropDownByVisibleText(By by, String select, String dropdownmenu) {
		try {
			waitForElement(by);
			Select dropdown = new Select(getDriver().findElement(by));
			dropdown.selectByVisibleText(select);
			generatePassReport(dropdownmenu + "  selected successfully with " + select);

		} catch (Exception e) {
			generateFailReport(
					dropdownmenu + "  selected successfully with " + select + "<br>" + UtilityMethods.getException(e));
		}

	}
	
	
	public void dropDownByValue(By by, String value, String dropdownmenu) {
		try {
			waitForElement(by);
			Select dropdown = new Select(getDriver().findElement(by));
			dropdown.selectByValue(value);
			generatePassReport(dropdownmenu + "  selected successfully with " + value);

		} catch (Exception e) {
			generateFailReport(
					dropdownmenu + "  selected successfully with " + value + "<br>" + UtilityMethods.getException(e));
		}

	}
	
	public void dropDownByIndex(By by, int index, String dropdownmenu) {
		try {
			waitForElement(by);
			Select dropdown = new Select(getDriver().findElement(by));
			dropdown.selectByIndex(index);
			generatePassReport(dropdownmenu + "  selected successfully with " + index);

		} catch (Exception e) {
			generateFailReport(
					dropdownmenu + "  selected successfully with " + index + "<br>" + UtilityMethods.getException(e));
		}

	}
	
	public void waitForPageLoaded() {
		waitForPageLoad(Integer.parseInt(getProperty("waitForPageLoadTimeOut")));
    }

	public void waitForPageLoad(int seconds) {
		try {
			generateInfoReport("Waiting for complete page load ....");
			WebDriverWait wait = new WebDriverWait(getDriver(), seconds);
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
		} catch (Exception e) {
			generateInfoReport(("Page not completely loaded after  "+seconds+" seconds"));
		}
	}
	
	public Response apiCallGET(String endPoint, Map<String, Object> headers, int expectedStatusCode, String... expectedResponseNodes) {
		return apiValidateGETandPost(null, endPoint,headers, "GET", null, null, expectedStatusCode, expectedResponseNodes);
	}
	
	public Response apiCallGET(String endPoint, Map<String, Object> headers, int expectedStatusCode) {
		return apiValidateGETandPost(null, endPoint,headers, "GET", null, null, expectedStatusCode, "");
	}
	
	public Response apiCallGET(String endPoint, Map<String, Object> headers) {
		return apiValidateGETandPost(null, endPoint,headers, "GET", null, null, 0, "");
	}
	
	/***
	 * This method is used to update data in a DB table based on provided query
	 * 
	 * @return String
	 * @throws Exception
	 */
	public void deleteDBData(Connection dbCon, String dbQuery) {
		try {
			Statement stmt = dbCon.createStatement();
			stmt.executeUpdate(dbQuery);
			generatePassReport("Excuted query successfully :"+dbQuery);
		} catch (Exception e) {
			generateFailReport("Exception caught: " + e.getMessage());
		}
	}
}
