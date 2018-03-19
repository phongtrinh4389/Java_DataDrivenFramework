package test.page.objects;

import org.openqa.selenium.By;

import com.nashtech.core.web.WebDriverMethod;

import test.common.WebProjectConstant;

public class EntryPage{

	private WebDriverMethod driverMethod;
	private Object [] data;

	// Web Element Locators
	private By lnkMyAccount = By.id("account");
	
	public EntryPage(WebDriverMethod driverMethod, Object [] data) {
		this.driverMethod = driverMethod;
		this.data = data;
	}
	
	
	public LoginPage clickMyAccount() throws Exception {
		
		driverMethod.openUrl(WebProjectConstant.url);
		driverMethod.click("My Account Link", lnkMyAccount);
		
		return new LoginPage(driverMethod, data);
	}
	
	public void checkMyAccount() throws Exception {
		driverMethod.compareScreenshot("MyAccount", lnkMyAccount);
	}
	
	public void checkLayout() throws Exception {
		driverMethod.compareScreenshot("EntryPage");
	}
}
