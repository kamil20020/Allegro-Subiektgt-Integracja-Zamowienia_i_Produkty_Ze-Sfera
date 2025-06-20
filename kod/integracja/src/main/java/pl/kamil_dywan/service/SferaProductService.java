package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.sfera.request.CreateProductsSetRequest;
import pl.kamil_dywan.api.sfera.request.GetProductByCodeAndEanRequest;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.api.sfera.SferaProductApi;
import pl.kamil_dywan.api.sfera.response.CreatedProductResponse;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;
import pl.kamil_dywan.mapper.sfera.SferaProductSetMapper;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

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

    public Integer saveProductsSets(List<ProductOfferResponse> allegroProductsSets) {

        Integer savedProductsSets = 0;

        for(ProductOfferResponse allegroProductSet : allegroProductsSets){

            try{

                saveProductsSet(allegroProductSet);

                savedProductsSets++;
            }
            catch(IllegalStateException e){

                e.printStackTrace();
            }
        }

        return savedProductsSets;
    }

    public void saveProductsSet(ProductOfferResponse allegroProductsSet) throws IllegalStateException{

        CreateProductsSetRequest request = SferaProductSetMapper.map(allegroProductsSet);

        HttpResponse<String> gotResponse = sferaProductApi.saveProductsSet(request);

        handleResponseErrors(gotResponse);
    }

}
