package pl.kamil_dywan.external.subiektgt.generated.invoice_head;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "language",
        "decimalSeparator",
        "precision"
    }
)
public class InvoiceHeadParameters {

    @XmlElement(name = "Language", required = true)
    protected String language;

    @XmlElement(name = "DecimalSeparator", required = true)
    protected String decimalSeparator;

    @XmlElement(name = "Precision")
    protected BigDecimal precision;

}
