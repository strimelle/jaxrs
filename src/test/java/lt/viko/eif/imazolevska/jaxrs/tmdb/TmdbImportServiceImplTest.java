package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbCrewCredit;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbMovieCredits;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import lt.viko.eif.imazolevska.jaxrs.repository.DirectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TmdbImportServiceImpl}. The TMDB client and the repository are mocks.
 */
@ExtendWith(MockitoExtension.class)
class TmdbImportServiceImplTest {

    @Mock
    private TmdbClient tmdbClient;

    @Mock
    private DirectorRepository directorRepository;

    private TmdbImportServiceImpl importService;

    @BeforeEach
    void setUp() {
        importService = new TmdbImportServiceImpl(tmdbClient, new TmdbDirectorMapper(), directorRepository);
    }

    @Test
    void importDirectorReturnsEmptyValueWhenPersonIsNotFound() {
        when(tmdbClient.findPerson(1)).thenReturn(Optional.empty());

        assertTrue(importService.importDirector(1).isEmpty());
        verify(directorRepository, never()).save(any());
    }

    @Test
    void importDirectorSavesNewDirectorWithMovies() {
        stubTmdb();
        when(directorRepository.findByTmdbId(525)).thenReturn(Optional.empty());
        when(directorRepository.save(any(Director.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Director imported = importService.importDirector(525).orElseThrow();

        assertEquals(525, imported.getTmdbId());
        assertEquals("Nolan", imported.getLastName());
        assertEquals(1, imported.getMovies().size());
    }

    @Test
    void importDirectorUpdatesDirectorImportedBefore() {
        stubTmdb();
        Director existing = new Director(525, "Old", "Name", 1900, false, null);
        existing.setId(3);
        existing.addMovie(new Movie(1, "Old movie", 2000, 5f, null));
        when(directorRepository.findByTmdbId(525)).thenReturn(Optional.of(existing));
        when(directorRepository.save(any(Director.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Director updated = importService.importDirector(525).orElseThrow();

        assertEquals(3, updated.getId());
        assertEquals("Christopher", updated.getFirstName());
        assertEquals(1970, updated.getBirthYear());
        assertTrue(updated.isActive());
        assertEquals(1, updated.getMovies().size());
        assertEquals("Inception", updated.getMovies().get(0).getTitle());
    }

    private void stubTmdb() {
        when(tmdbClient.findPerson(525)).thenReturn(Optional.of(
                new TmdbPerson(525, "Christopher Nolan", "1970-07-30", null, "Directing", null)));
        when(tmdbClient.getMovieCredits(525)).thenReturn(new TmdbMovieCredits(525, List.of(
                new TmdbCrewCredit(27205, "Inception", "Director", "2010-07-15", 8.4f, List.of(878), "/i.jpg"))));
    }
}
