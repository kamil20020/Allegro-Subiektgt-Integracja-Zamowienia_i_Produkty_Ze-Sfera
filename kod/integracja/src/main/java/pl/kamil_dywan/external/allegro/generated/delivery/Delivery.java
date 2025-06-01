package pl.kamil_dywan.external.allegro.generated.delivery;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.Cost;

import javax.annotation.processing.Generated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "address",
    "method",
    "pickupPoint",
    "cost",
    "time",
    "smart",
    "cancellation",
    "cancellation",
    "calculatedNumberOfPackages"
})
@Generated("jsonschema2pojo")
public class Delivery {

    @JsonProperty("address")
    private DeliveryAddress address;

    @JsonProperty("method")
    private DeliveryMethod method;

    @JsonProperty("pickupPoint")
    private DeliveryPickupPoint pickupPoint;

    @JsonProperty("cost")
    private Cost cost;

    @JsonProperty("time")
    private DeliveryTime time;

    @JsonProperty("smart")
    private Boolean smart;

    @JsonProperty("cancellation")
    private DeliveryCancellation cancellation;

    @JsonProperty("calculatedNumberOfPackages")
    private Integer calculatedNumberOfPackages;

}
