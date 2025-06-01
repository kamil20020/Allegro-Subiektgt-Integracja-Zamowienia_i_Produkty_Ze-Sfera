package pl.kamil_dywan.external.allegro.generated.order;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "text"
})
@Generated("jsonschema2pojo")
public class Note {

    @JsonProperty("text")
    private String text;

}
