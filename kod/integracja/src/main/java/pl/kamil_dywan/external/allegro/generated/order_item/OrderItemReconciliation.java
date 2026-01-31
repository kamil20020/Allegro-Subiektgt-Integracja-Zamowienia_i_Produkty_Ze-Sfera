
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.Cost;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "value",
    "type",
    "quantity"
})
@Generated("jsonschema2pojo")
public class OrderItemReconciliation {

    @JsonProperty("value")
    private Cost value;

    @JsonProperty("type")
    private String type;

    @JsonProperty("quantity")
    private Integer quantity;

}
