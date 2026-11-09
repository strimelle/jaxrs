package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbMovieCredits;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class TmdbClient {

    private final RestClient restClient;

    public TmdbClient(RestClient tmdbRestClient) {
        this.restClient = tmdbRestClient;
    }

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

    public TmdbMovieCredits getMovieCredits(int personId) {

            TmdbMovieCredits credits = restClient.get()
                    .uri("/person/{personId}/movie_credits", personId)
                    .retrieve()
                    .body(TmdbMovieCredits.class);
            return credits != null ? credits : new TmdbMovieCredits(personId,
                    List.of());
    }
}