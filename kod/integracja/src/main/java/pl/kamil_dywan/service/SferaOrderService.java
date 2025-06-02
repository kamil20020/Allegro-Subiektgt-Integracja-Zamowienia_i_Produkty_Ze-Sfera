package pl.kamil_dywan.service;

import pl.kamil_dywan.App;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.api.sfera.SferaOrderApi;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.api.sfera.request.GetOrderRequest;
import pl.kamil_dywan.api.sfera.response.CreatedDocumentResponse;
import pl.kamil_dywan.api.sfera.response.DocumentResponse;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.mapper.Base64Mapper;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class SferaOrderService {

    private final SferaOrderApi sferaOrderApi;

    public SferaOrderService(SferaOrderApi sferaOrderApi){

        this.sferaOrderApi = sferaOrderApi;
    }

    private GeneralResponse handleResponseErrors(HttpResponse<String> gotResponse) throws IllegalStateException{

        GeneralResponse generalResponse = Api.extractBody(gotResponse, GeneralResponse.class);

        if(generalResponse.getStatus().equals(ResponseStatus.ERROR.toString())){

            ErrorResponse errorResponse = Api.extractBody(gotResponse, ErrorResponse.class);

            throw new IllegalStateException(errorResponse.getMessage());
        }

        return generalResponse;
    }

    public void create(Order order) throws IllegalStateException {

        CreateOrderRequest createOrderRequest = SferaOrderMapper.map(order);

        HttpResponse<String> gotResponse = sferaOrderApi.create(createOrderRequest);

        GeneralResponse gotGeneralResponse = handleResponseErrors(gotResponse);

        CreatedDocumentResponse createdDocumentResponse = Api.extractBody((String) gotGeneralResponse.getData(), CreatedDocumentResponse.class);

        String gotOrderExternalId = createdDocumentResponse.getOrderExternalId();

        order.setExternalId(gotOrderExternalId);
    }

    public CreateOrderRequest getDocument(String orderExternalId){

        if(orderExternalId == null){

            throw new IllegalStateException("Dokument nie ma ustawionego zewnętrznego id");
        }

        GetOrderRequest getOrderRequest = new GetOrderRequest(orderExternalId);

        HttpResponse<String> gotResponse = sferaOrderApi.getDocument(getOrderRequest);

        GeneralResponse generalResponse = handleResponseErrors(gotResponse);

        return Api.extractBody(generalResponse.getData(), CreateOrderRequest.class);
    }

    public byte[] getDocumentContent(String orderExternalId) throws IllegalStateException{

        if(orderExternalId == null){

            throw new IllegalStateException("Dokument nie ma ustawionego zewnętrznego id");
        }

        GetOrderRequest getOrderRequest = new GetOrderRequest(orderExternalId);

        HttpResponse<String> gotResponse = sferaOrderApi.getDocumentContent(getOrderRequest);

        GeneralResponse generalResponse = handleResponseErrors(gotResponse);

        DocumentResponse documentResponse = Api.extractBody(generalResponse.getData(), DocumentResponse.class);

        String base64Content = documentResponse.getContent();

        return Base64.getDecoder().decode(base64Content);
    }

}
