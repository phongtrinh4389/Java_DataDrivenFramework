package com.nashtech.utils.report;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.nashtech.common.Common;
import com.nashtech.common.Constant;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

public class HtmlReporter implements Formatter, Reporter{

	private static ExtentReports _report;

	private static HashMap<String, ExtentTest> extentTestMap = new HashMap<String, ExtentTest>();

	private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
	private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
	private static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
	private static ThreadLocal<LinkedList<Step>> stepListThreadLocal = new InheritableThreadLocal<>();
	private static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();
	private boolean scenarioOutlineFlag;
    
    public HtmlReporter() throws UnknownHostException, URISyntaxException {
    	_report = setReporter(Common.correctPath(Common.strWebDailyReportFolder + "\\"
				+ Constant.reportFileName));
    	stepListThreadLocal.set(new LinkedList<>());
        scenarioOutlineFlag = false;
        System.out.println("++++constructor");
    }
    
	public static ExtentReports setReporter(String filename) throws UnknownHostException, URISyntaxException {
		
		if (_report == null) {
			_report = createInstance(filename);
		}
			
		setSystemInfo("Application", "Data Driven Framework");
		setSystemInfo("Os Name", System.getProperty("os.name"));
		setSystemInfo("Os Version", System.getProperty("os.version"));
		setSystemInfo("Os Architecture", System.getProperty("os.arch"));
		setSystemInfo("Host", InetAddress.getLocalHost().getHostName());
		setSystemInfo("Username", System.getProperty("user.name"));

		// Tests view
		_report.setAnalysisStrategy(AnalysisStrategy.TEST);

		return _report;

	}
	
	/**
	 * To set the strategy view (suite, class, test)
	 * @param strategy
	 */
	public static void setAnalysisStrategy(String strategy) {
		
		switch(strategy) {
			case "suite":
				_report.setAnalysisStrategy(AnalysisStrategy.SUITE);
				break;
			case "class":
				_report.setAnalysisStrategy(AnalysisStrategy.CLASS);
				break;
			default:
				_report.setAnalysisStrategy(AnalysisStrategy.TEST);
				break;
		}
		
	}

	/**
	 * To create an reporter instance
	 * 
	 * @param fileName
	 *            The report's name
	 * @return
	 * @throws URISyntaxException 
	 */
	public static ExtentReports createInstance(String fileName) throws URISyntaxException {
		
		File f = new File(fileName);
        
        if (f.exists()) {
        	f.delete();
        }
        
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		
		String file = Paths.get(Class.class.getResource(Constant.configReportFilePath).toURI()).toString();
		File reportConfigfile = new File(file);
		htmlReporter.loadXMLConfig(reportConfigfile);

		htmlReporter.setAppendExisting(true);

		ExtentReports report = new ExtentReports();
		report.attachReporter(htmlReporter);

		return report;
	}

	public static void setSystemInfo(String key, String value) {
		_report.setSystemInfo(key, value);
	}
	
	public static void assignCategory(String category) {
		getTest().assignCategory(category);
	}
	
	/**
	 * Write report
	 */
	public static void flush() {
		if (_report != null) {
			_report.flush();
		}
	}

	/**
	 * To Create an ExtentTest session
	 * 
	 * @param strTestMethodName
	 *            The method name
	 * @param strTestMethodDesc
	 *            The method description
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest createTest(String strTestMethodName,
			String strTestMethodDesc) {
		
		ExtentTest test = _report.createTest(strTestMethodName, strTestMethodDesc);
		extentTestMap.put("test_" + Thread.currentThread().getId(), test);
		return test;
	}

	/**
	 * To Create an ExtentTest session
	 * 
	 * @param strTestMethodName
	 *            The method name
	 * @param strTestMethodDesc
	 *            The method description
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest createTest(String strTestClassName) {
		
		ExtentTest test = _report.createTest(strTestClassName);
		extentTestMap.put("test_" + Thread.currentThread().getId(), test);
		return test;
	}
	
	/**
	 * To Create an ExtentTest session
	 * 
	 * @param keyword
	 *            GherkinKeyword
	 * @param strTestName
	 *            The test name
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest createTest(GherkinKeyword keyword ,
			String strTestName) {
		
		ExtentTest test = _report.createTest(keyword, strTestName);
		extentTestMap.put("test_" + Thread.currentThread().getId(), test);
		return test;
	}
	
	/**
	 * To Create a node of ExtentTest session
	 * 
	 * @param strTestMethodName
	 *            The method name
	 * @param strTestMethodDesc
	 *            The method description
	 * @param strNodeName
	 *            The node name
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest createNode(String strClassName,
			String strTestMethodName, String strTestMethodDesc) {
		ExtentTest test = getParent();
		if(test == null) {
			test = createTest(strClassName);
		}
		
		ExtentTest node = test.createNode(strTestMethodName, strTestMethodDesc);
		extentTestMap.put("node_" + Thread.currentThread().getId(), node);
		return node;
	}

	/**
	 * To get the ExtentTest's parent session to write report
	 * 
	 * @return ExtentTest session
	 */

