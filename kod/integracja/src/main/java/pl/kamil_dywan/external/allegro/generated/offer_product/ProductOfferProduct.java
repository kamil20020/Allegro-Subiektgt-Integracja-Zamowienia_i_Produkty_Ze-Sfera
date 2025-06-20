package pl.kamil_dywan.external.allegro.generated.offer_product;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "id",
    "parameters"
})
public class ProductOfferProduct {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("parameters")
    private List<OfferProductParameter> parameters;

    @JsonIgnore
    public static final String PRODUCER_CODE_KEY = "Kod producenta";

    @JsonIgnore
    public static final String EAN_CODE_KEY = "EAN (GTIN)";

    @JsonIgnore
    public String getEANCode(){

        return getParameterCode(EAN_CODE_KEY);
    }

    @JsonIgnore
    public String getProducerCode(){

        return getParameterCode(PRODUCER_CODE_KEY);
    }

    @JsonIgnore
    private String getParameterCode(String parameterName){

        if(parameters == null || parameters.isEmpty()) {
            return null;
        }

        Optional<OfferProductParameter> foundCodeParameter = parameters.stream()
            .filter(parameter -> Objects.equals(parameter.getName(), parameterName))
            .findFirst();

        if(foundCodeParameter.isEmpty()) {
            return null;
        }

        List<String> parameterValues = foundCodeParameter.get().getValues();

        if(parameterValues == null || parameterValues.isEmpty()) {
            return null;
        }

        String gotFirstValue = parameterValues.get(0);

        return gotFirstValue;
    }

}
