package com.wellmanage.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/**
 * This library is a wrapper for Selenium webdriver
 * 
 * @author averma
 * 
 */
public class SeleniumLib {

	private static final String XPTH_TXT_BOX = ".//input[@name=";
	private static final String BIC_NAME = "bicName";
	private static final String APP_URL_XPATH = "//*[@id=\"content-margins\"]//a[contains(text(),";
	private static final String FAILED_TRADE_REPORT = "FailedTradeReport";
	public static WebDriver driver;
	public static WebDriverWait wait;
	public static String reportDownloadTime = null;
	public static String parentWindowHandle = null;
	public static String childWindowHandle = null;
	public static String downloadFilepath = null;
	private static final Logger LOGGER = Logger.getLogger(SeleniumLib.class.getName());

	public static void startSSLSelenium(String serverUrl, String node, String downloadLocation)
			throws MalformedURLException {

		wait = new WebDriverWait(getDriverInstance(node, downloadLocation), 90);
		driver.get(serverUrl);
		parentWindowHandle = driver.getWindowHandle();
	}

	/**
	 * This method is used to get the chrome web driver instance.
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public static WebDriver getDriverInstance(String node, String downloadLocation) throws MalformedURLException {

		downloadFilepath = downloadLocation;
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new RemoteWebDriver(new URL(node), cap);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 90);
		return driver;
	}

	/**
	 * This method is used to start the ssl selenium for a chrome/ie browser.
	 * 
	 * @return
	 * @throws MalformedURLException
	 */

	public static void startSSLSelenium(String serverUrl, String node, String downloadLocation, String browser)
			throws MalformedURLException {

		wait = new WebDriverWait(getDriverInstance(node, downloadLocation, browser), 90);
		driver.get(serverUrl);
		parentWindowHandle = driver.getWindowHandle();
	}

	/**
	 * This method is used to get the chrome/ie web driver instance.
	 * 
	 * @return
	 * @throws MalformedURLException
	 */
	public static WebDriver getDriverInstance(String node, String downloadLocation, String browser)
			throws MalformedURLException {
		DesiredCapabilities cap;
		downloadFilepath = downloadLocation;

		if (browser.equals("chrome")) {
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			cap = DesiredCapabilities.chrome();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);

		} else {
			cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		}

