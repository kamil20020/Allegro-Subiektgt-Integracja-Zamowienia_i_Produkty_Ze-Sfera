package pl.kamil_dywan.api.allegro.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProduct;

import javax.annotation.processing.Generated;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "offers",
    "count",
    "totalCount"
})
@Generated("jsonschema2pojo")
public class OfferProductResponse {

    @JsonProperty("offers")
    private List<OfferProduct> offersProducts;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("totalCount")
    private Integer totalCount;

}
