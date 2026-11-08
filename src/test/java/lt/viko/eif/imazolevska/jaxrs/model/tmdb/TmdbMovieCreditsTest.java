package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link TmdbMovieCredits}.
 */
class TmdbMovieCreditsTest {

    @Test
    void directedMoviesReturnsOnlyDirectorJobs() {
        TmdbCrewCredit directed = new TmdbCrewCredit(1, "Directed", "Director", "2010-01-01", 8f, List.of(), null);
        TmdbCrewCredit written = new TmdbCrewCredit(2, "Written", "Writer", "2012-01-01", 7f, List.of(), null);

        TmdbMovieCredits credits = new TmdbMovieCredits(525, List.of(directed, written));

        assertEquals(List.of(directed), credits.directedMovies());
    }

    @Test
    void missingCrewListBecomesEmpty() {
        TmdbMovieCredits credits = new TmdbMovieCredits(525, null);

        assertTrue(credits.crew().isEmpty());
        assertTrue(credits.directedMovies().isEmpty());
    }
}
