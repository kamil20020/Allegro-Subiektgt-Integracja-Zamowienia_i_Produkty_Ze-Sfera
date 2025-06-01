package pl.kamil_dywan.external.allegro.generated.offer_product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.allegro.own.Country;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "rate",
    "countryCode"
})
@Generated("jsonschema2pojo")
public class TaxForCountry {

    @JsonProperty("rate")
    private BigDecimal taxRate;

    @JsonProperty("countryCode")
    private String country;
}
