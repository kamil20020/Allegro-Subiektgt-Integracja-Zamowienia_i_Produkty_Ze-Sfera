package pl.kamil_dywan.external.subiektgt.generated.invoice_line;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.generated.Type;
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
        "type",
        "percentage"
    }
)
public class PercentDiscount {

    @XmlElement(name = "Type", required = true)
    protected Type type;

    @XmlElement(name = "Percentage")
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal percentage;

}
