package pl.kamil_dywan.api.allegro;

import pl.kamil_dywan.api.BasicAuthApi;
import pl.kamil_dywan.service.AppProperties;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginApi extends BasicAuthApi {

    public LoginApi() {

        super("/auth/oauth");

        clientId = AppProperties.getProperty("allegro.api.client.id");
    }

    public HttpResponse<String> generateDeviceCodeAndVerification()  {

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.noBody())
            .uri(URI.create(API_PREFIX + "/device" + getQueryParamsPostFix("client_id", clientId)))
            .header("Content-Type", "application/x-www-form-urlencoded");;

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> generateAccessToken(String deviceCode) throws IllegalStateException {

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.noBody())
            .uri(
                URI.create(API_PREFIX + "/token" +
                    getQueryParamsPostFix(
                        "device_code", deviceCode,
                        "grant_type", "urn:ietf:params:oauth:grant-type:device_code"
                    )
                )
            );

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> refreshAccessToken(String refreshToken) throws IllegalStateException {

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.noBody())
            .uri(URI.create(API_PREFIX + "/token" +
                getQueryParamsPostFix("grant_type", "refresh_token", "refresh_token", refreshToken)
            ));

        return send(httpRequestBuilder);
    }
}
