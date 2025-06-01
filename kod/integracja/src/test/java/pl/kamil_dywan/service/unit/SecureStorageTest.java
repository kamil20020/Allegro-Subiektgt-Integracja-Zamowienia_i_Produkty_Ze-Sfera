package pl.kamil_dywan.service.unit;

import com.microsoft.credentialstorage.SecretStore;
import com.microsoft.credentialstorage.model.StoredCredential;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.service.AppProperties;
import pl.kamil_dywan.service.SecureStorage;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@Disabled
class SecureStorageTest {

    private static final SecretStore<StoredCredential> secretStoreMock = Mockito.mock(SecretStore.class);

    private static final String expectedKeyPrefix = "prefix";
    private static final String expectedKeyPostfix = "key";
    private static final String expectedKey = "prefix-key";
    private static final String expectedUsername = "username";
    private static final String expectedPassword = "password";

    @BeforeAll
    public static void setUp(){

        TestUtils.updatePrivateStaticField(SecureStorage.class, "store", secretStoreMock);
    }

    private static void setUpCredentialsKeyPrefix(String credentialsKeyPrefix){

        TestUtils.updatePrivateStaticField(SecureStorage.class, "CREDENTIALS_KEY_PREFIX", credentialsKeyPrefix + "-");
    }

    @Test
    void shouldLoad(){

        String expectedCredentialsPrefix = "expected credentials prefix";
        String expectedCredentialsPrefixKey = "secure-store.credentials-key";
        String expectedValue = expectedCredentialsPrefix + "-";

        try(
            MockedStatic<AppProperties> appPropertiesMock = Mockito.mockStatic(AppProperties.class);
        ){

            appPropertiesMock.when(() -> AppProperties.getProperty(any())).thenReturn(expectedCredentialsPrefix);

            SecureStorage.load();

            String gotValue = TestUtils.getPrivateStaticField(SecureStorage.class, "CREDENTIALS_KEY_PREFIX", String.class);

            assertEquals(expectedValue, gotValue);

            appPropertiesMock.verify(() -> AppProperties.getProperty(expectedCredentialsPrefixKey));
        }
    }

    @Test
    void shouldGetDoesExistWhenExist(){

        StoredCredential expectedValue = new StoredCredential("", new char[0]);

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        Mockito.when(secretStoreMock.get(any())).thenReturn(expectedValue);

        boolean doestExist = SecureStorage.doesExist(expectedKeyPostfix);

        assertTrue(doestExist);

        Mockito.verify(secretStoreMock).get(expectedKey);
    }

    @Test
    void shouldNotGetDoesExistWhenDoesNotExist(){

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        Mockito.when(secretStoreMock.get(any())).thenReturn(null);

        boolean doestExist = SecureStorage.doesExist(expectedKeyPostfix);

        assertFalse(doestExist);

        Mockito.verify(secretStoreMock).get(expectedKey);
    }

    @Test
    void shouldSaveCredentials(){

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        SecureStorage.saveCredentials(expectedKeyPostfix, expectedUsername, expectedPassword);

        ArgumentCaptor<StoredCredential> storedCredentialCaptor = ArgumentCaptor.forClass(StoredCredential.class);

        Mockito.verify(secretStoreMock).add(eq(expectedKey), storedCredentialCaptor.capture());

        StoredCredential gotStoredCredentials = storedCredentialCaptor.getValue();

        assertEquals(expectedUsername, gotStoredCredentials.getUsername());
        assertArrayEquals(expectedPassword.toCharArray(), gotStoredCredentials.getPassword());
    }

    @Test
    void shouldSaveCredentialsPassword(){

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        SecureStorage.saveCredentials(expectedKeyPostfix, expectedPassword);

        ArgumentCaptor<StoredCredential> storedCredentialCaptor = ArgumentCaptor.forClass(StoredCredential.class);

        Mockito.verify(secretStoreMock).add(eq(expectedKey), storedCredentialCaptor.capture());

        StoredCredential gotStoredCredentials = storedCredentialCaptor.getValue();

        assertEquals(expectedKeyPostfix, gotStoredCredentials.getUsername());
        assertArrayEquals(expectedPassword.toCharArray(), gotStoredCredentials.getPassword());
    }

    @Test
    void shouldGetCredentialsPassword(){

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        StoredCredential expectedStoredCredential = new StoredCredential(
            "username",
            expectedPassword.toCharArray()
        );

        Mockito.when(secretStoreMock.get(any())).thenReturn(expectedStoredCredential);

        String gotPassword = SecureStorage.getCredentialsPassword(expectedKeyPostfix);

        assertEquals(expectedPassword, gotPassword);

        Mockito.verify(secretStoreMock).get(expectedKey);
    }

    @Test
    void shouldNotGetCredentialsPasswordWhenKeyPostfixDoestNotExist(){

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        Mockito.when(secretStoreMock.get(any())).thenReturn(null);

        assertThrows(
            IllegalArgumentException.class,
            () -> SecureStorage.getCredentialsPassword(expectedKeyPostfix)
        );

        Mockito.verify(secretStoreMock).get(expectedKey);
    }

    @Test
    void shouldDelete() {

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        StoredCredential expectedStoredCredential = new StoredCredential(
            expectedUsername,
            expectedPassword.toCharArray()
        );

        Mockito.when(secretStoreMock.get(any())).thenReturn(expectedStoredCredential);
        Mockito.when(secretStoreMock.delete(any())).thenReturn(true);

        boolean wasDeleted = SecureStorage.delete(expectedKeyPostfix);

        assertTrue(wasDeleted);

        Mockito.verify(secretStoreMock).get(expectedKey);
        Mockito.verify(secretStoreMock).delete(expectedKey);
    }

    @Test
    void shouldNotDeleteWhenKeyPostfixDoestNotExist() {

        setUpCredentialsKeyPrefix(expectedKeyPrefix);

        Mockito.when(secretStoreMock.get(any())).thenReturn(null);

        boolean wasDeleted = SecureStorage.delete(expectedKeyPostfix);

        assertFalse(wasDeleted);

        Mockito.verify(secretStoreMock).get(expectedKey);
    }
}