package lt.viko.eif.imazolevska.jaxrs.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Genre}.
 */
class GenreTest {

    @Test
    void fromTmdbIdFindsKnownGenre() {
        assertEquals(Optional.of(Genre.SCIENCE_FICTION), Genre.fromTmdbId(878));
    }

    @Test
    void fromTmdbIdReturnsEmptyForUnknownId() {
        assertTrue(Genre.fromTmdbId(-1).isEmpty());
    }

    @Test
    void fromCodeIgnoresLetterCase() {
        assertEquals(Optional.of(Genre.DRAMA), Genre.fromCode('d'));
    }

    @Test
    void fromCodeReturnsEmptyForUnknownCode() {
        assertTrue(Genre.fromCode('?').isEmpty());
    }

    @Test
    void codesAreUnique() {
        long uniqueCodes = Arrays.stream(Genre.values()).map(Genre::getCode).distinct().count();
        assertEquals(Genre.values().length, uniqueCodes);
    }
}
