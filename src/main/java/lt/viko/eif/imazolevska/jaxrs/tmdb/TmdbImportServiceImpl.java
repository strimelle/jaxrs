package lt.viko.eif.imazolevska.jaxrs.tmdb;


import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import lt.viko.eif.imazolevska.jaxrs.repository.DirectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * {@link TmdbImportService} implementation that reads data with {@link TmdbClient},
 * converts it with {@link TmdbDirectorMapper} and saves it with {@link DirectorRepository}.
 */
@Service
@Transactional
public class TmdbImportServiceImpl implements TmdbImportService {
    private final TmdbClient tmdbClient;
    private final TmdbDirectorMapper mapper;
    private final DirectorRepository directorRepository;

    public TmdbImportServiceImpl(TmdbClient tmdbClient, TmdbDirectorMapper mapper, DirectorRepository directorRepository) {
        this.tmdbClient = tmdbClient;
        this.mapper = mapper;
        this.directorRepository = directorRepository;
    }

    @Override
    public Optional<Director> importDirector(int tmdbId) {
        Optional<TmdbPerson> person = tmdbClient.findPerson(tmdbId);
        if (person.isEmpty()) {
            return Optional.empty();
        }
        Director imported = mapper.toDirector(person.get(), tmdbClient.getMovieCredits(tmdbId));
        Director director = directorRepository.findByTmdbId(tmdbId)
                .map(existing -> copyImportedData(imported, existing))
                .orElse(imported);
        return Optional.of(directorRepository.save(director));
    }

    private static Director copyImportedData(Director source, Director
            target) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setBirthYear(source.getBirthYear());
        target.setActive(source.isActive());
        target.setMainGenre(source.getMainGenre());
        target.setMovies(source.getMovies());
        return target;
    }

}
