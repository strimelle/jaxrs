package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Genre;
import lt.viko.eif.imazolevska.jaxrs.model.Movie;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbCrewCredit;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbMovieCredits;
import lt.viko.eif.imazolevska.jaxrs.model.tmdb.TmdbPerson;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts the data received from TMDB to the {@link Director} and {@link Movie} entities.
 *
 * <p>Only released movies with a rating are kept, so that announced movies
 * do not lower the average rating of the director.</p>
 */
@Component
public class TmdbDirectorMapper {

    /**
     * Creates a director with movies from a TMDB person and the person's movie credits.
     *
     * @param person  person data from TMDB
     * @param credits movie credits of the person from TMDB
     * @return a new, not yet saved director with movies
     */
    public Director toDirector(TmdbPerson person, TmdbMovieCredits credits) {
        List<TmdbCrewCredit> releasedMovies = releasedDirectedMovies(credits);
        Director director = new Director(person.id(),
                firstName(person.name()),
                lastName(person.name()),
                person.birthYear(),
                person.alive(),
                mainGenre(releasedMovies));
        director.setMovies(releasedMovies.stream().map(TmdbDirectorMapper::toMovie).toList());
        return director;

    }


    private static String firstName(String fullName) {
        String name = fullName == null ? "" : fullName.trim();
        int lastSpace = name.lastIndexOf(' ');
        return lastSpace < 0 ? "" : name.substring(0, lastSpace);
    }

    private static String lastName(String fullName) {
        String name = fullName == null ? "" : fullName.trim();
        int lastSpace = name.lastIndexOf(' ');
        return lastSpace < 0 ? name : name.substring(lastSpace + 1);
    }

    private static List<TmdbCrewCredit> releasedDirectedMovies(TmdbMovieCredits credits) {
        int currentYear = Year.now().getValue();
        Map<Integer, TmdbCrewCredit> uniqueMovies = new LinkedHashMap<>();
        credits.directedMovies().stream()
                .filter(credit -> credit.releaseYear() > 0 && credit.releaseYear() <= currentYear)
                .filter(credit -> credit.voteAverage() > 0)
                .forEach(credit -> uniqueMovies.putIfAbsent(credit.id(), credit));
        return uniqueMovies.values().stream().sorted(Comparator.comparingInt(TmdbCrewCredit::releaseYear)).toList();
    }

    private static Movie toMovie(TmdbCrewCredit credit) {
        return new Movie(credit.id(), credit.title(), credit.releaseYear(), credit.voteAverage(), credit.posterPath());
    }

    private static Character mainGenre(List<TmdbCrewCredit> movies) {
        Map<Genre, Long> genreCounts = movies.stream()
                .flatMap(movie -> movie.genreIds().stream())
                .map(Genre::fromTmdbId)
                .flatMap(Optional::stream)
                .collect(Collectors.groupingBy(genre -> genre,
                        () -> new EnumMap<>(Genre.class),
                        Collectors.counting()));
        return genreCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().getCode())
                .orElse(null);
    }
}