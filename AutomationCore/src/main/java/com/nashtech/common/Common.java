package com.nashtech.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Listeners;

import com.nashtech.utils.report.*;

@Listeners(SuiteListener.class)
public class Common {

	public static LinkedList<Integer> passlistitv = new LinkedList<Integer>();

	// dd-MM-yyyy
	public static String date;

	// Setting Path
	public static String strWorkspacepath = getProjectPath();

	/*********** Setting path for Web Report *******************/
	// Web Report Folder : ./reports/web/
	public static String strWebReportFolder;
	// Baseline Screenshot folder: ./reports/web/BaseLineScreenshot/
	public static String strWebBaseLineScreenshotFolder;
	// Daily Report Folder: ./reports/web/dd-MM-yyyy HHmmss
	public static String strWebDailyReportFolder;
	// Daily Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/Screenshot
	public static String strWebScreenshotFolder;
	// Daily UI Test Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/UI
	public static String strWebUITestScreenshotFolder;
	// Daily UI Test, Different Screenshot Folder: ./reports/web/dd-MM-yyyy
	// HHmmss/Screenshot/Different
	public static String strWebDiffScreenshotFolder;
	// Daily UI Test, Different Screenshot Folder: ./reports/web/dd-MM-yyyy
	// HHmmss/Screenshot/Actual
	public static String strWebActualScreenshotFolder;

	/*********** Setting path for Mobile Report *******************/
	// Web Report Folder : ./reports/mobile/
	public static String strMobileReportFolder;
	// Baseline Screenshot folder: ./reports/Mobile/BaseLineScreenshot/
	public static String strMobileBaseLineScreenshotFolder;
	// Daily Report Folder: ./reports/Mobile/dd-MM-yyyy HHmmss
	public static String strMobileDailyReportFolder;
	// Daily Screenshot Folder: ./reports/Mobile/dd-MM-yyyy HHmmss/Screenshot
	public static String strMobileScreenshotFolder;
	// Daily UI Test Screenshot Folder: ./reports/Mobile/dd-MM-yyyy HHmmss/UI
	public static String strMobileUITestScreenshotFolder;
	// Daily UI Test, Different Screenshot Folder: ./reports/Mobile/dd-MM-yyyy
	// HHmmss/Screenshot/Different
	public static String strMobileDiffScreenshotFolder;
	// Daily UI Test, Different Screenshot Folder: ./reports/Mobile/dd-MM-yyyy
	// HHmmss/Screenshot/Actual
	public static String strMobileActualScreenshotFolder;

	public static String strOutputFileName = correctPath(strWebReportFolder
			+ Constant.reportZipFileName);

	/**
	 * This method is used to get the project path
	 * 
	 * @author Hanoi Automation team
	 * @return The absolute path of project
	 */
	public static String getProjectPath() {

		return System.getProperty("user.dir");

	}

