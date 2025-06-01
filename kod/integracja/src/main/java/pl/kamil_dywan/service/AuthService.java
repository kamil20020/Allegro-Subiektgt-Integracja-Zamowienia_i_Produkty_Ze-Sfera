package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.BasicAuthApi;
import pl.kamil_dywan.api.allegro.BearerAuthApi;
import pl.kamil_dywan.api.allegro.LoginApi;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.auth.AccessTokenResponse;
import pl.kamil_dywan.external.allegro.generated.auth.GenerateDeviceCodeResponse;
import pl.kamil_dywan.external.allegro.own.EncryptedAllegroLoginDetails;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthService {

    private final LoginApi loginApi;

    private static EncryptedAllegroLoginDetails encryptedAllegroLoginDetails;

    private static final FileReader<EncryptedAllegroLoginDetails> allegroLoginDetailsFileReader = new JSONFileReader<>(EncryptedAllegroLoginDetails.class);

    public AuthService(LoginApi loginApi){

        this.loginApi = loginApi;
    }

    public boolean isUserLogged(){

        return BearerAuthApi.isUserLogged();
    }

    public boolean doesUserPassedFirstLoginToApp(){

        return SecureStorage.doesExist(BasicAuthApi.ALLEGRO_SECRET_POSTFIX);
    }

    public void init() throws IllegalStateException{

        if(doesUserPassedFirstLoginToApp()){
            return;
        }

        try {
            encryptedAllegroLoginDetails = allegroLoginDetailsFileReader.loadFromOutside("./auth-data.json");
        }
        catch (URISyntaxException | IOException e) {

            e.printStackTrace();

            throw new IllegalStateException(e.getMessage());
        }

        new File("./auth-data.json").delete();
    }

    public void initAllegroSecret(String gotPassword) throws IllegalStateException, UnloggedException{

        if(gotPassword == null){

            throw new IllegalStateException("User is already logged in");
        }

        if(doesUserPassedFirstLoginToApp()){

            throw new IllegalStateException("The user was already logged in to the app for the first time");
        }

        byte[] base64EncryptedAes = encryptedAllegroLoginDetails.getKey().getBytes();
        byte[] decodedEncryptedAes = Base64.getDecoder().decode(base64EncryptedAes);
        String expectedAesHash = encryptedAllegroLoginDetails.getKeyHash();
        String base64EncryptedSecret = encryptedAllegroLoginDetails.getSecret();

        byte[] gotDecryptedAes = decryptAesWithUserPassword(gotPassword, decodedEncryptedAes);
        String gotDecryptedAesHash = hashValue(gotDecryptedAes);

        if(!gotDecryptedAesHash.equals(expectedAesHash)){

            throw new UnloggedException();
        }

        byte[] gotAllegroSecret = decryptAllegroSecret(gotDecryptedAes, base64EncryptedSecret);

        SecureStorage.saveCredentials(BasicAuthApi.ALLEGRO_SECRET_POSTFIX, new String(gotAllegroSecret));

        BasicAuthApi.init();
    }

    private byte[] decryptAesWithUserPassword(String gotPassword, byte[] base64EncryptedAes) throws UnloggedException, IllegalStateException{

        byte[] gotPasswordBytes = gotPassword.getBytes();

        byte[] targetGotPasswordBytes = new byte[16];

        System.arraycopy(gotPasswordBytes, 0, targetGotPasswordBytes, 0, Math.min(gotPasswordBytes.length, targetGotPasswordBytes.length));

        try {
            return SecurityService.decryptAes(targetGotPasswordBytes, base64EncryptedAes);
        }
        catch (BadPaddingException e){

            throw new UnloggedException();
        }
        catch (Exception e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not decrypt with user password");
        }
    }

    private String hashValue(byte[] value) throws IllegalStateException{

        byte[] gotHashValue;

        try {
            gotHashValue = SecurityService.hashSha(value);
        }
        catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

            throw new IllegalStateException("Did not found hash algorithm");
        }

        return new String(gotHashValue);
    }

    private byte[] decryptAllegroSecret(byte[] decryptedAes, String base64EncryptedSecret) throws IllegalStateException{

        byte[] decodedEncryptedSecret = Base64.getDecoder().decode(base64EncryptedSecret);

        try {
            return SecurityService.decryptAes(decryptedAes, decodedEncryptedSecret);
        }
        catch (Exception e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not decrypt allegro secret");
        }
    }

    public GenerateDeviceCodeResponse generateDeviceCodeAndVerificationToAllegro() throws IllegalStateException{

        HttpResponse<String> deviceCodeResponse = loginApi.generateDeviceCodeAndVerification();

        if(deviceCodeResponse.statusCode() != 200){
            throw new IllegalStateException("Could not generate device code");
        }

        return Api.extractBody(deviceCodeResponse, GenerateDeviceCodeResponse.class);
    }

    public void loginToAllegro(String deviceCode) throws IllegalStateException{

        HttpResponse<String> accessTokenResponse = loginApi.generateAccessToken(deviceCode);

        if(accessTokenResponse.statusCode() != 200){
            throw new IllegalStateException("User did not authorized the device code");
        }

        AccessTokenResponse accessTokenContent = Api.extractBody(accessTokenResponse, AccessTokenResponse.class);

        String accessToken = accessTokenContent.getAccessToken();
        String refreshToken = accessTokenContent.getRefreshToken();

        BearerAuthApi.saveAuthData(accessToken, refreshToken);
    }

    public void logout(){

        BearerAuthApi.logout();
    }
}
