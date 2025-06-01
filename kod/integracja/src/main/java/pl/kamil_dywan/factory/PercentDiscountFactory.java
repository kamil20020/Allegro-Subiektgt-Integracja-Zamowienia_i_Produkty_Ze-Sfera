package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.generated.Type;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.PercentDiscount;
import pl.kamil_dywan.external.subiektgt.own.Code;

import java.math.BigDecimal;

public interface PercentDiscountFactory {

    static PercentDiscount create(){

        return new PercentDiscount(
            new Type("", Code.LID),
            BigDecimal.ZERO
        );
    }
}
