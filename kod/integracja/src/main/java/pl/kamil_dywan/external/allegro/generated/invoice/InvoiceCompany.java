package pl.kamil_dywan.external.allegro.generated.invoice;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "name",
    "ids",
    "vatPayerStatus",
    "taxId"
})
@Generated("jsonschema2pojo")
public class InvoiceCompany {

    @JsonProperty("name")
    private String name;

    @JsonProperty("ids")
    private List<Object> ids = new ArrayList<>();

    @JsonProperty("vatPayerStatus")
    private String vatPayerStatus;

    @JsonProperty("taxId")
    private String taxId;

}
