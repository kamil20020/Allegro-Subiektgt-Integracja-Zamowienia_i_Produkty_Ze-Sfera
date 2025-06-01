package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceTotal;

public interface InvoiceTotalMapper {

    public static InvoiceTotal map(OrderTotalMoneyStats orderTotalMoneyStats){

        return InvoiceTotal.builder()
            .numberOfLines(orderTotalMoneyStats.getNumberOfOrderItems())
            .numberOfTaxRates(orderTotalMoneyStats.getNumberOfTaxes())
            .lineValueTotal(orderTotalMoneyStats.getTotalWithoutTax())
            .taxableTotal(orderTotalMoneyStats.getTotalWithoutTax())
            .taxTotal(orderTotalMoneyStats.getTaxValue())
            .netPaymentTotal(orderTotalMoneyStats.getTotalWithTax())
            .grossPaymentTotal(orderTotalMoneyStats.getTotalWithTax())
            .build();
    }
}
