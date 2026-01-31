package pl.kamil_dywan.external.allegro.generated.order;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "lineItemsSent"
})
@Generated("jsonschema2pojo")
public class ShipmentSummary {

    @JsonProperty("lineItemsSent")
    private String lineItemsSent;

}
