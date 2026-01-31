package pl.kamil_dywan.api.allegro.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import pl.kamil_dywan.external.allegro.generated.order.Order;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "checkoutForms",
    "count",
    "totalCount",
})
@Generated("jsonschema2pojo")
public class OrderResponse {

    @JsonProperty("checkoutForms")
    private List<Order> orders = new ArrayList<>();

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("totalCount")
    private Integer totalCount;

}
