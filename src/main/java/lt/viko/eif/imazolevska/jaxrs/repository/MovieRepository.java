package lt.viko.eif.imazolevska.jaxrs.repository;

import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides access to the movies stored in the database.
 *
 * <p>Movies are saved and deleted together with their director, so this
 * repository is used only to read them.</p>
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
