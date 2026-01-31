package pl.kamil_dywan.external.allegro.generated.invoice;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import pl.kamil_dywan.external.allegro.own.serialization.LocalDateSerializer;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "required",
    "address",
    "dueDate",
    "features"
})
@Generated("jsonschema2pojo")
public class Invoice {

    @JsonProperty("required")
    private boolean required;

    @JsonProperty("address")
    private InvoiceAddress address;

    @JsonProperty("dueDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dueDate;

    @JsonProperty("features")
    private List<String> features = new ArrayList<>();

    @JsonIgnore
    public boolean hasCompany(){

        return address.getCompany() != null;
    }

    @JsonIgnore
    public String getClientName(){

        if (address.getCompany() == null) {

            return address.getNaturalPerson().simpleToString();
        }

        return address.getCompany().getName();
    }

}
