package test.page.objects;

import org.openqa.selenium.By;

import com.nashtech.common.Common;
import com.nashtech.core.web.WebDriverMethod;

public class LoginPage{

	private WebDriverMethod driverMethod;
	private Object [] data;

	// Web Element Locators
	private By userName = By.id("log");
	private By password = By.id("pwd");
	private By btnLogin = By.id("login");
	private By errorMessage = By.xpath("//*[@id='ajax_loginform']//p[@class='response']");
	
	public LoginPage(WebDriverMethod driverMethod, Object [] data) {
		this.driverMethod = driverMethod;
		this.data = data;
	}
	
	public void login() throws Exception {
		
		driverMethod.inputText("user name", userName, Common.getCellDataProvider(data, "username"));
		driverMethod.inputText("password", password, Common.getCellDataProvider(data, "password"));
		driverMethod.click("Login", btnLogin);
	}
	
	public void verifyLoginUnsuccess() throws Exception {
		driverMethod.verifyText("error message", errorMessage, Common.getCellDataProvider(data, "message"));
	}
	
	public HomePage verifyLoginSuccess() throws Exception {
		
		driverMethod.verifyEqual(driverMethod.isElementPresent(btnLogin), false);
		return new HomePage(driverMethod, data);
	}

}
