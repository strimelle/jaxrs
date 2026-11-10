package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;

import java.util.Optional;

/**
 * Imports directors and their movies from TMDB into the database.
 */
public interface TmdbImportService {
    Optional<Director> importDirector(int tmdbId);
}