	/**
	 * Create the Reports folder
	 * @throws Exception 
	 */
	public static void initReports() throws Exception {

		date = new SimpleDateFormat(Constant.TIME_STAMP_5).format(new GregorianCalendar().getTime());
		
		String strMobileFlag = Common.getConfigValue("MobileFlag");
		
		/*********** Setting path for Web Report *******************/
		// Web Report Folder : ./reports/web/
		strWebReportFolder = correctPath(getProjectPath() + Constant.webReportFolder);
		
		// Baseline Screenshot folder: ./reports/web/BaseLineScreenshot/
		strWebBaseLineScreenshotFolder = correctPath(strWebReportFolder + "/BaseLineScreenshot/");
		
		// Daily Report Folder: ./reports/web/dd-MM-yyyy HHmmss
		strWebDailyReportFolder = correctPath(strWebReportFolder + "/" + date + "/");
		
		// Daily Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/Screenshot
		strWebScreenshotFolder = correctPath(strWebDailyReportFolder + "/Screenshots/");
		
		// Daily UI Test Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/UI
		strWebUITestScreenshotFolder = correctPath(strWebDailyReportFolder + "/UI/");
		
		// Daily UI Test, Different Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/Screenshot/Different
		strWebDiffScreenshotFolder = correctPath(strWebUITestScreenshotFolder + "/Different/");
		
		// Daily UI Test, Different Screenshot Folder: ./reports/web/dd-MM-yyyy HHmmss/Screenshot/Actual
		strWebActualScreenshotFolder = correctPath(strWebUITestScreenshotFolder + "/Actual/");

		/*********** Setting path for Web Report *******************/
		// Web Report Folder : ./reports/web/
		strMobileReportFolder = correctPath(getProjectPath() + Constant.mobileReportFolder);
		
		// Baseline Screenshot folder: ./reports/Mobile/BaseLineScreenshot/
		strMobileBaseLineScreenshotFolder = correctPath(strMobileReportFolder + "/BaseLineScreenshot/");
		
		// Daily Report Folder: ./reports/Mobile/dd-MM-yyyy HHmmss
		strMobileDailyReportFolder = correctPath(strMobileReportFolder + "/" + date + "/");
		
		// Daily Screenshot Folder: ./reports/Mobile/dd-MM-yyyy HHmmss/Screenshot
		strMobileScreenshotFolder = correctPath(strMobileDailyReportFolder + "/Screenshots/");
		
		// Daily UI Test Screenshot Folder: ./reports/Mobile/dd-MM-yyyy HHmmss/UI
		strMobileUITestScreenshotFolder = correctPath(strMobileDailyReportFolder + "/UI/");
		
		// Daily UI Test, Different Screenshot Folder: ./reports/Mobile/dd-MM-yyyy
		// HHmmss/Screenshot/Different
		strMobileDiffScreenshotFolder = correctPath(strMobileUITestScreenshotFolder + "/Different/");
		
		// Daily UI Test, Different Screenshot Folder: ./reports/Mobile/dd-MM-yyyy
		// HHmmss/Screenshot/Actual
		strMobileActualScreenshotFolder = correctPath(strMobileUITestScreenshotFolder + "/Actual/");
		
		// Create Reports folder for Mobile
		if(strMobileFlag.equalsIgnoreCase("y")) {
//			createPath(strMobileBaseLineScreenshotFolder);
			createPath(strMobileDailyReportFolder);
			createPath(strMobileScreenshotFolder);
//			createPath(strMobileUITestScreenshotFolder);
//			createPath(strMobileDiffScreenshotFolder);
//			createPath(strMobileActualScreenshotFolder);
		}
		else {
			// Create Reports folder for Web
//			createPath(strWebBaseLineScreenshotFolder);
			createPath(strWebDailyReportFolder);
			createPath(strWebScreenshotFolder);
//			createPath(strWebUITestScreenshotFolder);
//			createPath(strWebDiffScreenshotFolder);
//			createPath(strWebActualScreenshotFolder);
		}

	}

	/**
	 * This method is used to read the configuration file
	 * 
	 * @author Hanoi Automation team
	 * @param strKey
	 *            The key in the configuration file
	 * @return The value of key
	 * @throws Exception
	 */
	public static String getConfigValue(String strKey) throws Exception {

		Properties prop = new Properties();
		InputStream input = null;
		input = Class.class.getResourceAsStream(Constant.configFilePath);
		
		prop.load(input);
		
		return prop.getProperty(strKey);
	}
	
	/**
	 * This function is used to get the value of an dictionary based on the key
	 * 
	 * @param row
	 * @param key
	 * @return A String value
	 */
	@SuppressWarnings("unchecked")
	public static String getCellDataProvider(Object row[], String key) {

		String data = "";
		HashMap<String, String> temp = new HashMap<>();

		for (Object cell : row) {
			temp = (HashMap<String, String>) cell;
			if (temp.containsKey(key)) {
				data = temp.get(key);
				break;
			}

		}
		return data;
	}


	/**
	 * To get the string that represent for one row of Data Provider
	 * @param row
	 * @return
	 */
	public static String getDataProviderString(Object[] row) {
		ArrayList<Object> list = new ArrayList<>();
		Object[] array = (Object[]) row[0];
		for(int i = 0; i< array.length; i++) {
			list.add(array[i]);
		}
		
		return list.toString();
	}
	
