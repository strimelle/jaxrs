package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TmdbPerson}.
 */
class TmdbPersonTest {

    @Test
    void birthYearIsTakenFromBirthday() {
        TmdbPerson person = new TmdbPerson(525, "Christopher Nolan", "1970-07-30", null, "Directing", null);
        assertEquals(1970, person.birthYear());
    }

    @Test
    void personWithoutDeathdayIsAlive() {
        assertTrue(new TmdbPerson(1, "Name", null, null, "Directing", null).alive());
        assertTrue(new TmdbPerson(1, "Name", null, "", "Directing", null).alive());
    }

    @Test
    void personWithDeathdayIsNotAlive() {
        assertFalse(new TmdbPerson(1, "Name", "1899-08-13", "1980-04-29", "Directing", null).alive());
    }
}
