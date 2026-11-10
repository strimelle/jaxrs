package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link TmdbCrewCredit}.
 */
class TmdbCrewCreditTest {

    @Test
    void directorJobIsDirecting() {
        assertTrue(credit("Director").directing());
        assertFalse(credit("Writer").directing());
    }

    @Test
    void releaseYearIsTakenFromReleaseDate() {
        assertEquals(2010, credit("Director").releaseYear());
    }

    @Test
    void missingGenreListBecomesEmpty() {
        TmdbCrewCredit credit = new TmdbCrewCredit(1, "Title", "Director", "", 0f, null, null);
        assertTrue(credit.genreIds().isEmpty());
    }

    private static TmdbCrewCredit credit(String job) {
        return new TmdbCrewCredit(27205, "Inception", job, "2010-07-15", 8.4f, List.of(878, 28), "/p.jpg");
    }
}
