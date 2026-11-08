package lt.viko.eif.imazolevska.jaxrs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Movie}.
 */
class MovieTest {

    @Test
    void constructorSetsFields() {
        Movie movie = new Movie(27205, "Inception", 2010, 8.4f, "/poster.jpg");

        assertNull(movie.getId());
        assertEquals(27205, movie.getTmdbId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(2010, movie.getReleaseYear());
        assertEquals(8.4f, movie.getRating());
        assertEquals("/poster.jpg", movie.getPosterPath());
    }

    @Test
    void settersChangeFields() {
        Movie movie = new Movie();
        movie.setId(1);
        movie.setTmdbId(157336);
        movie.setTitle("Interstellar");
        movie.setReleaseYear(2014);
        movie.setRating(8.5f);
        movie.setPosterPath("/other.jpg");

        assertEquals(1, movie.getId());
        assertEquals(157336, movie.getTmdbId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals(2014, movie.getReleaseYear());
        assertEquals(8.5f, movie.getRating());
        assertEquals("/other.jpg", movie.getPosterPath());
    }

    @Test
    void toStringContainsTitle() {
        assertTrue(new Movie(1, "Tenet", 2020, 7.2f, null).toString().contains("Tenet"));
    }
}
