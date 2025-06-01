package pl.kamil_dywan.external.subiektgt.generated.settlement;

import jakarta.xml.bind.annotation.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"settlementTerms"}
)
@XmlRootElement(name = "Settlement")
public class Settlement {

    @XmlElement(name = "SettlementTerms", required = true)
    protected SettlementTerms settlementTerms;
}
