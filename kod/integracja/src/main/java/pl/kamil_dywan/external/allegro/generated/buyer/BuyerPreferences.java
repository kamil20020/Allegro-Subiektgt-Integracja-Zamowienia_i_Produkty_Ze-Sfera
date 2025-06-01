package pl.kamil_dywan.external.allegro.generated.buyer;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "language"
})
@Generated("jsonschema2pojo")
public class BuyerPreferences {

    @JsonProperty("language")
    private String language;

}
