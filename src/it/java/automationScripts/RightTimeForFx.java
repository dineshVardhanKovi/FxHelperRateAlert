package automationScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fxHelper.general.common.ActionType;
import com.fxHelper.general.common.XlRead;

public class RightTimeForFx extends ActionType {
	
	String URL = "file:///C:/Users/tnamburi/Downloads/dbstask-master/dbstask-master/index.html";
	
	By fromDate = By.id("fromDate");
	By toDate = By.id("toDate");
	By baseCurrency = By.id("s2id_baseCurrency");
	By exchangeCurrency = By.id("s2id_exchangeCurrency");
	By submitButton = By.id("send");
	By recordsOutput = By.id("");
	By resetButton = By.id("reset");
	
	
	
	
	private void compareResultEqual(Object expected, Object actual) {
		try {
			Assert.assertEquals(actual, expected);
			generatePassReportWithNoScreenShot(expected+" and "+actual+" are equal");
		}catch(AssertionError e) {
			generateFailReport("Expected value is "+expected+". But, actual value is "+actual);
		}
	}
	/**
	 * This method is to test date fields are empty by default
	 */
	@Test(priority = 0)
	public void verifyDateFieldByDefaultEmpty(){
		getDriver().get(URL);
		compareResultEqual(getDriver().findElement(fromDate).getAttribute("placeholder"), "YYYY-MM-DD");
		compareResultEqual(getDriver().findElement(toDate).getAttribute("placeholder"), "YYYY-MM-DD");
	}
	/**
	 * This method to test default values in the country selection
	 */
	@Test(priority = 1)
	public void verifyDefaultTextInCountrySelection(){
		By countrySelection1 = By.id("select2-chosen-1");
		By countrySelection2 = By.id("select2-chosen-2");
		compareResultEqual(getDriver().findElement(countrySelection1).getText(), "eg. India");
		compareResultEqual(getDriver().findElement(countrySelection2).getText(), "eg. USA");
	}
	
	/**
	 * This method is to test the reset functionality after filling some details
	 */
	@Test(dataProvider = "dataProviderForVerifyReset", priority = 2)
	public void verifyResetFunctionality(String...arg){
		selectItemFromDropDown(baseCurrency, "Base Currency", arg[0]);
		selectItemFromDropDown(exchangeCurrency, "Exchange Currency", arg[1]);
		type(fromDate, arg[2], "From Date");
		type(toDate, arg[3], "To Date");
		getDriver().findElement(resetButton).click();
		
		verifyDefaultTextInCountrySelection();
		verifyDateFieldByDefaultEmpty();
	}
	@DataProvider
	public String[][] dataProviderForVerifyReset() {
		return XlRead.fetchData("TestData/testData.xls", "resetValidation");
	}
	
	/**
	 * This method is to test end to end scenario, to check whether records loaded after user submitting the inputs
	 * @param arg : all parameters, two currency options and two dates
	 */
	@Test (dataProvider = "dataProviderForVerifyResultRecordsDisplayed", priority = 3)
	public void verifyResultRecordsDisplayed(String...arg)  {
		getDriver().get(URL);
		waitForElement(baseCurrency);
		try{
			selectItemFromDropDown(baseCurrency, "Base Currency", arg[0]);
			selectItemFromDropDown(exchangeCurrency, "Exchange Currency", arg[1]);
			type(fromDate, arg[2], "From Date");
			type(toDate, arg[3], "To Date");
			getDriver().findElement(submitButton).click();
			try{
				Assert.assertTrue(getDriver().findElement(recordsOutput).isDisplayed());
				generatePassReportWithNoScreenShot("Reports shown as output");
			}catch(AssertionError e){
				generateFailReport("Output report not shown after user submits the input");
			}catch(NoSuchElementException e){
				generateFailReport("Output report not loaded in DOM");
			}
		}catch(Exception e){
			generateFailReport("Exception while validating records display"+e.toString());
		}
	}
	
	@DataProvider
	public String[][] dataProviderForVerifyResultRecordsDisplayed() {
		return XlRead.fetchData("TestData/testData.xls", "submitValidation");
	}
}