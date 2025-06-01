package pl.kamil_dywan.api.allegro;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.api.allegro.request.PatchProductOfferRequest;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.own.OfferProductStatus;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;

public class ProductApi extends BearerAuthApi {

    public ProductApi(){

        super("api", "/sale");
    }

    public HttpResponse<String> getOffersProducts(int offset, int limit) throws IllegalStateException, UnloggedException {

        String offsetStr = String.valueOf(offset);
        String limitStr = String.valueOf(limit);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(API_PREFIX +  "/offers" + getQueryParamsPostFix(
                    "offset", offsetStr,
                    "limit", limitStr,
                    "publication.status", OfferProductStatus.ACTIVE.toString()
                )
            ))
            .header("Accept", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> getProductOfferById(Long id) throws IllegalStateException, UnloggedException{

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(API_PREFIX + "/product-offers/" + id))
            .header("Accept", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> patchOfferExternalById(Long offerId, String externalValue){

        ExternalId externalId = new ExternalId(externalValue);

        PatchProductOfferRequest patchProductOfferRequest = new PatchProductOfferRequest(externalId);

        String content = "";

        try {
            content = objectMapper.writeValueAsString(patchProductOfferRequest);
        }
        catch (JsonProcessingException e) {

            e.printStackTrace();
        }

        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(content);

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .method("PATCH", bodyPublisher)
            .uri(URI.create(API_PREFIX + "/product-offers/" + offerId))
            .header("Accept", "application/vnd.allegro.public.v1+json")
            .header("Content-Type", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }

    public HttpResponse<String> updateExternalIdOfOffers(Long id, LinkedHashMap<Long, String> offersIdsAndExternalsMappings){

        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(API_PREFIX + "/product-offers/" + id))
            .header("Accept", "application/vnd.allegro.public.v1+json");

        return send(httpRequestBuilder);
    }
}
