package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * One crew job of a person in a movie, from the {@code crew} list of the
 * TMDB endpoint {@code GET /3/person/{person_id}/movie_credits}.
 *
 * @param id          movie identifier in TMDB
 * @param title       movie title
 * @param job         job of the person in the movie, for example "Director" or "Writer"
 * @param releaseDate release date in the {@code yyyy-MM-dd} format, may be empty
 * @param voteAverage average rating from 0 to 10
 * @param genreIds    TMDB identifiers of the movie genres, never {@code null}
 * @param posterPath  TMDB path of the poster image, may be {@code null}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbCrewCredit(
        int id,
        String title,
        String job,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("vote_average") float voteAverage,
        @JsonProperty("genre_ids") List<Integer> genreIds,
        @JsonProperty("poster_path") String posterPath) {

    private static final String DIRECTOR_JOB = "Director";

    /**
     * Creates a crew credit and replaces a missing genre list with an empty one.
     */
    public TmdbCrewCredit {
        genreIds = genreIds == null ? List.of() : List.copyOf(genreIds);
    }

    /**
     * Tells whether the person directed this movie.
     *
     * @return {@code true} if the job is "Director"
     */
    public boolean directing() {
        return DIRECTOR_JOB.equals(job);
    }

    /**
     * Returns the year of release.
     *
     * @return year of release, or {@code 0} if unknown
     */
    public int releaseYear() {
        return TmdbDates.yearOf(releaseDate);
    }
}
