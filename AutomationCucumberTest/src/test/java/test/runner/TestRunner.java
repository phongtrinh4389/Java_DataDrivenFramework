package test.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.nashtech.common.Common;

/**
 * A sample test to demonstrate
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/test/resources/features/TheThirdTest.feature"},
    glue = {"test.steps"},
    plugin = {"com.nashtech.utils.report.HtmlReporter"}
)
public class TestRunner{
	
	@BeforeClass
	public static void before() throws Exception {
		Common.initReports();
	}
	

	
}