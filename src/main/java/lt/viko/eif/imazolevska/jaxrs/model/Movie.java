package lt.viko.eif.imazolevska.jaxrs.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Movie made by a director. The data is imported from TMDB.
 *
 * <p>The TMDB identifier is not unique in this table, because a movie made by
 * two directors is stored once for each of them.</p>
 */
@Entity
@Table(name = "movie")
@XmlRootElement(name = "movie")
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tmdb_id")
    private Integer tmdbId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "release_year")
    private int releaseYear;

    @Column(name = "rating")
    private float rating;

    @Column(name = "poster_path")
    private String posterPath;

    /**
     * Creates an empty movie. Required by JPA and JAXB.
     */
    public Movie() {
    }

    /**
     * Creates a movie with the given data.
     *
     * @param tmdbId      movie identifier in TMDB
     * @param title       movie title
     * @param releaseYear year of release, or {@code 0} if unknown
     * @param rating      average rating from 0 to 10
     * @param posterPath  TMDB path of the poster image, may be {@code null}
     */
    public Movie(Integer tmdbId, String title, int releaseYear, float rating, String posterPath) {
        this.tmdbId = tmdbId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.posterPath = posterPath;
    }

    /**
     * Returns the identifier of the movie in this service.
     *
     * @return identifier, or {@code null} if the movie is not saved yet
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the identifier of the movie in this service.
     *
     * @param id identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the movie in TMDB.
     *
     * @return TMDB identifier, or {@code null} for movies not imported from TMDB
     */
    public Integer getTmdbId() {
        return tmdbId;
    }

    /**
     * Sets the identifier of the movie in TMDB.
     *
     * @param tmdbId TMDB identifier
     */
    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    /**
     * Returns the title of the movie.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the movie.
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the year of release.
     *
     * @return year of release, or {@code 0} if unknown
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the year of release.
     *
     * @param releaseYear year of release
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Returns the average rating of the movie.
     *
     * @return rating from 0 to 10
     */
    public float getRating() {
        return rating;
    }

    /**
     * Sets the average rating of the movie.
     *
     * @param rating rating from 0 to 10
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Returns the TMDB path of the poster image, for example {@code /abc.jpg}.
     *
     * @return poster path, or {@code null} if the movie has no poster
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * Sets the TMDB path of the poster image.
     *
     * @param posterPath poster path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * Returns a short description of the movie for logs and debugging.
     *
     * @return identifier, title, year of release and rating
     */

    @Override
    public String toString() {
        return "Movie{id=" + id + ", title='" + title + "', releaseYear=" + releaseYear
                + ", rating=" + rating + "}";
    }
}
