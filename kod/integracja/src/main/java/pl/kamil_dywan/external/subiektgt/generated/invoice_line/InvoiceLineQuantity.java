package pl.kamil_dywan.external.subiektgt.generated.invoice_line;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.own.product.UOMCode;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "packsize",
        "amount"
    }
)
public class InvoiceLineQuantity {

    @XmlElement(name = "Packsize")
    protected Integer packsize;

    @XmlElement(name = "Amount")
    protected Integer amount;

    @XmlAttribute(name = "UOMCode")
    protected UOMCode uomCode;

}
