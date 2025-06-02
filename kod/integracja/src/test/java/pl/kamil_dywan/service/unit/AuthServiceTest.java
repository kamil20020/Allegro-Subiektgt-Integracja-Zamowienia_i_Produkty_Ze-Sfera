package pl.kamil_dywan.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.BasicAuthApi;
import pl.kamil_dywan.api.allegro.BearerAuthApi;
import pl.kamil_dywan.api.allegro.LoginApi;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.auth.AccessTokenResponse;
import pl.kamil_dywan.external.allegro.generated.auth.GenerateDeviceCodeResponse;
import pl.kamil_dywan.external.allegro.own.EncryptedAllegroLoginDetails;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.service.AuthService;
import pl.kamil_dywan.service.SecureStorage;
import pl.kamil_dywan.service.SecurityService;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private LoginApi loginApi;

    @ParameterizedTest
    @CsvSource(value = {
        "true, true",
        "false, false",
    })
    void shouldGetIsUserLogged(boolean expectedResult, boolean bearerResult) {

        try(
            MockedStatic<BearerAuthApi> bearerAuthApiMocked = Mockito.mockStatic(BearerAuthApi.class);
        ){

            bearerAuthApiMocked.when(() -> BearerAuthApi.isUserLogged()).thenReturn(bearerResult);

            boolean gotResult = authService.isUserLogged();

            assertEquals(expectedResult, gotResult);

            bearerAuthApiMocked.verify(() -> BearerAuthApi.isUserLogged());
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "true, true",
        "false, false",
    })
    void shouldGetDoesUserPassedFirstLoginToApp(boolean expectedResult, boolean secureStorageValue) {

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(secureStorageValue);

            boolean gotResult = authService.doesUserPassedFirstLoginToApp();

            assertEquals(expectedResult, gotResult);

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
        }
    }

    @Test
    public void shouldInitWhenItIsFirstLoginOfTheUser() throws URISyntaxException, IOException {

        FileReader<EncryptedAllegroLoginDetails> fileReaderMock = Mockito.mock(FileReader.class);

        TestUtils.updatePrivateStaticField(AuthService.class, "allegroLoginDetailsFileReader", fileReaderMock);

        String authDataPath = "./auth-data.json";

        EncryptedAllegroLoginDetails expectedEncryptedAllegroLoginDetails = new EncryptedAllegroLoginDetails(
            "key",
            "key hash",
            "secret"
        );

        Mockito.when(fileReaderMock.loadFromOutside(any())).thenReturn(expectedEncryptedAllegroLoginDetails);

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(false);

            authService.init();

            EncryptedAllegroLoginDetails assignedEncryptedAllegroLoginDetails = TestUtils.getPrivateStaticField(
                AuthService.class,
                "encryptedAllegroLoginDetails",
                EncryptedAllegroLoginDetails.class
            );

            assertEquals(expectedEncryptedAllegroLoginDetails, assignedEncryptedAllegroLoginDetails);

            boolean didSkipRemovingAuthData = new File(authDataPath).exists();

            assertFalse(didSkipRemovingAuthData);

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
        }

        Mockito.verify(fileReaderMock).loadFromOutside(authDataPath);
    }

    @Test
    public void shouldInitWhenUserWasLoggedInForTheFirstTime(){

        String authDataPath = "./auth-data.json";

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(true);

            authService.init();

            EncryptedAllegroLoginDetails assignedEncryptedAllegroLoginDetails = TestUtils.getPrivateStaticField(
                AuthService.class,
                "encryptedAllegroLoginDetails",
                EncryptedAllegroLoginDetails.class
            );

            assertNull(assignedEncryptedAllegroLoginDetails);

            boolean didSkipRemovingAuthData = new File(authDataPath).exists();

            assertFalse(didSkipRemovingAuthData);

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
        }
    }

    @Test
    void shouldInitAllegroSecret(){

        String gotPassword = "1234568901234567";

        String encryptedAesKey = "encrypted key";

        EncryptedAllegroLoginDetails expectedEncryptedAllegroLoginDetails = new EncryptedAllegroLoginDetails(
            "ZW5jcnlwdGVkIGtleQ==",
            "key hash",
            "ZW5jcnlwdGVkIHNlY3JldA=="
        );

        byte[] encryptedKey = expectedEncryptedAllegroLoginDetails.getKey().getBytes();
        byte[] encryptedSecret = "encrypted secret".getBytes();

        TestUtils.updatePrivateStaticField(AuthService.class, "encryptedAllegroLoginDetails", expectedEncryptedAllegroLoginDetails);

        byte[] expectedGotDecryptedAes = "got decrypted aes".getBytes();
        byte[] expectedGotDecryptedAesHashArr = expectedEncryptedAllegroLoginDetails.getKeyHash().getBytes(StandardCharsets.UTF_8);
        String expectedGotAllegroSecret = "got allegro secret";

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
            MockedStatic<SecurityService> securityServiceMock = Mockito.mockStatic(SecurityService.class);
            MockedStatic<BasicAuthApi> basicAuthApiMock = Mockito.mockStatic(BasicAuthApi.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(false);
            securityServiceMock.when(() -> SecurityService.decryptAes(any(), aryEq(encryptedAesKey.getBytes()))).thenReturn(expectedGotDecryptedAes);
            securityServiceMock.when(() -> SecurityService.hashSha(any())).thenReturn(expectedGotDecryptedAesHashArr);
            securityServiceMock.when(() -> SecurityService.decryptAes(aryEq(expectedGotDecryptedAes), any())).thenReturn(expectedGotAllegroSecret.getBytes());

            authService.initAllegroSecret(gotPassword);

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
            securityServiceMock.verify(() -> SecurityService.decryptAes(gotPassword.getBytes(), encryptedAesKey.getBytes()));
            securityServiceMock.verify(() -> SecurityService.hashSha(expectedGotDecryptedAes));
            securityServiceMock.verify(() -> SecurityService.decryptAes(expectedGotDecryptedAes, encryptedSecret));
            secureStorageMock.verify(() -> SecureStorage.saveCredentials(BasicAuthApi.ALLEGRO_SECRET_POSTFIX, expectedGotAllegroSecret));
            basicAuthApiMock.verify(() -> BasicAuthApi.init());
        }
    }

    @Test
    void shouldNotInitAllegroSecretWhenGotNoPassword() {

        assertThrows(
            IllegalStateException.class,
            () -> authService.initAllegroSecret(null)
        );
    }

    @Test
    void shouldNotInitAllegroSecretWhenUserPassedFirstLogin() {

        String gotPassword = "kamil-nowak";

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(true);

            assertThrows(
                IllegalStateException.class,
                () -> authService.initAllegroSecret(gotPassword)
            );

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
        }
    }

    @Test
    void shouldNotInitAllegroSecretWhenUserGaveWrongPassword(){

        String gotPassword = "1234568901234567";

        String encryptedAesKey = "encrypted key";

        EncryptedAllegroLoginDetails expectedEncryptedAllegroLoginDetails = new EncryptedAllegroLoginDetails(
            "ZW5jcnlwdGVkIGtleQ==",
            "key hash",
            "encrypted secret"
        );

        TestUtils.updatePrivateStaticField(AuthService.class, "encryptedAllegroLoginDetails", expectedEncryptedAllegroLoginDetails);

        try(
            MockedStatic<SecureStorage> secureStorageMock = Mockito.mockStatic(SecureStorage.class);
            MockedStatic<SecurityService> securityServiceMock = Mockito.mockStatic(SecurityService.class);
        ){

            secureStorageMock.when(() -> SecureStorage.doesExist(any())).thenReturn(false);
            securityServiceMock.when(() -> SecurityService.decryptAes(any(), any())).thenThrow(BadPaddingException.class);

            assertThrows(
                UnloggedException.class,
                () -> authService.initAllegroSecret(gotPassword)
            );

            secureStorageMock.verify(() -> SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX));
            securityServiceMock.verify(() -> SecurityService.decryptAes(gotPassword.getBytes(), encryptedAesKey.getBytes()));
        }
    }

    @Test
    void shouldGenerateDeviceCodeAndVerificationToAllegro() {

        String expectedBody = "expected body";

        HttpResponse<String> expectedResponse = TestHttpResponse.builder()
            .statusCode(200)
            .body(expectedBody)
            .build();

        GenerateDeviceCodeResponse expectedGenerateDeviceCodeResponse = new GenerateDeviceCodeResponse(
            "device code",
            "user code",
            "verification uri",
            "verification uri complete",
            1,
            2
        );

        Mockito.when(loginApi.generateDeviceCodeAndVerification()).thenReturn(expectedResponse);

        try(
            MockedStatic<Api> mockedApi = Mockito.mockStatic(Api.class);
        ){

            mockedApi.when(() -> Api.extractBody(any(HttpResponse.class), any())).thenReturn(expectedGenerateDeviceCodeResponse);

            GenerateDeviceCodeResponse gotResponseContent = authService.generateDeviceCodeAndVerificationToAllegro();

            assertEquals(expectedGenerateDeviceCodeResponse, gotResponseContent);

            mockedApi.verify(() -> Api.extractBody(expectedResponse, GenerateDeviceCodeResponse.class));
        }

        Mockito.verify(loginApi).generateDeviceCodeAndVerification();
    }

    @Test
    void shouldNotGenerateDeviceCodeAndVerificationToAllegroWhenResponseWasNot200() {

        HttpResponse<String> expectedResponse = TestHttpResponse.builder()
            .statusCode(404)
            .build();

        Mockito.when(loginApi.generateDeviceCodeAndVerification()).thenReturn(expectedResponse);

        assertThrows(
            IllegalStateException.class,
            () -> authService.generateDeviceCodeAndVerificationToAllegro()
        );

        Mockito.verify(loginApi).generateDeviceCodeAndVerification();
    }

    @Test
    void shouldLoginToAllegro() {

        String deviceCode = "device code";

        HttpResponse<String> expectedResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        String expectedAccessToken = "access token";
        String expectedRefreshToken = "refresh token";

        AccessTokenResponse expectedAccessTokenResponse = new AccessTokenResponse(
            expectedAccessToken,
            expectedRefreshToken
        );

        Mockito.when(loginApi.generateAccessToken(deviceCode)).thenReturn(expectedResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
            MockedStatic<BearerAuthApi> bearerAuthApiMock = Mockito.mockStatic(BearerAuthApi.class);
        ){

            apiMock.when(() -> Api.extractBody(any(HttpResponse.class), any())).thenReturn(expectedAccessTokenResponse);

            authService.loginToAllegro(deviceCode);

            apiMock.verify(() -> Api.extractBody(expectedResponse, AccessTokenResponse.class));
            bearerAuthApiMock.verify(() -> BearerAuthApi.saveAuthData(expectedAccessToken, expectedRefreshToken));
        }

        Mockito.verify(loginApi).generateAccessToken(deviceCode);
    }

    @Test
    void shouldLogout() {

        try(
            MockedStatic<BearerAuthApi> bearerAuthApiMock = Mockito.mockStatic(BearerAuthApi.class);
        ){

            authService.logout();

            bearerAuthApiMock.verify(() -> BearerAuthApi.logout());
        }
    }
}