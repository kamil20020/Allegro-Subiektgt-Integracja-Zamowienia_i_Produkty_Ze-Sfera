package pl.kamil_dywan.api.allegro.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;

import javax.annotation.processing.Generated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "external"
})
@Generated("jsonschema2pojo")
public class PatchProductOfferRequest {

    @JsonProperty("external")
    private ExternalId externalId;

}
