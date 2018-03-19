package com.nashtech.common;

public class Constant {

	// Delay time
	public static final int DEFAULT_WAITTIME_SECONDS = 30;
	public static final int SMALL_WAITTIME_SECONDS = 10; 
	public static final int TINY_WAITTIME_SECONDS = 10; 
	public static final int TIMEOUT_STEPS_SECONDS = 5; 
	public static final int IMPLICIT_WAIT_TIME = 20;
//	public static final int TINY_WAITTIME_SECONDS = 3;
	
	// Date time format
	public static final String TIME_STAMP_1 = "dd/MM/yyyy";
	public static final String TIME_STAMP_2 = "MMM-dd-yyyy_hmma z";
	public static final String TIME_STAMP_3 = "MM-dd-yyyy_HH-ss";
	public static final String TIME_STAMP_4 = "dd-MM-yyyy";
	public static final String TIME_STAMP_5 = "dd-MM-yyyy HHmmss";

	// Excel format
	public static final String XSSF_EXTENSION = ".xlsx";
	public static final String HSSF_EXTENSION = ".xls";

	// Configuration file path
	public static final String configFilePath = "/config/config.properties";
	public static final String configLogFilePath = "/config/log4j2.xml";
	public static final String configReportFilePath = "/config/extent-config.xml";
	public static final String webReportFolder = "/reports/web/";
	public static final String mobileReportFolder = "/reports/mobile/";
//	public static final String resourceFolder = "/src/resource/TestData/";

	// AutoIT Script path
	public static final String autoItFolder = "/src/main/resources/AutoIT/";
	
	// Os types
	public static final String driverFolder = "/src/main/resources/Driver/";
	public static final String WINDOWS = "Windows";
	public static final String LINUX = "Linux";
	public static final String MAC = "mac";
	
	public static final String reportZipFileName = "Reporter.zip";
	public static final String reportFileName = "Reporter.html";
	
	//Item properties
	public static final String failColor = "rgba(38, 38, 38, 1)";
	
	// Define test results
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	public static final String SKIP = "SKIP";
	
	// Screenshot format
	public static final String SCREENSHOT_FORMAT = "jpg";
	
	
}
