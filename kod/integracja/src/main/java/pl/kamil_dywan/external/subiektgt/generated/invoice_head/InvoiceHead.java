package pl.kamil_dywan.external.subiektgt.generated.invoice_head;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.generated.Currency;
import pl.kamil_dywan.external.subiektgt.generated.CurrencyHolder;
import pl.kamil_dywan.external.subiektgt.generated.Type;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "schema",
        "parameters",
        "invoiceType",
        "function",
        "invoiceCurrency",
        "checksum"
    }
)
@XmlRootElement(name = "InvoiceHead")
public class InvoiceHead {

    @XmlElement(name = "Schema", required = true)
    protected InvoiceHeadSchema schema;

    @XmlElement(name = "Parameters", required = true)
    protected InvoiceHeadParameters parameters;

    @XmlElement(name = "InvoiceType", required = true)
    protected Type invoiceType;

    @XmlElement(name = "Function", required = true)
    protected Type function;

    @XmlElement(name = "InvoiceCurrency", required = true)
    protected CurrencyHolder invoiceCurrency;

    @XmlElement(name = "Checksum")
    protected Integer checksum = 81410;

}
