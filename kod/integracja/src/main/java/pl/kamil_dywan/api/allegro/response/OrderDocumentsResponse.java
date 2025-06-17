package pl.kamil_dywan.api.allegro.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "invoices",
    "hasExternalInvoices"
})
@Generated("jsonschema2pojo")
public class OrderDocumentsResponse {

    @JsonProperty("invoices")
    private List<Object> invoices;

    @JsonProperty("hasExternalInvoices")
    private boolean hasExternalInvoices;

    @JsonIgnore
    public boolean documentsExist(){

        if(invoices == null || invoices.isEmpty()){

            return hasExternalInvoices;
        }

        return true;
    }

}