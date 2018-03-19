package test.steps;


import com.nashtech.core.web.WebDriverMethod;

import cucumber.api.java.en.*;

public class TestSteps{
	
	private WebDriverMethod driver;

	@Given("^This is given step$")
	public void this_is_given_step() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("this is given");
	}

	@When("^This is when step$")
	public void this_is_when_step() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("this is when");
	}

	@Then("^This is then step$")
	public void this_is_then_step() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("this is then");
	}
	
	@Given("^I am scenario outline$")
	public void i_am_scenario_outline() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		System.out.println("i_am_scenario_outline");
	}
	
	@Given("^I am in the Entry page$")
	public void i_am_in_the_Entry_page() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver = new WebDriverMethod();
		driver.openUrl("http://store.demoqa.com/");
	}

	@Then("^The entry page title is (.*)$")
	public void the_entry_page_title_is_index(String title) throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		
	}
}
