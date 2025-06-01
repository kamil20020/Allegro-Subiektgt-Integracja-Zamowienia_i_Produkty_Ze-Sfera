package pl.kamil_dywan.api.allegro.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "name"
})
@Generated("jsonschema2pojo")
public class CreateOrderInvoiceFile {

    @JsonProperty("name")
    private String name;

}
