package de.fsc.sprint.sprtest;

import io.cucumber.java.en.*;
import org.apache.http.HttpStatus;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest
public class WeatherCucumberStepDefinitions extends SpringIntegrationTest {
    private static Response latestResponse;


    @When("^the client calls /calc$")
    public void the_client_calls_GET_calc() throws Throwable {
        latestResponse = executeGet("http://localhost:8080/calc");
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        assertThat(latestResponse.getStatus(), is(HttpStatus.SC_OK));
    }
}
