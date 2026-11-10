package lt.viko.eif.imazolevska.jaxrs.config;

import lt.viko.eif.imazolevska.jaxrs.resource.DirectorResource;
import lt.viko.eif.imazolevska.jaxrs.resource.IllegalArgumentExceptionMapper;
import lt.viko.eif.imazolevska.jaxrs.resource.TmdbImportResource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link JerseyConfig}.
 */
class JerseyConfigTest {

    @Test
    void constructorRegistersResourcesAndExceptionMapper() {
        JerseyConfig config = new JerseyConfig();

        assertTrue(config.getClasses().contains(DirectorResource.class));
        assertTrue(config.getClasses().contains(TmdbImportResource.class));
        assertTrue(config.getClasses().contains(IllegalArgumentExceptionMapper.class));
    }
}
