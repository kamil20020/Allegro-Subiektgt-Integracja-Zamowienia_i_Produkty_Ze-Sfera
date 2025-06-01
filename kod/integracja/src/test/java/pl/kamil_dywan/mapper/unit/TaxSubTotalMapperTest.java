package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.own.order.OrderTaxSummary;
import pl.kamil_dywan.external.subiektgt.generated.TaxSubTotal;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;
import pl.kamil_dywan.mapper.TaxSubTotalMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxSubTotalMapperTest {

    @Test
    void shouldMap() {

        //given
        TaxRateCodeMapping taxRateCodeMapping = TaxRateCodeMapping.H;

        OrderTaxSummary orderTaxSummary = new OrderTaxSummary(
            new BigDecimal(taxRateCodeMapping.getValue()),
            new BigDecimal("23.45"),
            new BigDecimal("26.42"),
            new BigDecimal("3.48")
        );

        //when
        TaxSubTotal taxSubTotal = TaxSubTotalMapper.map(orderTaxSummary);

        //then
        assertNotNull(taxSubTotal);
        assertEquals(Code.PLN, taxSubTotal.getCode());
        assertEquals(orderTaxSummary.getTaxRateValue(), taxSubTotal.getTaxRate().getValue());
        assertEquals(taxRateCodeMapping.getCode(), taxSubTotal.getTaxRate().getCode());
        assertEquals(orderTaxSummary.getTotalWithoutTax(), taxSubTotal.getTaxableValueAtRate());
        assertEquals(orderTaxSummary.getTotalTaxValue(), taxSubTotal.getTaxAtRate());
        assertEquals(orderTaxSummary.getTotalWithTax(), taxSubTotal.getGrossPaymentAtRate());
        assertEquals(orderTaxSummary.getTotalWithTax(), taxSubTotal.getNetPaymentAtRate());
    }
}