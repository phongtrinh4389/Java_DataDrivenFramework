package com.nashtech.core.web;

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
import com.nashtech.utils.report.HtmlReporter;

public class WebTestBaseSetup   {


	@BeforeSuite
	public void beforeSuite() throws Exception {
		/*********** Create Report Folder ******************/
		Common.initReports();

		/*********** Init Html reporter *************************/
		HtmlReporter.setReporter(Common
				.correctPath(Common.strWebDailyReportFolder + "\\"
						+ Constant.reportFileName));
	}

	@BeforeClass
	public void beforeClass() throws Exception {
		
		HtmlReporter.createTest(this.getClass().getName());
		
	}

	@BeforeMethod
	public void beforeMethod(Method method, Object[] data) throws Exception{
		
		HtmlReporter.createNode(this.getClass().getName(), method.getName(),
				Common.getDataProviderString(data));
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws Exception {
		
	}

	@AfterClass
	public void afterClass() throws Exception {

		
	}

	@AfterSuite
	public void afterSuite() {
		HtmlReporter.flush();
	}
}
