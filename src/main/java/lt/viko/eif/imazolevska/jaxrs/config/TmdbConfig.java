package lt.viko.eif.imazolevska.jaxrs.config;


import lt.viko.eif.imazolevska.jaxrs.tmdb.TmdbProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;


@Configuration
@EnableConfigurationProperties(TmdbProperties.class)
public class TmdbConfig {

    @Bean
    public RestClient tmdbRestClient(TmdbProperties properties){
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " +
                        properties.apiToken())
                .defaultHeader(HttpHeaders.ACCEPT,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
