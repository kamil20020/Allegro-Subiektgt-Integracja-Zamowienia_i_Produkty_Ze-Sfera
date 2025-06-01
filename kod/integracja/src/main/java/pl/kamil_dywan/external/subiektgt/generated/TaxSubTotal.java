package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import pl.kamil_dywan.external.subiektgt.own.serialization.BigDecimalAdapter;
import pl.kamil_dywan.external.subiektgt.own.Code;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "taxRate",
        "taxableValueAtRate",
        "taxAtRate",
        "netPaymentAtRate",
        "grossPaymentAtRate",
        "taxCurrency"
    }
)
@XmlRootElement(name = "TaxSubTotal")
public class TaxSubTotal {

    @XmlElement(name = "TaxRate", required = true)
    protected TaxRate taxRate;

    @XmlElement(name = "TaxableValueAtRate", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal taxableValueAtRate = BigDecimal.ZERO;

    @XmlElement(name = "TaxAtRate", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal taxAtRate = BigDecimal.ZERO;

    @XmlElement(name = "NetPaymentAtRate", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal netPaymentAtRate = BigDecimal.ZERO;

    @XmlElement(name = "GrossPaymentAtRate", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal grossPaymentAtRate = BigDecimal.ZERO;

    @XmlElement(name = "TaxCurrency", required = true)
    protected String taxCurrency = null;

    @XmlAttribute(name = "Code")
    protected Code code;

}
