package com.nashtech.utils.report;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.nashtech.common.Common;

public class SuiteListener implements ISuiteListener {

	public static int globalitv;

	@Override
	public synchronized void onStart(ISuite suite) {
		// HtmlReporter.getReporter(Common.correctPath(Common.strWebDailyReportFolder
		// + "\\" + Constant.reportFileName));
	}

	@Override
	public synchronized void onFinish(ISuite suite) {
		
		try {
			String strMobileFlag = Common.getConfigValue("MobileFlag");
			String strEmailTrigger = Common.getConfigValue("EmailTrigger");
			Email email = new Email();
			
			// Mobile testing and Trigger email
			if (strMobileFlag.equalsIgnoreCase("y") && strEmailTrigger.equalsIgnoreCase("y")) {
				email.compressReport(Common.strMobileReportFolder + Common.date + ".zip",
						Common.strMobileDailyReportFolder,
						Common.strMobileReportFolder);
				email.sendEmail(Common.strMobileReportFolder + Common.date + ".zip");
			}
			// Web testing and Trigger email
			else if (strEmailTrigger.equalsIgnoreCase("y")){
				email.compressReport(Common.strWebReportFolder + Common.date + ".zip",
						Common.strWebDailyReportFolder,
						Common.strWebReportFolder);
				email.sendEmail(Common.strWebReportFolder + Common.date + ".zip");
			}
		} catch (Exception e) {
			Log.error("Can't compress the report");
		}

	}

}