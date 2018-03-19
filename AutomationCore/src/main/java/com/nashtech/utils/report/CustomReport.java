package com.nashtech.utils.report;

import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.uncommons.reportng.HTMLReporter;

public class CustomReport extends HTMLReporter {
	@Override
	public void generateReport(java.util.List<XmlSuite> xmlSuites,
			java.util.List<ISuite> suites, String s) {
		
//		String source = Common.strWorkspacepath + "\\test-output";
//		String dest = Common.strWebDailyReportFolder + "\\html_report";
//		try {
//			Common.copyDirectory(source, dest);
//		}
//		catch(Exception e) {
//			
//		}
		
//		try {
//			Common.compressReport();
//			if (Common.strMailTrigger.equalsIgnoreCase("Y")) {
//				Common.triggerEmail();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}
}
