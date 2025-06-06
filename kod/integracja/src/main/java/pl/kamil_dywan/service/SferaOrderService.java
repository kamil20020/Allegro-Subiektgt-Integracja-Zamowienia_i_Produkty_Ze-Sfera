package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.request.GetDocumentByExternalIdRequest;
import pl.kamil_dywan.api.sfera.SferaOrderApi;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.api.sfera.request.GetOrderRequest;
import pl.kamil_dywan.api.sfera.response.CreatedDocumentResponse;
import pl.kamil_dywan.api.sfera.response.DocumentResponse;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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

    public int create(List<Order> orders) {

        int numberOfSavedOrders = 0;

        for (Order selectedOrder : orders) {

            if ((selectedOrder.getExternalId() != null && !selectedOrder.getExternalId().isEmpty()) || selectedOrder.isHasDocument()) {
                continue;
            }

            try {

                create(selectedOrder);

                numberOfSavedOrders++;

            }
            catch (IllegalStateException e) {

                e.printStackTrace();
            }
        }

        return numberOfSavedOrders;
    }

    public void create(Order order) throws IllegalStateException {

        CreateOrderRequest createOrderRequest = SferaOrderMapper.map(order);

        HttpResponse<String> gotResponse = sferaOrderApi.create(createOrderRequest);

        GeneralResponse gotGeneralResponse = handleResponseErrors(gotResponse);

        CreatedDocumentResponse createdDocumentResponse = Api.extractBody(gotGeneralResponse.getData(), CreatedDocumentResponse.class);

        String gotOrderExternalId = createdDocumentResponse.getOrderExternalId();

        order.setExternalId(gotOrderExternalId);
    }

    public String getSubiektIdByExternalId(String orderExternalId) throws IllegalStateException{

        GetDocumentByExternalIdRequest request = new GetDocumentByExternalIdRequest(orderExternalId);

        HttpResponse<String> gotResponse = sferaOrderApi.getSubiektIdByExternalId(request);

        GeneralResponse generalResponse = handleResponseErrors(gotResponse);

        String gotSubiektId = generalResponse.getData();

        if(gotSubiektId.equals("null")){

            gotSubiektId = null;
        }

        return gotSubiektId;
    }

    public List<byte[]> getContents(List<Order> orders) throws IllegalStateException{

        List<byte[]> contents = new ArrayList<>();

        for (Order selectedOrder : orders) {

            String selectedOrderExternalId = selectedOrder.getExternalId();

            byte[] documentContent;

            if (selectedOrderExternalId != null && !selectedOrderExternalId.isEmpty()) {

                documentContent = getDocumentContent(selectedOrderExternalId);
            }
            else {
                documentContent = new byte[0];
            }

            contents.add(documentContent);
        }

        return contents;
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
