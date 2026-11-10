package lt.viko.eif.imazolevska.jaxrs.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Film director with the list of movies he or she directed. The data is imported from TMDB.
 *
 * <p>Movies are loaded together with the director, because the REST layer
 * serializes them after the database transaction is closed.</p>
 */
@Entity
@Table(name = "director")
@XmlRootElement(name = "director")
@XmlAccessorType(XmlAccessType.FIELD)
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tmdb_id", unique = true)
    private Integer tmdbId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "average_rating")
    private float averageRating;

    @Column(name = "active")
    private boolean active;

    @Column(name = "main_genre", length = 1)
    @XmlJavaTypeAdapter(CharAdapter.class)
    private Character mainGenre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id")
    @XmlElementWrapper(name = "movies")
    @XmlElement(name = "movie")
    @JsonProperty("movies")
    private List<Movie> movies = new ArrayList<>();

    /**
     * Creates an empty director. Required by JPA and JAXB.
     */
    public Director() {
    }

    /**
     * Creates a director with the given data and no movies.
     *
     * @param tmdbId    person identifier in TMDB
     * @param firstName first name
     * @param lastName  last name
     * @param birthYear year of birth, or {@code 0} if unknown
     * @param active    {@code true} if the director is still working
     * @param mainGenre code of the main genre, see {@link Genre#getCode()}
     */
    public Director(Integer tmdbId, String firstName, String lastName, int birthYear,
                    boolean active, Character mainGenre) {
        this.tmdbId = tmdbId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.active = active;
        this.mainGenre = mainGenre;
    }

    /**
     * Adds a movie to the director and recalculates the average rating.
     *
     * @param movie movie to add
     */
    public void addMovie(Movie movie) {
        movies.add(movie);
        recalculateAverageRating();
    }

    /**
     * Calculates the average rating of all movies of the director.
     * The rating is {@code 0} if the director has no movies.
     */
    public void recalculateAverageRating() {
        averageRating = (float) movies.stream()
                .mapToDouble(Movie::getRating)
                .average()
                .orElse(0);
    }

    /**
     * Returns the identifier of the director in this service.
     *
     * @return identifier, or {@code null} if the director is not saved yet
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the identifier of the director in this service.
     *
     * @param id identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the director in TMDB.
     *
     * @return TMDB identifier, or {@code null} for directors not imported from TMDB
     */
    public Integer getTmdbId() {
        return tmdbId;
    }

    /**
     * Sets the identifier of the director in TMDB.
     *
     * @param tmdbId TMDB identifier
     */
    public void setTmdbId(Integer tmdbId) {
        this.tmdbId = tmdbId;
    }

    /**
     * Returns the first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the year of birth.
     *
     * @return year of birth, or {@code 0} if unknown
     */
    public int getBirthYear() {
        return birthYear;
    }

    /**
     * Sets the year of birth.
     *
     * @param birthYear year of birth
     */
    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    /**
     * Returns the average rating of the director's movies.
     *
     * @return average rating from 0 to 10
     */
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the average rating of the director's movies.
     *
     * @param averageRating average rating from 0 to 10
     */
    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Tells whether the director is still working.
     *
     * @return {@code true} if the director is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the director is still working.
     *
     * @param active {@code true} if the director is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the code of the director's main genre, for example {@code 'S'} for science fiction.
     *
     * @return genre code, or {@code null} if unknown
     * @see Genre
     */
    public Character getMainGenre() {
        return mainGenre;
    }

    /**
     * Sets the code of the director's main genre.
     *
     * @param mainGenre genre code
     */
    public void setMainGenre(Character mainGenre) {
        this.mainGenre = mainGenre;
    }

    /**
     * Returns the movies of the director.
     *
     * @return list of movies, never {@code null}
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * Replaces the movies of the director and recalculates the average rating.
     *
     * @param movies new list of movies; {@code null} removes all movies
     */
    public void setMovies(List<Movie> movies) {
        this.movies.clear();
        if (movies != null) {
            this.movies.addAll(movies);
        }
        recalculateAverageRating();
    }

    /**
     * Returns a short description of the director for logs and debugging.
     *
     * @return identifier, full name and number of movies
     */

    @Override
    public String toString() {
        return "Director{id=" + id + ", name='" + firstName + " " + lastName
                + "', movies=" + movies.size() + "}";
    }
}