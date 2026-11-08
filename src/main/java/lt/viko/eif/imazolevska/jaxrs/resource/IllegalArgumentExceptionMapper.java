package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Converts an {@link IllegalArgumentException} thrown by the services
 * to a 400 Bad Request response with the error message as plain text.
 */
@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    /**
     * Creates the error response.
     *
     * @param exception exception thrown because of invalid request data
     * @return 400 Bad Request with the exception message
     */
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}