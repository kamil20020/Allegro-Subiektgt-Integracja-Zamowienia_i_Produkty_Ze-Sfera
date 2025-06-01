package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.generated.TaxRate;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.LineTax;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LineTaxFactoryTest {

    @Test
    void shouldCreate() {

        //given
        BigDecimal tax = new BigDecimal("23.25");
        TaxRateCodeMapping taxRateCodeMapping = TaxRateCodeMapping.H;

        //when
        LineTax gotLineTax = LineTaxFactory.create(tax, taxRateCodeMapping);

        //then
        assertNotNull(gotLineTax);
        assertNotNull(gotLineTax.getTaxRate());

        TaxRate gotTaxRate = gotLineTax.getTaxRate();

        assertEquals(tax, gotLineTax.getTaxValue());
        assertEquals(taxRateCodeMapping.getCode(), gotTaxRate.getCode());
        assertEquals(taxRateCodeMapping.getValue(), gotTaxRate.getValue().intValue());
    }
}