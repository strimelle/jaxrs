package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Person returned by the TMDB endpoint {@code GET /3/person/{person_id}}.
 *
 * @param id                 person identifier in TMDB
 * @param name               full name, for example "Christopher Nolan"
 * @param birthday           date of birth in the {@code yyyy-MM-dd} format, may be {@code null}
 * @param deathday           date of death in the {@code yyyy-MM-dd} format, {@code null} if alive
 * @param knownForDepartment main department of the person, for example "Directing"
 * @param profilePath        TMDB path of the profile photo, may be {@code null}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbPerson(
        int id,
        String name,
        String birthday,
        String deathday,
        @JsonProperty("known_for_department") String knownForDepartment,
        @JsonProperty("profile_path") String profilePath) {

    /**
     * Returns the year of birth.
     *
     * @return year of birth, or {@code 0} if unknown
     */
    public int birthYear() {
        return TmdbDates.yearOf(birthday);
    }

    /**
     * Tells whether the person is alive according to TMDB.
     *
     * @return {@code true} if TMDB has no date of death
     */
    public boolean alive() {
        return deathday == null || deathday.isBlank();
    }
}
