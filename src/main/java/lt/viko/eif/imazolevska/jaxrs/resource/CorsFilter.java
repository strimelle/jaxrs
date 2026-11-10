package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

/**
 * Adds CORS headers to every response, so that browser-based tools such as
 * Swagger Editor, which run on another address, can call the service.
 *
 * <p>Any origin is allowed, which is acceptable for local development and testing.</p>
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    /**
     * Adds the CORS headers to the response.
     *
     * @param requestContext  request that was received
     * @param responseContext response that will be sent
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.putSingle("Access-Control-Allow-Origin", "*");
        headers.putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.putSingle("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        headers.putSingle("Access-Control-Expose-Headers", "Location");
    }
}