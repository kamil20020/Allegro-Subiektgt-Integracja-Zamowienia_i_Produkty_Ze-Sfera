package pl.kamil_dywan.external.allegro.generated.delivery;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "date"
})
@Generated("jsonschema2pojo")
public class DeliveryCancellation {

    @JsonProperty("date")
    private OffsetDateTime date;

}
