package lt.viko.eif.imazolevska.jaxrs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Checks that the Spring application context starts: the database is created
 * by Liquibase and all beans (repositories, services, Jersey resources) are created.
 * The TMDB import is turned off, so the test does not depend on the internet.
 */
@SpringBootTest(properties = "tmdb.import-on-startup=false")
class JaxrsApplicationTests {

    /**
     * Passes if the application context loads without errors.
     */
    @Test
    void contextLoads() {
    }
}
