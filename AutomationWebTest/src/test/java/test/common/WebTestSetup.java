package test.common;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.nashtech.common.Common;
import com.nashtech.common.Constant;
import com.nashtech.core.web.WebDriverMethod;
import com.nashtech.core.web.WebTestBaseSetup;
import com.nashtech.utils.excel.ExcelReader;
import com.nashtech.utils.excel.ExcelWriter;
import com.nashtech.utils.report.Log;
import com.nashtech.utils.report.TestngLogger;

import test.common.WebProjectConstant;

public class WebTestSetup extends WebTestBaseSetup {

	// Web driver
	public WebDriverMethod driver;
	
	// Configure for the Test Data path
	public String testDataPath = WebProjectConstant.testDataPath;
	public String testCaseFileName = WebProjectConstant.excelTestcaseFileName;
	public String testCaseSheetName = WebProjectConstant.excelTestcaseSheetName;
	public String resultTestDataColumName = WebProjectConstant.resultTestDataColumn;
	public int resultTestCaseColumIndex = WebProjectConstant.resultTestCaseColumn;

	// Test Data sheet
	public String strTestCaseName;
	public String testDataSheetName;

	// Variables for test running
	private int dataIndex = 2;
	private int failedTestNumber = 0;

	// Excel reader and writer
	public ExcelReader excelReader;
	public ExcelWriter excelWriter;

	public Object[][] getTestProvider() throws Exception {
		// return the data from excel file
		Object[][] data = excelReader.getDictionaryExcelData(testDataSheetName);
		return data;
	}

	@BeforeSuite
	public void beforeSuite() throws Exception {
		
		super.beforeSuite();

		/*********** Migrate Test Data *************************/
		String strOriginalData = Common.correctPath(Common.strWorkspacepath
				+ testDataPath + testCaseFileName);
		String strNewData = Common.correctPath(Common.strWebDailyReportFolder
				+ testCaseFileName);
		Common.copyFile(strOriginalData, strNewData);
		testDataPath = strNewData;
		
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		
		super.beforeClass();
		
		/*********** Init excel *************************/
		excelReader = ExcelReader.getInstance(Common.strWebDailyReportFolder,
				testCaseFileName);
		excelWriter = ExcelWriter.getInstance(Common.strWebDailyReportFolder,
				testCaseFileName);
		Log.startTestCase(this.strTestCaseName);
		driver = new WebDriverMethod();
		
	}

	@BeforeMethod
	public void beforeMethod(Method method, Object[] data) throws Exception {
		Object[][] dataProvider = getTestProvider();
		if (dataProvider.length > 1) {
			Log.info("+++++++++ Start testing with row: " + (dataIndex - 1)
					+ " ++++++++++++++");
		}
		
		super.beforeMethod(method, dataProvider[dataIndex-2]);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws Exception {

		try {
			// Write result of test to excel file
			if (result.getStatus() == ITestResult.SUCCESS) {
				excelWriter.setCellData(testDataSheetName, dataIndex,
						resultTestDataColumName, Constant.PASS);
			} else if (result.getStatus() == ITestResult.SKIP) {
				excelWriter.setCellData(testDataSheetName, dataIndex,
						resultTestDataColumName, Constant.SKIP);
			} else {
				excelWriter.setCellData(testDataSheetName, dataIndex,
						resultTestDataColumName, Constant.FAIL);
				this.failedTestNumber++;
			}
		} catch (Exception e) {
			Log.error("There is an error when writing to the row [" + dataIndex
					+ "] of the data sheet [" + testDataSheetName + "]");
			TestngLogger.writeLog("There is an error when writing to the row ["
					+ dataIndex + "] of the data sheet [" + testDataSheetName
					+ "]");
		}
		dataIndex++;
	}

	@AfterClass
	public void afterClass() throws Exception {

		try {
			Log.endTestCase(this.strTestCaseName);
			// If all test data passed, the test case is passed
			if (this.failedTestNumber == 0) {
				excelWriter.writeResultToExcel(testCaseSheetName,
						strTestCaseName, resultTestCaseColumIndex,
						Constant.PASS);
			}
			// If there is any test data failed, the test case is failed
			else {
				excelWriter.writeResultToExcel(testCaseSheetName,
						strTestCaseName, resultTestCaseColumIndex,
						Constant.FAIL);
			}
		} catch (Exception e) {
			Log.error("There is an error when writing the test case name ["
					+ strTestCaseName + "] the master sheet ["
					+ testCaseSheetName + "]");
			TestngLogger
					.writeLog("There is an error when writing to the test case name ["
							+ strTestCaseName
							+ "] the master sheet ["
							+ testCaseSheetName + "]");
		}
		driver.closeDriver();
	}

	@AfterSuite
	public void afterSuite() {
		super.afterSuite();
	}
}
