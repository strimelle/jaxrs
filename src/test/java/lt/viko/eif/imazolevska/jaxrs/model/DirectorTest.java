package lt.viko.eif.imazolevska.jaxrs.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Director}.
 */
class DirectorTest {

    private static final float DELTA = 0.001f;

    @Test
    void constructorSetsFieldsAndStartsWithoutMovies() {
        Director director = new Director(525, "Christopher", "Nolan", 1970, true, 'S');

        assertEquals(525, director.getTmdbId());
        assertEquals("Christopher", director.getFirstName());
        assertEquals("Nolan", director.getLastName());
        assertEquals(1970, director.getBirthYear());
        assertTrue(director.isActive());
        assertEquals('S', director.getMainGenre());
        assertNotNull(director.getMovies());
        assertTrue(director.getMovies().isEmpty());
        assertEquals(0f, director.getAverageRating());
    }

    @Test
    void addMovieRecalculatesAverageRating() {
        Director director = new Director();
        director.addMovie(new Movie(1, "First", 2000, 8.0f, null));
        director.addMovie(new Movie(2, "Second", 2005, 6.0f, null));

        assertEquals(2, director.getMovies().size());
        assertEquals(7.0f, director.getAverageRating(), DELTA);
    }

    @Test
    void setMoviesReplacesMoviesAndRecalculatesRating() {
        Director director = new Director();
        director.addMovie(new Movie(1, "Old", 1999, 5.0f, null));

        director.setMovies(List.of(new Movie(2, "New", 2020, 9.0f, null)));

        assertEquals(1, director.getMovies().size());
        assertEquals("New", director.getMovies().get(0).getTitle());
        assertEquals(9.0f, director.getAverageRating(), DELTA);
    }

    @Test
    void setMoviesWithNullRemovesAllMovies() {
        Director director = new Director();
        director.addMovie(new Movie(1, "Movie", 2001, 7.0f, null));

        director.setMovies(null);

        assertTrue(director.getMovies().isEmpty());
        assertEquals(0f, director.getAverageRating());
    }

    @Test
    void settersChangeFields() {
        Director director = new Director();
        director.setId(3);
        director.setTmdbId(138);
        director.setFirstName("Quentin");
        director.setLastName("Tarantino");
        director.setBirthYear(1963);
        director.setAverageRating(7.5f);
        director.setActive(false);
        director.setMainGenre('R');

        assertEquals(3, director.getId());
        assertEquals(138, director.getTmdbId());
        assertEquals("Quentin", director.getFirstName());
        assertEquals("Tarantino", director.getLastName());
        assertEquals(1963, director.getBirthYear());
        assertEquals(7.5f, director.getAverageRating());
        assertFalse(director.isActive());
        assertEquals('R', director.getMainGenre());
    }

    @Test
    void toStringContainsName() {
        Director director = new Director(1, "Greta", "Gerwig", 1983, true, 'C');
        assertTrue(director.toString().contains("Greta Gerwig"));
    }
}
