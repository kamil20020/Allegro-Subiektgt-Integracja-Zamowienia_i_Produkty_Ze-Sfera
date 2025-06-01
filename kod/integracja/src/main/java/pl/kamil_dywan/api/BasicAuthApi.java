package pl.kamil_dywan.api;

import com.microsoft.credentialstorage.model.StoredCredential;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.mapper.Base64Mapper;
import pl.kamil_dywan.service.AppProperties;
import pl.kamil_dywan.service.SecureStorage;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BasicAuthApi extends Api {

    protected static String clientId = "";
    private static String authHeaderContent = "";

    public static final String ALLEGRO_SECRET_POSTFIX = "secret";

    public BasicAuthApi(String subDomain, String laterPrefix) {

        super(subDomain, laterPrefix);
    }

    public BasicAuthApi(String laterPrefix) {

        super(laterPrefix);
    }

    public static void init(){

        if(!SecureStorage.doesExist(ALLEGRO_SECRET_POSTFIX)){
            return;
        }

        clientId = AppProperties.getProperty("allegro.api.client.id");
        String secret = SecureStorage.getCredentialsPassword(ALLEGRO_SECRET_POSTFIX);

        authHeaderContent = "Basic " + Base64Mapper.mapToBase64(clientId + ":" + secret);
    }

    @Override
    public HttpResponse<String> send(HttpRequest.Builder httpRequestBuilder) throws IllegalStateException {

        httpRequestBuilder
            .header("Authorization", authHeaderContent);

        return super.send(httpRequestBuilder);
    }
}
