package lt.viko.eif.imazolevska.jaxrs.repository;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Provides access to the directors stored in the database.
 */
@Repository
public interface DirectorRepository extends JpaRepository<Director, Integer> {

    /**
     * Finds the directors with the given main genre.
     *
     * @param mainGenre genre code, for example {@code 'S'} for science fiction
     * @return matching directors, empty if there are none
     */
    List<Director> findByMainGenre(Character mainGenre);

    /**
     * Finds the director imported from TMDB with the given identifier.
     *
     * @param tmdbId person identifier in TMDB
     * @return the director, or an empty value if it was not imported
     */
    Optional<Director> findByTmdbId(Integer tmdbId);
}