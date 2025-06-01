package pl.kamil_dywan.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.service.AppProperties;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class Api {

    protected String API_PREFIX;

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private static final Logger log = LoggerFactory.getLogger(Api.class);
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    public Api(String subDomain, String laterPrefix){

        if(subDomain != null && !subDomain.isBlank()){
            subDomain += '.';
        }

        API_PREFIX =  "https://" + subDomain + getEnvApiHost() + laterPrefix;
    }

    public Api(String laterPrefix){

        API_PREFIX = "https://" + getEnvApiHost() + laterPrefix;
    }

    protected static String getQueryParamsPostFix(String... titlesAndParams){

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("?");

        for(int i = 0; i < titlesAndParams.length - 1; i += 2){

            if(i > 0){

                stringBuilder.append("&");
            }

            stringBuilder.append(titlesAndParams[i]);
            stringBuilder.append("=");
            stringBuilder.append(titlesAndParams[i + 1]);
        }

        return stringBuilder.toString();
    }

    private String getEnvApiHost(){

        return AppProperties.getProperty("allegro.api.host", String.class);
    }

    public HttpResponse<String> send(HttpRequest.Builder httpRequestBuilder) throws IllegalStateException{

        HttpRequest httpRequest = httpRequestBuilder.build();

        log.info("Request: ");
        log.info("Url: " + httpRequest.toString());

        try {
            HttpResponse<String> gotResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            log.info("Response: ");
            log.info("Status code: " + gotResponse.statusCode());
            log.info("Body: " + gotResponse.body());

            return gotResponse;
        }
        catch (IOException | InterruptedException e) {

            e.printStackTrace();

            throw new IllegalStateException(e.getMessage());
        }
    }

    public static <T> T extractBody(HttpResponse<String> httpResponse, Class<T> type){

        JSONFileReader<T> jsonFileReader = new JSONFileReader<T>(type);

        T gotConvertedBody = jsonFileReader.loadFromStr(httpResponse.body());

        log.info(gotConvertedBody.toString());

        return gotConvertedBody;
    }
}
