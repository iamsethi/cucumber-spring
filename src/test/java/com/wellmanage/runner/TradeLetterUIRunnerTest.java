package com.wellmanage.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:target/cucumber", "json:target/cucumber/cucumber_TLETUI.json",
		"usage:target/cucumber/cucumber-usage.json",
		"junit:target/cucumber/cucumber-results.xml" }, glue = "com.wellmanage.steps", features = "classpath:features/tlet.feature")
public class TradeLetterUIRunnerTest {

}
