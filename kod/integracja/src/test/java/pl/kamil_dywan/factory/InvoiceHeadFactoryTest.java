package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.generated.invoice_head.InvoiceHead;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.invoice.InvoiceType;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceHeadFactoryTest {

    @Test
    void shouldCreate() {

        //given
        Code currencyCode = Code.PLN;

        //when
        InvoiceHead gotInvoiceHead = InvoiceHeadFactory.create(currencyCode);

        //then
        assertNotNull(gotInvoiceHead);
        assertNotNull(gotInvoiceHead.getSchema());
        assertNotNull(gotInvoiceHead.getParameters());
        assertNotNull(gotInvoiceHead.getInvoiceType());
        assertNotNull(gotInvoiceHead.getFunction());
        assertNotNull(gotInvoiceHead.getInvoiceCurrency());
        assertNotNull(gotInvoiceHead.getInvoiceCurrency().getCurrency());

        assertEquals(3, gotInvoiceHead.getSchema().getVersion());

        assertEquals(Code.PL.toString(), gotInvoiceHead.getParameters().getLanguage());
        assertEquals(",", gotInvoiceHead.getParameters().getDecimalSeparator());
        assertEquals(new BigDecimal("20.3"), gotInvoiceHead.getParameters().getPrecision());

        assertEquals(InvoiceType.VAT.toString(), gotInvoiceHead.getInvoiceType().getValue());
        assertEquals(Code.INVOICE, gotInvoiceHead.getInvoiceType().getCode());

        assertEquals("", gotInvoiceHead.getFunction().getValue());
        assertEquals(Code.FII, gotInvoiceHead.getFunction().getCode());

        assertEquals(currencyCode, gotInvoiceHead.getInvoiceCurrency().getCurrency().getCode());
    }
}