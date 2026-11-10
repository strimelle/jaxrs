package lt.viko.eif.imazolevska.jaxrs.repository;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link DirectorRepository} with the H2 database created by Liquibase.
 * Every test runs in a transaction that is rolled back afterwards.
 */
@SpringBootTest(properties = "tmdb.import-on-startup=false")
@Transactional
class DirectorRepositoryTest {

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private MovieRepository movieRepository;

    private Director savedDirector;

    @BeforeEach
    void saveDirector() {
        directorRepository.deleteAll();
        Director director = new Director(525, "Christopher", "Nolan", 1970, true, 'S');
        director.addMovie(new Movie(27205, "Inception", 2010, 8.4f, "/inception.jpg"));
        director.addMovie(new Movie(157336, "Interstellar", 2014, 8.5f, "/interstellar.jpg"));
        savedDirector = directorRepository.saveAndFlush(director);
    }

    @Test
    void saveStoresDirectorWithMovies() {
        Director found = directorRepository.findById(savedDirector.getId()).orElseThrow();

        assertEquals("Nolan", found.getLastName());
        assertEquals(2, found.getMovies().size());
        assertNotNull(found.getMovies().get(0).getId());
        assertEquals(2, movieRepository.count());
    }

    @Test
    void findByTmdbIdReturnsImportedDirector() {
        Optional<Director> found = directorRepository.findByTmdbId(525);

        assertTrue(found.isPresent());
        assertEquals(savedDirector.getId(), found.get().getId());
        assertTrue(directorRepository.findByTmdbId(-1).isEmpty());
    }

    @Test
    void findByMainGenreReturnsMatchingDirectors() {
        List<Director> sciFi = directorRepository.findByMainGenre('S');

        assertEquals(1, sciFi.size());
        assertTrue(directorRepository.findByMainGenre('H').isEmpty());
    }

    @Test
    void deleteRemovesDirectorMovies() {
        directorRepository.delete(savedDirector);
        directorRepository.flush();

        assertEquals(0, movieRepository.count());
    }
}
