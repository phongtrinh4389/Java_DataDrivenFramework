package test.testcases.login;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nashtech.utils.report.HtmlReporter;

import test.common.WebTestSetup;
import test.page.objects.EntryPage;
import test.page.objects.HomePage;
import test.page.objects.LoginPage;

public class LoginSuccessTest extends WebTestSetup{

	public final String DataSheetName = "LoginSuccess";
	public final String TestCaseName = "Test Case 2";
	
	public LoginSuccessTest() {
		
		testDataSheetName = DataSheetName;
		strTestCaseName = TestCaseName;
	}
	
	
	@DataProvider(name=DataSheetName)
	public Object[][] LoginSuccessTestData() throws Exception{
		//return the data from excel file
		Object[][] data = getTestProvider();
		return data;
	}

	@Test(dataProvider = DataSheetName)
	public void LoginSuccessTestMethod(Object data[]) throws Exception {
		
		EntryPage entryPage = new EntryPage(driver, data);
		LoginPage loginPage = new LoginPage(driver, data);
		HomePage homePage = new HomePage(driver, data);
		
		/*********** on Entry Page ***************************/
		loginPage = entryPage.clickMyAccount();
		HtmlReporter.label("clicked on my account");
		/*********** on Login Page ***************************/
		// Try to login
		loginPage.login();
		// Check login success
		homePage = loginPage.verifyLoginSuccess();
		
		/*********** on Home Page ***************************/
		// Check the title of Home Page
		homePage.checkTitle();
		// Check username title
		homePage.checkHomepage();
		// Logoff
		homePage.logoff();
	}
	
}
