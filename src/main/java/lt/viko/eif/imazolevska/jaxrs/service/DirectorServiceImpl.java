package lt.viko.eif.imazolevska.jaxrs.service;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Genre;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import lt.viko.eif.imazolevska.jaxrs.repository.DirectorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;

/**
 * {@link DirectorService} implementation that stores directors with {@link DirectorRepository}.
 */
@Service
@Transactional
public class DirectorServiceImpl implements DirectorService {

    private static final int EARLIEST_BIRTH_YEAR = 1800;

    private final DirectorRepository directorRepository;

    /**
     * Creates the service with the given repository.
     *
     * @param directorRepository repository that stores directors
     */
    public DirectorServiceImpl(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Director> findAll() {
        return directorRepository.findAll(Sort.by("id"));
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Optional<Director> findById(int id) {
        return directorRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Director> findByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre code must not be empty");
        }
        if (genre.length() != 1) {
            throw new IllegalArgumentException("Genre code must be one letter: " + genre);
        }
        return directorRepository.findByMainGenre(normalizeGenre(genre.charAt(0)));
    }

    /** {@inheritDoc} */
    @Override
    public Director create(Director director) {
        validate(director);
        director.setId(null);
        director.setTmdbId(null);
        director.setMainGenre(normalizeGenre(director.getMainGenre()));
        director.getMovies().forEach(movie -> movie.setId(null));
        director.recalculateAverageRating();
        return directorRepository.save(director);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Director> update(int id, Director changes) {
        validate(changes);
        Character mainGenre = normalizeGenre(changes.getMainGenre());
        return directorRepository.findById(id).map(existing -> {
            existing.setFirstName(changes.getFirstName());
            existing.setLastName(changes.getLastName());
            existing.setBirthYear(changes.getBirthYear());
            existing.setActive(changes.isActive());
            existing.setMainGenre(mainGenre);
            existing.recalculateAverageRating();
            return directorRepository.save(existing);
        });
    }

    /** {@inheritDoc} */
    @Override
    public boolean delete(int id) {
        if (!directorRepository.existsById(id)) {
            return false;
        }
        directorRepository.deleteById(id);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Optional<List<Movie>> findMovies(int id) {
        return directorRepository.findById(id)
                .map(director -> List.copyOf(director.getMovies()));
    }

    /**
     * Checks the data a client may send for a director.
     *
     * @param director director to check
     * @throws IllegalArgumentException if a required field is missing or a value is out of range
     */
    private void validate(Director director) {
        if (director == null) {
            throw new IllegalArgumentException("Director data must not be empty");
        }
        if (isBlank(director.getFirstName()) || isBlank(director.getLastName())) {
            throw new IllegalArgumentException("First name and last name must not be empty");
        }
        int birthYear = director.getBirthYear();
        boolean birthYearKnown = birthYear != 0;
        if (birthYearKnown && (birthYear < EARLIEST_BIRTH_YEAR || birthYear > Year.now().getValue())) {
            throw new IllegalArgumentException("Birth year is out of range: " + birthYear);
        }
    }

    /**
     * Converts a genre code to upper case and checks that it is known.
     *
     * @param code genre code, may be {@code null}
     * @return upper case genre code, or {@code null} if the code is {@code null}
     * @throws IllegalArgumentException if the code is unknown
     */
    private static Character normalizeGenre(Character code) {
        if (code == null) {
            return null;
        }
        return Genre.fromCode(code)
                .map(Genre::getCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown genre code: " + code));
    }

    /**
     * Checks whether a text is missing or contains only spaces.
     *
     * @param value text to check
     * @return {@code true} if the text is {@code null} or blank
     */
    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}