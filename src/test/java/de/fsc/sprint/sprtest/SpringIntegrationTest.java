package de.fsc.sprint.sprtest;

import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import javax.ws.rs.core.*;
import java.io.IOException;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Slf4j
public class SpringIntegrationTest {

    /**
     * Liefert ein Rest-Client-Target für die URI.
     *
     * @param uri Uri
     * @return ResteasyWebTarget Target
     */
    private ResteasyWebTarget getRestClientTarget(final String uri) {
        ResteasyClient restClient = new ResteasyClientBuilder().connectionPoolSize(3).build();
        ResteasyWebTarget target = restClient.target(uri);
        return target;
    }

    Response executeGet(String url) throws IOException {
        log.info("calling " + url);
        return getRestClientTarget(url)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}