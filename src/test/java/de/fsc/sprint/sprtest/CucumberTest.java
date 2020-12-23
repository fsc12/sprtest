package de.fsc.sprint.sprtest;

import io.cucumber.junit.*;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", publish = false)
public class CucumberTest extends SpringIntegrationTest {


}
