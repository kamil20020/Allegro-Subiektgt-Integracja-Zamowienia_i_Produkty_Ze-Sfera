package pl.kamil_dywan.external.allegro.own.offer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.kamil_dywan.api.Api;

import java.util.ArrayList;
import java.util.List;

public record Signature(
    String subiektSymbol,
    Integer quantity
) {
    private static final Logger log = LoggerFactory.getLogger(Signature.class);
    public static final String SEPARATOR = "#";

    @Override
    public String toString() {

        if(quantity == null){
            return subiektSymbol;
        }

        return subiektSymbol + SEPARATOR + quantity;
    }

    public static Signature extract(String signatureItemCode) throws IllegalArgumentException{

        if(signatureItemCode == null || signatureItemCode.isBlank()){
            return null;
        }

        String[] gotData = signatureItemCode.split(SEPARATOR);

        if(gotData.length == 0){
            return null;
        }

        String subiektSymbol = gotData[0];

        if(gotData.length == 1){

            return new Signature(subiektSymbol, null);
        }

        int quantity;
        try{
            quantity = Integer.parseInt(gotData[1]);
        }
        catch (NumberFormatException e){

            log.error("Otrzmano niepoprawną liczbę produktów dla sygnatury " + signatureItemCode);
            return null;
        }

        return new Signature(subiektSymbol, quantity);
    }
}
