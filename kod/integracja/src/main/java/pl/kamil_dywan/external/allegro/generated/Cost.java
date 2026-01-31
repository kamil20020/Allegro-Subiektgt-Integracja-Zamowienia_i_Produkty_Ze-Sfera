package pl.kamil_dywan.external.allegro.generated;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.serialization.BigDecimalStringSerializer;
import pl.kamil_dywan.external.allegro.own.Currency;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "amount",
    "currency"
})
@Generated("jsonschema2pojo")
public class Cost {

    @JsonProperty("amount")
    @JsonSerialize(using = BigDecimalStringSerializer.class)
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

}
