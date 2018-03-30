package com.wellmanage.steps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import com.wellmanage.common.Locators;
import com.wellmanage.common.SeleniumLib;
//import com.wellmanage.database.TletIdrDao;
//import com.wellmanage.jira.JiraIntegration;

import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java8.En;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberScenario;

@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath*:/spring-context/applicationContext-smoke-suite.xml" })
public class TradeLetterTestUIScenario implements En {

	@Value("${browser}")
	public String browser;

	@Value("${envtype}")
	public String envtype;

	@Value("${url}")
	public String url;

	@Value("${username}")
	public String node;

	@Value("${downLoadLocation}")
	public String downLoadLocation;

	@Value("${erurl}")
	public String erurl;

	private Scenario scenario;

	@Value("#{'${ipaddress.map}'.split(',')}")
	private List<String> fields;

	private Map<String, String> machineip;
	private LinkedList<String> tradeLetter = new LinkedList<>();

	//@Autowired
	//private TletIdrDao tletIdrDao;

	@Autowired
	// private JiraIntegration jiraIntegration;
	private static final Logger LOGGER = Logger.getLogger(TradeLetterTestUIScenario.class.getName());

	@After
	public void afterScenario(Scenario sce) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException, URISyntaxException {

		this.scenario = sce;
		Field f = this.scenario.getClass().getDeclaredField("reporter");
		f.setAccessible(true);
		JUnitReporter reporter = (JUnitReporter) f.get(this.scenario);
		Field e = reporter.getClass().getDeclaredField("executionUnitRunner");
		e.setAccessible(true);
		ExecutionUnitRunner execution = (ExecutionUnitRunner) e.get(reporter);
		Field g = execution.getClass().getDeclaredField("cucumberScenario");
		g.setAccessible(true);
		CucumberScenario cubScenario = (CucumberScenario) g.get(execution);
		LOGGER.debug("Scenario Status: " + this.scenario.getStatus());
		// jiraIntegration.jiraMap(this.scenario.getSourceTagNames(), cubScenario,
		// this.scenario.getStatus());
		if (SeleniumLib.getDriver() == null) {
			LOGGER.debug("###Driver is Closed Not Active####");
		} else {
			LOGGER.debug("###Driver is Active Hence CLosing it####");
			SeleniumLib.stopSelenium();
		}

		if (!scenario.getSourceTagNames().stream().filter(tag -> tag.contains("@smoke")).collect(Collectors.toList())
				.contains("@smoke")) {
			//tletIdrDao.deleteTradeLetter(new ArrayList<>(tradeLetter));
		}

	}

