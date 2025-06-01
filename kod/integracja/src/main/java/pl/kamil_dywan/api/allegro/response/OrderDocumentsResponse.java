package pl.kamil_dywan.api.allegro.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "invoices"
})
@Generated("jsonschema2pojo")
public class OrderDocumentsResponse {

    @JsonProperty("invoices")
    private List<Object> invoices;
}
