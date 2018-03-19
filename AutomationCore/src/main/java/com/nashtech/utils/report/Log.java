package com.nashtech.utils.report;

import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;

import com.nashtech.common.Constant;

public class Log {

	private static Logger getLogger() {

		try {
			
			LoggerContext context = (LoggerContext) LogManager.getContext(false);

			context.setConfigLocation(Class.class.getResource(
					Constant.configLogFilePath).toURI());
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		Logger log = LogManager.getLogger();
		String logFileName = "log_" + Thread.currentThread().getName() + "_"
				+ Thread.currentThread().getId();
		ThreadContext.put("ROUTINGKEY", logFileName);
		return log;

	}

	/**
	 * This method is used at the start of test case, help for developer can
	 * trace log easier.
	 * 
	 * @author Hanoi Automation team
	 * @param sTestCaseName
	 *            The name of test case
	 */
	public static void startTestCase(String sTestCaseName) {

		getLogger()
				.info("****************************************************************************************");

		getLogger()
				.info("****************************************************************************************");

		getLogger().info(
				"$$$$$$$$$$$$$$$$$$$$$                 " + sTestCaseName
						+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");

		getLogger()
				.info("****************************************************************************************");

		getLogger()
				.info("****************************************************************************************");

	}

	/**
	 * This method is used at the end of test case, help for developer can trace
	 * log easier.
	 * 
	 * @author Hanoi Automation team
	 * @param sTestCaseName
	 *            The name of test case
	 */
	public static void endTestCase(String sTestCaseName) {

		getLogger().info(
				"XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-"
						+ "             XXXXXXXXXXXXXXXXXXXXXX");

		getLogger().info("X");

		getLogger().info("X");

		getLogger().info("X");

		getLogger().info("X");

	}

	/**
	 * Writing information message to log file
	 * 
	 * @author Hanoi Automation team
	 * @param info
	 *            The information meesage
	 */
	public static void info(String info) {

		getLogger().info(info);

	}

	/**
	 * Writing warning message to log file
	 * 
	 * @author Hanoi Automation team
	 * @param warn
	 *            The warning message
	 */
	public static void warn(String warn) {

		getLogger().warn(warn);

	}

	/**
	 * Writing error message to log file
	 * 
	 * @author Hanoi Automation team
	 * @param error
	 *            The error message
	 */
	public static void error(String error) {

		getLogger().error(error);

	}

	/**
	 * Writing fatal message to log file
	 * 
	 * @author Hanoi Automation team
	 * @param fatal
	 *            The fatal message
	 */
	public static void fatal(String fatal) {

		getLogger().fatal(fatal);

	}

	/**
	 * Writing debug message to log file
	 * 
	 * @author Hanoi Automation team
	 * @param debug
	 *            The debug message
	 */
	public static void debug(String debug) {

		getLogger().debug(debug);

	}

}