	public TradeLetterTestUIScenario() {

		Given("^User navigates to IAPortal$", () -> {
			try {
				LOGGER.debug("###Launching IA Portal####");
				SeleniumLib.startSSLSelenium(url, node, downLoadLocation, browser);
				SeleniumLib.switchToFrame(By.xpath(Locators.xpthMenuFrame));
				SeleniumLib.click(By.xpath(Locators.xpthPortalLink));
				SeleniumLib.switchDefaultContent();
				SeleniumLib.switchToFrame(By.xpath(Locators.xpthTCSFrame));
			} catch (Exception e) {
				e.printStackTrace();
				SeleniumLib.stopSelenium();
			}
		});

		When("^User clicks on \"([^\"]*)\" URL$", (String pageURL) -> {
			try {
				String msg = "####Opening page#### " + pageURL;
				LOGGER.debug(msg);
				String xpath = SeleniumLib.getXpathForURL(pageURL);
				if (browser.equals("chrome")) {
					SeleniumLib.waitForElement(By.xpath(xpath), 5);
					SeleniumLib.click(By.xpath(xpath));
					ArrayList<String> tabs2 = new ArrayList<String>(SeleniumLib.driver.getWindowHandles());
					SeleniumLib.driver.switchTo().window(tabs2.get(1));
				} else {
					SeleniumLib.driver.get(SeleniumLib.driver.findElement(By.xpath(xpath)).getAttribute("href"));
					if (SeleniumLib.driver.getTitle().contains("Certificate Error")) {
						LOGGER.info(" IE Certificate Error : " + SeleniumLib.driver.getTitle());
						SeleniumLib.driver.navigate().to(Locators.xpthOvrrideSSL);
						LOGGER.info(" IE Override SSL Done");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				SeleniumLib.stopSelenium();
				org.junit.Assert.assertTrue(false);
			}
		});

		When("^User add new Trade letter with below information$", (DataTable info) -> {
			SeleniumLib.click(By.xpath(Locators.xpthAddNew));
			SeleniumLib.waitForElement(By.xpath(Locators.xpthSaveBtn), 5);
			info.raw().stream().filter(i -> i.get(1).equals("Y"))
					.forEach(i -> SeleniumLib.click(By.xpath(Locators.getXpthOptionType.apply(i.get(0)))));
			info.raw().stream().filter(i -> !i.get(1).equals("Y")).filter(i -> !i.get(1).equals("N"))
					.forEach(optionList -> {
						try {
							SeleniumLib.type(By.xpath(Locators.getXpthTxtInfo.apply(optionList.get(0))),
									optionList.get(1));
						} catch (NoSuchElementException e1) {
							try {
								SeleniumLib.click(By.xpath(Locators.getXpthDrpDwn.apply(optionList.get(0))));
								SeleniumLib.click(By.xpath(Locators.xpthUnchkAll));
								SeleniumLib.type(By.xpath(Locators.xpthId), optionList.get(1));
								SeleniumLib.click(By.xpath(Locators.xpthCbx));
								LOGGER.info("Exception ex1 - " + optionList.get(0) + " : " + optionList.get(1));
							} catch (NoSuchElementException e2) {
								try {
									WebElement select = SeleniumLib.findElement(By.xpath(Locators.xpthAdmin));
									SeleniumLib.selectListElement(new Select(select), optionList.get(1));
									LOGGER.info("Exception ex2 - " + optionList.get(0) + " : " + optionList.get(1));
								} catch (NoSuchElementException e3) {
									SeleniumLib.click(By.xpath(Locators.xpthCbx));
									LOGGER.info("Exception ex3 - " + optionList.get(0) + " : " + optionList.get(1));
								}
							}
						}
					});

			SeleniumLib.click(By.xpath(Locators.xpthSaveBtn));
			SeleniumLib.waitForElementVisible(Locators.xpthAddNewSuccess);
			tradeLetter.add(SeleniumLib.findElement(By.xpath(Locators.xpthAddNewSuccess)).getText().split(" - ")[1]);
			LOGGER.info("Success : Generated Trade Letter ID - "
					+ SeleniumLib.findElement(By.xpath(Locators.xpthAddNewSuccess)).getText().split(" - ")[1]);
		});

		Then("^User should be presented with the below message$", (DataTable msg) -> {
			SeleniumLib.waitForElementVisible(Locators.xpthAddNewSuccess);
			Assert.assertEquals("There is some error in Add New Trade Letter", msg.asList(String.class).get(0),
					SeleniumLib.findElement(By.xpath(Locators.xpthAddNewSuccess)).getText().split(" - ")[0]);
			LOGGER.info("Assertion done : " + SeleniumLib.findElement(By.xpath(Locators.xpthAddNewSuccess)).getText());

		});

		When("^User search for Trade Letter with below info$", (DataTable info) -> {
			SeleniumLib.waitForElementVisible(Locators.xpthClr);
			SeleniumLib.click(By.xpath(Locators.xpthClr));
			try {
				SeleniumLib.type(By.xpath(Locators.xpthOnDmndTLId), tradeLetter.getLast());
			} catch (java.util.NoSuchElementException ex) {
				LOGGER.info("No Trade Letter generated at runtime.Smoke Test");
			}
			info.raw().stream().filter(param -> !param.get(0).contains("Date")).forEach(param -> {
				SeleniumLib.type(By.xpath(Locators.getXpthSrch.apply(param.get(0))), param.get(1));
				SeleniumLib.waitForElementVisible(Locators.getXpthTypeAhead.apply(param.get(1)));
				SeleniumLib.click(By.xpath(Locators.getXpthTypeAhead.apply(param.get(1))));
			});

			info.raw().stream().filter(param -> param.get(0).contains("Date")).forEach(param -> {
				if (param.get(1).equals("Today")) {
					Arrays.asList(Locators.getOpenCalendar.apply(param.get(0)), Locators.xpthDtTdy).stream()
							.map(By::xpath).forEach(SeleniumLib::click);
				} else {
					SeleniumLib.type(By.xpath(Locators.getCrtdDt.apply(param.get(0))), param.get(1));
				}
			});
			SeleniumLib.click(By.xpath(Locators.xpthSrch));
			SeleniumLib.waitForElementVisible(Locators.xpthExpndTL);
			SeleniumLib.click(By.xpath(Locators.xpthExpndTL));
			LOGGER.info("Search successfull");

		});

		Then("^Verify search results are displayed with below info$", (DataTable info) -> {
			SeleniumLib.waitForElementVisible(Locators.xpthLblGridResult);
			info.raw().stream().forEach(param -> {
				SeleniumLib.waitForElementVisible(Locators.getGridCell.apply(param.get(0)));
				if (!param.get(1).equals("Today")) {
					Assert.assertEquals(param.get(1),
							SeleniumLib.findElement(By.xpath(Locators.getGridCell.apply(param.get(0)))).getText());
				} else {
					Assert.assertEquals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
							SeleniumLib.findElement(By.xpath(Locators.getGridCell.apply(param.get(0)))).getText());
				}
			});
			LOGGER.info("Assertion done : " + info);

		});

		When("^User updates Trade Letter with below info$", (DataTable info) -> {
			SeleniumLib.click(By.xpath(Locators.xpthEdtIcn));
			SeleniumLib.waitForElementVisible(Locators.xpthUpdtBtn);
			info.raw().forEach(optionList -> {
				try {
					SeleniumLib.type(By.xpath(Locators.getXpthTxtInfo.apply(optionList.get(0))), optionList.get(1));
				} catch (NoSuchElementException e1) {
					try {
						SeleniumLib.click(By.xpath(Locators.getXpthDrpDwn.apply(optionList.get(0))));
						SeleniumLib.click(By.xpath(Locators.xpthUnchkAll));
						SeleniumLib.type(By.xpath(Locators.xpthId), optionList.get(1));
						SeleniumLib.click(By.xpath(Locators.xpthCbx));
					} catch (NoSuchElementException e2) {
						try {
							WebElement select = SeleniumLib.findElement(By.xpath(Locators.xpthAdmin));
							SeleniumLib.selectListElement(new Select(select), optionList.get(1));
						} catch (NoSuchElementException e3) {
							SeleniumLib.click(By.xpath(Locators.xpthCbx));
						}
					}
				}
			});

			SeleniumLib.click(By.xpath(Locators.xpthUpdtBtn));
			LOGGER.info("Success : Trade Letter updated successfully");

		});

		When("^User delete a Trade Letter$", () -> {
			tradeLetter.forEach(id -> {
				SeleniumLib.click(By.xpath(Locators.xpthClr));
				SeleniumLib.type(By.xpath(Locators.xpthOnDmndTLId), id);
				SeleniumLib.click(By.xpath(Locators.xpthSrch));
				SeleniumLib.waitForElementVisible(Locators.xpthDltIcn);
				SeleniumLib.click(By.xpath(Locators.xpthDltIcn));
				SeleniumLib.waitForElementVisible(Locators.xpthDltBtn);
				SeleniumLib.click(By.xpath(Locators.xpthDltBtn));
				SeleniumLib.waitForElementVisible(Locators.xpthAddNewSuccess);
				LOGGER.info("Success : Trade Letter Deleted , ID : " + id);
			});

		});

		When("^User copy Trade Letter with below info$", (DataTable info) -> {
			SeleniumLib.click(By.xpath(Locators.xpthCopyIcn));
			SeleniumLib.waitForElementVisible(Locators.xpthSaveBtn);
			info.raw().forEach(optionList -> {
				try {
					SeleniumLib.type(By.xpath(Locators.getXpthTxtInfo.apply(optionList.get(0))), optionList.get(1));
				} catch (NoSuchElementException e1) {
					try {
						SeleniumLib.click(By.xpath(Locators.getXpthDrpDwn.apply(optionList.get(0))));
						SeleniumLib.click(By.xpath(Locators.xpthUnchkAll));
						SeleniumLib.type(By.xpath(Locators.xpthId), optionList.get(1));
						SeleniumLib.click(By.xpath(Locators.xpthCbx));
					} catch (NoSuchElementException e2) {
						try {
							WebElement select = SeleniumLib.findElement(By.xpath(Locators.xpthAdmin));
							SeleniumLib.selectListElement(new Select(select), optionList.get(1));
						} catch (NoSuchElementException e3) {
							SeleniumLib.click(By.xpath(Locators.xpthCbx));
						}
					}
				}
			});

			SeleniumLib.click(By.xpath(Locators.xpthSaveBtn));
			SeleniumLib.waitForElementVisible(Locators.xpthAddNewSuccess);
			tradeLetter.add(SeleniumLib.findElement(By.xpath(Locators.xpthAddNewSuccess)).getText().split(" - ")[1]);
			LOGGER.info("Success : Generated Trade Letter ID - "
					+ SeleniumLib.findElement(By.xpath(Locators.xpthAddNewSuccess)).getText().split(" - ")[1]);

		});

		When("^User generate the Trade Letter from maintenance screen$", () -> {
			Arrays.asList(Locators.xpthTLcbx, Locators.xpthDlvrSlctd).stream().map(By::xpath)
					.forEach(SeleniumLib::click);
			SeleniumLib.waitForElementVisible(Locators.xpthDlvrFrmDt);
			SeleniumLib.click(By.xpath(Locators.xpthDlvrFrmDt));
			SeleniumLib.waitForElementVisible(Locators.xpthDlvrBtn);
			Arrays.asList(Locators.xpthDlvrDtTdy, Locators.xpthDlvrStatus, Locators.xpthDlvrBtn).stream().map(By::xpath)
					.forEach(SeleniumLib::click);
			LOGGER.info("Success : User generated the Trade Letter from maintenance screen");
		});

		When("^User navigate to Trade Letters-On Demand$", () -> {
			SeleniumLib.click(By.xpath(Locators.xpthOnDmnd));
			LOGGER.info("Success : User navigated to Trade Letter-On Demand");

		});

		When("^User generate the Trade Letter$", () -> {
			tradeLetter.forEach(id -> {
				SeleniumLib.type(By.xpath(Locators.xpthOnDmndTLId), id);
				Arrays.asList(Locators.xpthOnDmndFrmDt, Locators.xpthDtTdy, Locators.xpthOnDmndToDt, Locators.xpthDtTdy,
						Locators.xpthDlvrBtn).stream().map(By::xpath).forEach(SeleniumLib::click);
			});
			LOGGER.info("Success : Trade Letter generated");
		});

		When("^User clicks on Search button$", () -> {
			SeleniumLib.click(By.xpath(Locators.xpthSrch));
			LOGGER.info("User clicked on search button");
		});

		Then("^Verify search result is displayed$", () -> {
			SeleniumLib.waitForElementVisible(Locators.xpthLblGridResult);
			Assert.assertTrue("There is some error Trade Letter search",
					SeleniumLib.findElementsNum(By.xpath(Locators.xpthSrch)) > 0);
			LOGGER.info("Assertion done : Search results are displayed");
		});

		When("^User selects a search result$", () -> {
			SeleniumLib.waitForElementVisible(Locators.xpthLblGridResult);
			SeleniumLib.click(By.xpath(Locators.xpthSrtStatus));
			SeleniumLib.type(By.xpath(Locators.xpthOnDmndTLId),
					SeleniumLib.findElement(By.xpath(Locators.xpthRcntTL)).getText());
			SeleniumLib.click(By.xpath(Locators.xpthSrch));
			SeleniumLib.waitForElementVisible(Locators.xpthLblGridResult);
			SeleniumLib.click(By.xpath(Locators.xpthTLcbx));
			LOGGER.info("Success : User selected the first search result");

		});

		When("^User clicks on Deliver Selected button$", () -> {
			SeleniumLib.waitForElementVisible(Locators.xpthDlvrSlctd);
			SeleniumLib.click(By.xpath(Locators.xpthDlvrSlctd));
			LOGGER.info("Success : User clicked on Deliver Selected button");

		});

		Then("^User should be able to access \"([^\"]*)\" screen$", (String screenName) -> {
			if (screenName.contains("Deliver")) {
				SeleniumLib.waitForElementVisible(Locators.xpthDlvrScrn);
				Assert.assertTrue("There is some error in accessing " + screenName + " screen",
						SeleniumLib.driver.findElement(By.xpath(Locators.xpthDlvrScrn)).getText().equals(screenName));
			} else {
				SeleniumLib.waitForElementVisible(Locators.xpthERScrn);
				Assert.assertTrue("There is some error in accessing " + screenName + " screen",
						SeleniumLib.driver.getTitle().equals(screenName));
			}
			LOGGER.info("Success  : User is able to access " + screenName + " screen");

		});

		When("^User clicks on Preview in ER button$", () -> {
			SeleniumLib.waitForElementVisible(Locators.xpthPreview);
			if (browser.equals("chrome")) {
				SeleniumLib.click(By.xpath(Locators.xpthPreview));
				ArrayList<String> tabs2 = new ArrayList<String>(SeleniumLib.driver.getWindowHandles());
				SeleniumLib.driver.switchTo().window(tabs2.get(2));
			} else {
				SeleniumLib.driver.get(erurl);
			}
			LOGGER.info("Success : User clicked on ER button in Trade Letter-On Demand Screen");
		});

	}

	@PostConstruct
	public void init() {
		machineip = new HashMap<String, String>();
		if (fields != null && fields.size() != 0) {
			for (String field : fields) {
				String[] splittedField = field.split("=");
				machineip.put(splittedField[0].trim(), splittedField[1].trim());
			}
		}
		node = "http://" + machineip.get(node.toLowerCase().trim()) + ":4444/wd/hub";

	}

}
