package com.wellmanage.steps;

import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.java8.En;

@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ContextConfiguration(locations = { "classpath*:/spring-context/applicationContext-smoke-suite.xml" })

public class SlsUIScenario implements En {
	public SlsUIScenario() {
		Given("^I want to write a step with precondition$", () -> {

		});

		Given("^some other precondition$", () -> {

		});

		When("^I complete action$", () -> {

		});

		When("^some other action$", () -> {

		});

		When("^yet another action$", () -> {

		});

		Then("^I validate the outcomes$", () -> {

		});

		Then("^check more outcomes$", () -> {

		});

		Given("^I want to write a step with name(\\d+)$", (Integer arg1) -> {

		});

		When("^I check for the (\\d+) in step$", (Integer arg1) -> {

		});

		Then("^I verify the success in step$", () -> {

		});

		Then("^I verify the Fail in step$", () -> {

		});

	}

}