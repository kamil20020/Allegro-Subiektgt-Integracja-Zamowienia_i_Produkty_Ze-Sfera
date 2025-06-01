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
import pl.kamil_dywan.external.subiektgt.own.serialization.BigDecimalAdapter;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"unitPrice"}
)
public class UnitPriceHolder {

    @XmlElement(name = "UnitPrice", required = true)
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal unitPrice;

}