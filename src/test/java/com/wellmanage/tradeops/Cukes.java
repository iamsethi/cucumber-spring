package com.wellmanage.tradeops;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty",
		"html:target/cucumber" }, glue = "com.wellmanage.steps", features = "classpath:features/tlet.feature")
public class Cukes {

}