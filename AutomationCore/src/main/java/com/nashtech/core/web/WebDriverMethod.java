package com.nashtech.core.web;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import com.nashtech.common.*;
import com.nashtech.core.ui.ImageCompare;
import com.nashtech.utils.report.HtmlReporter;
import com.nashtech.utils.report.Log;
import com.nashtech.utils.report.TestngLogger;

import autoitx4java.AutoItX;

public class WebDriverMethod extends WebDriverFactory {

	public WebDriverMethod() throws Exception {
		super();
	}

	/**
	 * This method is used to navigate the browser to the url
	 * 
	 * @author Hanoi Automation team
	 * @param url
	 *            the url of website
	 * @return None
	 * @throws Exception
	 *             The exception is thrown if the driver can't navigate to the
	 *             url
	 */
	public void openUrl(String url) throws Exception {

		try {
			driver.get(url);
			Log.info("Navigated to the url : [" + url + "]");
			TestngLogger.writeResult("Navigated to the url : [" + url + "]",
					true);
			HtmlReporter.pass("Navigated to the url : [" + url + "]");

		} catch (Exception e) {

			Log.error("Can't navigate to the url : [" + url + "]");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't navigate to the url : [" + url
					+ "]", false);
			TestngLogger.writeLog(e.getMessage());
			HtmlReporter.fail("Can't navigate to the url : [" + url + "]", e,
					takeScreenshot());
			throw (e);

		}
	}
	
	public String getTitle() throws Exception {
		try {
			String title = driver.getTitle();
			
			Log.info("Got the title : [" + title + "]");
			TestngLogger.writeResult("Got the title : [" + title + "]", true);
			HtmlReporter.pass("Got the title : [" + title + "]");
			
			return title;
		}
		catch(Exception e) {
			Log.error("Can't get the title");
			TestngLogger.writeResult("Can't get the title", false);
			HtmlReporter.fail("Can't get the title", e, takeScreenshot());
			throw (e);
		}
	}

	/**
	 * Set the time out to wait for page load
	 * 
	 * @param seconds
	 *            Wait time in seconds
	 */
	public void setPageLoadTimeout(int seconds) {
		try {
			// driver.manage().timeouts().pageLoadTimeout(seconds,
			// TimeUnit.SECONDS);
			driver.manage().timeouts()
					.setScriptTimeout(seconds, TimeUnit.SECONDS);
		} catch (Exception e) {

		}
	}

	/**
	 * This method is used to wait for the page load
	 * 
	 * @author Hanoi Automation team
	 * @param
	 * @return None
	 * @throws Exception
	 */

	public void waitForPageLoad() {

		WebDriverWait wait = new WebDriverWait(driver,
				Constant.DEFAULT_WAITTIME_SECONDS);

		// Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver)
						.executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		// JQuery Wait
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Long) ((JavascriptExecutor) driver)
						.executeScript("return jQuery.active") == 0;
			}
		};

		wait.until(jsLoad);
		wait.until(jQueryLoad);
	}

	/**
	 * This method is used to send keys into a text box without cleaning before.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if sending keys not success
	 */
	public void sendkeys(String elementName, By byWebElementObject,
			String keysToSend) throws Exception {

		try {

			findElement(byWebElementObject).sendKeys(keysToSend);
			Log.info("Keys [" + keysToSend + "] are sent to the element: ["
					+ elementName + "]");
			TestngLogger.writeResult("Keys [" + keysToSend
					+ "] are sent to the element: [" + elementName + "]", true);
			HtmlReporter.pass("Keys [" + keysToSend
					+ "] are sent to the element: [" + elementName + "]");

		} catch (Exception e) {

			Log.error("Can't sendkeys to the element: [" + elementName + "]");
			Log.error(e.toString());
			TestngLogger.writeResult("Can't sendkeys to the element: ["
					+ elementName + "]", false);
			TestngLogger.writeLog(e.toString());
			HtmlReporter.fail("Can't sendkeys to the element: [" + elementName
					+ "]", e, takeScreenshot());
			throw (e);

		}
	}

	/**
	 * This method is used to send keys into a text box.
	 * 
	 * @author Hanoi Automation team
	 * @param elementName
	 *            The name of text box
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param keysToSend
	 *            The keys are sent
	 * @throws Exception
	 *             The exception is throws if input text not success
	 */
	public void inputText(String elementName, By byWebElementObject,
			String keysToSend) throws Exception {
		try {

			for (int time = 0; time < Constant.DEFAULT_WAITTIME_SECONDS; time += Constant.TIMEOUT_STEPS_SECONDS) {
				try {
					WebElement txtElement = findElement(byWebElementObject);
					txtElement.click();
					txtElement.clear();
					txtElement.sendKeys(keysToSend);
					break;
				} catch (StaleElementReferenceException e) {
					Common.sleep(Constant.TIMEOUT_STEPS_SECONDS);
				}
			}
			Log.info("Text [" + keysToSend + "] is inputted to the element: ["
					+ elementName + "]");
			TestngLogger.writeResult("Text [" + keysToSend
					+ "] is inputted to the element: [" + elementName + "]",
					true);
			HtmlReporter.pass("Text [" + keysToSend
					+ "] is inputted to the element: [" + elementName + "]");

		} catch (Exception e) {

			Log.error("Can't input text into the element: [" + elementName
					+ "]");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't input text into the element: ["
					+ elementName + "]", false);
			TestngLogger.writeLog(e.getMessage());
			HtmlReporter.fail("Can't input text into the element: ["
					+ elementName + "]", e, takeScreenshot());
			throw (e);

		}
	}

	/**
	 * Execute javascript. This method used to execute a javascript
	 * 
	 * @author Hanoi Automation team
	 * @param jsFunction
	 *            the js function
	 * @throws Exception
	 *             The exception is thrown if can't execute java script
	 */
	public void executeJavascript(String jsFunction) throws Exception {
		try {

			((JavascriptExecutor) driver).executeScript(jsFunction);
			Log.info("Excecuted the java script: [" + jsFunction + "]");
			TestngLogger.writeResult("Excecuted the java script: ["
					+ jsFunction + "]", true);
			HtmlReporter
					.pass("Excecuted the java script: [" + jsFunction + "]");

		} catch (Exception e) {

			Log.error("Can't excecute the java script: [" + jsFunction + "]");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't excecute the java script: ["
					+ jsFunction + "]", false);
			TestngLogger.writeLog(e.getMessage());
			HtmlReporter.fail("Can't excecute the java script: [" + jsFunction
					+ "]", e, takeScreenshot());
			throw (e);

		}
	}

	/**
	 * This method is used to execute a java script function for an object
	 * argument.
	 * 
	 * @author Hanoi Automation team
	 * @param jsFunction
	 *            The java script function
	 * @param object
	 *            The argument to execute script
	 * @throws Exception
	 *             The exception is thrown if object is invalid.
	 */
	public void executeJavascript(String jsFunction, Object object)
			throws Exception {

		try {

			((JavascriptExecutor) driver).executeScript(jsFunction, object);
			Log.info("Excecute the java script: [" + jsFunction
					+ "] for the object: [" + object + "]");
			TestngLogger.writeResult("Excecute the java script: [" + jsFunction
					+ "] for the object: [" + object + "]", true);
			HtmlReporter.pass("Excecute the java script: [" + jsFunction
					+ "] for the object: [" + object + "]");

		} catch (Exception e) {

			Log.error("Can't excecute the java script: [" + jsFunction
					+ "] for the object: [" + object + "]");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't excecute the java script: ["
					+ jsFunction + "] for the object: [" + object + "]", false);
			TestngLogger.writeLog(e.getMessage());
			HtmlReporter
					.fail("Can't excecute the java script: [" + jsFunction
							+ "] for the object: [" + object + "]", e,
							takeScreenshot());
			throw (e);

		}
	}

	/**
	 * Get the text of a web element
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of web element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getText(String elementName, By byWebElementObject)
			throws Exception {
		try {

			String text = findElement(byWebElementObject).getText();

			if (text.equals("")) {
				text = getAttribute(elementName, byWebElementObject, "value");
			}

			Log.info("Got the text of element [" + elementName + "] is : ["
					+ text + "]");
			TestngLogger.writeResult("Got the text of element [" + elementName
					+ "] is : [" + text + "]", true);
			HtmlReporter.pass("Got the text of element [" + elementName
					+ "] is : [" + text + "]");
			return text;

		} catch (Exception e) {

			Log.error("Can't get text of element: [" + elementName + "]");
			TestngLogger.writeResult("Can't get text of element: ["
					+ elementName + "]", false);
			HtmlReporter.fail("Can't get text of element: [" + elementName
					+ "]", e, takeScreenshot());
			return "";

		}
	}

	/**
	 * Get the text of a selected option from a Dropdown list
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of selected element
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getTextSelectedDDL(String elementName, By byWebElementObject)
			throws Exception {
		try {
			String text = "";
			for (int time = 0; time < Constant.DEFAULT_WAITTIME_SECONDS; time += Constant.TIMEOUT_STEPS_SECONDS) {
				try {
					Select ddl = new Select(findElement(byWebElementObject));
					text = ddl.getFirstSelectedOption().getText();
					break;
				} catch (StaleElementReferenceException e) {
					Common.sleep(Constant.TIMEOUT_STEPS_SECONDS);
				}
			}

			Log.info("Got the text of Dropdown [" + elementName + "] is : ["
					+ text + "]");
			TestngLogger.writeResult("Got the text of Dropdown [" + elementName
					+ "] is : [" + text + "]", true);
			HtmlReporter.pass("Got the text of Dropdown [" + elementName
					+ "] is : [" + text + "]");
			return text;

		} catch (Exception e) {

			Log.error("Can't get text of Dropdown: [" + elementName + "]");
			TestngLogger.writeResult("Can't get text of Dropdown: ["
					+ elementName + "]", false);
			HtmlReporter.fail("Can't get text of Dropdown: [" + elementName
					+ "]", e, takeScreenshot());
			return "";

		}
	}

	/**
	 * Get the text of a Dropdown list
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The by object of web element
	 * @return The text of a dropdown list
	 * @throws Exception
	 *             The exception is thrown if can't get text successfully.
	 */
	public String getTextDDL(String elementName, By byWebElementObject)
			throws Exception {
		try {
			String text = "";
			for (int time = 0; time < Constant.DEFAULT_WAITTIME_SECONDS; time += Constant.TIMEOUT_STEPS_SECONDS) {
				try {
					Select ddl = new Select(findElement(byWebElementObject));
					for (WebElement option : ddl.getOptions()) {
						text = text + option.getText();
					}
					break;
				} catch (StaleElementReferenceException e) {
					Common.sleep(Constant.TIMEOUT_STEPS_SECONDS);
				}
			}

			Log.info("Got the text of Dropdown [" + elementName + "] is : ["
					+ text + "]");
			TestngLogger.writeResult("Got the text of Dropdown [" + elementName
					+ "] is : [" + text + "]", true);
			HtmlReporter.pass("Got the text of Dropdown [" + elementName
					+ "] is : [" + text + "]");
			return text;

		} catch (Exception e) {

			Log.error("Can't get text of Dropdown: [" + elementName + "]");
			TestngLogger.writeResult("Can't get text of Dropdown: ["
					+ elementName + "]", false);
			HtmlReporter.fail("Can't get text of Dropdown: [" + elementName
					+ "]", e, takeScreenshot());
			return "";

		}
	}

	/**
	 * Get the attribute value of a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param attribute
	 *            The attribute need to get value
	 * @return The attribute value as string
	 * @throws Exception
	 */
	public String getAttribute(String elementName, By byWebElementObject,
			String attribute) throws Exception {
		try {

			String attributeValue = findElement(byWebElementObject)
					.getAttribute(attribute);

			TestngLogger.writeResult("getAttribute of the element ["
					+ elementName + "]: [" + attributeValue + "]", true);
			HtmlReporter.pass("getAttribute of the element [" + elementName
					+ "]: [" + attributeValue + "]");
			return attributeValue;

		} catch (Exception e) {

			e.printStackTrace();
			TestngLogger.writeResult("Can't get the attribute [" + attribute
					+ "] of element: [" + elementName + "]", false);
			HtmlReporter.fail("Can't get the attribute [" + attribute
					+ "] of element: [" + elementName + "]", e,
					takeScreenshot());
			throw e;

		}
	}

	/**
	 * Click on a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void click(String elementName, By byWebElementObject)
			throws Exception {
		try {
			waitForElementToBeClickable(byWebElementObject,
					Constant.DEFAULT_WAITTIME_SECONDS).click();
			Log.info("Click on the element: [" + elementName + "]");
			TestngLogger.writeResult("Click on the element: [" + elementName
					+ "]", true);
			HtmlReporter.pass("Click on the element: [" + elementName + "]");

		} catch (Exception e) {
			Log.error("Can't click on the element: [" + elementName + "]");
			TestngLogger.writeResult("Can't click on the element: ["
					+ elementName + "]", false);
			HtmlReporter.fail("Can't click on the element: [" + elementName
					+ "]", e, takeScreenshot());
			throw (e);
		}
	}

	/**
	 * Perform double click
	 * 
	 * @param by
	 *            The By locator object of element
	 * @param elementName
	 *            Name of element used to write
	 * @return
	 * @throws Exception
	 */

	public void doubleClick(String elementName, By by) throws Exception {
		try {

			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(by)).doubleClick().build()
					.perform();
			Log.info("DoubleClick [" + elementName + "] successfully");
			TestngLogger.writeResult("DoubleClick [" + elementName
					+ "] successfully", true);
			HtmlReporter.pass("DoubleClick on the element: [" + elementName
					+ "]");

		} catch (Exception e) {

			Log.error("DoubleClick [" + elementName + "] failed");
			TestngLogger.writeResult(
					"DoubleClick [" + elementName + "] failed", false);
			HtmlReporter.fail("DoubleClick on the element: [" + elementName
					+ "] failed", e, takeScreenshot());
			throw e;

		}
	}

	/**
	 * Click on a web element using javascript
	 * 
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void clickByJS(String elementName, By byWebElementObject)
			throws Exception {
		try {

			executeJavascript("arguments[0].click();",
					findElement(byWebElementObject));

			Log.info("Click by JavaScript on the element: [" + elementName
					+ "]");
			TestngLogger.writeResult("Click by JavaScript on the element: ["
					+ elementName + "]", true);
			HtmlReporter.pass("Click by JavaScript on the element: ["
					+ elementName + "]");

		} catch (Exception e) {

			Log.error("Can't click by Java Script on the element: ["
					+ elementName + "]");
			TestngLogger.writeResult(
					"Can't click by Java Script on the element: ["
							+ elementName + "]", false);
			HtmlReporter.fail("Can't click by Java Script on the element: ["
					+ elementName + "]", e, takeScreenshot());
			throw (e);

		}
	}

	/**
	 * Move to the element then click
	 * 
	 * @param elementName
	 * @param elementName
	 *            The name of web element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void clickByAction(String elementName, By byWebElementObject)
			throws Exception {
		try {

			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(byWebElementObject))
					.click().build().perform();
			Log.info("Click by Actions on the element: [" + elementName + "]");
			TestngLogger.writeResult("Click by Actions on the element: ["
					+ elementName + "]", true);
			HtmlReporter.pass("Click by Actions on the element: ["
					+ elementName + "]");

		} catch (Exception e) {

			Log.error("Click by Actions on [" + elementName + "] failed");
			TestngLogger.writeResult("Click by Actions on [" + elementName
					+ "] failed", false);
			HtmlReporter.fail("Click by Actions on [" + elementName
					+ "] failed", e, takeScreenshot());
			throw e;

		}
	}

	/**
	 * Select a radio button
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void selectRadioButton(String elementName, By byWebElementObject)
			throws Exception {
		try {

			WebElement rbElement = findElement(byWebElementObject);

			if (!rbElement.isSelected()) {
				rbElement.click();
			}

			Log.info("Radio button element: [" + elementName + "] is selected.");
			TestngLogger.writeResult("Radio button element: [" + elementName
					+ "] is selected.", true);
			HtmlReporter.pass("Radio button element: [" + elementName
					+ "] is selected.");

		} catch (Exception e) {

			Log.error("Radio button element: [" + elementName
					+ "] isn't selected.");
			TestngLogger.writeResult("Radio button element: [" + elementName
					+ "] isn't selected.", false);
			HtmlReporter.fail("Radio button element: [" + elementName
					+ "] isn't selected.", e, takeScreenshot());
			throw (e);
		}

	}

	/**
	 * Select a check box
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void selectCheckBox(String elementName, By byWebElementObject)
			throws Exception {
		try {

			WebElement chkElement = findElement(byWebElementObject);

			if (!chkElement.isSelected()) {
				chkElement.click();
			}

			Log.info("Checkbox element: [" + elementName + "] is selected.");
			TestngLogger.writeResult("Checkbox element: [" + elementName
					+ "] is selected.", true);
			HtmlReporter.pass("Checkbox element: [" + elementName
					+ "] is selected.");

		} catch (Exception e) {

			Log.error("Checkbox element: [" + elementName + "] isn't selected.");
			TestngLogger.writeResult("Checkbox element: [" + elementName
					+ "] isn't selected.", false);
			HtmlReporter.fail("Checkbox element: [" + elementName
					+ "] isn't selected.", e, takeScreenshot());
			throw (e);
		}

	}

	/**
	 * De-select a check box
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */
	public void deselectCheckBox(String elementName, By byWebElementObject)
			throws Exception {
		try {
			WebElement chkElement = findElement(byWebElementObject);

			if (chkElement.isSelected()) {
				chkElement.click();
			}

			Log.info("Checkbox element: " + elementName + " is deselected.");
			TestngLogger.writeResult("Checkbox element: " + elementName
					+ " is deselected.", true);
			HtmlReporter.pass("Checkbox element: [" + elementName
					+ "] is deselected.");

		} catch (Exception e) {

			Log.error("Checkbox element: " + elementName + " isn't deselected.");
			TestngLogger.writeResult("Checkbox element: " + elementName
					+ " isn't deselected.", false);
			HtmlReporter.fail("Checkbox element: [" + elementName
					+ "] isn't deselected.", e, takeScreenshot());
			throw (e);
		}

	}

	/**
	 * Verify Status of check box/selection box
	 * 
	 * @param elementName
	 *            The name of check box/selection box
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @throws Exception
	 */

	public void verifyStatusCheckbox(String elementName, By byWebElementObject,
			boolean isSelected) throws Exception {
		try {
			WebElement checkbox = findElement(byWebElementObject);
			if (checkbox.isSelected() == isSelected) {
				Log.info("The status of Checkbox [" + elementName
						+ "] is verified");
				TestngLogger.writeResult("The status of Checkbox ["
						+ elementName + "] is verified", true);
				HtmlReporter.pass("The status of Checkbox [" + elementName
						+ "] is verified");
			} else {
				throw new Exception("The checkbox status is: ["
						+ checkbox.isSelected() + "], but expectation is ["
						+ isSelected + "]");
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			TestngLogger.writeResult(e.getMessage(), false);
			HtmlReporter

			.fail("Verify the status of checkbox element: [" + elementName
					+ "] failed", e, takeScreenshot());

			throw (e);
		}
	}

	/**
	 * Select an option in the Drop Down list
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param chosenOption
	 *            The option is chosen
	 * @throws Exception
	 */
	public void selectDDLByText(String elementName, By byWebElementObject,
			String chosenOption) throws Exception {
		try {
			for (int time = 0; time < Constant.DEFAULT_WAITTIME_SECONDS; time += Constant.TIMEOUT_STEPS_SECONDS) {
				try {
					Select ddl = new Select(findElement(byWebElementObject));
					ddl.selectByVisibleText(chosenOption);
					break;
				} catch (StaleElementReferenceException e) {
					Common.sleep(Constant.TIMEOUT_STEPS_SECONDS);
				}
			}
			Log.info("Select option by Text: [" + chosenOption
					+ "] from select box: [" + elementName + "]");
			TestngLogger.writeResult("Select option by Text: [" + chosenOption
					+ "] from select box: [" + elementName + "]", true);
			HtmlReporter.pass("Select option by Text: [" + chosenOption
					+ "] from select box: [" + elementName + "]");

		} catch (Exception e) {
			Log.error("Can't select option: [" + chosenOption
					+ "] by Text from the select box: [" + elementName + "]");
			TestngLogger.writeResult("Can't select option: [" + chosenOption
					+ "] by Text from the select box: [" + elementName + "]",
					false);
			HtmlReporter

			.fail("Can't select option: [" + chosenOption
					+ "] by Text from the select box: [" + elementName + "]",
					e, takeScreenshot());

			throw (e);
		}
	}

	/**
	 * Select an option in the Drop Down list by value
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param value
	 *            The value is chosen
	 * @throws Exception
	 */
	public void selectDDLByValue(String elementName, By byWebElementObject,
			String value) throws Exception {
		try {

			Select ddl = new Select(findElement(byWebElementObject));
			ddl.selectByValue(value);
			Log.info("Select option by Value: [" + value
					+ "] from select box: [" + elementName + "]");
			TestngLogger.writeResult("Select option by Value: [" + value
					+ "] from select box: [" + elementName + "]", true);
			HtmlReporter.pass("Select option by Value: [" + value
					+ "] from select box: [" + elementName + "]");

		} catch (Exception e) {
			Log.error("Can't select option: [" + value
					+ "] by Value from the select box: [" + elementName + "]");
			TestngLogger.writeResult("Can't select option: [" + value
					+ "] by Value from the select box: [" + elementName + "]",
					false);
			HtmlReporter

			.fail("Can't select option: [" + value
					+ "] by Value from the select box: [" + elementName + "]",
					e, takeScreenshot());

			throw e;
		}
	}

	/**
	 * Verify the display of select DDL is correct
	 * 
	 * @param elementName
	 *            The name of element
	 * @param chosenOption
	 *            The text of options list that should be correct as
	 * @param byWebElementObject
	 *            The By of object
	 * @throws Exception
	 */

	public void verifyDisplayDDL(String elementName, By byWebElementObject,
			String chosenOption) throws Exception {
		try {

			Select ddl = new Select(findElement(byWebElementObject));
			String txt = ddl.getFirstSelectedOption().getText();
			Assert.assertEquals(txt, chosenOption);

			Log.info("Option: [" + chosenOption
					+ "] is displayed correctly on the select box: ["
					+ elementName + "]");
			TestngLogger.writeResult("Option: [" + chosenOption
					+ "] is displayed correctly on the select box: ["
					+ elementName + "]", true);
			HtmlReporter.pass("Option: [" + chosenOption
					+ "] is displayed correctly on the select box: ["
					+ elementName + "]");

		} catch (Exception e) {

			Log.error("Option: [" + chosenOption
					+ "] is not displayed correctly on the select box : ["
					+ elementName + "]");
			TestngLogger.writeResult("Option: [" + chosenOption
					+ "] is not displayed correctly on the select box : ["
					+ elementName + "]", false);
			HtmlReporter

			.fail("Option: [" + chosenOption
					+ "] is not displayed correctly on the select box : ["
					+ elementName + "]", e, takeScreenshot());

			throw e;

		}
	}

	/**
	 * Wait for a time until VisibilityOfElementLocated
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public WebElement waitForVisibilityOfElementLocated(By by, int time)
			throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, time);

		try {
			return wait
					.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Wait for a time until invisibilityOfElementLocated
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public void waitForInvisibilityOfElementLocated(By by, int time)
			throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, time);

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Wait for a time until presenceOfElementLocated
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public WebElement waitForPresenceOfElementLocated(By by, int time)
			throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, time);

		try {
			return wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Wait for a time until elementToBeClickable
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public WebElement waitForElementToBeClickable(By by, int time)
			throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, time);

		try {
			return wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Checking a web element is present or not
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return True if the element is present, False if the element is not
	 *         present
	 */
	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Get a web element object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public WebElement findElement(By by) throws Exception {
		WebElement element = null;
		try {
			waitForPageLoad();
			waitForVisibilityOfElementLocated(by,
					Constant.DEFAULT_WAITTIME_SECONDS);
			element = driver.findElement(by);
			// Log.info("The element : " + by + " is found.");
			// TestngLogger.writeLog("The element : " + by + " is found.");
		} catch (Exception e) {
			Log.error("The element : [" + by + "] isn't found. : " + e);
			TestngLogger.writeResult("The element : [" + by
					+ "] isn't found. : " + e.toString(), false);
			throw (e);
		}
		return element;
	}

	/**
	 * Check correction of element text
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return void
	 * @throws Exception
	 */
	public void verifyText(String elementName, By byWebElementObject,
			String compareText) throws Exception {

		String actualText = "";
		try {

			actualText = getText(elementName, byWebElementObject);
			Assert.assertEquals(actualText, compareText);
			Log.info("[" + elementName + "] has correct text [" + actualText
					+ "]");
			TestngLogger.writeResult("[" + elementName + "] has correct text ["
					+ actualText + "]", true);
			HtmlReporter.pass("[" + elementName + "] has correct text ["
					+ actualText + "]");

		} catch (AssertionError e) {

			Log.error("The text of element [" + elementName + "] is ["
					+ actualText + "], but expectation is [" + compareText
					+ "]");
			TestngLogger.writeResult("The text of element [" + elementName
					+ "] is [" + actualText + "], but expectation is ["
					+ compareText + "]", false);
			HtmlReporter

			.fail("The text of element [" + elementName + "] is [" + actualText
					+ "], but expectation is [" + compareText + "]", e,
					takeScreenshot());

			throw (e);

		}
	}

	/**
	 * Check correction of element text (Contain compare text)
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void verifyNotEqualText(String elementName, By byWebElementObject,
			String comparedText) throws Exception {
		try {
			String txt = getText(elementName, byWebElementObject);
			Assert.assertNotEquals(txt, comparedText);

			Log.info("verifyNotEqualText for the element [" + elementName
					+ "]: Actual [" + txt + "], ComparedText [" + comparedText
					+ "]");
			HtmlReporter.pass("verifyNotEqualText for the element ["
					+ elementName + "]: Actual [" + txt + "], ComparedText ["
					+ comparedText + "]");

		} catch (AssertionError e) {

			Log.error("Can't verifyNotEqualText of the element: ["
					+ elementName + "] to the compared text [" + comparedText
					+ "]");
			TestngLogger.writeResult(
					"Can't verifyNotEqualText of the element: [" + elementName
							+ "] to the compared text [" + comparedText + "]",
					false);
			HtmlReporter

			.fail("Can't verifyNotEqualText of the element: [" + elementName
					+ "] to the compared text [" + comparedText + "]", e,
					takeScreenshot());

			throw (e);

		}
	}

	/**
	 * Check correction of element text (not equal to compare text)
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void verifyContainText(String elementName, By byWebElementObject,
			String containText) throws Exception {
		try {
			String txt = getText(elementName, byWebElementObject);
			org.apache.commons.lang3.StringUtils.containsIgnoreCase(txt,
					containText);

			Log.info("verifyContainText for the element [" + elementName
					+ "]: Actual [" + txt + "], containText [" + containText
					+ "]");
			HtmlReporter.pass(
					"verifyContainText for the element [" + elementName
							+ "]: Actual [" + txt + "], containText ["
							+ containText + "]");

		} catch (Exception e) {
			Log.error("The text of element: [" + elementName
					+ "] does not contain [" + containText + "]");
			TestngLogger.writeResult("The text of element: [" + elementName
					+ "] does not contain [" + containText + "]", false);
			HtmlReporter
					.fail("The text of element: [" + elementName
							+ "] does not contain [" + containText + "]", e, takeScreenshot());
			throw (e);
		}
	}

	/**
	 * Compare 2 object
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void verifyEqual(Object actual, Object expected) throws Exception {

		try {

			Assert.assertEquals(actual, expected);
			Log.info("Actual object [" + actual.toString()
					+ "] equals the expected object [" + expected.toString()
					+ "]");
			TestngLogger.writeResult("Actual object [" + actual.toString()
					+ "] equals the expected object [" + expected.toString()
					+ "]", true);
			HtmlReporter.pass("Actual object [" + actual.toString()
					+ "] equals the expected object [" + expected.toString()
					+ "]");

		} catch (AssertionError e) {
			Log.error("Actual object [" + actual.toString()
					+ "] not equals the expected object ["
					+ expected.toString() + "]");
			TestngLogger.writeResult("Actual object [" + actual.toString()
					+ "] equals the expected object [" + expected.toString()
					+ "]", false);
			HtmlReporter

			.fail("Actual object [" + actual.toString()
					+ "] not equals the expected object ["
					+ expected.toString() + "]", e, takeScreenshot());

			throw (e);
		}
	}

	/**
	 * This method is used to verify the hidden text
	 * 
	 * @param elementName
	 *            The friendly name
	 * @param byWebElementObject
	 *            By object
	 * @param compareText
	 *            The expected text
	 * @throws Exception
	 */
	public void verifyHiddenText(String elementName, By byWebElementObject,
			String compareText) throws Exception {
		try {

			String actualText = getAttribute("ddlContractType",
					byWebElementObject, "textContent").trim().replaceAll("	",
					"");
			Assert.assertEquals(actualText, compareText);
			Log.info("Hidden element [" + elementName
					+ "] has a correct text [" + actualText + "]");
			TestngLogger.writeResult("Hidden element [" + elementName
					+ "] has a correct text [" + actualText + "]", true);
			HtmlReporter.pass("Hidden element [" + elementName
					+ "] has a correct text [" + actualText + "]");

		} catch (Exception e) {

			Log.error("The hidden element: [" + elementName
					+ "] is not correct");
			TestngLogger.writeResult("The hidden element: [" + elementName
					+ "] is not correct ", false);
			HtmlReporter

			.fail("The hidden element: [" + elementName + "] is not correct ",
					e, takeScreenshot());

			throw (e);

		}
	}

	/**
	 * Check correction of title
	 * 
	 * @param elementName
	 *            The name of element
	 * @param compareText
	 *            The text that used to verify
	 * @throws Exception
	 */
	public void verifyTitle(String expectedTitle) throws Exception {

		String title = "";
		try {
			title = driver.getTitle();
			Assert.assertEquals(title, expectedTitle);
			Log.info("The title is correct: [" + title + "]");
			HtmlReporter.pass("The title is correct: [" + title + "]");
		} catch (Exception e) {
			Log.error("The title [" + title + "] is incorrect");
			TestngLogger.writeResult("The title [" + title + "] is incorrect",
					false);
			HtmlReporter.fail("The title [" + title + "] is incorrect", e,
					takeScreenshot());

			throw (e);
		}
	}

	/**
	 * Upload file
	 * 
	 * @param elementName
	 *            The element name
	 * @param byWebElementObject
	 *            The button Browse
	 * @param url
	 *            Url to file upload
	 * @throws Exception
	 */
	public void uploadfile(String elementName, By byWebElementObject, String url)
			throws Exception {
		try {
			url = Common.correctPath(url);
			String strWinTitle = "";
			String strBrowserType = getBrowserType();
			if (strBrowserType.equalsIgnoreCase(BrowserType.FIREFOX)) {
				strWinTitle = "File Upload";
			} else if (strBrowserType.equalsIgnoreCase(BrowserType.CHROME)) {
				strWinTitle = "Open";
			} else if (strBrowserType.equalsIgnoreCase(BrowserType.EDGE)) {
				strWinTitle = "Open";
			} else if (strBrowserType.equalsIgnoreCase(BrowserType.IE)) {
				strWinTitle = "Choose File to Upload";
			} else {
				// Need to implement on other browser types
			}
			click("Open upload location", byWebElementObject);
			AutoItX autoit = new AutoItX();
			autoit.winWait(strWinTitle);
			autoit.controlFocus(strWinTitle, "", "Edit1");
			autoit.sleep(1000);
			autoit.ControlSetText(strWinTitle, "", "Edit1", url);
			autoit.controlClick(strWinTitle, "", "Button1");

			HtmlReporter.pass(
					"Upload file [" + url + "] to the element [" + elementName
							+ "]");

		} catch (Exception e) {
			Log.error(elementName + " uploaded fail ");
			TestngLogger.writeResult(elementName + " uploaded fail ", false);
			HtmlReporter.fail(elementName + " uploaded fail ", e, takeScreenshot());
			throw (e);
		}
	}

	/**
	 * Upload file using autoit exe file
	 * 
	 * @param btnBrowse
	 *            The button Browse
	 * @param strFilePath
	 *            The path to file uploaded
	 * @throws Exception
	 */
	public void uploadfile(By btnBrowse, String strFilePath) throws Exception {
		try {
			strFilePath = Common.correctPath(strFilePath);
			String strWinTitle = "";
			String strBrowserType = getBrowserType();
			if (strBrowserType.equalsIgnoreCase(BrowserType.FIREFOX)) {
				strWinTitle = "File Upload";
			} else if (strBrowserType.equalsIgnoreCase(BrowserType.CHROME)) {
				strWinTitle = "Open";
			} else if (strBrowserType.equalsIgnoreCase(BrowserType.EDGE)) {
				strWinTitle = "Open";
			} else if (strBrowserType.equalsIgnoreCase(BrowserType.IE)) {
				strWinTitle = "Choose File to Upload";
			} else {
				// Need to implement on other browser types
			}
			// Start the executable script to wait for the Upload window appears
			String strClassRoot = new File(this.getClass().getProtectionDomain()
					.getCodeSource().getLocation().toURI()).getParentFile()
					.getParent();
			String strUploadScriptPath = Common.correctPath(strClassRoot + Constant.autoItFolder + "UploadFile.exe");
			new ProcessBuilder(strUploadScriptPath, strWinTitle, strFilePath).start();
			// Click the button Browse
			click("Open upload location", btnBrowse);

			HtmlReporter.pass("Upload file [" + strFilePath + "]");

		} catch (Exception e) {
			Log.error("Uploaded fail ");
			TestngLogger.writeResult("Uploaded fail ", false);
			HtmlReporter.fail("Uploaded fail ", e, takeScreenshot());

			throw (e);
		}
	}

	/**
	 * Upload file using Robot
	 * 
	 * @param filePath
	 *            Url to file upload
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void uploadfile(String filePath) throws Exception {

		try {
			filePath = Common.correctPath(filePath);
			String strBrowserType = getBrowserType();

			StringSelection selection = new StringSelection(filePath);
			Clipboard clipboard = Toolkit.getDefaultToolkit()
					.getSystemClipboard();
			clipboard.setContents(selection, selection);
			Robot robot = new Robot();
			if (strBrowserType.equalsIgnoreCase(BrowserType.SAFARI)) {
				// Cmd + Tab is needed since it launches a Java app and the
				// browser looses focus
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_TAB);

				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_TAB);
				robot.delay(500);

				// Open Goto window
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_G);

				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_G);
				robot.delay(500);

				// Paste the clipboard value
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_V);

				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_V);
				robot.delay(500);

				// Press Enter key to close the Goto window and Upload window
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.delay(500);

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

			} else if (strBrowserType.equalsIgnoreCase(BrowserType.EDGE)) {

				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

			} else {

				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.delay(1000);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			}

			HtmlReporter.pass("Upload file [" + filePath + "]");

		} catch (Exception e) {
			Log.error("uploaded fail ");
			TestngLogger.writeResult("uploaded fail ", false);
			HtmlReporter.fail("Uploaded fail ", e, takeScreenshot());

			throw (e);
		}
	}

	/**
	 * Open url in new tab
	 * 
	 * @param url
	 *            Url to of new tab *
	 * @throws Exception
	 */
	public void openNewTab(String url) throws Exception {
		try {
			// Open tab 2 using CTRL + t keys.
			driver.findElement(By.cssSelector("body")).sendKeys(
					Keys.CONTROL + "t");
			// Open URL In 2nd tab.
			driver.get(url);
			// Switch to current selected tab's content.
			driver.switchTo().defaultContent();

			HtmlReporter.pass("Open new tab");

		} catch (Exception e) {
			Log.error("Open tab failed ");
			TestngLogger.writeResult("Open tab failed ", false);
			HtmlReporter.fail("Open tab failed", e, takeScreenshot());

			throw (e);

		}
	}

	/**
	 * Get the attribute value of a web element
	 * 
	 * @param elementName
	 *            The name of element
	 * @param byWebElementObject
	 *            The By locator object of element
	 * @param attribute
	 *            The attribute need to get value
	 * @param verifyAttribute
	 *            The attribute value used to compare
	 * @return The attribute value as string
	 * @throws Exception
	 */
	public void verifyAttribute(String elementName, By byWebElementObject,
			String attribute, String verifyAttribute) throws Exception {
		try {

			String attributeValue = getAttribute(elementName,
					byWebElementObject, attribute);
			Assert.assertEquals(attributeValue, verifyAttribute);

			Log.info("The attribute [" + attribute + "] of element: ["
					+ elementName + "] is verified, Actual [" + attributeValue
					+ "], Expected [" + verifyAttribute + "]");
			HtmlReporter.pass("The attribute [" + attribute + "] of element: ["
					+ elementName + "] is verified, Actual [" + attributeValue
					+ "], Expected [" + verifyAttribute + "]");

		} catch (Exception e) {
			Log.error("Can't verify the attribute [" + attribute
					+ "] of element: [" + elementName + "]");
			TestngLogger.writeResult("Can't verify the attribute [" + attribute
					+ "] of element: [" + elementName + "]", false);
			HtmlReporter

			.fail("Can't verify the attribute [" + attribute
					+ "] of element: [" + elementName + "]", e,
					takeScreenshot());

			throw e;
		}
	}

	/**
	 * Verify the css value of an element as expectation
	 * 
	 * @param elementName
	 *            The friendly name of element
	 * @param byWebElementObject
	 *            By locator
	 * @param cssAttribute
	 *            css attribute
	 * @param verifyAttribute
	 *            Expected value
	 * @return
	 * @throws Exception
	 */
	public void verifyCSS(String elementName, By byWebElementObject,
			String cssAttribute, String verifyAttribute) throws Exception {
		try {
			String attributeValue = findElement(byWebElementObject)
					.getCssValue(cssAttribute);
			Assert.assertEquals(attributeValue, verifyAttribute);

			Log.info("The css [" + cssAttribute + "] of element: ["
					+ elementName + "] is verified, Actual [" + attributeValue
					+ "], Expected [" + verifyAttribute + "]");
			HtmlReporter.pass("The css [" + cssAttribute + "] of element: ["
					+ elementName + "] is verified, Actual [" + attributeValue
					+ "], Expected [" + verifyAttribute + "]");
		} catch (Exception e) {
			Log.error("Can't verify the css value [" + cssAttribute
					+ "] of element: [" + elementName + "]");
			TestngLogger.writeResult("Can't verify the css value ["
					+ cssAttribute + "] of element: [" + elementName + "]",
					false);
			HtmlReporter

			.fail("Can't verify the css value [" + cssAttribute
					+ "] of element: [" + elementName + "]", e,
					takeScreenshot());

			throw e;
		}
	}

	/**
	 * Verify the present of an alert
	 * 
	 * @return
	 */
	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (Exception Ex) {
			return false;
		}
	}

	/**
	 * Accept an Alert
	 * 
	 * @throws Exception
	 */
	public void acceptAlert() throws Exception {
		try {
			if (isAlertPresent()) {
				driver.switchTo().alert().accept();
				HtmlReporter.pass("Accept Alert");
			}
		} catch (Exception e) {
			Log.error("Can't accept Alert");
			TestngLogger.writeResult("Can't accept Alert", false);
			HtmlReporter.fail("Can't accept Alert", e, takeScreenshot());

			throw (e);
		}
	}

	/**
	 * Check an element displayed or not
	 * 
	 * @param by
	 *            By Locator
	 * @return
	 * @throws Exception
	 */
	public boolean displayedElement(By by) throws Exception {
		boolean check = false;
		try {
			waitForVisibilityOfElementLocated(by,
					Constant.DEFAULT_WAITTIME_SECONDS);
			check = driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			check = false;
		}
		return check;
	}

	/**
	 * Hide an element by javascript
	 * 
	 * @param by
	 *            By locator
	 * @throws Exception
	 */
	public void hideElement(By by) throws Exception {
		try {
			WebElement element = driver.findElement(by);
			executeJavascript("arguments[0].style.visibility='hidden'", element);
			waitForPageLoad();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Perform mouse hover action
	 * 
	 * @param by
	 *            The By locator object of element
	 * @param elementName
	 *            Name of element used to write
	 * @return
	 * @throws Exception
	 */

	public void mouseHover(String elementName, By by) throws Exception {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(driver.findElement(by)).perform();
			Log.info("mouseHover [" + elementName + "] successfully");
			TestngLogger.writeResult("mouseHover [" + elementName
					+ "] successfully", true);
			HtmlReporter.pass("mouseHover [" + elementName + "] successfully");
		} catch (Exception e) {
			Log.error("mouseHover [" + elementName + "] failed");
			TestngLogger.writeResult("mouseHover [" + elementName + "] failed",
					false);
			HtmlReporter.fail("mouseHover [" + elementName + "] failed", e,
					takeScreenshot());

			throw e;
		}
	}

	/**
	 * Scroll the web page to the element
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void scrollIntoView(String elementName, By by) throws Exception {
		try {

			executeJavascript("arguments[0].scrollIntoView(true);",
					findElement(by));

			Log.info("Scroll into [" + elementName + "] successfully");
			TestngLogger.writeResult("Scroll into [" + elementName
					+ "] successfully", true);
			HtmlReporter.pass("Scroll into [" + elementName + "] successfully");

		} catch (Exception e) {

			Log.error("Can not scroll into [" + elementName + "]");
			TestngLogger.writeResult("Can not scroll into [" + elementName
					+ "]", false);
			HtmlReporter.fail("Can not scroll into [" + elementName + "]", e,
					takeScreenshot());

			throw (e);

		}
	}

	/**
	 * This method is used to capture a screenshot then write to the TestNG
	 * Logger
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot() {

		String failureImageFileName = new SimpleDateFormat(
				Constant.TIME_STAMP_3)
				.format(new GregorianCalendar().getTime())
				+ "." + Constant.SCREENSHOT_FORMAT;

		try {

			if (driver != null) {
				String strImagePath = Common
						.correctPath(Common.strWebScreenshotFolder
								+ failureImageFileName);
				File scrFile = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(scrFile, new File(strImagePath));
				TestngLogger.writeLogWithScreenshot(strImagePath);
				return strImagePath;
			}
			return "";
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * This method is used to capture a screenshot
	 * 
	 * @author Hanoi Automation team
	 * 
	 * @return A html tag that reference to the image, it's attached to the
	 *         report.html
	 * @throws Exception
	 */
	public String takeScreenshot(String filename) throws Exception {

		String screenShotDirector = Common.strWebScreenshotFolder;
		String screenshotFile = Common.correctPath(screenShotDirector
				+ filename);

		try {
			if (driver != null) {

				File scrFile = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(scrFile, new File(screenshotFile));

				return screenshotFile;

			} else {
				return "";
			}
		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't capture the screenshot ", false);
			throw e;
		}
	}

	/**
	 * This method is used to capture a screenshot with Ashot
	 * 
	 * @author Hanoi Automation team
	 * @param filename
	 * @return The screenshot path
	 * @throws Exception
	 */
	public String takeScreenshotWithAshot(String fileDir) throws Exception {

		fileDir = Common.correctPath(fileDir);
		try {

			if (driver != null) {
				Screenshot screenshot = new AShot().shootingStrategy(
						ShootingStrategies.viewportPasting(100))
						.takeScreenshot(driver);
				ImageIO.write(screenshot.getImage(),
						Constant.SCREENSHOT_FORMAT, new File(fileDir));
			} else {
				fileDir = "";
			}

		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't capture the screenshot ", false);
			throw e;
		}
		return fileDir;
	}

	/**
	 * This method is used to capture an element's screenshot with Ashot
	 * 
	 * @author Hanoi Automation team
	 * @param filename
	 * @return The screenshot path
	 * @throws Exception
	 */
	public String takeScreenshotWithAshot(String fileDir, By by)
			throws Exception {

		fileDir = Common.correctPath(fileDir);

		try {

			if (driver != null) {
				WebElement element = findElement(by);
				Screenshot screenshot = new AShot().shootingStrategy(
						ShootingStrategies.viewportPasting(100))
						.takeScreenshot(driver, element);
				ImageIO.write(screenshot.getImage(),
						Constant.SCREENSHOT_FORMAT, new File(fileDir));
			}

		} catch (Exception e) {
			Log.error("Can't capture the screenshot");
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't capture the screenshot ", false);
			throw e;
		}
		return fileDir;
	}

	/**
	 * To compare the layout of a web page with baseline
	 * 
	 * @param filename
	 *            The name of screenshot
	 * @throws Exception
	 */
	public void compareScreenshot(String filename) throws Exception {
		String screenshotFileName = filename + "." + Constant.SCREENSHOT_FORMAT;
		String baseLineImage = Common.strWebBaseLineScreenshotFolder
				+ screenshotFileName;
		String actualImage = Common.strWebActualScreenshotFolder
				+ screenshotFileName;
		String diffImage = Common.strWebDiffScreenshotFolder
				+ screenshotFileName;

		try {
			waitForPageLoad();
			if (!Common.pathExist(baseLineImage)) {
				takeScreenshotWithAshot(baseLineImage);
			} else {
				takeScreenshotWithAshot(actualImage);
				ImageCompare imageComparitor = new ImageCompare();
				BufferedImage diffBuff = imageComparitor.diffImages(
						baseLineImage, actualImage, 10);
				if (diffBuff == null) {
					Log.info("The actual screenshot of page [" + filename
							+ "] matches with the baseline");
					TestngLogger.writeResult("The actual screenshot of page ["
							+ filename + "] matches with the baseline", true);
					TestngLogger.writeLogWithScreenshot(actualImage);
				} else {
					Log.error("The actual screenshot of page [" + filename
							+ "] doesn't match with the baseline");
					TestngLogger.writeResult("The actual screenshot of page ["
							+ filename + "] doesn't match with the baseline",
							false);
					ImageIO.write(diffBuff, Constant.SCREENSHOT_FORMAT,
							new File(Common.strWebDiffScreenshotFolder,
									screenshotFileName));
					TestngLogger.writeLogWithScreenshot(diffImage);
					throw new Exception(
							"The actual screenshot doesn't match with the baseline");
				}

			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * To compare the layout of a web element with baseline
	 * 
	 * @param filename
	 *            The name of screenshot
	 * @throws Exception
	 */
	public void compareScreenshot(String filename, By locator) throws Exception {
		String screenshotFileName = filename + "." + Constant.SCREENSHOT_FORMAT;
		String baseLineImage = Common.strWebBaseLineScreenshotFolder
				+ screenshotFileName;
		String actualImage = Common.strWebActualScreenshotFolder
				+ screenshotFileName;
		String diffImage = Common.strWebDiffScreenshotFolder
				+ screenshotFileName;

		try {
			waitForPageLoad();
			if (!Common.pathExist(baseLineImage)) {
				takeScreenshotWithAshot(baseLineImage, locator);
			} else {
				takeScreenshotWithAshot(actualImage, locator);
				ImageCompare imageComparitor = new ImageCompare();
				BufferedImage diffBuff = imageComparitor.diffImages(
						baseLineImage, actualImage, 10);
				if (diffBuff == null) {
					Log.info("The actual screenshot of element [" + filename
							+ "] matches with the baseline");
					TestngLogger.writeResult(
							"The actual screenshotof element [" + filename
									+ "] matches with the baseline", true);
					TestngLogger.writeLogWithScreenshot(actualImage);
				} else {
					Log.error("The actual screenshot of element [" + filename
							+ "] doesn't match with the baseline");
					TestngLogger.writeResult(
							"The actual screenshot of element [" + filename
									+ "] doesn't match with the baseline",
							false);
					ImageIO.write(diffBuff, Constant.SCREENSHOT_FORMAT,
							new File(Common.strWebDiffScreenshotFolder,
									screenshotFileName));
					TestngLogger.writeLogWithScreenshot(diffImage);
					throw new Exception("The actual screenshot of element ["
							+ filename + "] doesn't match with the baseline");
				}

			}
		} catch (Exception e) {
			throw e;
		}
	}
}
