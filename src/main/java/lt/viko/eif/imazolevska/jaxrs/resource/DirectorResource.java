package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import lt.viko.eif.imazolevska.jaxrs.service.DirectorService;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

/**
 * REST resource for directors and their movies, available at {@code /api/directors}.
 *
 * <p>Responses are returned as JSON or XML, depending on the {@code Accept} header
 * of the request. Invalid data is answered with 400 Bad Request by
 * {@link IllegalArgumentExceptionMapper}.</p>
 */
@Component
@Path("/directors")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class DirectorResource {

    private final DirectorService directorService;

    /**
     * Creates the resource with the given service.
     *
     * @param directorService service that performs operations with directors
     */
    public DirectorResource(DirectorService directorService) {
        this.directorService = directorService;
    }

    /**
     * Returns all directors, or only the directors of the given genre.
     *
     * @param genre optional one-letter genre code, for example {@code S}
     * @return list of directors (200 OK)
     */
    @GET
    public List<Director> getDirectors(@QueryParam("genre") String genre) {
        if (genre == null) {
            return directorService.findAll();
        }
        return directorService.findByGenre(genre);
    }

    /**
     * Returns the director with the given identifier.
     *
     * @param id director identifier
     * @return the director (200 OK), or 404 Not Found
     */
    @GET
    @Path("/{id}")
    public Response getDirector(@PathParam("id") int id) {
        return directorService.findById(id)
                .map(director -> Response.ok(director).build())
                .orElseGet(DirectorResource::notFound);
    }

    /**
     * Creates a new director.
     *
     * @param director data of the new director
     * @param uriInfo  information about the request URI, provided by Jersey
     * @return the created director with its address in the {@code Location} header (201 Created)
     */
    @POST
    public Response createDirector(Director director, @Context UriInfo uriInfo) {
        Director created = directorService.create(director);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(created.getId()))
                .build();
        return Response.created(location).entity(created).build();
    }

    /**
     * Changes the data of an existing director.
     *
     * @param id      director identifier
     * @param changes new data of the director
     * @return the changed director (200 OK), or 404 Not Found
     */
    @PUT
    @Path("/{id}")
    public Response updateDirector(@PathParam("id") int id, Director changes) {
        return directorService.update(id, changes)
                .map(director -> Response.ok(director).build())
                .orElseGet(DirectorResource::notFound);
    }

    /**
     * Deletes the director together with its movies.
     *
     * @param id director identifier
     * @return 204 No Content, or 404 Not Found
     */
    @DELETE
    @Path("/{id}")
    public Response deleteDirector(@PathParam("id") int id) {
        if (directorService.delete(id)) {
            return Response.noContent().build();
        }
        return notFound();
    }

    /**
     * Returns the movies of the director.
     *
     * @param id director identifier
     * @return list of movies (200 OK), or 404 Not Found if the director does not exist
     */
    @GET
    @Path("/{id}/movies")
    public Response getMovies(@PathParam("id") int id) {
        return directorService.findMovies(id)
                .map(movies -> Response.ok(new GenericEntity<List<Movie>>(movies) { }).build())
                .orElseGet(DirectorResource::notFound);
    }

    private static Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}