package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer;
import pl.kamil_dywan.external.subiektgt.generated.invoice_head.InvoiceHead;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.InvoiceLine;
import pl.kamil_dywan.external.subiektgt.generated.settlement.Settlement;
import pl.kamil_dywan.external.subiektgt.generated.supplier.Supplier;
import pl.kamil_dywan.external.subiektgt.own.serialization.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "invoiceHead",
        "invoiceReferences",
        "invoiceDate",
        "cityOfIssue",
        "taxPointDate",
        "supplier",
        "buyer",
        "invoiceLines",
        "narrative",
        "specialInstructions",
        "settlement",
        "taxSubTotals",
        "invoiceTotal"
    }
)
@XmlRootElement(name = "Invoice", namespace = "")
public class Invoice {

    @XmlElement(name = "InvoiceHead", required = true)
    protected InvoiceHead invoiceHead;

    @XmlElement(name = "InvoiceReferences", required = true)
    protected InvoiceReferences invoiceReferences;

    @XmlElement(name = "InvoiceDate", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    protected LocalDate invoiceDate;

    @XmlElement(name = "CityOfIssue", required = true)
    protected String cityOfIssue;

    @XmlElement(name = "TaxPointDate", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    protected LocalDate taxPointDate;

    @XmlElement(name = "Supplier", required = true)
    protected Supplier supplier;

    @XmlElement(name = "Buyer", required = true)
    protected Buyer buyer;

    @XmlElement(name = "InvoiceLine", required = true)
    protected List<InvoiceLine> invoiceLines;

    @XmlElement(name = "Narrative", required = true)
    protected String narrative;

    @XmlElement(name = "SpecialInstructions", required = true)
    protected String specialInstructions;

    @XmlElement(name = "Settlement", required = true)
    protected Settlement settlement;

    @XmlElement(name = "TaxSubTotal", required = true)
    protected List<TaxSubTotal> taxSubTotals;

    @XmlElement(name = "InvoiceTotal", required = true)
    protected InvoiceTotal invoiceTotal;
}
