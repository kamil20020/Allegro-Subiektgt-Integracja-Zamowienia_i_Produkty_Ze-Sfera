package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.sfera.request.GetDocumentByExternalIdRequest;
import pl.kamil_dywan.api.sfera.SferaOrderApi;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.api.sfera.request.GetOrderRequest;
import pl.kamil_dywan.api.sfera.response.CreatedDocumentResponse;
import pl.kamil_dywan.api.sfera.response.DocumentResponse;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.exception.ConflictException;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.net.http.HttpResponse;
import java.util.*;

public class SferaOrderService {

    private final SferaOrderApi sferaOrderApi;
    private final SferaProductService sferaProductService;

    public SferaOrderService(SferaOrderApi sferaOrderApi, SferaProductService sferaProductService){

        this.sferaOrderApi = sferaOrderApi;
        this.sferaProductService = sferaProductService;
    }

    private GeneralResponse handleResponseErrors(HttpResponse<String> gotResponse) throws IllegalStateException{

        GeneralResponse generalResponse = Api.extractBody(gotResponse, GeneralResponse.class);

        if(generalResponse.getStatus().equals(ResponseStatus.ERROR.toString())){

            ErrorResponse errorResponse = Api.extractBody(gotResponse, ErrorResponse.class);

            throw new IllegalStateException(errorResponse.getMessage());
        }

        return generalResponse;
    }

    public int create(List<Order> orders, Map<String, String> errors) {

        int numberOfSavedOrders = 0;

        for (Order selectedOrder : orders) {

            try {

                create(selectedOrder);

                numberOfSavedOrders++;

            }
            catch (IllegalStateException | ConflictException e) {

                String orderIdStr = selectedOrder.getId().toString();

                errors.put(orderIdStr, e.getMessage());

                e.printStackTrace();
            }
        }

        return numberOfSavedOrders;
    }

    public void create(Order order) throws ConflictException, IllegalStateException {

        if (order.getExternalId() != null && !order.getExternalId().isEmpty()){

            throw new ConflictException("Istnieje już obiekt w Subiekcie");
        }

        validateExistsProductsInSubiekt(order);

        if (order.isHasDocument()) {

            throw new ConflictException("W Allegro przypisano już dokument potwierdzający sprzedaż");
        }

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

    public void validateExistsProductsInSubiekt(Order order) throws IllegalStateException{

        StringBuilder errorBuilder = new StringBuilder();

        for(OrderItem orderItem : order.getOrderItems()){

            Offer offer = orderItem.getOffer();
            ExternalId externalId = offer.getExternal();

            if(offer.getName().equals("Dostawa do klienta")){
                continue;
            }

            if(externalId == null){

                errorBuilder
                    .append("Oferta ")
                    .append(offer.getName())
                    .append(" nie ma przypisanej sygnatury")
                    .append(System.lineSeparator());

                continue;
            }

            String externalIdValue = externalId.getId();

            boolean doesExist = sferaProductService.existsByCode(externalIdValue);

            if(doesExist){
                continue;
            }

            errorBuilder
                .append("Oferta ")
                .append(offer.getName())
                .append(" ma przypisaną niepoprawną sygnaturę ")
                .append(externalIdValue)
                .append(System.lineSeparator());

        }

        if(!errorBuilder.isEmpty()){

            throw new IllegalStateException(errorBuilder.toString());
        }
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
