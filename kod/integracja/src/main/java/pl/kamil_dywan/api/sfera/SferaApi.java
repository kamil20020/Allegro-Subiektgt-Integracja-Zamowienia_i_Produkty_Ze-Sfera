package pl.kamil_dywan.api.sfera;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.sfera.request.GeneralRequest;
import pl.kamil_dywan.mapper.Base64Mapper;
import pl.kamil_dywan.service.AppProperties;
import pl.kamil_dywan.service.SecureStorage;

public abstract class SferaApi extends Api {

    private static String apiKey;

    private static final String HOST_KEY = "sfera.api.host";

    public static final String SFERA_SECRET_POSTFIX = "sfera.secret";

    public SferaApi(String subDomain, String laterPrefix) {

        super(subDomain, Api.getQueryParamsPostFix("c", laterPrefix), HOST_KEY, "http");
    }

    public SferaApi(String laterPrefix) {

        this(null, laterPrefix);
    }

    public static void init(){

        if(!SecureStorage.doesExist(SFERA_SECRET_POSTFIX)){
            return;
        }

        apiKey = SecureStorage.getCredentialsPassword(SFERA_SECRET_POSTFIX);
    }

    protected static GeneralRequest createGeneralRequest(Object data){

        return new GeneralRequest("l6cw8lrjmntwa9muu63ynkpoywt2wp", data);
    }

}
