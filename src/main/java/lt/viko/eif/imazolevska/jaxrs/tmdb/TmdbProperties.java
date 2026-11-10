package lt.viko.eif.imazolevska.jaxrs.tmdb;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Settings of the TMDB API connection, read from the {@code tmdb.*} properties.
 *
 * @param baseUrl            base address of the TMDB API, for example {@code https://api.themoviedb.org/3}
 * @param apiToken           TMDB API Read Access Token; empty if it is not set
 * @param importOnStartup    {@code true} if the directors are imported when the application starts
 * @param startupDirectorIds TMDB person identifiers of the directors imported on startup
 */
@ConfigurationProperties(prefix = "tmdb")
public record TmdbProperties(String baseUrl, String apiToken, boolean importOnStartup,
                             List<Integer> startupDirectorIds) {

    /**
     * Tells whether the API token is set.
     *
     * @return {@code true} if the token is not empty
     */
    public boolean hasApiToken() {
        return apiToken != null && !apiToken.isBlank();
    }

    /**
     * Creates the settings and replaces a missing director list with an empty one.
     */
    public TmdbProperties {
        startupDirectorIds = startupDirectorIds == null ? List.of() : List.copyOf(startupDirectorIds);
    }
}
