package pl.kamil_dywan.api.sfera;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.kamil_dywan.api.allegro.request.GetProductByCodeAndEanRequest;
import pl.kamil_dywan.api.sfera.request.GeneralRequest;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SferaProductApi extends SferaApi{

    public SferaProductApi() {

        super("product");
    }

    public HttpResponse<String> getSubiektIdByCodeAndEan(GetProductByCodeAndEanRequest request){

        GeneralRequest generalRequest = createGeneralRequest(request);

        String requestStr = "";

        try {
            requestStr = objectMapper.writeValueAsString(generalRequest);
        }
        catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(requestStr))
            .uri(URI.create(API_PREFIX + "/getSymbolByCodeOrEan"))
            .header("Content-Type", "application/json")
            .header("Accept", "application/pdf");

        return send(httpRequestBuilder);
    }
}
