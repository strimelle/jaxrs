package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import lt.viko.eif.imazolevska.jaxrs.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DirectorResource}. The service is replaced with a mock.
 */
@ExtendWith(MockitoExtension.class)
class DirectorResourceTest {

    @Mock
    private DirectorService directorService;

    @Mock
    private UriInfo uriInfo;

    private DirectorResource resource;

    @BeforeEach
    void setUp() {
        resource = new DirectorResource(directorService);
    }

    @Test
    void getDirectorsWithoutGenreReturnsAllDirectors() {
        List<Director> directors = List.of(nolan());
        when(directorService.findAll()).thenReturn(directors);

        assertEquals(directors, resource.getDirectors(null));
        verify(directorService, never()).findByGenre(any());
    }

    @Test
    void getDirectorsWithGenreReturnsFilteredDirectors() {
        List<Director> directors = List.of(nolan());
        when(directorService.findByGenre("S")).thenReturn(directors);

        assertEquals(directors, resource.getDirectors("S"));
        verify(directorService, never()).findAll();
    }

    @Test
    void getDirectorReturnsOkWhenDirectorExists() {
        Director director = nolan();
        when(directorService.findById(1)).thenReturn(Optional.of(director));

        Response response = resource.getDirector(1);

        assertEquals(200, response.getStatus());
        assertEquals(director, response.getEntity());
    }

    @Test
    void getDirectorReturnsNotFoundWhenDirectorDoesNotExist() {
        when(directorService.findById(9)).thenReturn(Optional.empty());

        assertEquals(404, resource.getDirector(9).getStatus());
    }

    @Test
    void createDirectorReturnsCreatedWithLocation() {
        when(directorService.create(any(Director.class))).thenAnswer(invocation -> {
            Director director = invocation.getArgument(0);
            director.setId(7);
            return director;
        });
        when(uriInfo.getAbsolutePathBuilder())
                .thenReturn(UriBuilder.fromUri("http://localhost:8080/api/directors"));

        Response response = resource.createDirector(nolan(), uriInfo);

        assertEquals(201, response.getStatus());
        assertEquals(URI.create("http://localhost:8080/api/directors/7"), response.getLocation());
        assertEquals(7, ((Director) response.getEntity()).getId());
    }

    @Test
    void createDirectorPassesInvalidDataErrorToExceptionMapper() {
        when(directorService.create(any(Director.class))).thenThrow(new IllegalArgumentException("Invalid"));

        assertThrows(IllegalArgumentException.class, () -> resource.createDirector(new Director(), uriInfo));
    }

    @Test
    void updateDirectorReturnsOkWhenDirectorExists() {
        Director changed = nolan();
        when(directorService.update(anyInt(), any(Director.class))).thenReturn(Optional.of(changed));

        Response response = resource.updateDirector(1, changed);

        assertEquals(200, response.getStatus());
        assertEquals(changed, response.getEntity());
    }

    @Test
    void updateDirectorReturnsNotFoundWhenDirectorDoesNotExist() {
        when(directorService.update(anyInt(), any(Director.class))).thenReturn(Optional.empty());

        assertEquals(404, resource.updateDirector(9, nolan()).getStatus());
    }

    @Test
    void deleteDirectorReturnsNoContentWhenDeleted() {
        when(directorService.delete(1)).thenReturn(true);

        Response response = resource.deleteDirector(1);

        assertEquals(204, response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    void deleteDirectorReturnsNotFoundWhenDirectorDoesNotExist() {
        when(directorService.delete(9)).thenReturn(false);

        assertEquals(404, resource.deleteDirector(9).getStatus());
    }

    @Test
    void getMoviesReturnsMoviesOfDirector() {
        List<Movie> movies = List.of(new Movie(27205, "Inception", 2010, 8.4f, null));
        when(directorService.findMovies(1)).thenReturn(Optional.of(movies));

        Response response = resource.getMovies(1);

        assertEquals(200, response.getStatus());
        assertEquals(movies, unwrap(response.getEntity()));
    }

    @Test
    void getMoviesReturnsNotFoundWhenDirectorDoesNotExist() {
        when(directorService.findMovies(9)).thenReturn(Optional.empty());

        assertEquals(404, resource.getMovies(9).getStatus());
    }

    private static Object unwrap(Object entity) {
        if (entity instanceof GenericEntity<?> genericEntity) {
            return genericEntity.getEntity();
        }
        return entity;
    }

    private static Director nolan() {
        Director director = new Director(525, "Christopher", "Nolan", 1970, true, 'S');
        director.setId(1);
        return director;
    }
}