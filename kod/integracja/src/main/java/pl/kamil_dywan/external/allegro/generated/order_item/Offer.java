
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProduct;
import pl.kamil_dywan.external.allegro.own.offer.Signature;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "name",
    "external",
    "productSet"
})
@Generated("jsonschema2pojo")
public class Offer {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("external")
    private ExternalId external;

    @JsonProperty("productSet")
    private OrderProductSet productSet;

    @JsonIgnore
    private Signature signature;

    @JsonIgnore
    public Offer(String name){

        this.name = name;
    }

    @JsonIgnore
    public boolean hasSingleProduct(){

        return hasProducts() && productSet.getProducts().size() == 1;
    }

    @JsonIgnore
    public boolean hasManyProducts(){

        return hasProducts() && productSet.getProducts().size() > 1;
    }

    @JsonIgnore
    public boolean hasProducts(){

        if(productSet == null || productSet == null){

            return false;
        }

        List<OrderProduct> orderProducts = productSet.getProducts();

        return orderProducts != null && !orderProducts.isEmpty();
    }

    @JsonIgnore
    public void validateSignature() throws IllegalStateException{

        if(productSet == null){
            return;
        }

        List<OrderProduct> offerProducts = productSet.getProducts();

        if(offerProducts == null || offerProducts.isEmpty()){
            return;
        }

        int numberOfSignatureItems = signature.signatureItems().size();
        int numberOfProducts = offerProducts.size();

        if(numberOfSignatureItems != numberOfProducts) {

            throw new IllegalStateException("Liczba produktów z sygnatury oferty różni się z liczbą produktów w ofercie");
        }
    }

}
