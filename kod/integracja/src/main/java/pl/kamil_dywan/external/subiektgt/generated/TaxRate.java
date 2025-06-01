package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    propOrder = {"value"}
)
public class TaxRate {

    @XmlValue
    @XmlJavaTypeAdapter(value = BigDecimalAdapter.class)
    protected BigDecimal value;

    @XmlAttribute(name = "Code")
    protected Code code;

}
