package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lt.viko.eif.imazolevska.jaxrs.tmdb.TmdbImportService;
import org.springframework.stereotype.Component;

/**
 * REST resource that imports directors from TMDB, available at {@code /api/directors/import}.
 */
@Component
@Path("/directors/import")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TmdbImportResource {

    private final TmdbImportService importService;

    public TmdbImportResource(TmdbImportService importService) {
        this.importService = importService;
    }

    /**
     * Imports the director with the given TMDB identifier, or updates it if it was imported before.
     *
     * @param tmdbId TMDB person identifier, for example {@code 525} for Christopher Nolan
     * @return the imported director with movies (200 OK), or 404 Not Found if TMDB has no such person
     */
    @POST
    @Path("/{tmdbId}")
    public Response importDirector(@PathParam("tmdbId") int tmdbId) {
        return importService.importDirector(tmdbId)
                .map(director -> Response.ok(director).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}