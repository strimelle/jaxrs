package lt.viko.eif.imazolevska.jaxrs.config;

import lt.viko.eif.imazolevska.jaxrs.tmdb.TmdbProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link TmdbConfig}.
 */
class TmdbConfigTest {

    @Test
    void tmdbRestClientIsCreated() {
        TmdbProperties properties = new TmdbProperties("https://api.themoviedb.org/3", "token", false, null);

        assertNotNull(new TmdbConfig().tmdbRestClient(properties));
    }
}
