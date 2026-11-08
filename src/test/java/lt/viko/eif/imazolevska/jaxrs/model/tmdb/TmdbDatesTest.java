package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link TmdbDates}.
 */
class TmdbDatesTest {

    @Test
    void yearOfReturnsYearOfValidDate() {
        assertEquals(1970, TmdbDates.yearOf("1970-07-30"));
    }

    @Test
    void yearOfReturnsZeroForMissingOrInvalidDate() {
        assertEquals(0, TmdbDates.yearOf(null));
        assertEquals(0, TmdbDates.yearOf(""));
        assertEquals(0, TmdbDates.yearOf("abcd-01-01"));
    }
}
