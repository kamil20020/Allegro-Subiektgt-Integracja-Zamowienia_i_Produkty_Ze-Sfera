
package pl.kamil_dywan.external.allegro.generated.order_item;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id"
})
@Generated("jsonschema2pojo")
public class ExternalId {

    @JsonProperty("id")
    private String id;

    @JsonIgnore
    private static final String SEPARATOR = "#";

    @JsonIgnore
    public static String getCombinedCode(String producerCode, String eanCode){

        String combinedCode = SEPARATOR;

        if(verifyCode(producerCode)){

            combinedCode = producerCode + combinedCode;
        }

        if(verifyCode(eanCode)){

            combinedCode = combinedCode + eanCode;
        }

        if(combinedCode.equals(SEPARATOR)){

            return null;
        }

        return combinedCode;
    }

    @JsonIgnore
    public String getProducerCode(){

        return getCodeOnPosition(0);
    }

    @JsonIgnore
    public String getEanCode(){

        return getCodeOnPosition(1);
    }

    @JsonIgnore
    private String getCodeOnPosition(int position){

        if(!verifyCode(id)){
            return null;
        }

        String[] data = id.split(SEPARATOR, 2);

        if(data.length == 1){

            if(position == 0){
                return data[0];
            }

            return null;
        }

        String codeOnPosition = data[position];

        if(!verifyCode(codeOnPosition)){
            return null;
        }

        return codeOnPosition;
    }

    @JsonIgnore
    private static boolean verifyCode(String code){

        return code != null && !code.isEmpty();
    }

}
