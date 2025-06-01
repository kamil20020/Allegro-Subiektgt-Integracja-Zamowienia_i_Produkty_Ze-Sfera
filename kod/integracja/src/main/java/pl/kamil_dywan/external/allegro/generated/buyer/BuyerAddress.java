package pl.kamil_dywan.external.allegro.generated.buyer;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.Country;

import javax.annotation.processing.Generated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "street",
    "city",
    "postCode",
    "countryCode"
})
@Generated("jsonschema2pojo")
public class BuyerAddress {

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;

    @JsonProperty("postCode")
    private String postCode;

    @JsonProperty("countryCode")
    private String countryCode;

}
