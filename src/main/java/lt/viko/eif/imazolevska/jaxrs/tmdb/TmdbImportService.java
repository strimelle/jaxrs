package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;

import java.util.Optional;

public interface TmdbImportService {
    Optional<Director> importDirector(int tmdbId);
}
