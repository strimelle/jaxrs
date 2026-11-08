package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Movie credits of a person returned by the TMDB endpoint
 * {@code GET /3/person/{person_id}/movie_credits}. The {@code cast} list is ignored.
 *
 * @param id   person identifier in TMDB
 * @param crew crew jobs of the person, never {@code null}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbMovieCredits(int id, List<TmdbCrewCredit> crew) {

    /**
     * Creates movie credits and replaces a missing crew list with an empty one.
     */
    public TmdbMovieCredits {
        crew = crew == null ? List.of() : List.copyOf(crew);
    }

    /**
     * Returns only the movies the person directed.
     *
     * @return directed movies, empty if there are none
     */
    public List<TmdbCrewCredit> directedMovies() {
        return crew.stream()
                .filter(TmdbCrewCredit::directing)
                .toList();
    }
}
