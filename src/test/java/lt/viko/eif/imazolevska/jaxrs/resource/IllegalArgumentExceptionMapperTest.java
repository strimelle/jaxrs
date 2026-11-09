package lt.viko.eif.imazolevska.jaxrs.resource;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link IllegalArgumentExceptionMapper}.
 */
class IllegalArgumentExceptionMapperTest {

    @Test
    void toResponseReturnsBadRequestWithMessage() {
        Response response = new IllegalArgumentExceptionMapper()
                .toResponse(new IllegalArgumentException("Unknown genre code: Q"));

        assertEquals(400, response.getStatus());
        assertEquals("Unknown genre code: Q", response.getEntity());
        assertEquals(MediaType.TEXT_PLAIN_TYPE, response.getMediaType());
    }
}