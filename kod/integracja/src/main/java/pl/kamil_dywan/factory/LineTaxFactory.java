package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.generated.TaxRate;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.LineTax;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;

import java.math.BigDecimal;

public interface LineTaxFactory {

    static LineTax create(BigDecimal tax, TaxRateCodeMapping taxRateCodeMapping){

        TaxRate taxRate = new TaxRate(
            BigDecimal.valueOf(taxRateCodeMapping.getValue()),
            taxRateCodeMapping.getCode()
        );

        return LineTax.builder()
            .taxRate(taxRate)
            .taxValue(tax)
            .build();
    }
}
