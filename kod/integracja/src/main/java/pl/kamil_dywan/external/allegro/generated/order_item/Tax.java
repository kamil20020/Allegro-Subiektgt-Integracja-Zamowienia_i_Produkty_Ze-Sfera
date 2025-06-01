
package pl.kamil_dywan.external.allegro.generated.order_item;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.serialization.BigDecimalStringSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "rate",
    "subject",
    "exemption"
})
@Generated("jsonschema2pojo")
public class Tax {

    @JsonProperty("rate")
    @JsonSerialize(using = BigDecimalStringSerializer.class)
    private BigDecimal rate;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("exemption")
    private String exemption;

}