	public static synchronized ExtentTest getParent() {
		return extentTestMap.get( "test_" + Thread.currentThread().getId());
	}

	/**
	 * To get the ExtentTest's node session to write report
	 * 
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest getNode() {
		
		if(stepTestThreadLocal.get() != null) {
			return stepTestThreadLocal.get();
		}
		
		if(scenarioOutlineThreadLocal.get() != null) {
			return scenarioOutlineThreadLocal.get();
		}
		
		if(scenarioThreadLocal.get() != null) {
			return scenarioThreadLocal.get();
		}
		
		return extentTestMap.get( "node_" + Thread.currentThread().getId());
	}
	
	/**
	 * To get the ExtentTest session to write report
	 * 
	 * @return ExtentTest session
	 */
	public static synchronized ExtentTest getTest() {
		if(getNode() == null) {
			return getParent();
		}
		else {
			return getNode();
		}
	}
	
	/**
	 * To write a passed step to report
	 * 
	 * @param strDescription
	 *            The Step's description
	 */
	public static void pass(String strDescription) {

		try {
			getTest().pass(strDescription);
		} catch (Exception ex) {
			Log.info("Can't write to htm report, initialize it first");
		}

	}

	/**
	 * To write a passed step to report with screenshot
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void pass(String strDescription, String strScreenshotPath) {

		try {
			if (strDescription.equalsIgnoreCase("")) {
				getTest().pass(strDescription);
			} else {
				strScreenshotPath = "file:///" + strScreenshotPath;
				getTest().pass(strDescription).addScreenCaptureFromPath(
						strScreenshotPath);
			}
		} catch (Exception e) {
			Log.info("Can't write to htm report, initialize it first");
		}
	}

	/**
	 * To write a failed step to report with screenshot
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void fail(String strDescription, String strScreenshotPath) {

		try {
			if (strScreenshotPath.equalsIgnoreCase("")) {
				getTest().fail(strDescription);
			} else {
				strScreenshotPath = "file:///" + strScreenshotPath;
				getTest().fail(strDescription).addScreenCaptureFromPath(
						strScreenshotPath);
			}
		} catch (Exception ex) {
			Log.info("Can't write to htm report, initialize it first");
		}

	}

	/**
	 * To write a failed step to report with screenshot and throwable stacktrace
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param e
	 *            Throwable object
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void fail(String strDescription, Throwable e,
			String strScreenshotPath) {

		try {
			if (strScreenshotPath.equalsIgnoreCase("")) {
				getTest().fail(strDescription).fail(e);
			} else {
				strScreenshotPath = "file:///" + strScreenshotPath;
				getTest().fail(strDescription).fail(e)
						.addScreenCaptureFromPath(strScreenshotPath);
			}
		} catch (Exception ex) {
			Log.info("Can't write to htm report, initialize it first");
		}

	}

	/**
	 * To write a skipped step to report with screenshot
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void skip(String strDescription, String strScreenshotPath) {

		try {
			if (strDescription.equalsIgnoreCase("")) {
				getTest().skip(strDescription);
			} else {
				strScreenshotPath = "file:///" + strScreenshotPath;
				getTest().skip(strDescription).addScreenCaptureFromPath(
						strScreenshotPath);
			}
		} catch (Exception ex) {
			Log.info("Can't write to htm report, initialize it first");
		}

	}

	/**
	 * To write a skipped step to report with screenshot and throwable
	 * stacktrace
	 * 
	 * @param strDescription
	 *            The Step's description
	 * @param e
	 *            Throwable object
	 * @param strScreenshotPath
	 *            The screenshot's path
	 * @throws IOException
	 *             If the screenshot doesn't exist
	 */
	public static void skip(String strDescription, Throwable e,
			String strScreenshotPath) throws IOException {

		try {
			if (strDescription.equalsIgnoreCase("")) {
				getTest().skip(strDescription).skip(e);
			} else {
				strScreenshotPath = "file:///" + strScreenshotPath;
				getTest().skip(strDescription).skip(e)
						.addScreenCaptureFromPath(strScreenshotPath);
			}
		} catch (Exception ex) {
			Log.info("Can't write to htm report, initialize it first");
		}

	}

