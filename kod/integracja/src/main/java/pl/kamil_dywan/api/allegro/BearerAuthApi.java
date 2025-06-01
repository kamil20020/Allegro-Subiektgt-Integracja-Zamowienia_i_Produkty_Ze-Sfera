package pl.kamil_dywan.api.allegro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.auth.AccessTokenResponse;
import pl.kamil_dywan.service.SecureStorage;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

public class BearerAuthApi extends AllegroApi {

    private static String accessToken;
    private static String refreshToken;
    private static String bearerAuthContent = "";
    private static Integer refreshTokenExpiresIn;
    private static Function<String, HttpResponse<String>> refreshAccessToken;

    private static final String ACCESS_TOKEN_CREDENTIALS_KEY_POSTFIX = "access_token";
    private static final String REFRESH_TOKEN_CREDENTIALS_KEY_POSTFIX = "refresh_token";

    private static final Logger log = LoggerFactory.getLogger(Api.class);

    public BearerAuthApi(String subDomain, String laterPrefix){

        super(subDomain, laterPrefix);
    }

    public BearerAuthApi(String laterPrefix){

        super(laterPrefix);
    }

    public static void init(Function<String, HttpResponse<String>> refreshAccessToken1){

        if(SecureStorage.doesExist(ACCESS_TOKEN_CREDENTIALS_KEY_POSTFIX)){

            accessToken = SecureStorage.getCredentialsPassword(ACCESS_TOKEN_CREDENTIALS_KEY_POSTFIX);
            refreshToken = SecureStorage.getCredentialsPassword(REFRESH_TOKEN_CREDENTIALS_KEY_POSTFIX);
            bearerAuthContent = getBearerAuthContent(accessToken);
        }

        refreshAccessToken = refreshAccessToken1;
    }

    public static void saveAuthData(String newAccessToken, String newRefreshToken){

        accessToken = newAccessToken;
        refreshToken = newRefreshToken;
        bearerAuthContent = getBearerAuthContent(newAccessToken);

        SecureStorage.saveCredentials(ACCESS_TOKEN_CREDENTIALS_KEY_POSTFIX, accessToken);
        SecureStorage.saveCredentials(REFRESH_TOKEN_CREDENTIALS_KEY_POSTFIX, refreshToken);
    }

    @Override
    public HttpResponse<String> send(HttpRequest.Builder httpRequestBuilder) throws IllegalStateException, UnloggedException {

        httpRequestBuilder
            .header("Authorization", bearerAuthContent);

        HttpResponse<String> gotResponse = super.send(httpRequestBuilder);

        if (gotResponse.statusCode() == 401) {

            log.info("401 - Expired access token");

            log.info("Refreshing access token");

            if(!handleRefreshAccessToken()){

                log.info("401 - Expired refresh token");

                throw new UnloggedException();
            }
            else{

                log.info("Refreshed access token");

                httpRequestBuilder
                    .header("Authorization", bearerAuthContent);

                gotResponse = super.send(httpRequestBuilder);
            }
        }

        return gotResponse;
    }

    private boolean handleRefreshAccessToken() throws IllegalStateException{

        HttpResponse<String> gotResponse = refreshAccessToken.apply(refreshToken);

        if(gotResponse.statusCode() == 401 || gotResponse.statusCode() == 400){

            logout();

            return false;
        }

        AccessTokenResponse accessTokenResponse = Api.extractBody(gotResponse, AccessTokenResponse.class);

        saveAuthData(accessTokenResponse.getAccessToken(), accessTokenResponse.getRefreshToken());

        return true;
    }

    public static void logout(){

        accessToken = null;
        refreshToken = null;
        bearerAuthContent = "";

        SecureStorage.delete(ACCESS_TOKEN_CREDENTIALS_KEY_POSTFIX);
        SecureStorage.delete(REFRESH_TOKEN_CREDENTIALS_KEY_POSTFIX);
    }

    private static String getBearerAuthContent(String token){

        return "Bearer " + token;
    }

    public static boolean isUserLogged(){

        return accessToken != null;
    }
}
