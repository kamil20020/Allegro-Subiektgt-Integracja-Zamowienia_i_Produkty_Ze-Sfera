package pl.kamil_dywan.external.allegro.generated.invoice;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "firstName",
    "lastName"
})
@Generated("jsonschema2pojo")
public class InvoiceNaturalPerson {

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonIgnore
    public String simpleToString(){

        return firstName + " " + lastName;
    }

}
