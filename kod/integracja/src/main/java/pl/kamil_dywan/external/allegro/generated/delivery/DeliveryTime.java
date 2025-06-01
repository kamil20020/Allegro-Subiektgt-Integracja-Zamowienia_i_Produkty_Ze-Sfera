package pl.kamil_dywan.external.allegro.generated.delivery;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.DateTimeRange;

import javax.annotation.processing.Generated;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "from",
    "to",
    "guaranteed",
    "dispatch"
})
@Generated("jsonschema2pojo")
public class DeliveryTime {

    @JsonProperty("from")
    private OffsetDateTime from;

    @JsonProperty("to")
    private OffsetDateTime to;

    @JsonProperty("guaranteed")
    private DateTimeRange guaranteed;

    @JsonProperty("dispatch")
    private DateTimeRange dispatch;

}
