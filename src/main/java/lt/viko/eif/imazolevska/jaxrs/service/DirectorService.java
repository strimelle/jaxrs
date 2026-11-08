package lt.viko.eif.imazolevska.jaxrs.service;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;

import java.util.List;
import java.util.Optional;

/**
 * Business operations with directors and their movies.
 *
 * <p>Methods that receive invalid data throw {@link IllegalArgumentException}.</p>
 */
public interface DirectorService {

    /**
     * Returns all directors sorted by identifier.
     *
     * @return list of directors, empty if there are none
     */
    List<Director> findAll();

    /**
     * Finds the director with the given identifier.
     *
     * @param id director identifier
     * @return the director, or an empty value if it does not exist
     */
    Optional<Director> findById(int id);

    /**
     * Returns the directors whose main genre has the given one-letter code.
     * The code is not case sensitive.
     *
     * @param genre genre code, for example "S" for science fiction
     * @return matching directors, empty if there are none
     * @throws IllegalArgumentException if the code is empty, longer than one letter or unknown
     */
    List<Director> findByGenre(String genre);

    /**
     * Saves a new director together with its movies. Identifiers sent by the
     * client are ignored and the average rating is calculated from the movies.
     *
     * @param director director to create
     * @return the saved director with its new identifier
     * @throws IllegalArgumentException if the director data is invalid
     */
    Director create(Director director);

    /**
     * Changes the data of an existing director. The identifiers and the movies
     * of the director are not changed.
     *
     * @param id      identifier of the director to change
     * @param changes new data of the director
     * @return the changed director, or an empty value if it does not exist
     * @throws IllegalArgumentException if the new data is invalid
     */
    Optional<Director> update(int id, Director changes);

    /**
     * Deletes the director together with its movies.
     *
     * @param id identifier of the director to delete
     * @return {@code true} if the director was deleted, {@code false} if it does not exist
     */
    boolean delete(int id);

    /**
     * Returns the movies of the director.
     *
     * @param id director identifier
     * @return movies of the director (possibly an empty list),
     *         or an empty value if the director does not exist
     */
    Optional<List<Movie>> findMovies(int id);
}