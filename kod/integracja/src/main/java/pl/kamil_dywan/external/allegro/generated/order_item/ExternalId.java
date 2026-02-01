
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.offer.Signature;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id"
})
@Generated("jsonschema2pojo")
public class ExternalId {

    @JsonProperty("id")
    private String id;

    public ExternalId(Signature signature){

        id = signature.toString();
    }

}
