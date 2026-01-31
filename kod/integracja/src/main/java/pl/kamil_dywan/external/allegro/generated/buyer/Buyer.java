package pl.kamil_dywan.external.allegro.generated.buyer;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.annotation.processing.Generated;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "email",
    "login",
    "firstName",
    "lastName",
    "companyName",
    "guest",
    "personalIdentity",
    "phoneNumber",
    "preferences",
    "address"
})
@Generated("jsonschema2pojo")
public class Buyer {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("login")
    private String login;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("guest")
    private boolean guest;

    @JsonProperty("personalIdentity")
    private String personalIdentity;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("preferences")
    private BuyerPreferences preferences;

    @JsonProperty("address")
    private BuyerAddress address;

    @JsonIgnore
    public boolean hasCompany(){

        return companyName != null;
    }

    @JsonIgnore
    public String getName(){

        if(companyName != null){

            return companyName;
        }

        return firstName + " " + lastName;
    }

}
