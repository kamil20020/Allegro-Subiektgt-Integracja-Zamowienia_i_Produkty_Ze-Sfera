
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "name",
    "external",
    "productSet"
})
@Generated("jsonschema2pojo")
public class Offer {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("external")
    private ExternalId external;

    @JsonProperty("productSet")
    private OrderProductSet productSet;

    public Offer(String name){

        this.name = name;
    }

}
