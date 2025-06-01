package pl.kamil_dywan.external.subiektgt.generated.invoice_line;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import pl.kamil_dywan.external.subiektgt.own.serialization.BigDecimalAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "lineNumber",
        "product",
        "quantity",
        "unitPrice",
        "percentDiscount",
        "lineTax",
        "lineTotal",
        "invoiceLineInformation"
    }
)
@XmlRootElement(name = "InvoiceLine")
public class InvoiceLine {

    @XmlElement(name = "LineNumber")
    protected Integer lineNumber;

    @XmlElement(name = "Product", required = true)
    protected Product product;

    @XmlElement(name = "Quantity", required = true)
    protected InvoiceLineQuantity quantity;

    @XmlElement(name = "Price", required = true)
    protected UnitPriceHolder unitPrice;

    @XmlElement(name = "PercentDiscount", required = true)
    protected PercentDiscount percentDiscount;

    @XmlElement(name = "LineTax", required = true)
    protected LineTax lineTax;

    @XmlElement(name = "LineTotal", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal lineTotal;

    @XmlElement(name = "InvoiceLineInformation", required = true)
    protected String invoiceLineInformation;

}
