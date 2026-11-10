package lt.viko.eif.imazolevska.jaxrs.tmdb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TmdbProperties}.
 */
class TmdbPropertiesTest {

    @Test
    void hasApiTokenIsTrueOnlyForNonEmptyToken() {
        assertTrue(new TmdbProperties("url", "token", true, null).hasApiToken());
        assertFalse(new TmdbProperties("url", "", true, null).hasApiToken());
        assertFalse(new TmdbProperties("url", "  ", true, null).hasApiToken());
        assertFalse(new TmdbProperties("url", null, true, null).hasApiToken());
    }

    @Test
    void missingDirectorListBecomesEmpty() {
        assertTrue(new TmdbProperties("url", "token", true, null).startupDirectorIds().isEmpty());
    }
}
