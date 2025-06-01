package pl.kamil_dywan.external.allegro.generated.order;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.Cost;

import javax.annotation.processing.Generated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "totalToPay"
})
@Generated("jsonschema2pojo")
public class Summary {

    @JsonProperty("totalToPay")
    private Cost totalToPay;

}
