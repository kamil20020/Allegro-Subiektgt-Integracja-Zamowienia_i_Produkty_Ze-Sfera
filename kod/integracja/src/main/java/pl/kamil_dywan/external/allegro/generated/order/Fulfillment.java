package pl.kamil_dywan.external.allegro.generated.order;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.order.FulFillmentStatus;

import javax.annotation.processing.Generated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "status",
    "shipmentSummary"
})
@Generated("jsonschema2pojo")
public class Fulfillment {

    @JsonProperty("status")
    private FulFillmentStatus status;

    @JsonProperty("shipmentSummary")
    private ShipmentSummary shipmentSummary;

}
