package pl.kamil_dywan.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.allegro.BearerAuthApi;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.auth.AccessTokenResponse;
import pl.kamil_dywan.service.SecureStorage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class BearerAuthApiTest {

    private static final BearerAuthApi bearerAuthApi = new BearerAuthApi("subdomain", "prefix");
    private static HttpClient httpClientMock;

    @BeforeEach
    public void setUpBeforeEach(){

        httpClientMock = Mockito.mock(HttpClient.class);
        TestUtils.updatePrivateStaticField(Api.class, "httpClient", httpClientMock);
    }

    @AfterEach
    public void clear(){

        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "accessToken", null);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "refreshToken", null);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "bearerAuthContent", "");
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "refreshAccessToken", null);
    }

    @Test
    void shouldInit() {

        String expectedAccessToken = "access token";
        String expectedRefreshToken = "refresh token";
        String expectedAuthHeader = "Bearer access token";

        Function expectedRefreshTokenFunction = refreshToken -> TestHttpResponse.builder()
            .statusCode(200)
            .build();

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(true);
            secureStorageMock.when(() -> SecureStorage.getCredentialsPassword(eq("access_token"))).thenReturn(expectedAccessToken);
            secureStorageMock.when(() -> SecureStorage.getCredentialsPassword(eq("refresh_token"))).thenReturn(expectedRefreshToken);

            BearerAuthApi.init(expectedRefreshTokenFunction);

            String gotAccessToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "accessToken", String.class);
            String gotRefreshToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshToken", String.class);
            String gotBearerAuthContent = TestUtils.getPrivateStaticField(BearerAuthApi.class, "bearerAuthContent", String.class);
            Function gotRefreshTokenFunction = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshAccessToken", Function.class);

            assertEquals(expectedAccessToken, gotAccessToken);
            assertEquals(expectedRefreshToken, gotRefreshToken);
            assertEquals(expectedAuthHeader, gotBearerAuthContent);
            assertEquals(expectedRefreshTokenFunction, gotRefreshTokenFunction);

            secureStorageMock.verify(() -> SecureStorage.doesExist("access_token"));
            secureStorageMock.verify(() -> SecureStorage.getCredentialsPassword("access_token"));
            secureStorageMock.verify(() -> SecureStorage.getCredentialsPassword("refresh_token"));
        }
    }

    @Test
    void shouldNotInitWhenAccessTokenDoesNotExist() {

        Function expectedRefreshTokenFunction = refreshToken -> TestHttpResponse.builder()
            .statusCode(200)
            .build();

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ) {

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(false);

            BearerAuthApi.init(expectedRefreshTokenFunction);

            Function gotRefreshTokenFunction = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshAccessToken", Function.class);

            assertEquals(expectedRefreshTokenFunction, gotRefreshTokenFunction);

            secureStorageMock.verify(() -> SecureStorage.doesExist("access_token"));
        }
    }

    @Test
    void shouldSaveAuthData() {

        String expectedAccessToken = "expected access token";
        String expectedRefreshToken = "expected refresh token";
        String expectedAuthContent = "Bearer expected access token";

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ) {

            BearerAuthApi.saveAuthData(expectedAccessToken, expectedRefreshToken);

            String gotAccessToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "accessToken", String.class);
            String gotRefreshToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshToken", String.class);
            String gotBearerAuthContent = TestUtils.getPrivateStaticField(BearerAuthApi.class, "bearerAuthContent", String.class);

            assertEquals(expectedAccessToken, gotAccessToken);
            assertEquals(expectedRefreshToken, gotRefreshToken);
            assertEquals(expectedAuthContent, gotBearerAuthContent);

            secureStorageMock.verify(() -> SecureStorage.saveCredentials("access_token", expectedAccessToken));
            secureStorageMock.verify(() -> SecureStorage.saveCredentials("refresh_token", expectedRefreshToken));
        }
    }

    @Test
    void shouldSend() throws Exception{

        //given
        String expectedAuthHeader = "Bearer token";

        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "bearerAuthContent", expectedAuthHeader);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost"));

        HttpResponse expectedResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        //when
        Mockito.when(httpClientMock.send(any(), any())).thenReturn(expectedResponse);

        HttpResponse gotResponse = bearerAuthApi.send(httpRequestBuilder);

        //then
        ArgumentCaptor<HttpRequest> httpRequestCaptor = ArgumentCaptor.forClass(HttpRequest.class);

        Mockito.verify(httpClientMock).send(httpRequestCaptor.capture(), any());

        HttpRequest gotHttpRequest = httpRequestCaptor.getValue();

        String gotAuthContent = gotHttpRequest.headers().firstValue("Authorization").get();

        assertEquals(expectedResponse, gotResponse);
        assertEquals(expectedAuthHeader, gotAuthContent);
    }

    @Test
    void shouldSendRefreshTokenWhenGotAUnauthorized() throws Exception{

        //given
        String expectedAuthHeader = "Bearer access token";
        String expectedAccessToken = "access token";
        String expectedRefreshToken = "refresh token";

        HttpResponse expectedRefreshTokenHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        Function expectedRefreshTokenFunction = refreshToken -> {

            assertEquals(expectedRefreshToken, refreshToken);

            return expectedRefreshTokenHttpResponse;
        };

        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "refreshAccessToken", expectedRefreshTokenFunction);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "bearerAuthContent", expectedAuthHeader);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "refreshToken", expectedRefreshToken);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost"));

        HttpResponse expectedResponse = TestHttpResponse.builder()
            .statusCode(401)
            .build();

        AccessTokenResponse expectedAccessTokenResponse = new AccessTokenResponse(
            expectedAccessToken,
            expectedRefreshToken
        );

        //when
        Mockito.when(httpClientMock.send(any(), any())).thenReturn(expectedResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            apiMock.when(() -> Api.extractBody(any(HttpResponse.class), any())).thenReturn(expectedAccessTokenResponse);

            bearerAuthApi.send(httpRequestBuilder);

            String gotAccessToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "accessToken", String.class);
            String gotRefreshToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshToken", String.class);
            String gotBearerAuthContent = TestUtils.getPrivateStaticField(BearerAuthApi.class, "bearerAuthContent", String.class);

            //then
            apiMock.verify(() -> Api.extractBody(eq(expectedRefreshTokenHttpResponse), any()));
            secureStorageMock.verify(() -> SecureStorage.saveCredentials("access_token", expectedAccessToken));
            secureStorageMock.verify(() -> SecureStorage.saveCredentials("refresh_token", expectedRefreshToken));

            assertEquals(expectedAccessToken, gotAccessToken);
            assertEquals(expectedRefreshToken, gotRefreshToken);
            assertEquals(expectedAuthHeader, gotBearerAuthContent);

        }

        Mockito.verify(httpClientMock, Mockito.times(2)).send(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenFailedSendRefreshTokenWhenGotAUnauthorized() throws Exception{

        //given
        String expectedAuthHeader = "Bearer access token";
        String expectedAccessToken = "access token";
        String expectedRefreshToken = "refresh token";

        HttpResponse expectedRefreshTokenHttpResponse = TestHttpResponse.builder()
            .statusCode(401)
            .build();

        Function expectedRefreshTokenFunction = refreshToken -> expectedRefreshTokenHttpResponse;

        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "refreshAccessToken", expectedRefreshTokenFunction);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "bearerAuthContent", expectedAuthHeader);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost"));

        HttpResponse expectedResponse = TestHttpResponse.builder()
            .statusCode(401)
            .build();

        AccessTokenResponse expectedAccessTokenResponse = new AccessTokenResponse(
            expectedAccessToken,
            expectedRefreshToken
        );

        //when
        Mockito.when(httpClientMock.send(any(), any())).thenReturn(expectedResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            apiMock.when(() -> Api.extractBody(any(HttpResponse.class), any())).thenReturn(expectedAccessTokenResponse);

            //then
            assertThrows(
                UnloggedException.class,
                () -> bearerAuthApi.send(httpRequestBuilder)
            );

            String gotAccessToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "accessToken", String.class);
            String gotRefreshToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshToken", String.class);
            String gotBearerAuthContent = TestUtils.getPrivateStaticField(BearerAuthApi.class, "bearerAuthContent", String.class);

            assertNull(gotAccessToken);
            assertNull(gotRefreshToken);
            assertTrue(gotBearerAuthContent.isEmpty());

            secureStorageMock.verify(() -> SecureStorage.delete("access_token"));
            secureStorageMock.verify(() -> SecureStorage.delete("refresh_token"));
        }

        Mockito.verify(httpClientMock).send(any(), any());
    }

    @Test
    void shouldLogout() {

        String initAccessToken = "access token";
        String initRefreshToken = "refresh token";
        String initBearerAuthContent = "Bearer token";

        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "accessToken", initAccessToken);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "refreshToken", initRefreshToken);
        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "bearerAuthContent", initBearerAuthContent);

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ) {

            secureStorageMock.when(() -> SecureStorage.delete(eq("access_token"))).thenReturn(false);
            secureStorageMock.when(() -> SecureStorage.delete(eq("refresh_token"))).thenReturn(false);

            BearerAuthApi.logout();

            String gotAccessToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "accessToken", String.class);
            String gotRefreshToken = TestUtils.getPrivateStaticField(BearerAuthApi.class, "refreshToken", String.class);
            String gotBearerAuthContent = TestUtils.getPrivateStaticField(BearerAuthApi.class, "bearerAuthContent", String.class);

            assertNull(gotAccessToken);
            assertNull(gotRefreshToken);
            assertTrue(gotBearerAuthContent.isEmpty());

            secureStorageMock.verify(() -> SecureStorage.delete("access_token"));
            secureStorageMock.verify(() -> SecureStorage.delete("refresh_token"));
        }
    }

    @Test
    void shouldGetIsUserLoggedWhenUserIsLogged() {

        String expectedAccessToken = "expected access token";

        TestUtils.updatePrivateStaticField(BearerAuthApi.class, "accessToken", expectedAccessToken);

        boolean isUserLogged = BearerAuthApi.isUserLogged();

        assertTrue(isUserLogged);
    }

    @Test
    void shouldNotGetIsUserLoggedWhenUserIsNotLogged() {

        boolean isUserLogged = BearerAuthApi.isUserLogged();

        assertFalse(isUserLogged);
    }
}