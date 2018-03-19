package test.testcases.login;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.common.WebTestSetup;
import test.page.objects.EntryPage;
import test.page.objects.LoginPage;

public class LoginUnsuccessTest extends WebTestSetup{

	public final String DataSheetName = "LoginUnsuccess";
	public final String TestCaseName = "Test Case 1";
	
	public LoginUnsuccessTest() {
		
		testDataSheetName = DataSheetName;
		strTestCaseName = TestCaseName;
	}
	
	@DataProvider(name=DataSheetName)
	public Object[][] LoginUnsuccessTestData() throws Exception{
		//return the data from excel file
		Object[][] data = getTestProvider();
		return data;
	}

	@Test(dataProvider = DataSheetName)
	public void LoginUnsuccessTestMethod(Object data[]) throws Exception {
		
		EntryPage entryPage = new EntryPage(driver, data);
		LoginPage loginPage = new LoginPage(driver, data);
		
		/*********** on Entry Page ***************************/
		loginPage = entryPage.clickMyAccount();
		
		/*********** on Login Page ***************************/
		// Try to login
		loginPage.login();
		// Check error message
		loginPage.verifyLoginUnsuccess();
	}
	
}
