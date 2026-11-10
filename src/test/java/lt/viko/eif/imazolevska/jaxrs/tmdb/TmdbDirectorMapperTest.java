package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbCrewCredit;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbMovieCredits;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TmdbDirectorMapper}.
 */
class TmdbDirectorMapperTest {

    private static final float DELTA = 0.001f;

    private final TmdbDirectorMapper mapper = new TmdbDirectorMapper();

    @Test
    void toDirectorCopiesPersonData() {
        Director director = mapper.toDirector(nolan(), new TmdbMovieCredits(525, List.of()));

        assertEquals(525, director.getTmdbId());
        assertEquals("Christopher", director.getFirstName());
        assertEquals("Nolan", director.getLastName());
        assertEquals(1970, director.getBirthYear());
        assertTrue(director.isActive());
        assertNull(director.getId());
    }

    @Test
    void toDirectorKeepsOnlyReleasedDirectedMoviesWithoutDuplicates() {
        TmdbMovieCredits credits = new TmdbMovieCredits(525, List.of(
                credit(157336, "Interstellar", "Director", "2014-11-05", 8.4f, List.of(12, 878)),
                credit(27205, "Inception", "Director", "2010-07-15", 8.2f, List.of(28, 878)),
                credit(27205, "Inception", "Director", "2010-07-15", 8.2f, List.of(28, 878)),
                credit(27205, "Inception", "Writer", "2010-07-15", 8.2f, List.of(28, 878)),
                credit(1, "Announced", "Director", "", 0f, List.of(18)),
                credit(2, "Future", "Director", "2999-01-01", 0f, List.of(18))));

        Director director = mapper.toDirector(nolan(), credits);

        assertEquals(2, director.getMovies().size());
        assertEquals("Inception", director.getMovies().get(0).getTitle());
        assertEquals("Interstellar", director.getMovies().get(1).getTitle());
        assertEquals(8.3f, director.getAverageRating(), DELTA);
    }

    @Test
    void mainGenreIsMostFrequentGenre() {
        TmdbMovieCredits credits = new TmdbMovieCredits(525, List.of(
                credit(1, "First", "Director", "2000-01-01", 7f, List.of(18, 878)),
                credit(2, "Second", "Director", "2005-01-01", 7f, List.of(878)),
                credit(3, "Third", "Director", "2010-01-01", 7f, List.of(28))));

        assertEquals('S', mapper.toDirector(nolan(), credits).getMainGenre());
    }

    @Test
    void mainGenreIsNullWithoutMovies() {
        assertNull(mapper.toDirector(nolan(), new TmdbMovieCredits(525, null)).getMainGenre());
    }

    @Test
    void personWithDeathdayIsNotActive() {
        TmdbPerson hitchcock = new TmdbPerson(2636, "Alfred Hitchcock", "1899-08-13", "1980-04-29", "Directing", null);

        assertFalse(mapper.toDirector(hitchcock, new TmdbMovieCredits(2636, null)).isActive());
    }

    @Test
    void nameWithOneWordBecomesLastName() {
        TmdbPerson person = new TmdbPerson(1, "Madonna", null, null, "Directing", null);

        Director director = mapper.toDirector(person, new TmdbMovieCredits(1, null));

        assertEquals("", director.getFirstName());
        assertEquals("Madonna", director.getLastName());
    }

    private static TmdbPerson nolan() {
        return new TmdbPerson(525, "Christopher Nolan", "1970-07-30", null, "Directing", null);
    }

    private static TmdbCrewCredit credit(int id, String title, String job, String date,
                                         float rating, List<Integer> genres) {
        return new TmdbCrewCredit(id, title, job, date, rating, genres, null);
    }
}
