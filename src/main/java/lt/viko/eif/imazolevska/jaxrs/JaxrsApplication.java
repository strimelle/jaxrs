package lt.viko.eif.imazolevska.jaxrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application. Starts Spring Boot, which runs the XSL
 * transformations and publishes the SOAP web service.
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
