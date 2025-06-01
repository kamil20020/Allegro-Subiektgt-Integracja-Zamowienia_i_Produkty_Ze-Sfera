package pl.kamil_dywan.external.allegro.own;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "key",
    "keyHash",
    "secret"
})
@Generated("jsonschema2pojo")
public class EncryptedAllegroLoginDetails {

    @JsonProperty("key")
    private String key;

    @JsonProperty("keyHash")
    private String keyHash;

    @JsonProperty("secret")
    private String secret;
}
