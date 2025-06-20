package pl.kamil_dywan.api.allegro;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceRequest;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.own.order.OrderStatus;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OrderApi extends BearerAuthApi {

    public OrderApi(){

        super("api", "/order/checkout-forms");
    }

    public HttpResponse<String> getOrders(int offset, int limit) throws IllegalStateException, UnloggedException {

        String offsetStr = String.valueOf(offset);
        String limitStr = String.valueOf(limit);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(API_PREFIX + getQueryParamsPostFix(
                    "offset", offsetStr,
                    "limit", limitStr,
                    "status", OrderStatus.READY_FOR_PROCESSING.toString())
                )
            )
            .header("Accept", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> createDocument(String orderId, CreateOrderInvoiceRequest createOrderInvoiceRequest) throws IllegalStateException{

        String content = handleMapRequestToString(createOrderInvoiceRequest);

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(content);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .POST(bodyPublisher)
            .uri(
                URI.create(API_PREFIX + "/" + orderId + "/invoices")
            )
            .header("Accept", "application/vnd.allegro.public.v1+json")
            .header("Content-Type", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> saveDocument(String orderId, String documentId, byte[] data){

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(data);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .PUT(bodyPublisher)
            .uri(
                URI.create(API_PREFIX + "/" + orderId + "/invoices/" + documentId + "/file")
            )
            .header("Accept", "application/vnd.allegro.public.v1+json")
            .header("Content-Type", "application/pdf");

        return send(httpRequestBuilder);
    }
    
    public HttpResponse<String> getDocuments(String orderId){

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(
                URI.create(API_PREFIX + "/" + orderId + "/invoices")
            )
            .header("Accept", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }
}
