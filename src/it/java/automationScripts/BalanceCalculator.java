package automationScripts;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.fxHelper.general.common.ActionType;
import com.fxHelper.general.common.ReadProperties;

public class BalanceCalculator extends ActionType{
	String url = ReadProperties.url;
	By sourceCurrency = By.id("");
	By destinationCurrency = By.id("");
	By amountRequired = By.id("");
	By submitButton = By.id("");
	By resetButton = By.id("");
	
	@Test(dataProvider = "")
	public void validateBalanceCalculator(String source, String destination, String amount){
		getDriver().get(url);
		selectItemFromDropDown(sourceCurrency, "Source Currency", source);
		selectItemFromDropDown(destinationCurrency, "Destination Currency", destination);
		type(amountRequired, amount, "Required Amount");
		getDriver().findElement(submitButton).click();
		try{
			//logic to check 5 days values
		}catch(Exception e){
			
		}
	}
	
	@Test void validateDefaultValues(){
		
	}
	
	@Test(dataProvider = "")
	public void validateResetOption(String source, String destination, String amount){
		getDriver().get(url);
		selectItemFromDropDown(sourceCurrency, "Source Currency", source);
		selectItemFromDropDown(destinationCurrency, "Destination Currency", destination);
		type(amountRequired, amount, "Required Amount");
		getDriver().findElement(resetButton).click();
		try{
			//logic to check 5 days values
		}catch(Exception e){
			
		}
	}

}
