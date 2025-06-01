
package pl.kamil_dywan.external.allegro.generated.order_item;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "products"
})
@Generated("jsonschema2pojo")
public class OrderProductSet {

    @JsonProperty("products")
    private List<OrderProduct> products = new ArrayList<>();

}
