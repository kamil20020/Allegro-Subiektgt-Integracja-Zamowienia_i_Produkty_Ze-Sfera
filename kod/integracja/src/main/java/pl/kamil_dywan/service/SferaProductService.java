package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.sfera.request.ExistsSymbolRequest;
import pl.kamil_dywan.api.sfera.SferaProductApi;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.external.allegro.own.offer.Signature;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;

import java.net.http.HttpResponse;

public class SferaProductService {

    private final SferaProductApi sferaProductApi;

    public SferaProductService(SferaProductApi sferaProductApi){

        this.sferaProductApi = sferaProductApi;
    }

    private GeneralResponse handleResponseErrors(HttpResponse<String> gotResponse) throws IllegalStateException{

        GeneralResponse generalResponse = Api.extractBody(gotResponse, GeneralResponse.class);

        if(generalResponse.getStatus().equals(ResponseStatus.ERROR.toString())){

            ErrorResponse errorResponse = Api.extractBody(gotResponse, ErrorResponse.class);

            throw new IllegalStateException(errorResponse.getMessage());
        }

        return generalResponse;
    }

    public boolean existsByCode(String code) throws IllegalStateException{

        if(code == null || code.isBlank()){
            return false;
        }

        Signature extractedSignature = Signature.extract(code);

        if(extractedSignature == null){
            return false;
        }

        String extractedSubiektSymbol = extractedSignature.subiektSymbol();
        ExistsSymbolRequest request = new ExistsSymbolRequest(extractedSubiektSymbol);

        HttpResponse<String> gotResponse = sferaProductApi.existsSubiektId(request);

        GeneralResponse generalResponse = handleResponseErrors(gotResponse);

        String gotSubiektId = generalResponse.getData();

        return gotSubiektId.equals("true");
    }

}
