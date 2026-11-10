package lt.viko.eif.imazolevska.jaxrs.tmdb;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "tmdb")
public record TmdbProperties (String baseUrl, String apiToken, boolean importOnStartup, List<Integer> startupDirectorIds) {

    public boolean hasApiToken(){
        return apiToken != null && !apiToken.isBlank();
    }

    /**
     * Creates the settings and replaces a missing director list with an empty one.
     */
    public TmdbProperties {
        startupDirectorIds = startupDirectorIds == null ? List.of() : List.copyOf(startupDirectorIds);
    }
}
