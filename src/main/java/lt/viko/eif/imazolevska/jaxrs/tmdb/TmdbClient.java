package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbMovieCredits;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

/**
 * Reads person and movie data from the TMDB API.
 *
 * <p>Errors other than "not found" (for example an invalid token or a network problem)
 * are thrown as {@link org.springframework.web.client.RestClientException}.</p>
 */
@Component
public class TmdbClient {

    private final RestClient restClient;

    /**
     * Creates the client.
     *
     * @param tmdbRestClient HTTP client configured with the TMDB address and token
     */
    public TmdbClient(RestClient tmdbRestClient) {
        this.restClient = tmdbRestClient;
    }

    /**
     * Reads a person from {@code GET /person/{personId}}.
     *
     * @param personId TMDB person identifier
     * @return the person, or an empty value if TMDB has no person with this identifier
     */
    public Optional<TmdbPerson> findPerson(int personId) {
        try {
            TmdbPerson person = restClient.get()
                    .uri("/person/{personId}", personId)
                    .retrieve()
                    .body(TmdbPerson.class);
            return Optional.ofNullable(person);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }

    /**
     * Reads the movie credits of a person from {@code GET /person/{personId}/movie_credits}.
     *
     * @param personId TMDB person identifier
     * @return movie credits of the person, never {@code null}
     */
    public TmdbMovieCredits getMovieCredits(int personId) {

        TmdbMovieCredits credits = restClient.get()
                .uri("/person/{personId}/movie_credits", personId)
                .retrieve()
                .body(TmdbMovieCredits.class);
        return credits != null ? credits : new TmdbMovieCredits(personId,
                List.of());
    }
}