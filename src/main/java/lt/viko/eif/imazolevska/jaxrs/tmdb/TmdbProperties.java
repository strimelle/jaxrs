package lt.viko.eif.imazolevska.jaxrs.tmdb;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tmdb")
public record TmdbProperties (String baseUrl, String apiToken) {

    public boolean hasApiToken(){
        return apiToken != null && !apiToken.isBlank();
    }


}
