package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.core.Response;
import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.tmdb.TmdbImportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TmdbImportResource}. The import service is a mock.
 */
@ExtendWith(MockitoExtension.class)
class TmdbImportResourceTest {

    @Mock
    private TmdbImportService importService;

    @Test
    void importDirectorReturnsOkWithImportedDirector() {
        Director director = new Director(525, "Christopher", "Nolan", 1970, true, 'S');
        when(importService.importDirector(525)).thenReturn(Optional.of(director));

        Response response = new TmdbImportResource(importService).importDirector(525);

        assertEquals(200, response.getStatus());
        assertEquals(director, response.getEntity());
    }

    @Test
    void importDirectorReturnsNotFoundWhenTmdbHasNoSuchPerson() {
        when(importService.importDirector(1)).thenReturn(Optional.empty());

        assertEquals(404, new TmdbImportResource(importService).importDirector(1).getStatus());
    }
}
