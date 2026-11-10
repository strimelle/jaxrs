package lt.viko.eif.imazolevska.jaxrs.tmdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

/**
 * Imports the directors listed in {@code tmdb.startup-director-ids} when the application starts.
 *
 * <p>The import is skipped if it is turned off or the TMDB token is not set. An error
 * with one director is logged and does not stop the import of the others or the application.</p>
 */
@Component
public class TmdbStartupImporter implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TmdbStartupImporter.class);

    private final TmdbProperties properties;
    private final TmdbImportService importService;

    /**
     * Creates the importer.
     *
     * @param properties    TMDB settings
     * @param importService service that imports one director
     */
    public TmdbStartupImporter(TmdbProperties properties, TmdbImportService importService) {
        this.properties = properties;
        this.importService = importService;
    }

    /**
     * Imports the directors from TMDB.
     *
     * @param args command line arguments of the application, not used
     */
    @Override
    public void run(ApplicationArguments args) {
        if (!properties.importOnStartup()) {
            LOG.info("TMDB import on startup is turned off");
            return;
        }
        if (!properties.hasApiToken()) {
            LOG.warn("TMDB_API_TOKEN environment variable is not set, directors are not imported from TMDB");
            return;
        }
        properties.startupDirectorIds().forEach(this::importDirector);
    }

    /**
     * Imports one director and logs the result.
     *
     * @param tmdbId TMDB person identifier
     */
    private void importDirector(int tmdbId) {
        try {
            importService.importDirector(tmdbId).ifPresentOrElse(
                    director -> LOG.info("Imported director {} {} with {} movies",
                            director.getFirstName(), director.getLastName(), director.getMovies().size()),
                    () -> LOG.warn("TMDB person {} was not found", tmdbId));
        } catch (RestClientException e) {
            LOG.error("Could not import TMDB person {}: {}", tmdbId, e.getMessage());
        }
    }
}