package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.sfera.request.CreateProductsSetsRequest;
import pl.kamil_dywan.api.sfera.request.GetProductByCodeAndEanRequest;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.api.sfera.SferaProductApi;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.sfera.generated.ProductSet;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;
import pl.kamil_dywan.mapper.ProductOfferMapper;
import pl.kamil_dywan.mapper.sfera.SferaProductMapper;
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

    public void saveProductsSets(List<ProductOfferResponse> allegroProductsSets) throws IllegalStateException{

        List<ProductSet> productsSets = allegroProductsSets.stream()
            .map(product -> SferaProductSetMapper.map(product))
            .collect(Collectors.toList());

        CreateProductsSetsRequest request = new CreateProductsSetsRequest(productsSets);

        HttpResponse<String> gotResponse = sferaProductApi.saveProductsSets(request);

        handleResponseErrors(gotResponse);
    }

}
