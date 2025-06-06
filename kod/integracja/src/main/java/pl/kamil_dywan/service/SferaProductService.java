package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.request.GetProductByCodeAndEanRequest;
import pl.kamil_dywan.api.sfera.SferaProductApi;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;

import java.net.http.HttpResponse;
import java.util.Optional;

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

    public String getSubiektIdByCodeOrEan(String code, String ean) throws IllegalStateException{

        GetProductByCodeAndEanRequest request = new GetProductByCodeAndEanRequest(code, ean);

        HttpResponse<String> gotResponse = sferaProductApi.getSubiektIdByCodeAndEan(request);

        GeneralResponse generalResponse = handleResponseErrors(gotResponse);

        String gotSubiektId = generalResponse.getData();

        if(gotSubiektId.equals("null")){

            gotSubiektId = null;
        }

        return gotSubiektId;
    }

}
