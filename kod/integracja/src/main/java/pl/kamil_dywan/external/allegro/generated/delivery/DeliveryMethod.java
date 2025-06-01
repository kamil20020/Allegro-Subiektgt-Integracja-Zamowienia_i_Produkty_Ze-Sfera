package pl.kamil_dywan.external.allegro.generated.delivery;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "name"
})
@Generated("jsonschema2pojo")
public class DeliveryMethod {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

}
