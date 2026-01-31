package pl.kamil_dywan.external.allegro.generated;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.time.OffsetDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "type",
    "provider",
    "finishedAt",
    "paidAmount",
    "reconciliation",
    "features"
})
@Generated("jsonschema2pojo")
public class Payment {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("finishedAt")
    private OffsetDateTime finishedAt;

    @JsonProperty("paidAmount")
    private Cost paidAmount;

    @JsonProperty("reconciliation")
    private Cost reconciliation;

    @JsonProperty("features")
    private List<String> features = new ArrayList<>();

}
