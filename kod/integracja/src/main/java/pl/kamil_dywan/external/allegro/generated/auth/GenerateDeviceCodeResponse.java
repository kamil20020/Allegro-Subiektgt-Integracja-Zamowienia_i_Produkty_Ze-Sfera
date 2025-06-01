package pl.kamil_dywan.external.allegro.generated.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "deviceCode",
    "userCode",
    "verificationUri",
    "verificationUriComplete",
    "expiresIn",
    "interval"
})
@Generated("jsonschema2pojo")
public class GenerateDeviceCodeResponse {

    @JsonProperty("device_code")
    private String deviceCode;

    @JsonProperty("user_code")
    private String userCode;

    @JsonProperty("verification_uri")
    private String verificationUri;

    @JsonProperty("verification_uri_complete")
    private String verificationUriComplete;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("interval")
    private Integer interval;
}
