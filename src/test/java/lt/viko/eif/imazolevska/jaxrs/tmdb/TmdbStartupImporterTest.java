package lt.viko.eif.imazolevska.jaxrs.tmdb;

import lt.viko.eif.imazolevska.jaxrs.model.Director;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TmdbStartupImporter}.
 */
@ExtendWith(MockitoExtension.class)
class TmdbStartupImporterTest {

    private final ApplicationArguments args = new DefaultApplicationArguments();

    @Mock
    private TmdbImportService importService;

    @Test
    void runDoesNothingWhenImportIsTurnedOff() {
        importer(new TmdbProperties("url", "token", false, List.of(525))).run(args);

        verifyNoInteractions(importService);
    }

    @Test
    void runDoesNothingWithoutApiToken() {
        importer(new TmdbProperties("url", "", true, List.of(525))).run(args);

        verifyNoInteractions(importService);
    }

    @Test
    void runImportsEveryDirectorAndContinuesAfterErrors() {
        when(importService.importDirector(138)).thenThrow(new RestClientException("Timeout"));
        when(importService.importDirector(525))
                .thenReturn(Optional.of(new Director(525, "Christopher", "Nolan", 1970, true, 'S')));
        when(importService.importDirector(1)).thenReturn(Optional.empty());

        importer(new TmdbProperties("url", "token", true, List.of(138, 525, 1))).run(args);

        verify(importService).importDirector(138);
        verify(importService).importDirector(525);
        verify(importService).importDirector(1);
    }

    private TmdbStartupImporter importer(TmdbProperties properties) {
        return new TmdbStartupImporter(properties, importService);
    }
}
