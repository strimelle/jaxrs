package lt.viko.eif.imazolevska.jaxrs.service;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import lt.viko.eif.imazolevska.jaxrs.repository.DirectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DirectorServiceImpl}. The repository is replaced with a mock.
 */
@ExtendWith(MockitoExtension.class)
class DirectorServiceImplTest {

    private static final float DELTA = 0.001f;

    @Mock
    private DirectorRepository directorRepository;

    private DirectorServiceImpl directorService;

    @BeforeEach
    void setUp() {
        directorService = new DirectorServiceImpl(directorRepository);
    }

    @Test
    void findAllReturnsDirectorsSortedById() {
        List<Director> directors = List.of(nolan());
        when(directorRepository.findAll(Sort.by("id"))).thenReturn(directors);

        assertEquals(directors, directorService.findAll());
    }

    @Test
    void findByIdReturnsDirectorOrEmptyValue() {
        Director director = nolan();
        when(directorRepository.findById(1)).thenReturn(Optional.of(director));
        when(directorRepository.findById(2)).thenReturn(Optional.empty());

        assertEquals(Optional.of(director), directorService.findById(1));
        assertTrue(directorService.findById(2).isEmpty());
    }

    @Test
    void findByGenreSearchesWithUpperCaseCode() {
        when(directorRepository.findByMainGenre('S')).thenReturn(List.of(nolan()));

        assertEquals(1, directorService.findByGenre("s").size());
    }

    @Test
    void findByGenreRejectsInvalidCodes() {
        assertThrows(IllegalArgumentException.class, () -> directorService.findByGenre(null));
        assertThrows(IllegalArgumentException.class, () -> directorService.findByGenre(" "));
        assertThrows(IllegalArgumentException.class, () -> directorService.findByGenre("Sci"));
        assertThrows(IllegalArgumentException.class, () -> directorService.findByGenre("?"));
        verifyNoInteractions(directorRepository);
    }

    @Test
    void createIgnoresClientIdentifiersAndCalculatesRating() {
        Director newDirector = nolan();
        newDirector.setAverageRating(1f);
        newDirector.setMainGenre('s');
        newDirector.getMovies().get(0).setId(99);
        when(directorRepository.save(any(Director.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Director saved = directorService.create(newDirector);

        assertNull(saved.getId());
        assertNull(saved.getTmdbId());
        assertNull(saved.getMovies().get(0).getId());
        assertEquals('S', saved.getMainGenre());
        assertEquals(8.4f, saved.getAverageRating(), DELTA);
        verify(directorRepository).save(newDirector);
    }

    @Test
    void createRejectsInvalidDirector() {
        assertThrows(IllegalArgumentException.class, () -> directorService.create(null));
        assertThrows(IllegalArgumentException.class,
                () -> directorService.create(new Director(null, " ", "Nolan", 1970, true, 'S')));
        assertThrows(IllegalArgumentException.class,
                () -> directorService.create(new Director(null, "Old", "Man", 1500, true, null)));
        assertThrows(IllegalArgumentException.class,
                () -> directorService.create(new Director(null, "Bad", "Genre", 1970, true, 'Q')));
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateChangesDataButKeepsIdentifiersAndMovies() {
        when(directorRepository.findById(1)).thenReturn(Optional.of(nolan()));
        when(directorRepository.save(any(Director.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Director changes = new Director(999, "Chris", "Nolan", 1971, false, 'd');
        changes.setId(42);

        Director updated = directorService.update(1, changes).orElseThrow();

        assertEquals(1, updated.getId());
        assertEquals(525, updated.getTmdbId());
        assertEquals("Chris", updated.getFirstName());
        assertEquals(1971, updated.getBirthYear());
        assertFalse(updated.isActive());
        assertEquals('D', updated.getMainGenre());
        assertEquals(1, updated.getMovies().size());
    }

    @Test
    void updateReturnsEmptyValueWhenDirectorDoesNotExist() {
        when(directorRepository.findById(5)).thenReturn(Optional.empty());

        assertTrue(directorService.update(5, new Director(null, "A", "B", 1970, true, null)).isEmpty());
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateRejectsInvalidData() {
        assertThrows(IllegalArgumentException.class,
                () -> directorService.update(1, new Director(null, "", "B", 0, true, null)));
        verifyNoInteractions(directorRepository);
    }

    @Test
    void deleteRemovesExistingDirector() {
        when(directorRepository.existsById(1)).thenReturn(true);

        assertTrue(directorService.delete(1));
        verify(directorRepository).deleteById(1);
    }

    @Test
    void deleteReturnsFalseWhenDirectorDoesNotExist() {
        when(directorRepository.existsById(2)).thenReturn(false);

        assertFalse(directorService.delete(2));
        verify(directorRepository, never()).deleteById(any());
    }

    @Test
    void findMoviesReturnsMoviesOfDirector() {
        when(directorRepository.findById(1)).thenReturn(Optional.of(nolan()));

        List<Movie> movies = directorService.findMovies(1).orElseThrow();

        assertEquals(1, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
    }

    @Test
    void findMoviesReturnsEmptyValueWhenDirectorDoesNotExist() {
        when(directorRepository.findById(3)).thenReturn(Optional.empty());

        assertTrue(directorService.findMovies(3).isEmpty());
    }

    private static Director nolan() {
        Director director = new Director(525, "Christopher", "Nolan", 1970, true, 'S');
        director.setId(1);
        director.addMovie(new Movie(27205, "Inception", 2010, 8.4f, null));
        return director;
    }
}