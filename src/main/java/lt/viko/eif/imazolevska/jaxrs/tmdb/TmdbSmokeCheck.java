package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import lt.viko.eif.imazolevska.jaxrs.model.Genre;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class TmdbSmokeCheck implements ApplicationRunner {

    private final TmdbImportService tmdbImportService;

    public TmdbSmokeCheck(TmdbImportService tmdbImportService) {
        this.tmdbImportService = tmdbImportService;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Imported: " + tmdbImportService.importDirector(525));
        System.out.println("Imported: " + tmdbImportService.importDirector(525));

    }
}


