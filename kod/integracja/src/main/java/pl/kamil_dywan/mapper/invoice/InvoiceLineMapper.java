package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.*;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;
import pl.kamil_dywan.factory.LineTaxFactory;
import pl.kamil_dywan.factory.PercentDiscountFactory;

public interface InvoiceLineMapper {

    static InvoiceLine map(Integer invoiceLineNumber, OrderItem allegroOrderItem, OrderItemMoneyStats orderItemMoneyStats){

        Offer allegroOffer = allegroOrderItem.getOffer();

        Integer taxRateValue = orderItemMoneyStats.getTaxRatePercentage().intValue();

        TaxRateCodeMapping taxRateCodeMapping = TaxRateCodeMapping.getByValue(taxRateValue);

        InvoiceLineQuantity quantity = InvoiceLineQuantityMapper.map(allegroOrderItem);
        LineTax lineTax = LineTaxFactory.create(orderItemMoneyStats.getTotalTaxValue(), taxRateCodeMapping);

        return InvoiceLine.builder()
            .lineNumber(invoiceLineNumber)
            .product(InvoiceProductMapper.map(allegroOffer))
            .quantity(quantity)
            .unitPrice(new UnitPriceHolder(orderItemMoneyStats.getUnitPriceWithoutTax()))
            .percentDiscount(PercentDiscountFactory.create())
            .lineTax(lineTax)
            .lineTotal(orderItemMoneyStats.getTotalPriceWithTax())
            .invoiceLineInformation(allegroOffer.getName())
            .build();
    }
}
