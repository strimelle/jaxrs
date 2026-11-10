package lt.viko.eif.imazolevska.jaxrs.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Movie genre with its TMDB identifier and a one-letter code.
 *
 * <p>The one-letter code is stored in {@link Director#getMainGenre()}.</p>
 */
public enum Genre {

    /**
     * Action movies.
     */
    ACTION(28, 'A'),
    /**
     * Adventure movies.
     */
    ADVENTURE(12, 'V'),
    /**
     * Animated movies.
     */
    ANIMATION(16, 'N'),
    /**
     * Comedies.
     */
    COMEDY(35, 'C'),
    /**
     * Crime movies.
     */
    CRIME(80, 'R'),
    /**
     * Documentaries.
     */
    DOCUMENTARY(99, 'U'),
    /**
     * Dramas.
     */
    DRAMA(18, 'D'),
    /**
     * Family movies.
     */
    FAMILY(10751, 'F'),
    /**
     * Fantasy movies.
     */
    FANTASY(14, 'Y'),
    /**
     * Historical movies.
     */
    HISTORY(36, 'I'),
    /**
     * Horror movies.
     */
    HORROR(27, 'H'),
    /**
     * Music movies.
     */
    MUSIC(10402, 'M'),
    /**
     * Mystery movies.
     */
    MYSTERY(9648, 'E'),
    /**
     * Romance movies.
     */
    ROMANCE(10749, 'O'),
    /**
     * Science fiction movies.
     */
    SCIENCE_FICTION(878, 'S'),
    /**
     * Movies made for television.
     */
    TV_MOVIE(10770, 'T'),
    /**
     * Thrillers.
     */
    THRILLER(53, 'L'),
    /**
     * War movies.
     */
    WAR(10752, 'W'),
    /**
     * Westerns.
     */
    WESTERN(37, 'X');

    private final int tmdbId;
    private final char code;

    /**
     * Creates a genre constant.
     *
     * @param tmdbId genre identifier in TMDB
     * @param code   one-letter genre code
     */

    Genre(int tmdbId, char code) {
        this.tmdbId = tmdbId;
        this.code = code;
    }

    /**
     * Returns the genre identifier used by TMDB.
     *
     * @return TMDB genre identifier
     */
    public int getTmdbId() {
        return tmdbId;
    }

    /**
     * Returns the one-letter code of the genre.
     *
     * @return genre code
     */
    public char getCode() {
        return code;
    }

    /**
     * Finds a genre by its TMDB identifier.
     *
     * @param tmdbId TMDB genre identifier
     * @return matching genre, or an empty value if the identifier is unknown
     */
    public static Optional<Genre> fromTmdbId(int tmdbId) {
        return Arrays.stream(values())
                .filter(genre -> genre.tmdbId == tmdbId)
                .findFirst();
    }

    /**
     * Finds a genre by its one-letter code. The search ignores letter case.
     *
     * @param code genre code
     * @return matching genre, or an empty value if the code is unknown
     */
    public static Optional<Genre> fromCode(char code) {
        char upperCode = Character.toUpperCase(code);
        return Arrays.stream(values())
                .filter(genre -> genre.code == upperCode)
                .findFirst();
    }
}
