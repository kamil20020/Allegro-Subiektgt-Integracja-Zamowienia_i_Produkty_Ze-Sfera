
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

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

}
