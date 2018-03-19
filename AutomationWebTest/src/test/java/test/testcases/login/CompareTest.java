package test.testcases.login;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import test.common.WebTestSetup;
import test.page.objects.EntryPage;

public class CompareTest extends WebTestSetup{

	public final String DataSheetName = "LoginSuccess";
	public final String TestCaseName = "Test Case 2";
	
	public CompareTest() {
		
		testDataSheetName = DataSheetName;
		strTestCaseName = TestCaseName;
	}
	
	
	@DataProvider(name=DataSheetName)
	public Object[][] LoginClientLocatorData() throws Exception{
		//return the data from excel file
		Object[][] data = getTestProvider();
		return data;
	}

	@Test(dataProvider = DataSheetName)
	public void LoginClientLocator(Object data[]) throws Exception {
		
		EntryPage entry = new EntryPage(driver, data);
		entry.clickMyAccount();
//		entry.checkMyAccount();
		entry.checkLayout();
	}
	
}
