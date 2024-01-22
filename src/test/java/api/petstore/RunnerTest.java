package api.petstore;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features= "src/test/resources",
        glue= {"api/petstore/stepdefs"},
        monochrome = true,
        plugin = {"pretty",
                "html:target/cucumber-reports/restAssured.html"},
        tags = "@api")

public class RunnerTest {

}