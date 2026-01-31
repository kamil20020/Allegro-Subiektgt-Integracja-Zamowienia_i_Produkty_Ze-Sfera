package pl.kamil_dywan.external.allegro.generated.order_item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import pl.kamil_dywan.external.allegro.generated.Cost;

import javax.annotation.processing.Generated;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "code",
    "type",
    "status",
    "externalTransactionId",
    "value"
})
@Generated("jsonschema2pojo")
public class Voucher {

    @JsonProperty("code")
    private String code;

    @JsonProperty("type")
    private String type;

    @JsonProperty("status")
    private String status;

    @JsonProperty("externalTransactionId")
    private String externalTransactionId;

    @JsonProperty("value")
    private Cost value;

}
