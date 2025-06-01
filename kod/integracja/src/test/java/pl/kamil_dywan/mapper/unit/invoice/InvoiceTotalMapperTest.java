package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceTotal;
import pl.kamil_dywan.mapper.invoice.InvoiceTotalMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTotalMapperTest {

    @Test
    void shouldMap() {

        //given
        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats(
            1,
            2,
            new BigDecimal("12.34"),
            new BigDecimal("22.36"),
            new BigDecimal("36.48")
        );

        //when
        InvoiceTotal invoiceTotal = InvoiceTotalMapper.map(orderTotalMoneyStats);

        //then
        assertEquals(orderTotalMoneyStats.getNumberOfOrderItems(), invoiceTotal.getNumberOfLines());
        assertEquals(orderTotalMoneyStats.getNumberOfTaxes(), invoiceTotal.getNumberOfTaxRates());
        assertEquals(orderTotalMoneyStats.getTotalWithoutTax(), invoiceTotal.getTaxableTotal());
        assertEquals(orderTotalMoneyStats.getTotalWithoutTax(), invoiceTotal.getLineValueTotal());
        assertEquals(orderTotalMoneyStats.getTaxValue(), invoiceTotal.getTaxTotal());
        assertEquals(orderTotalMoneyStats.getTotalWithTax(), invoiceTotal.getGrossPaymentTotal());
        assertEquals(orderTotalMoneyStats.getTotalWithTax(), invoiceTotal.getNetPaymentTotal());
    }
}