package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.generated.Currency;
import pl.kamil_dywan.external.subiektgt.generated.CurrencyHolder;
import pl.kamil_dywan.external.subiektgt.generated.Type;
import pl.kamil_dywan.external.subiektgt.generated.invoice_head.InvoiceHead;
import pl.kamil_dywan.external.subiektgt.generated.invoice_head.InvoiceHeadParameters;
import pl.kamil_dywan.external.subiektgt.generated.invoice_head.InvoiceHeadSchema;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.invoice.InvoiceType;

import java.math.BigDecimal;

public interface InvoiceHeadFactory {

    static InvoiceHead create(Code currencyCode){

        return InvoiceHead.builder()
            .schema(new InvoiceHeadSchema(3))
            .parameters(new InvoiceHeadParameters(Code.PL.toString(), ",", new BigDecimal("20.3")))
            .invoiceType(new Type(InvoiceType.VAT.toString(), Code.INVOICE))
            .function(new Type("", Code.FII))
            .invoiceCurrency(new CurrencyHolder(new Currency(currencyCode)))
            .checksum(81410)
            .build();
    }
}
