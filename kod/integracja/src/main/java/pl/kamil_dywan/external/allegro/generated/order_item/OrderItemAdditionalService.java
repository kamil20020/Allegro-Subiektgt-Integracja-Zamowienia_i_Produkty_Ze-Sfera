package pl.kamil_dywan.external.allegro.generated.order_item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import pl.kamil_dywan.external.allegro.generated.Cost;

import javax.annotation.processing.Generated;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "definitionId",
    "name",
    "price",
    "quantity",
})
@Generated("jsonschema2pojo")
public class OrderItemAdditionalService {

    @JsonProperty("definitionId")
    private String definitionId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Cost price;

    @JsonProperty("quantity")
    private Integer quantity;

}
