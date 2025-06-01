package pl.kamil_dywan.external.allegro.generated.offer_product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "product",
    "quantity"
})
@Generated("jsonschema2pojo")
public class ProductOfferProductRelatedData {

    @JsonProperty("product")
    private ProductOfferProduct product;

    @JsonProperty("quantity")
    private ProductOfferProductRelatedDataQuantity quantity;

    public ProductOfferProductRelatedData(ProductOfferProduct product) {

        this.product = product;
    }
}