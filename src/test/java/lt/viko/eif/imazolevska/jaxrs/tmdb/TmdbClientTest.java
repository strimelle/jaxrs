package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbCrewCredit;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbMovieCredits;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Unit tests for {@link TmdbClient}. TMDB is replaced with a mock HTTP server.
 */
class TmdbClientTest {

    private static final String BASE_URL = "https://api.test/3";

    private MockRestServiceServer server;
    private TmdbClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        client = new TmdbClient(builder.build());
    }

    @Test
    void findPersonReadsTmdbJson() {
        server.expect(requestTo(BASE_URL + "/person/525"))
                .andRespond(withSuccess("""
                        {"id": 525, "name": "Christopher Nolan", "birthday": "1970-07-30",
                         "deathday": null, "known_for_department": "Directing",
                         "profile_path": "/nolan.jpg", "popularity": 10.5}
                        """, MediaType.APPLICATION_JSON));

        TmdbPerson person = client.findPerson(525).orElseThrow();

        assertEquals("Christopher Nolan", person.name());
        assertEquals("Directing", person.knownForDepartment());
        assertEquals(1970, person.birthYear());
        server.verify();
    }

    @Test
    void findPersonReturnsEmptyValueWhenTmdbAnswersNotFound() {
        server.expect(requestTo(BASE_URL + "/person/1"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertTrue(client.findPerson(1).isEmpty());
    }

    @Test
    void getMovieCreditsReadsCrewList() {
        server.expect(requestTo(BASE_URL + "/person/525/movie_credits"))
                .andRespond(withSuccess("""
                        {"id": 525, "cast": [],
                         "crew": [{"id": 27205, "title": "Inception", "job": "Director",
                                   "release_date": "2010-07-15", "vote_average": 8.4,
                                   "genre_ids": [28, 878], "poster_path": "/inception.jpg"}]}
                        """, MediaType.APPLICATION_JSON));

        TmdbMovieCredits credits = client.getMovieCredits(525);

        assertEquals(1, credits.directedMovies().size());
        TmdbCrewCredit movie = credits.crew().get(0);
        assertEquals("Inception", movie.title());
        assertEquals(2010, movie.releaseYear());
        assertEquals(List.of(28, 878), movie.genreIds());
    }

    @Test
    void otherTmdbErrorsAreThrown() {
        server.expect(requestTo(BASE_URL + "/person/525/movie_credits"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));

        assertThrows(RestClientException.class, () -> client.getMovieCredits(525));
    }
}
