package test.page.objects;

import org.openqa.selenium.By;

import com.nashtech.common.Common;
import com.nashtech.core.web.WebDriverMethod;

public class HomePage {
	
	private WebDriverMethod driverMethod;
	private Object [] data;
	
	// Web Element Locators
	private By lnkLogOut = By.id("account_logout");
	private By usernameTitle = By.xpath("//*[@id='wp-admin-bar-my-account']/a");

	public HomePage(WebDriverMethod driverMethod, Object [] data) {
		this.driverMethod = driverMethod;
		this.data = data;
	}
	
	public void checkTitle() throws Exception {
		driverMethod.verifyTitle(Common.getCellDataProvider(data, "Home Page Title"));
	}
	
	public void checkHomepage() throws Exception {
		driverMethod.verifyText("User Name Title", usernameTitle, Common.getCellDataProvider(data, "Username"));
		driverMethod.verifyEqual(driverMethod.isElementPresent(lnkLogOut), true);
	}
	
	public LoginPage logoff() throws Exception {
		driverMethod.click("Log off", lnkLogOut);
		return new LoginPage(driverMethod, data);
	}

}
