
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id"
})
@Generated("jsonschema2pojo")
public class ExternalId {

    @JsonProperty("id")
    private String id;

}
