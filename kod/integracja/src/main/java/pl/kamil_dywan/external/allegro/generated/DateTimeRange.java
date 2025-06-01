package pl.kamil_dywan.external.allegro.generated;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "from",
    "to"
})
@Generated("jsonschema2pojo")
public class DateTimeRange {

    @JsonProperty("from")
    private OffsetDateTime from;

    @JsonProperty("to")
    private OffsetDateTime to;

}
