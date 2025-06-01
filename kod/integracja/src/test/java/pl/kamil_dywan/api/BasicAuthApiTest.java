package pl.kamil_dywan.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.service.AppProperties;
import pl.kamil_dywan.service.SecureStorage;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class BasicAuthApiTest {

    private static final BasicAuthApi basicAuthApi = new BasicAuthApi("subdomain", "prefix");
    private static final HttpClient httpClientMock = Mockito.mock(HttpClient.class);

    @BeforeAll
    public static void setUp(){

        TestUtils.updatePrivateStaticField(Api.class, "httpClient", httpClientMock);
    }

    @AfterEach
    public void clear(){

        TestUtils.updatePrivateStaticField(BasicAuthApi.class, "clientId", "");
        TestUtils.updatePrivateStaticField(BasicAuthApi.class, "authHeaderContent", "");
    }

    @Test
    void shouldInit(){

        String expectedClientId = "expected client id";
        String expectedSecret = "expected secret";
        String expectedAuthHeader = "Basic ZXhwZWN0ZWQgY2xpZW50IGlkOmV4cGVjdGVkIHNlY3JldA==";

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
            MockedStatic<AppProperties> appPropertiesMock = Mockito.mockStatic(AppProperties.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(true);
            appPropertiesMock.when(() -> AppProperties.getProperty(any())).thenReturn(expectedClientId);
            secureStorageMock.when(() -> SecureStorage.getCredentialsPassword(any())).thenReturn(expectedSecret);

            BasicAuthApi.init();

            String gotClientId = BasicAuthApi.clientId;
            String gotAuthHeader = TestUtils.getPrivateStaticField(BasicAuthApi.class, "authHeaderContent", String.class);

            assertEquals(expectedClientId, gotClientId);
            assertEquals(expectedAuthHeader, gotAuthHeader);

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
            appPropertiesMock.verify(() -> AppProperties.getProperty("allegro.api.client.id"));
            secureStorageMock.verify(() -> SecureStorage.getCredentialsPassword(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
        }
    }

    @Test
    void shouldNotInitOneMoreTime(){

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
            MockedStatic<AppProperties> appPropertiesMock = Mockito.mockStatic(AppProperties.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(false);

            BasicAuthApi.init();

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
            appPropertiesMock.verifyNoInteractions();
        }
    }

    @Test
    public void shouldSend() throws Exception{

        String authHeader = "auth header";

        TestUtils.updatePrivateStaticField(BasicAuthApi.class, "authHeaderContent", authHeader);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost"));

        HttpResponse expectedResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        Mockito.when(httpClientMock.send(any(), any())).thenReturn(expectedResponse);

        basicAuthApi.send(httpRequestBuilder);

        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);

        Mockito.verify(httpClientMock).send(requestCaptor.capture(), any());

        HttpRequest gotRequest = requestCaptor.getValue();

        assertEquals(authHeader, gotRequest.headers().firstValue("Authorization").get());
    }
}