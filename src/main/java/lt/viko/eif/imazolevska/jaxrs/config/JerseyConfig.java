package lt.viko.eif.imazolevska.jaxrs.config;


import lt.viko.eif.imazolevska.jaxrs.resource.DirectorResource;
import lt.viko.eif.imazolevska.jaxrs.resource.IllegalArgumentExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * Jersey configuration that registers the JAX-RS resources and providers.
 *
 * <p>The resources are available under the path set by
 * {@code spring.jersey.application-path} ({@code /api}). Jersey also publishes
 * the WADL description of the service at {@code /api/application.wadl}.</p>
 */

@Component
public class JerseyConfig extends ResourceConfig {

    /**
     * Registers the resources and the exception mapper.
     */
    public JerseyConfig(){
        register(DirectorResource.class);
        register(IllegalArgumentExceptionMapper.class);
    }







}
