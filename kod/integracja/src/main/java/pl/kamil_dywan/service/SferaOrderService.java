package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.api.sfera.SferaOrderApi;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.external.allegro.generated.order.Order;

import java.net.http.HttpResponse;
import java.util.List;

public class SferaOrderService {

    private final SferaOrderApi sferaOrderApi;

    public SferaOrderService(SferaOrderApi sferaOrderApi){

        this.sferaOrderApi = sferaOrderApi;
    }

    public void create(CreateOrderRequest createOrderRequest) throws IllegalStateException {

        HttpResponse<String> gotResponse = sferaOrderApi.create(createOrderRequest);

        if(gotResponse.statusCode() != 200){

            throw new IllegalStateException(gotResponse.body());
        }
    }
}
