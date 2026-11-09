package lt.viko.eif.imazolevska.jaxrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application.
 *
 * <p>Starts Spring Boot, which creates the H2 database with Liquibase and
 * publishes the JAX-RS (Jersey) web service at {@code http://localhost:8082/api}.</p>
 */

@SpringBootApplication
public class JaxrsApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command line arguments
     */

    public static void main(String[] args) {
        SpringApplication.run(JaxrsApplication.class, args);
    }

}
