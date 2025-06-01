package pl.kamil_dywan.external.subiektgt.own.product;

import jakarta.xml.bind.annotation.XmlEnum;
import pl.kamil_dywan.external.subiektgt.own.Code;

import java.math.BigDecimal;

@XmlEnum
public enum TaxRateCodeMapping {

    L(Code.L, 8),
    H(Code.H, 23),
    Z(Code.Z, 0);

    private Code code;
    private Integer value;

    private TaxRateCodeMapping(Code code, Integer value){

        this.code = code;
        this.value = value;
    }

    public static TaxRateCodeMapping getByValue(BigDecimal value){

        return getByValue(value.intValue());
    }

    public static TaxRateCodeMapping getByValue(Integer value){

        return switch(value){

            case 8 -> TaxRateCodeMapping.L;
            case 23 -> TaxRateCodeMapping.H;
            case 0 -> TaxRateCodeMapping.Z;
            default -> throw new IllegalArgumentException("Tax rate of subiekt was not found");
        };
    }

    public Code getCode(){

        return code;
    }

    public Integer getValue(){

        return value;
    }
}
