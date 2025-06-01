package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;
import pl.kamil_dywan.external.subiektgt.own.serialization.BigDecimalAdapter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "numberOfLines",
        "numberOfTaxRates",
        "lineValueTotal",
        "taxableTotal",
        "taxTotal",
        "netPaymentTotal",
        "grossPaymentTotal"
    }
)
@XmlRootElement(name = "InvoiceTotal")
public class InvoiceTotal {

    @XmlElement(name = "NumberOfLines")
    protected Integer numberOfLines = 0;

    @XmlElement(name = "NumberOfTaxRates")
    protected Integer numberOfTaxRates = 0;

    @XmlElement(name = "LineValueTotal")
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal lineValueTotal = BigDecimal.ZERO;

    @XmlElement(name = "TaxableTotal")
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal taxableTotal = BigDecimal.ZERO;

    @XmlElement(name = "TaxTotal", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal taxTotal = BigDecimal.ZERO;

    @XmlElement(name = "NetPaymentTotal", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal netPaymentTotal = BigDecimal.ZERO;

    @XmlElement(name = "GrossPaymentTotal", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal grossPaymentTotal = BigDecimal.ZERO;

}
