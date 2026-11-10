# JAX-RS Web Service with TMDB Data

Spring Boot application that provides a RESTful (JAX-RS) web service for film directors
and their movies. The data is imported from the external [TMDB API](https://developer.themoviedb.org)
and stored in an H2 database.

Created as Web Services Assessment Task 3 ("JAX-RS Web service").

## Technologies

- Java 25
- Spring Boot 4.1
- Jersey (JAX-RS implementation, JSON, XML and WADL)
- Spring Data JPA, Hibernate
- H2 in-memory database, Liquibase (database schema)
- Spring `RestClient` (calls to the TMDB API)
- JUnit 5, Mockito (tests)
- Maven

## How it works

When the application starts:

1. Liquibase creates the `director` and `movie` tables in the H2 database.
2. Jersey publishes the REST resources at `http://localhost:8082/api`.
3. `TmdbStartupImporter` imports the directors listed in `tmdb.startup-director-ids`
   (Christopher Nolan, Quentin Tarantino, Steven Spielberg, Denis Villeneuve,
   Alfred Hitchcock, Stanley Kubrick) together with the movies they directed.

The import of one director:

1. `TmdbClient` reads the person (`GET /person/{id}`) and the person's movie credits
   (`GET /person/{id}/movie_credits`) from TMDB.
2. `TmdbDirectorMapper` converts the TMDB data to a `Director` with `Movie` objects:
   it splits the name, keeps only released movies the person directed, calculates
   the average rating and finds the main genre.
3. `TmdbImportServiceImpl` saves a new director, or updates a director imported before.

The application starts without a TMDB token too; it then only skips the import.

## Data model

| Class | Fields |
| --- | --- |
| `Director` | `id`, `tmdbId`, `firstName` (String), `lastName` (String), `birthYear` (int), `averageRating` (float), `active` (boolean), `mainGenre` (char), `movies` (List of `Movie`) |
| `Movie` | `id`, `tmdbId`, `title`, `releaseYear`, `rating`, `posterPath` |

`mainGenre` is a one-letter genre code: A action, V adventure, N animation, C comedy,
R crime, U documentary, D drama, F family, Y fantasy, I history, H horror, M music,
E mystery, O romance, S science fiction, T TV movie, L thriller, W war, X western.

## REST API

Base address: `http://localhost:8082/api`. Responses are JSON by default, or XML with the
header `Accept: application/xml`. Invalid data is answered with `400 Bad Request` and a
plain text message, for example `Unknown genre code: Q`.

| Method | Path | Description | Responses |
| --- | --- | --- | --- |
| GET | `/directors` | all directors | 200 |
| GET | `/directors?genre=S` | directors of one genre | 200, 400 (unknown genre) |
| GET | `/directors/{id}` | one director | 200, 404 |
| GET | `/directors/{id}/movies` | movies of the director | 200, 404 |
| POST | `/directors` | create a director | 201 with `Location` header, 400 |
| PUT | `/directors/{id}` | change a director | 200, 400, 404 |
| DELETE | `/directors/{id}` | delete a director with its movies | 204, 404 |
| POST | `/directors/import/{tmdbId}` | import a director from TMDB | 200, 404 |
| GET | `/application.wadl` | WADL description of the service | 200 |

Example body for `POST /directors`:

```json
{
  "firstName": "Greta",
  "lastName": "Gerwig",
  "birthYear": 1983,
  "active": true,
  "mainGenre": "C"
}
```

## Running the application

### 1. TMDB token

The TMDB data needs an API Read Access Token:

1. Create an account at [themoviedb.org](https://www.themoviedb.org).
2. Open **Settings → API** and request a developer API key.
3. Copy the **API Read Access Token**.

The token is read from the `TMDB_API_TOKEN` environment variable and must never be
written to the code or to `application.properties`.

In IntelliJ IDEA: **Run → Edit Configurations → JaxrsApplication → Modify options →
Environment variables**, then add `TMDB_API_TOKEN=<your token>`.

### 2. Start

From IntelliJ IDEA, run `JaxrsApplication`, or from the project folder:

```
mvnw.cmd spring-boot:run
```

(on Linux or macOS: `./mvnw spring-boot:run`). In PowerShell the token can be set for
the current window with `$env:TMDB_API_TOKEN="<your token>"` before the command.

### 3. Check

- REST API: `http://localhost:8082/api/directors`
- WADL: `http://localhost:8082/api/application.wadl`
- H2 console: `http://localhost:8082/h2-console` (JDBC URL `jdbc:h2:mem:directors`,
  user `sa`, empty password)

## Testing tools

The results of testing the service with external tools are stored in the `docs` and
`postman` folders:

| File | Tool | How it was made / how to use it |
| --- | --- | --- |
| `docs/application.wadl` | SoapUI | WADL description saved from `http://localhost:8082/api/application.wadl` |
| `docs/soapui-project.xml` | SoapUI | REST project created by importing the WADL; open it with **File → Import Project** |
| `docs/openapi.json` | APIMATIC, Swagger Editor | OpenAPI 3.0 description converted from the WADL with the APIMATIC CLI (`apimatic api transform --format=oas3 --file=./docs/application.wadl`); open it in [Swagger Editor](https://editor.swagger.io) with **File → Import file** |
| `postman/collections/JAX-RS Directors` | Postman | collection generated from `openapi.json`, with filled-in requests; Postman stores it in the `postman` folder of the project, open it in Postman with **Import** and select the folder |

Browser tools such as Swagger Editor run on another address than the service, so the
service sends CORS headers (`CorsFilter`) that allow these requests.

## Tests

```
mvnw.cmd test
```

Every class has unit tests. The TMDB API is replaced with a mock HTTP server or Mockito
mocks, so the tests do not need the internet or a token.

## JavaDoc

```
mvnw.cmd javadoc:javadoc
```

The documentation is generated to `target/reports/apidocs/index.html`.

## Project structure

```
src/main/java/lt/viko/eif/imazolevska/jaxrs
├── config       Jersey and TMDB configuration
├── model        Director, Movie, Genre
│   └── tmdb     classes for the TMDB JSON responses
├── repository   Spring Data JPA repositories
├── resource     JAX-RS resources, exception mapper and CORS filter
├── service      business logic for directors
└── tmdb         TMDB client, mapper, import service and startup import
src/main/resources
├── application.properties
└── db/changelog Liquibase changelogs
docs             WADL, OpenAPI description and SoapUI project
postman          Postman collection
```

## TMDB attribution

This program uses TMDB and the TMDB APIs but is not endorsed, certified, or otherwise
approved by TMDB. Movie and person data is provided by
[The Movie Database (TMDB)](https://www.themoviedb.org).
