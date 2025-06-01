package pl.kamil_dywan.external.allegro.generated.delivery;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.Country;

import javax.annotation.processing.Generated;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "street",
    "zipCode",
    "city",
    "countryCode"
})
@Generated("jsonschema2pojo")
public class DeliveryPickupPointAddress {

    @JsonProperty("street")
    private String street;

    @JsonProperty("zipCode")
    private String zipCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("countryCode")
    private String countryCode;

}