	/**
	 * To label a key step into the report
	 * 
	 * @param strDescription
	 *            The step's description
	 */
	public static void label(String strDescription) {

		try {
			getTest().info(
					MarkupHelper.createLabel(strDescription, ExtentColor.BLUE));
		} catch (Exception ex) {
			Log.info("Can't write to htm report, initialize it first");
		}

	}
	
	@Override
	public void before(Match match, Result result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void result(Result result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void after(Match match, Result result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void match(Match match) {
		Step step = stepListThreadLocal.get().poll();
        String data[][] = null;
        if (step.getRows() != null) {
            List<DataTableRow> rows = step.getRows();
            int rowSize = rows.size();
            for (int i = 0; i < rowSize; i++) {
                DataTableRow dataTableRow = rows.get(i);
                List<String> cells = dataTableRow.getCells();
                int cellSize = cells.size();
                if (data == null) {
                    data = new String[rowSize][cellSize];
                }
                for (int j = 0; j < cellSize; j++) {
                    data[i][j] = cells.get(j);
                }
            }
        }

        ExtentTest scenarioTest = scenarioThreadLocal.get();
        ExtentTest stepTest = null;

        try {
            stepTest = scenarioTest.createNode(new GherkinKeyword(step.getKeyword()), step.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (data != null) {
            Markup table = MarkupHelper.createTable(data);
            stepTest.info(table);
        }

        stepTestThreadLocal.set(stepTest);
		
	}

	@Override
	public void embedding(String mimeType, byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syntaxError(String state, String event,
			List<String> legalEvents, String uri, Integer line) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uri(String uri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void feature(Feature feature) {
		featureTestThreadLocal.set(_report.createTest(
				com.aventstack.extentreports.gherkin.model.Feature.class,
				feature.getName()));

	}

	@Override
	public void scenarioOutline(ScenarioOutline scenarioOutline) {
		scenarioOutlineFlag = true;
        ExtentTest node = featureTestThreadLocal.get()
            .createNode(com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioOutline.getName());
        
        scenarioOutlineThreadLocal.set(node);
		
	}

	@Override
	public void examples(Examples examples) {
		ExtentTest test = scenarioOutlineThreadLocal.get();

        String[][] data = null;
        List<ExamplesTableRow> rows = examples.getRows();
        int rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            ExamplesTableRow examplesTableRow = rows.get(i);
            List<String> cells = examplesTableRow.getCells();
            int cellSize = cells.size();
            if (data == null) {
                data = new String[rowSize][cellSize];
            }
            for (int j = 0; j < cellSize; j++) {
                data[i][j] = cells.get(j);
            }
        }
        test.info(MarkupHelper.createTable(data));
        scenarioOutlineThreadLocal.set(test);
		
	}

	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {
		if (scenarioOutlineFlag) {
            scenarioOutlineFlag = false;
        }

        ExtentTest scenarioNode;
        if (scenarioOutlineThreadLocal.get() != null && scenario.getKeyword().trim()
            .equalsIgnoreCase("Scenario Outline")) {
            scenarioNode =
                scenarioOutlineThreadLocal.get().createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
        } else {
            scenarioNode =
                featureTestThreadLocal.get().createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
        }

        for (Tag tag : scenario.getTags()) {
            scenarioNode.assignCategory(tag.getName());
        }
        scenarioThreadLocal.set(scenarioNode);
		
	}

	@Override
	public void background(Background background) {
		
	}

	@Override
	public void scenario(Scenario scenario) {
		
	}

	@Override
	public void step(Step step) {
		if (scenarioOutlineFlag) {
            return;
        }
        stepListThreadLocal.get().add(step);
		
	}

	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void done() {
		flush();
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eof() {
		// TODO Auto-generated method stub
		
	}

}
