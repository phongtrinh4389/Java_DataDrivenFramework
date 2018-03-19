package com.nashtech.core.mobile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import com.nashtech.utils.report.*;
import com.nashtech.common.*;
import com.nashtech.core.ui.ImageCompare;

import autoitx4java.AutoItX;
import io.appium.java_client.android.AndroidDriver;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class AppiumDriver extends Common {

	// public WebDriver driver;
	Dimension size;
	public AndroidDriver<WebElement> driver = null;

	/**
	 * This method is used to open a webdriver, it's used for selenium grid as
	 * well
	 * 
	 * @author Hanoi Automation team
	 * @param None
	 * @return None
	 * @throws Exception
	 *             The method throws an exeption when browser is invalid or
	 *             can't start webdriver
	 */

	public void openMobileDriver() throws Exception {
		
		String strMobileWeb = Common.getConfigValue("MobileWeb");
		String strMobileBrowser = Common.getConfigValue("MobileBrowser");
		String strMobileVersion = Common.getConfigValue("MobileVersion");
		String strMobilePlatform = Common.getConfigValue("MobilePlatform");
		String strDeviceName = Common.getConfigValue("DeviceName");
		String strMobileAppPackage = Common.getConfigValue("MobileAppPackage");
		String strMobileAppActivity = Common.getConfigValue("MobileAppActivity");

		DesiredCapabilities capabilities = null;
		try {
			if (strMobileWeb.equalsIgnoreCase("Y")) {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability(CapabilityType.BROWSER_NAME,
						"Android");
				capabilities.setCapability(CapabilityType.VERSION,
						strMobileVersion);
				capabilities.setCapability("app", strMobileBrowser);
				capabilities.setCapability("deviceName", "Android Emulator");
				capabilities.setCapability("platformName", strMobilePlatform);
			} else {
				capabilities = new DesiredCapabilities();
				capabilities.setCapability("VERSION", strMobileVersion);
				capabilities.setCapability("deviceName", strDeviceName);
				capabilities.setCapability("platformName", strMobilePlatform);
				capabilities.setCapability("appPackage", strMobileAppPackage);
				capabilities.setCapability("appActivity", strMobileAppActivity);
			}

			driver = new AndroidDriver<WebElement>(new URL(
					"http://127.0.0.1:4723/wd/hub"), capabilities);
			Log.info("Starting remote webdriver for: Platform: "
					+ strMobilePlatform + " ," + " version: "
					+ strMobileVersion);
			TestngLogger.writeResult(
					"Starting remote webdriver for: Platform: "
							+ strMobilePlatform + " ," + " version: "
							+ strMobileVersion, true);
			driver.manage()
					.timeouts()
					.implicitlyWait(Constant.IMPLICIT_WAIT_TIME,
							TimeUnit.SECONDS);
		} catch (Exception e) {

			Log.error("Can't start the webdriver!!! : " + e);
			TestngLogger.writeResult("Can't starting Webdriver!!! : " + e,
					false);
			throw (e);

		}

	}

	/**
	 * This method is used to close a webdriver
	 * 
	 * @author Hanoi Automation team
	 * @param None
	 * @return None
	 * @throws Exception
	 *             The exception is thrown when can't close the webdriver.
	 */
	public void closeDriver() throws Exception {

		try {

			if (driver != null) {
				driver.quit();
				Log.info("The webdriver is closed!!!");
				TestngLogger.writeResult("The webdriver is closed!!!", true);
			}

		} catch (Exception e) {

			Log.error("The webdriver is not closed!!! " + e.getMessage());
			TestngLogger.writeResult(
					"The webdriver is not closed!!! " + e.getMessage(), false);
			throw (e);

		}
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
			Log.info("Navigate to the url : " + url);
			TestngLogger.writeResult("Navigate to the url : " + url, true);

		} catch (Exception e) {

			 Log.error("Can't navigate to the url : " + url);
			 Log.error(e.getMessage());
			 TestngLogger.writeResult("Can't navigate to the url : " + url,
			 false);
			 TestngLogger.writeLog(e.getMessage());
			throw (e);

		}
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
			Log.info("Keys are sent to the element: " + elementName);
			TestngLogger.writeResult("Keys are sent to the element: "
					+ elementName, true);

		} catch (Exception e) {

			Log.error("Keys aren't sent to the element: " + elementName);
			Log.error(e.toString());
			TestngLogger.writeResult("Keys aren't sent to the element: "
					+ elementName, false);
			TestngLogger.writeLog(e.toString());
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
			Log.info("Input text into the element: " + elementName);
			TestngLogger.writeResult("Input text into the element: "
					+ elementName, true);

		} catch (Exception e) {

			Log.error("Can't input text into the element: " + elementName);
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't input text into the element: "
					+ elementName, false);
			TestngLogger.writeLog(e.getMessage());
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
			Log.info("Excecuting the java script: " + jsFunction);
			TestngLogger.writeResult("Excecuting the java script: "
					+ jsFunction, true);

		} catch (Exception e) {

			Log.error("Can't excecute the java script: " + jsFunction);
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't excecute the java script: "
					+ jsFunction, false);
			TestngLogger.writeLog(e.getMessage());
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
			Log.info("Excecuting the java script: " + jsFunction);
			TestngLogger.writeResult("Excecuting the java script: "
					+ jsFunction, true);

		} catch (Exception e) {

			Log.error("Can't excecute the java script: " + jsFunction
					+ " for the object: " + object);
			Log.error(e.getMessage());
			TestngLogger.writeResult("Can't excecute the java script: "
					+ jsFunction + " for the object: " + object, false);
			TestngLogger.writeLog(e.getMessage());
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

			Log.info("Got the text of element : " + elementName + " : " + text);
			TestngLogger.writeResult("Got the text of element : " + elementName
					+ " : " + text, true);
			return text;

		} catch (Exception e) {

			Log.error("Can't get text of element: " + elementName);
			TestngLogger.writeResult("Can't get text of element: "
					+ elementName, false);
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
			TestngLogger.writeResult("Verify that element \"" + elementName
					+ "\" is PRESENT", true);
			return attributeValue;
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			TestngLogger.writeResult("Verify that element \"" + elementName
					+ "\" is PRESENT", false);
			return null;
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
			findElement(byWebElementObject).click();
			Log.info("Click to the element: " + elementName);
			TestngLogger.writeResult("Click to the element: " + elementName,
					true);

		} catch (Exception e) {
			Log.error("Can't click to the element: " + elementName);
			TestngLogger.writeResult("Can't click to the element: "
					+ elementName, false);
			throw (e);
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
			Log.info("Click by Java Script on the element: " + elementName);
			TestngLogger.writeResult("Click by Java Script on the element: "
					+ elementName, true);

		} catch (Exception e) {
			Log.error("Can't click by Java Script on the element: "
					+ elementName);
			TestngLogger
					.writeResult("Can't click by Java Script on the element: "
							+ elementName, false);
			throw (e);
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

			Log.info("Radio button element: " + elementName + " is selected.");
			TestngLogger.writeResult("Radio button element: " + elementName
					+ " is selected.", true);

		} catch (Exception e) {

			Log.error("Radio button element: " + elementName
					+ " isn't selected.");
			TestngLogger.writeResult("Radio button element: " + elementName
					+ " isn't selected.", false);

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

			Log.info("Checkbox element: " + elementName + " is selected.");
			TestngLogger.writeResult("Checkbox element: " + elementName
					+ " is selected.", true);

		} catch (Exception e) {

			Log.error("Checkbox element: " + elementName + " isn't selected.");
			TestngLogger.writeResult("Checkbox element: " + elementName
					+ " isn't selected.", false);

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

		} catch (Exception e) {

			Log.error("Checkbox element: " + elementName + " isn't deselected.");
			TestngLogger.writeResult("Checkbox element: " + elementName
					+ " isn't deselected.", false);

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
	public void selectDDL(String elementName, By byWebElementObject,
			String chosenOption) throws Exception {
		try {

			Select ddl = new Select(findElement(byWebElementObject));
			ddl.selectByVisibleText(chosenOption);
			Log.info("Select option: " + chosenOption + " from select box: "
					+ elementName);
			TestngLogger.writeResult("Select option: " + chosenOption
					+ " from select box: " + elementName, true);

		} catch (Exception e) {
			Log.error("Can't select option: " + chosenOption
					+ " from select box: " + elementName);
			TestngLogger.writeResult("Can't select option: " + chosenOption
					+ " from select box: " + elementName, false);
		}
	}

	/**
	 * Wait for a time until a web element located
	 * 
	 * @param by
	 *            The by locator object of element
	 * @param time
	 *            Time to wait in seconds
	 * @throws Exception
	 */
	public void waitForElementPresent(By by, int time) throws Exception {
		int i = 0;
		WebDriverWait wait = new WebDriverWait(driver,
				Constant.TIMEOUT_STEPS_SECONDS);
		while (i < time) {
			try {
				wait.until(ExpectedConditions.presenceOfElementLocated(by));
				return;
			} catch (Exception e) {

			}
			i += Constant.TIMEOUT_STEPS_SECONDS;
			Log.info("Wait for element " + by + " in " + i + " seconds");
			TestngLogger.writeResult("Wait for element " + by + " in " + i
					+ " seconds", true);
		}
		Log.error("The element " + by + " not present ");
		TestngLogger.writeResult("The element " + by + " not present ", false);
		throw new TimeoutException("The element " + by + " not present ");
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
			waitForElementPresent(by, Constant.DEFAULT_WAITTIME_SECONDS);
			element = driver.findElement(by);
			Log.info("The element : " + by + " is found.");
			TestngLogger.writeLog("The element : " + by + " is found.");
		} catch (Exception e) {
			Log.error("The element : " + by + " isn't found. : " + e);
			TestngLogger.writeLog("The element : " + by + " isn't found. : "
					+ e);
			throw (e);
		}
		return element;
	}

	/**
	 * Check correction of element text
	 * 
	 * @param by
	 *            The By locator object of element
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void verifyText(String elementName, By byWebElementObject,
			String compareText) throws Exception {
		try {
			String txt = getText(elementName, byWebElementObject);
			Assert.assertEquals(txt, compareText);
			Log.info(elementName + " is correct ");
		} catch (Exception e) {
			Log.error("The element: " + elementName + " is not correct: ");
			TestngLogger.writeResult("The element: " + elementName
					+ " is not correct: ", false);
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
			String compareText) throws Exception {
		try {
			String txt = getText(elementName, byWebElementObject);
			Assert.assertNotEquals(txt, compareText);
			Log.info(elementName + " is not equal to " + compareText);
		} catch (Exception e) {
			Log.error("The element: " + elementName + " is not equal to "
					+ compareText);
			TestngLogger.writeResult("The element: " + elementName
					+ " is not equal to " + compareText, false);
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
	public boolean verifyContainText(String elementName, By byWebElementObject,
			String containText) throws Exception {
		try {
			String txt = getText(elementName, byWebElementObject);

			if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(txt,
					containText)) {
				Log.info(elementName + " contains " + containText);
				return true;
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			Log.error("The element: " + elementName + " does not contain "
					+ containText);
			TestngLogger.writeResult("The element: " + elementName
					+ " does not contain " + containText, false);
			throw (e);
		}
	}

	/**
	 * Upload file
	 * 
	 * @param elementName
	 *            The element name
	 * @param title
	 *            Title of window select box
	 * @param url
	 *            Url to file upload
	 * @return The WebElement object
	 * @throws Exception
	 */
	public void uploadfile(String elementName, By byWebElementObject,
			String title, String url) throws Exception {
		try {
			click("Open upload location", byWebElementObject);
			AutoItX autoit = new AutoItX();
			autoit.winWait(title);
			autoit.controlFocus(title, "", "Edit1");
			autoit.sleep(2000);
			autoit.ControlSetText(title, "", "Edit1", url);
			autoit.controlClick(title, "", "Button1");
			Log.info("File is uploaded");
		} catch (Exception e) {
			Log.error(elementName + " uploaded fail ");
			TestngLogger.writeResult(elementName + " uploaded fail ", false);
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
		} catch (Exception e) {

			Log.error("Open tab failed ");
			TestngLogger.writeResult("Open tab failed ", false);
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
	public boolean verifyStatusCheckbox(String elementName,
			By byWebElementObject, boolean status) throws Exception {
		try {
			if (driver.findElement(byWebElementObject).isSelected()
					&& status == true)
				Log.info("Checkbox related to: '" + elementName
						+ "' is checked.");
			else if (driver.findElement(byWebElementObject).isSelected() == false
					&& status == false)
				Log.info("Checkbox related to: '" + elementName
						+ "' is unchecked.");
			else
				throw new Exception();
			return (driver.findElement(byWebElementObject).isSelected());
		} catch (Exception e) {
			Log.error("Error while Checking if the checkbox related to "
					+ elementName + " is checked or not. -" + e.getMessage());
			TestngLogger.writeResult(
					"Error while Checking if the checkbox related to "
							+ elementName + " is checked or not. -"
							+ e.getMessage(), false);
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
	public boolean verifyAttribute(String elementName, By byWebElementObject,
			String attribute, String verifyAttribute) throws Exception {
		try {
			String attributeValue = getAttribute(elementName,
					byWebElementObject, attribute);
			Assert.assertEquals(attributeValue, verifyAttribute);
			Log.info(elementName + " is correct ");
			return true;
		} catch (Exception e) {
			Log.error("Verify that element \"" + elementName + "\" is PRESENT");
			TestngLogger.writeResult("Verify that element \"" + elementName
					+ "\" is PRESENT", false);
			return true;
		}
	}

	public boolean verifyCSS(String elementName, By byWebElementObject,
			String cssAttribute, String verifyAttribute) throws Exception {
		try {
			String attributeValue = findElement(byWebElementObject).getCssValue(cssAttribute);
			Assert.assertEquals(attributeValue, verifyAttribute);
			Log.info(elementName + " is correct ");
			return true;
		} catch (Exception e) {
			Log.error("Verify that element \"" + elementName + "\" is PRESENT");
			TestngLogger.writeResult("Verify that element \"" + elementName
					+ "\" is PRESENT", false);
			return true;
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (Exception Ex) {
			return false;
		}
	}

	public void checkAlert() throws Exception {
		try {
			// WebDriverWait wait = new WebDriverWait(driver,
			// Constant.DEFAULT_WAITTIME_SECONDS);
			// wait.until(ExpectedConditions.alertIsPresent());
			// Alert alert = driver.switchTo().alert();
			// alert.accept();
			if (isAlertPresent()) {
				driver.switchTo().alert().accept();
			}
		} catch (Exception e) {
			Log.error("Alert is not present");
			TestngLogger.writeResult("Verify that the alert is PRESENT", false);
			throw (e);
		}
	}

	public boolean displayedElement(By by) throws Exception {
		boolean check;
		try {
			waitForElementPresent(by, Constant.DEFAULT_WAITTIME_SECONDS);
			check = driver.findElement(by).isDisplayed();
			Log.info("The element : " + by + " is found.");
			TestngLogger.writeLog("The element : " + by + " is found.");
		} catch (Exception e) {
			Log.error("The element : " + by + " isn't found. : " + e);
			TestngLogger.writeLog("The element : " + by + " isn't found. : "
					+ e);
			throw (e);
		}
		return check;
	}

	// -----------------------------------------------------------------------------
	/**
	 * Verify the enabled status of a web element
	 * 
	 * @param byWebElementObject
	 *            The by object of text box element
	 * @param status
	 *            The expect enabled status of element
	 * @return True if verification is correct
	 * @throws Exception
	 */
	public boolean verifyElementIsEnabled(By byWebElementObject, boolean status)
			throws Exception {
		boolean check;
		try {
			waitForElementPresent(byWebElementObject,
					Constant.DEFAULT_WAITTIME_SECONDS);
			check = driver.findElement(byWebElementObject).isEnabled();
			if (check == status) {
				Log.info("The enabled status of element : "
						+ byWebElementObject + " is correct.");
				TestngLogger.writeLog("The enabled status of element : "
						+ byWebElementObject + " is correct.");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			Log.error("The enabled status of element : " + byWebElementObject
					+ " is incorrect." + e);
			TestngLogger.writeLog("The enabled status of element : "
					+ byWebElementObject + " is incorrect." + e);
			throw (e);
		}
		return check;
	}

	/**
	 * Swipe the android mobile from right to left
	 * 
	 * @throws Exception
	 */
	public void swipeLeft() throws Exception {

		try {

			Thread.sleep(5000);
			// Get the size of screen.
			size = driver.manage().window().getSize();
			// Find startx point which is at right side of screen.
			int startx = (int) (size.width * 0.96);
			// Find endx point which is at left side of screen.
			int endx = (int) (size.width * 0.50);
			// Find vertical point where you wants to swipe. It is in middle of
			// screen height.
			int starty = size.height / 2;
			// Swipe from Right to Left.
			driver.swipe(startx, starty, endx, starty, 1);
			driver.swipe(startx, starty, endx, starty, 500);
			Log.info("Swipe left successfully");
			TestngLogger.writeResult("Swipe left successfully", true);

		} catch (Exception e) {
			Log.error("Can't swipe left!!! : " + e);
			TestngLogger.writeResult("Can't swipe left!!! : " + e, false);
			throw (e);
		}
	}

	/**
	 * Swipe the android mobile by location in screen
	 * 
	 * @param fromx
	 *            % vertical of screen for starting point
	 * 
	 * @param fromy
	 *            % horizontal of screen for starting point
	 * 
	 * @param tox
	 *            % vertical of screen for ending point
	 * 
	 * @param toy
	 *            % horizontal of screen for ending point
	 * 
	 * @throws Exception
	 */
	public void swipe(double fromx, double fromy, double tox, double toy)
			throws Exception {
		try {
			Thread.sleep(5000);
			// Get the size of screen.
			Dimension size = driver.manage().window().getSize();
			int startx = (int) (size.width * fromx);
			int endx = (int) (size.height * fromy);
			int starty = (int) (size.width * tox);
			int endy = (int) (size.height * toy);
			driver.swipe(startx, endx, starty, endy, 1000);
			Log.info("Swipe successfully");
			TestngLogger.writeResult("Swipe successfully", true);

		} catch (Exception e) {
			Log.error("Can't swipe! : " + e);
			TestngLogger.writeResult("Can't swipe! : " + e, false);
			throw (e);
		}
	}

	/**
	 * Scan the image and return all text in image
	 * 
	 * @param screenshotName
	 *            name of image that used to scan
	 * 
	 * @return All string that available in screen
	 * 
	 * @throws Exception
	 */
	public String readToastMessage(String screenshotName) throws Exception {
		String result;
		try {
			File imageFile = new File("screenshot" + "/" + screenshotName
					+ ".png");
			ITesseract instance = new Tesseract();
			result = instance.doOCR(imageFile);
			Log.info("Image read successfully");
			TestngLogger.writeResult("Image read successfully", true);
		} catch (Exception e) {
			Log.error("Image read failled: " + e);
			TestngLogger.writeResult("Image read failed: " + e, false);
			throw (e);
		}
		return result;
	}

	/**
	 * Verify that a text that available in screen
	 * 
	 * @param compareText
	 *            string that need to be verify
	 * 
	 * @return All string that available in screen
	 * 
	 * @throws Exception
	 */
	public void verifyToastMessage(String compareText) throws Exception {
		try {
			String imageClientCode = "ClientCodeEmptyImage";
			this.takeScreenshot(imageClientCode);
			String TessMessage = readToastMessage(imageClientCode);
			Assert.assertTrue(TessMessage.contains(compareText));
			Log.info("String \"" + compareText + "\" is available in screen");
			TestngLogger.writeResult("String \"" + compareText
					+ "\" is available in screen", true);

		} catch (Exception e) {
			Log.error("String \"" + compareText
					+ "\" is not available in screen");
			TestngLogger.writeResult("String \"" + compareText
					+ "\" is not available in screen", true);
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
	public void takeScreenshot() throws Exception {

		String failureImageFileName = new SimpleDateFormat(
				Constant.TIME_STAMP_3)
				.format(new GregorianCalendar().getTime())
				+ ".jpg";

		try {
			if (driver != null) {

				File scrFile = ((TakesScreenshot) driver)
						.getScreenshotAs(OutputType.FILE);

				String screenShotDirector = Common.strMobileScreenshotFolder;
				FileUtils.copyFile(scrFile, new File(screenShotDirector
						+ failureImageFileName));
				TestngLogger.writeLogWithScreenshot(screenShotDirector
						+ failureImageFileName);
			}
		} catch (Exception e) {

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

		String screenShotDirector = Common.strMobileScreenshotFolder;
		String screenshotFile = Common.correctPath(screenShotDirector + filename);

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
				ImageIO.write(screenshot.getImage(), "jpg", new File(
						fileDir));
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
				ImageIO.write(screenshot.getImage(), "jpg", new File(fileDir));
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
	 * @param filename
	 * 		The name of screenshot
	 * @throws Exception
	 */
	public void compareScreenshot(String filename) throws Exception {
		String screenshotFileName = filename + ".jpg";
		String baseLineImage = Common.strMobileBaseLineScreenshotFolder + screenshotFileName;
		String actualImage = Common.strMobileActualScreenshotFolder + screenshotFileName;
		String diffImage = Common.strMobileDiffScreenshotFolder + screenshotFileName;
	
		try {
			if(!Common.pathExist(baseLineImage)) {
				takeScreenshotWithAshot(baseLineImage);
			}
			else {
				takeScreenshotWithAshot(actualImage);
				ImageCompare imageComparitor = new ImageCompare();
				BufferedImage diffBuff = imageComparitor.diffImages(baseLineImage, actualImage, 10);
				if(diffBuff == null) {
					Log.info("The actual screenshot of page [" + filename + "] matches with the baseline");
					TestngLogger.writeResult("The actual screenshot of page [" + filename + "] matches with the baseline", true);
					TestngLogger.writeLogWithScreenshot(actualImage);
				}
				else {
					Log.error("The actual screenshot of page [" + filename + "] doesn't match with the baseline");
					TestngLogger.writeResult("The actual screenshot of page [" + filename + "] doesn't match with the baseline", false);
					ImageIO.write(diffBuff, "jpg", new File(Common.strMobileDiffScreenshotFolder, screenshotFileName));
					TestngLogger.writeLogWithScreenshot(diffImage);
					throw new Exception("The actual screenshot doesn't match with the baseline");
				}
					
			}
		}
		catch(Exception e) {
			throw e;
		}
	}


	/**
	 * To compare the layout of a web element with baseline
	 * @param filename
	 * 		The name of screenshot
	 * @throws Exception
	 */
	public void compareScreenshot(String filename, By locator) throws Exception {
		String screenshotFileName = filename + ".jpg";
		String baseLineImage = Common.strMobileBaseLineScreenshotFolder + screenshotFileName;
		String actualImage = Common.strMobileActualScreenshotFolder + screenshotFileName;
		String diffImage = Common.strMobileDiffScreenshotFolder + screenshotFileName;
	
		try {
			if(!Common.pathExist(baseLineImage)) {
				takeScreenshotWithAshot(baseLineImage, locator);
			}
			else {
				takeScreenshotWithAshot(actualImage, locator);
				ImageCompare imageComparitor = new ImageCompare();
				BufferedImage diffBuff = imageComparitor.diffImages(baseLineImage, actualImage, 10);
				if(diffBuff == null) {
					Log.info("The actual screenshot of element [" + filename + "] matches with the baseline");
					TestngLogger.writeResult("The actual screenshotof element [" + filename + "] matches with the baseline", true);
					TestngLogger.writeLogWithScreenshot(actualImage);
				}
				else {
					Log.error("The actual screenshot of element [" + filename + "] doesn't match with the baseline");
					TestngLogger.writeResult("The actual screenshot of element [" + filename + "] doesn't match with the baseline", false);
					ImageIO.write(diffBuff, "jpg", new File(Common.strMobileDiffScreenshotFolder, screenshotFileName));
					TestngLogger.writeLogWithScreenshot(diffImage);
					throw new Exception("The actual screenshot of element [" + filename + "] doesn't match with the baseline");
				}
					
			}
		}
		catch(Exception e) {
			throw e;
		}
	}
}
