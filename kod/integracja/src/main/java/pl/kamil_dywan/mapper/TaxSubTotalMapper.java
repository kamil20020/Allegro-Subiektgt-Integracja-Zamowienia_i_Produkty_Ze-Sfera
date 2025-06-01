package pl.kamil_dywan.mapper;

import pl.kamil_dywan.external.allegro.own.order.OrderTaxSummary;
import pl.kamil_dywan.external.subiektgt.generated.TaxRate;
import pl.kamil_dywan.external.subiektgt.generated.TaxSubTotal;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;

import java.math.BigDecimal;

public interface TaxSubTotalMapper {

    public static TaxSubTotal map(OrderTaxSummary orderTaxSummary){

        BigDecimal taxRateValue = orderTaxSummary.getTaxRateValue();

        TaxRate taxRate = new TaxRate(
            taxRateValue,
            TaxRateCodeMapping.getByValue(taxRateValue).getCode()
        );

        return TaxSubTotal.builder()
            .code(Code.PLN)
            .taxRate(taxRate)
            .taxableValueAtRate(orderTaxSummary.getTotalWithoutTax())
            .taxAtRate(orderTaxSummary.getTotalTaxValue())
            .netPaymentAtRate(orderTaxSummary.getTotalWithTax())
            .grossPaymentAtRate(orderTaxSummary.getTotalWithTax())
            .build();
    }
}