	/**
	 * Delay method
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @param seconds
	 *            The delay time in seconds
	 * @throws Exception
	 */
	public static void sleep(int seconds){
		try {
			long milliSeconds = seconds * 1000;
			Thread.sleep(milliSeconds);
		}
		catch(Exception e) {
			
		}

	}

	/**
	 * This method is used to get current date (dd/mm/yyyy)
	 * 
	 * @author Hanoi Automation team
	 * @return The current date in String
	 */
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat(Constant.TIME_STAMP_1);
		Date date = new Date();
		String strCurrentDate = dateFormat.format(date);
		return strCurrentDate;
	}

	/**
	 * Get the current date and time (MMM-dd-yyyy_hmma z)
	 * 
	 * @author Hanoi Automation team
	 * @return The current date time as String
	 */
	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(Constant.TIME_STAMP_2);
		Date date = new Date();
		String strCurrentDatetime = dateFormat.format(date);
		return strCurrentDatetime;
	}



	/**
	 * Get the list of itv
	 * 
	 * @author Hanoi Automation team
	 * @return
	 * @throws Exception
	 */
	public static LinkedList<Integer> generateitvlist() throws Exception {

		String strIteration = Common.getConfigValue("Iteration");

		LinkedList<Integer> listitv = new LinkedList<Integer>();
		System.out.println("strIterastion" + strIteration);
		if (strIteration.contains("-")) {
			LinkedList<String> numbers = new LinkedList<String>();
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(strIteration);
			while (m.find()) {
				numbers.add(m.group());
			}
			System.out.println("if part- " + numbers);
			int min = Integer.parseInt(numbers.get(0));
			int max = Integer.parseInt(numbers.get(1));
			for (int z = min; z <= max; z++) {
				listitv.add(z);
			}
		} else {
			LinkedList<String> numbers = new LinkedList<String>();
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(strIteration);
			while (m.find()) {
				numbers.add(m.group());
			}
			System.out.println("else part, " + numbers);
			int size = numbers.size();
			System.out.println("size is " + size);
			for (int t = 0; t < size; ++t) {
				listitv.add(Integer.parseInt(numbers.get(t)));
			}
		}
		return listitv;
	}

	/**
	 * Get the Test Files folder path
	 * 
	 * @author Hanoi Automation team
	 * @return The path of Test Files folder
	 */
	public static String getTestFilesFolderPath() {
		String testFilesFolderDir = correctPath(strWorkspacepath + "\\TestFiles\\");
		return testFilesFolderDir;
	}

	/**
	 * Correct the file path based on the OS system type
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static String correctPath(String path){
		
		return path.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"));

	}

	/**
	 * Verify file or path is existing on system
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static boolean pathExist(String path) throws Exception {
		try {
			File file = new File(path);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Delete a file or a path
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static void deletePath(String path) throws Exception {
		try {
			File file = new File(path);
			file.delete();
			Log.info("File is deleted successfully");
			TestngLogger.writeResult("File is deleted successfully", true);
		} catch (Exception e) {
			Log.error("File is deleted failed");
			TestngLogger.writeResult("File is deleted failed", false);
		}
	}

	/**
	 * create a path
	 * 
	 * @param path
	 *            the path to file
	 * @throws Exception
	 */
	public static void createPath(String path) {
		try {

			if (!pathExist(path)) {
				new File(path).mkdirs();
			}

		} catch (Exception e) {
		}
	}

	/**
	 * To copy a file from the source file to the destination file
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest) throws IOException {

		File sourceFile = new File(src);
		File destFile = new File(dest);

		Files.copy(sourceFile.toPath(), destFile.toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * To copy a folder from the source folder to the destination folder
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyDirectory(String src, String dest) throws IOException {

		File sourceFile = new File(src);
		File destFile = new File(dest);
		createPath(dest);

		FileUtils.copyDirectory(sourceFile, destFile, true);
	}
	
	/**
	 * To kill a system process
	 * @param strProcessName
	 * 		The process name
	 */
	public static void killProcess(String strProcessName){

		try {
			Runtime.getRuntime().exec("taskkill /F /IM " + strProcessName); 
		}
		catch(Exception e) {
			
		}
	}
}
