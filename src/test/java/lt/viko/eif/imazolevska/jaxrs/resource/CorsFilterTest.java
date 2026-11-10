package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link CorsFilter}.
 */
class CorsFilterTest {

    @Test
    void filterAddsCorsHeaders() {
        ContainerRequestContext request = mock(ContainerRequestContext.class);
        ContainerResponseContext response = mock(ContainerResponseContext.class);
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        when(response.getHeaders()).thenReturn(headers);

        new CorsFilter().filter(request, response);

        assertEquals("*", headers.getFirst("Access-Control-Allow-Origin"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS", headers.getFirst("Access-Control-Allow-Methods"));
        assertEquals("Content-Type, Accept, Authorization", headers.getFirst("Access-Control-Allow-Headers"));
        assertEquals("Location", headers.getFirst("Access-Control-Expose-Headers"));
    }
}