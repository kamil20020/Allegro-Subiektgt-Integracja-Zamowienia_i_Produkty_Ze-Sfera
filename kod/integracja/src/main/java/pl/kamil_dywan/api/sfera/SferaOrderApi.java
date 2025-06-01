package pl.kamil_dywan.api.sfera;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.api.sfera.request.GeneralRequest;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SferaOrderApi extends SferaApi{

    public SferaOrderApi() {

        super("order");
    }

    public HttpResponse<String> create(CreateOrderRequest createOrderRequest)  {

        GeneralRequest generalRequest = createGeneralRequest(createOrderRequest);

        String requestStr = "";

        try {
            requestStr = objectMapper.writeValueAsString(generalRequest);
        }
        catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(requestStr))
            .uri(URI.create(API_PREFIX + "/add"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json");

        return send(httpRequestBuilder);
    }
}