		driver = new RemoteWebDriver(new URL(node), cap);
		driver.manage().window().setPosition(new Point(0, 0));
		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, 90);
		return driver;
	}

	/**
	 * Method to start the selenium using webdriver
	 */
	public static void startSelenium(WebDriver myDriver, String serverUrl) {
		myDriver.get(serverUrl);
		((JavascriptExecutor) myDriver)
				.executeScript("window.moveTo(0, 0); window.resizeTo(screen.width, screen.height);");
	}

	/**
	 * Method to stop selenium
	 */
	public static void stopSelenium() {
		driver.quit();
		driver = null;
	}

	/**
	 * Methos to stop Selenium using webdriver
	 */
	public static void stopSelenium(WebDriver myDriver) {
		myDriver.quit();
		myDriver = null;
	}

	/**
	 * Method to switch to default content
	 * 
	 */
	public static void switchDefaultContent() {
		driver.switchTo().defaultContent();
	}

	/**
	 * Method to switch to default content using Webdriver new instance
	 * 
	 */
	public static void switchDefaultContent(WebDriver myDriver) {
		myDriver.switchTo().defaultContent();
	}

	/**
	 * Method to switch to frame by frame name
	 * 
	 * @throws Exception
	 * 
	 */
	public static void switchToFrame(By bylocator) {
		switchToFrame(driver, bylocator);
	}

	public static void switchToFrame(WebDriver myDriver, By bylocator) {
		WebElement frame = myDriver.findElement(bylocator);
		myDriver.switchTo().frame(frame);
	}

	/**
	 * finds the element and return particular webElement elements
	 * 
	 * @param locator
	 */
	public static WebElement findElement(By locator) {
		return driver.findElement(locator);
	}

	/**
	 * finds the element and return true if it exists
	 * 
	 * @param locator
	 */
	public static boolean findElements(String locator) {
		if (driver.findElements(By.xpath(locator)).size() > 0) {
			return true;
		} else
			return false;
	}

	/**
	 * finds the element and return true if it exists
	 * 
	 * @param locator
	 */
	public static boolean findElements(WebDriver myDriver, String locator) {
		if (myDriver.findElements(By.xpath(locator)).size() > 0) {
			return true;
		} else
			return false;
	}

	/**
	 * Wait until element is visible
	 * 
	 * @param locator
	 * @param time
	 */
	public static void waitForElement(By locator, int time) {
		waitForElement(driver, locator, time);
	}

	/**
	 * Wait until element is visible
	 * 
	 * @param myDriver-
	 *            A WebDriver instance for the second browser
	 * @param locator
	 * @param time
	 */
	public static void waitForElement(WebDriver myDriver, By locator, int time) {
		long end = System.currentTimeMillis() + time;

		while (System.currentTimeMillis() < end) {
			Boolean exists = myDriver.findElements(locator).size() != 0;
			if (exists) {
				WebElement resultsDiv = myDriver.findElement(locator);
				if (resultsDiv.isDisplayed()) {
					break;
				}
			}
		}
	}

	/**
	 * This method waits until the element in located in the page
	 * 
	 * @param locator
	 * @return
	 */
	static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
		return new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(locator);
			}
		};
	}

	/**
	 * Wait until the element is present
	 * 
	 * @param locator
	 */
	public static void waitForElementPresent(final By locator) {
		wait.until(presenceOfElementLocated(locator));
	}

	/**
	 * Wait until element is present, if string for the locator is passed along
	 * with its type (xpath, link, class etc) then By element is constructed
	 * 
	 * @param locator
	 * @param locatorType
	 */
	public static void waitForElementPresent(String locator, String locatorType) {
		wait.until(presenceOfElementLocated(getLocatorBy(locator, locatorType)));
	}

	/**
	 * Returns boolean and checks if element is visible
	 * 
	 * @param myDriver
	 * @param locator
	 * @return
	 */
	public static boolean isElementVisible(WebDriver myDriver, By locator) {
		WebElement resultsDiv = null;
		try {
			resultsDiv = myDriver.findElement(locator);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return resultsDiv.isDisplayed();
	}

	/**
	 * Returns boolean and checks if element is visible
	 * 
	 * @param locator
	 * @return
	 */
	public static boolean isElementVisible(By locator) {
		return isElementVisible(driver, locator);
	}

	/**
	 * Returns the By locator if string for the locator is passed along with its
	 * type (xpath, link, class etc) then By element is constructed
	 * 
	 * @param locator
	 * @param locatorType
	 * @return
	 */
	public static By getLocatorBy(String locator, String locatorType) {
		By by = null;
		if (locatorType.equals("xpath")) {
			by = By.xpath(locator);
			return by;
		} else if (locatorType.equals("name")) {
			by = By.name(locator);
			return by;
		} else if (locatorType.equals("id")) {
			by = By.id(locator);
			return by;
		}
		return by;
	}

	/**
	 * Method to sleep for specified time
	 * 
	 * @param i
	 */
	public static void waitSeconds(int i) {
		try {

			int sleepTime = i * 1000;
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			throw new RuntimeException(e.toString(), e);
		}
	}

	/**
	 * Checks if element is visible. If the specified identifier has multiple
	 * elements then it checks at least one element is present
	 * 
	 * @param xpathLocator
	 * @param timeoutInSec
	 * @return
	 * @throws Exception
	 */
	public static boolean isElementPresent(String xpathLocator, int timeoutInSec) throws Exception {
		return isElementPresent(driver, xpathLocator, timeoutInSec);
	}

	/**
	 * Checks if element is visible.
	 * 
	 * @param myDriver
	 *            the WebDriver object representing the second browser.
	 * @param xpathLocator
	 * @param timeoutInSec
	 * @return true if element is found in the resulting page, false otherwise
	 * @throws Exception
	 */
	public static boolean isElementPresent(WebDriver myDriver, String xpathLocator, int timeoutInSec) throws Exception {
		Boolean exists = myDriver.findElements(By.xpath(xpathLocator)).size() != 0;
		if (exists) {
			return exists;
		} else {
			myDriver.manage().timeouts().implicitlyWait(timeoutInSec, TimeUnit.SECONDS);
			exists = myDriver.findElements(By.xpath(xpathLocator)).size() != 0;
			return exists;
		}
	}

	/**
	 * Click the locator
	 * 
	 * @param bylocator
	 * @throws Exception
	 */
	public static void click(By bylocator) {
		click(driver, bylocator);
	}

	/**
	 * Click the locator
	 * 
	 * @param myDriver
	 *            a WebDriver instance for a second browser
	 * @param bylocator
	 *            an Xpath for the location to be clicked
	 * @throws Exception
	 */
	public static void click(WebDriver myDriver, By bylocator) {
		myDriver.findElement(bylocator).click();
	}

	/**
	 * Click the element and wait until element is present
	 * 
	 * @param myDriver
	 * @param bylocator
	 * @param element
	 * @throws Exception
	 */
	public static void clickWaitForElementToAppear(WebDriver myDriver, By bylocator, By element) throws Exception {
		myDriver.findElement(bylocator).click();
		waitForElementPresent(element);
	}

	/**
	 * Click the element and wait until element is present
	 * 
	 * @param bylocator
	 * @param element
	 * @throws Exception
	 */
	public static void clickWaitForElementToAppear(By bylocator, By element) throws Exception {
		clickWaitForElementToAppear(driver, bylocator, element);
	}

	/**
	 * Method to perform click at the center
	 * 
	 * @param bylocator
	 * @throws Exception
	 */
	public static void clickAtMiddleOfElement(By bylocator) {
		Actions action = new Actions(driver);
		action.click(driver.findElement(bylocator));
		action.release();
	}

	/**
	 * Method to take screenshot
	 * 
	 * @return
	 */
	public static File takeScreenShot() {
		File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return screenShotFile;
	}

	public static File takescreen() {

		File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return screen;

	}

	/**
	 * Method to take ScreenShot using Web Driver
	 */
	public static File takeScreenShot(WebDriver driver) {
		File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		return screenShotFile;
	}

	/**
	 * Verify if text is present in the page
	 * 
	 * @param myDriver
	 * @param value
	 * @return
	 */
	public static boolean verifyTextPresent(WebDriver myDriver, String value) {
		return getPageSource(myDriver).contains(value);
	}

	/**
	 * Retrieve the source of the current page.
	 * 
	 * @param myDriver
	 * @return a String containing the source of the current page
	 */
	public static String getPageSource(WebDriver myDriver) {
		return myDriver.getPageSource();
	}

	/**
	 * Retrieve the source of the current page.
	 * 
	 * @return a String containing the source of the current page
	 */
	public static String getPageSource() {
		return getPageSource(driver);
	}

	/**
	 * Verify if text is present in the page
	 * 
	 * @param value
	 * @return
	 */
	public static boolean verifyTextPresent(String value) {
		return verifyTextPresent(driver, value);
	}

	/**
	 * Get the text of the element
	 * 
	 * @param myDriver
	 * @param elementID
	 * @return
	 */
	public static String getTextForElement(WebDriver myDriver, String elementID) {
		return myDriver.findElement(By.xpath(elementID)).getText();
	}

	/**
	 * Get the text of the element
	 * 
	 * @param elementID
	 * @return
	 */
	public static String getTextForElement(String elementID) {
		return getTextForElement(driver, elementID);
	}

	/**
	 * Type the text value
	 * 
	 * @param by
	 * @param textField
	 */
	public static void type(By by, String textField) {
		type(driver, by, textField);
	}

	/**
	 * Type the text value for the second browser
	 * 
	 * @param myDriver
	 *            a WebDriver instance for a second browser
	 * @param by
	 *            an Xpath locator for the text field
	 * @param textField
	 *            the String to be entered in the text field
	 */
	public static void type(WebDriver myDriver, By by, String textField) {
		myDriver.findElement(by).sendKeys(textField);
	}

	/**
	 * Clear the text box
	 * 
	 * @param by
	 */
	public static void clearText(By by) {
		clearText(driver, by);
	}

	/**
	 * Clear the text box
	 * 
	 * @param myDriver
	 * @param by
	 */
	public static void clearText(WebDriver myDriver, By by) {
		myDriver.findElement(by).clear();
	}

	/**
	 * Get the value of the element using xpath locator
	 * 
	 * @param myDriver
	 * @param elementID
	 * @return
	 */
	public static String getValueOfElement(WebDriver myDriver, String elementID) {
		return myDriver.findElement(By.xpath(elementID)).getAttribute("value");
	}

	/**
	 * Get the value of the element using xpath locator
	 * 
	 * @param elementID
	 * @return
	 */
	public static String getValueOfElement(String elementID) {
		return getValueOfElement(driver, elementID);
	}

	/**
	 * Get the value of the element using By object
	 * 
	 * @param myDriver
	 * @param by
	 * @return
	 */
	public static String getValueOfElementUsingBy(WebDriver myDriver, By by) {
		return myDriver.findElement(by).getAttribute("value");
	}

	/**
	 * Get the value of the element using By object
	 * 
	 * @param by
	 * @return
	 */
	public static String getValueOfElementUsingBy(By by) {
		return getValueOfElementUsingBy(driver, by);
	}

	/**
	 * Check whether a checkbox or radio button is selected
	 * 
	 * @param myDriver
	 * @param by
	 * @return
	 */
	public static boolean isSelected(WebDriver myDriver, By by) {
		return myDriver.findElement(by).isSelected();
	}

	/**
	 * Check whether a checkbox or radio button is selected
	 * 
	 * @param by
	 * @return
	 */
	public static boolean isSelected(By by) {
		return isSelected(driver, by);
	}

	/**
	 * Get the table count
	 * 
	 * @param myDriver
	 * @param xpath
	 * @return
	 */
	public static int getTableRowCount(WebDriver myDriver, String xpath) {
		String rowXpath = xpath + "/tbody/tr";
		WebElement tableElement = myDriver.findElement(By.xpath(xpath));
		List<WebElement> trCollection = tableElement.findElements(By.xpath(rowXpath));
		return trCollection.size();
	}

	/**
	 * Get the table count
	 * 
	 * @param myDriver
	 * @param xpath
	 * @return
	 */
	public static int getTableColumnCount(WebDriver myDriver, String xpath) {
		String colXpath = xpath + "/tbody/tr[1]/td";
		WebElement tableElement = myDriver.findElement(By.xpath(xpath));
		List<WebElement> tcCollection = tableElement.findElements(By.xpath(colXpath));
		return tcCollection.size();
	}

	/**
	 * Get the table row count
	 * 
	 * @param xpath
	 * @return integer number
	 */

	public static int getTableRC(String xpath) {
		return getcount(driver, xpath);
	}

	public static int getcount(WebDriver mydriver, String xpath) {
		String i = xpath + "/tbody/tr";
		int count;
		count = mydriver.findElements(By.xpath(i)).size();
		return count;
	}

	/**
	 * Get the table row count
	 * 
	 * @param xpath
	 * @return
	 */
	public static int getTableRowCount(String xpath) {
		return getTableRowCount(driver, xpath);
	}

	/**
	 * Get the table column count
	 * 
	 * @param xpath
	 * @return
	 */
	public static int getTableColumnCount(String xpath) {
		return getTableColumnCount(driver, xpath);
	}

	/**
	 * Get the dropdown list
	 * 
	 * @param by
	 * @return
	 */
	public static Select getDropDownList(By by) {
		WebElement dropDownListBox = driver.findElement(by);
		Select select = new Select(dropDownListBox);
		return select;
	}

	/**
	 * Get the dropdown list
	 * 
	 * @param myDriver
	 *            a WebDriver object representing a separate browser instance.
	 * @param by
	 * @return a Select object for the desired drop down list
	 */
	public static Select getDropDownList(WebDriver myDriver, By by) {
		WebElement dropDownListBox = myDriver.findElement(by);
		Select select = new Select(dropDownListBox);
		return select;
	}

	/**
	 * Select element from the drop down list using given text
	 * 
	 * @param select
	 * @param selectText
	 */
	public static void selectListElement(Select select, String selectText) {
		select.selectByVisibleText(selectText);
	}

	/**
	 * Select element from the drop down list using Value
	 * 
	 * @param select
	 * @param selectText
	 */
	public static void selectListElementByValue(Select select, String selectValue) {
		select.selectByValue(selectValue);
	}

	/**
	 * Get the current URL
	 * 
	 * @param myDriver
	 *            the WebDriverinstance in use
	 * @return the URL for browser instance in use
	 */
	public static String getCurrentUrl(WebDriver myDriver) {
		return myDriver.getCurrentUrl();
	}

	/**
	 * Get the current URL
	 * 
	 * @return the URL for browser instance in use
	 */
	public static String getCurrentUrl() {
		return getCurrentUrl(driver);
	}

	/**
	 * Load a new web page in the current browser window
	 * 
	 * @param myDriver
	 *            the WebDriverinstance in use
	 * @param url
	 *            a String containing the URL to be loaded
	 */
	public static void loadUrl(WebDriver myDriver, String url) {
		myDriver.get(url);
	}

	/**
	 * Load a new web page in the current browser window
	 * 
	 * @param url
	 *            a String containing the URL to be loaded
	 */
	public static void loadUrl(String url) {
		loadUrl(driver, url);
	}

	/**
	 * Getter method for driver
	 * 
	 * @return the WebDriver reference
	 */
	public static WebDriver getDriver() {
		return driver;
	}

	/**
	 * Get the selected element from the drop down
	 * 
	 * @param select
	 * @param selectText
	 */
	public static String getSelectedListElement(Select select) {
		WebElement option = select.getFirstSelectedOption();
		String selectedOption = option.getText();
		return selectedOption;
	}

	/**
	 * Navigate to URL
	 * 
	 * @param String
	 *            url
	 * 
	 */
	public static void navigateToURL(String url) {
		driver.navigate().to(url);
	}

	/**
	 * Navigate to URL
	 * 
	 * @param String
	 *            url
	 * @param WebDriver
	 *            myDriver
	 */
	public static void navigateToURL(WebDriver myDriver, String url) {
		myDriver.navigate().to(url);
	}

	/**
	 * Refresh the Page
	 * 
	 * @param WebDriver
	 *            myDriver
	 */
	public static void refresh(WebDriver myDriver) {
		myDriver.navigate().refresh();
	}

	/**
	 * Refresh the Page
	 * 
	 * @param WebDriver
	 *            myDriver
	 */
	public static void refresh() {
		driver.navigate().refresh();
	}

	/**
	 * Return the Set of handles for the open windows.
	 * 
	 * @param myDriver
	 *            the WebDriver instance in use
	 * @return a Set of handles for the windows opened by myDriver
	 */
	public static Set<String> getWindowHandles(WebDriver myDriver) {
		return myDriver.getWindowHandles();
	}

	/**
	 * Return the Set of handles for the open windows.
	 * 
	 * @return a Set of handles for the windows opened by the default WebDriver
	 *         instance
	 */
	public static Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	/**
	 * Wait for an element to be visible to avoid ElementNotVisibleException
	 * 
	 * @param locator
	 *            The xpath for the element that should be visible
	 */
	public static void waitForElementVisible(String locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
	}

	/**
	 * Set the implicit wait.
	 * 
	 * @param locator
	 *            The time out is in seconds
	 */
	public static String SetImplicitWaitInSeconds(int timeOut) {
		driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
		return "Timeout set to " + timeOut + " seconds.";
	}

	/**
	 * Get the table data method it will return the Table existence as boolean
	 * value
	 * 
	 * @param xpthdatatable
	 *            The xpath for the element that should be visible
	 *
	 * @param i
	 *            Integer value as 0 and 1 for Locator formation
	 */
	public static boolean getTableData(String xpthdatatable, int i) {
		String xpath = null;
		WebElement dataTable = null;

		if (i == 0)
			xpath = xpthdatatable + "/tbody[2]";
		else if (i == 1)
			xpath = xpthdatatable + "/tbody/tr";
		if (driver.findElements(By.xpath(xpath)).size() > 0)
			dataTable = driver.findElement(By.xpath(xpath));
		else
			return false;

		if (!dataTable.getText().isEmpty()) {
			return true;
		} else {
			return false;
		}

	}

	public static String getTableData(String xpthdatatable) {
		String xpath = xpthdatatable + "/tbody[2]";

		xpath = xpath + "/tr" + "/td[" + 2 + "]";
		WebElement dataTable = null;
		try {
			if (driver.findElements(By.xpath(xpath)).size() > 0) {
				dataTable = driver.findElement(By.xpath(xpath));
				return (dataTable.getText());
			}
		} catch (Exception e) {
			return "";
		}

		return "";
	}

	/**
	 * Check is alert pop is displayed
	 * 
	 * 
	 * Returns True if pop up is shown, false if pop up is not shown
	 */
	public static boolean getAlert() {
		Alert alert = null;
		try {
			alert = driver.switchTo().alert();
		} catch (NoAlertPresentException e) {
			return false;
		}
		return true;
	}

	/**
	 * Verify if report is downloaded
	 * 
	 * 
	 * Returns True if downloaded
	 */

	public static boolean verifyReportDownloaded(int beforeNumber) {
		File file = new File(downloadFilepath);
		int afterNumber = file.list().length;
		if (file.isDirectory()) {
			System.out.println("Size is : " + file.list().length);
			if (afterNumber > beforeNumber) {
				LOGGER.debug("###Directory is not empty!###");
				return true;
			} else {
				LOGGER.debug("###Directory is empty!###");
				return false;
			}

		} else {
			LOGGER.debug("###This is not a directory!###");
			return false;

		}
	}

	/**
	 * Delete the Directory
	 * 
	 * 
	 * Returns path
	 */
	public static boolean deleteDirectory(File path) {
		System.out.println("Path is :" + path.getName().toString());
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getAbsolutePath());
				if (files[i].isFile()) {
					// deleteDirectory(files[i]);
				} else {
					System.out.println("In Else");
					files[i].deleteOnExit();
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static void SetReportDownloadTime() {
		reportDownloadTime = new SimpleDateFormat("MMddyyyyHHmm").format(new Date());
	}

	/**
	 * get the date in the MM-dd-YYYY format
	 * 
	 * @param i
	 * @return Start Date
	 */
	public static String getBeginDate(int i) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, i);
		SimpleDateFormat s = null;
		if (i == -7)
			s = new SimpleDateFormat("MM/dd/YYYY");
		else
			s = new SimpleDateFormat("MM/dd/YYYY");

		String beginDate = s.format(new Date(cal.getTimeInMillis()));
		return beginDate;
	}

	/**
	 * get Current date in MM-dd-YYYY format
	 * 
	 * @param i
	 * @return Current date
	 */
	public static String getCurrentDate(int i) {
		String currDate = null;
		if (i == -7)
			currDate = new SimpleDateFormat("MM/dd/YYYY").format(new Date());
		else
			currDate = new SimpleDateFormat("MM/dd/YYYY").format(new Date());
		return currDate;
	}

	/**
	 * get the xpath value for the Link from the IAPortal
	 * 
	 * @param pageURL
	 * @return the xpath for the Link from the IAPortal
	 */
	public static String getXpathForURL(String pageURL) {
		return APP_URL_XPATH + "'" + pageURL + "')]";
	}

	public static String getXpathForTextBox(String input) {
		switch (input) {
		case BIC_NAME:
			return XPTH_TXT_BOX + "'" + input + "']";
		}
		return "";
	}

	public static Integer getOpenWindowCount() {
		Set<String> handles = getWindowHandles();
		int i = 0;
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()) {
			String str = iterator.next();
			i++;
			if (i > 1)
				childWindowHandle = str;
		}

		return i;
	}

	public static void closeFailTradeAppian() {
		waitSeconds(5);
		driver.close();
		driver.switchTo().window(parentWindowHandle);
	}

	/**
	 * wait Load method for perticular Loader
	 * 
	 * @param element
	 */
	public static void waitLoad(String element) {
		long end = System.currentTimeMillis() + 60000;
		WebElement elem = driver.findElement(By.xpath(element));
		while (System.currentTimeMillis() < end) {
			try {
				elem.getAttribute("title");
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	/**
	 * Explicit Wait condition for the visibility of Element
	 * 
	 * @param timeunit
	 *            in Seconds
	 * @param element,
	 *            Locator for WebElement
	 */
	public static void explicitwait(String element, int timeunit) {
		wait = new WebDriverWait(driver, timeunit);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
	}

	/**
	 * Finds elements and return number of elements
	 * 
	 * @param locator
	 */
	public static int findElementsNum(By locator) {
		return driver.findElements(locator).size();
	}

}
