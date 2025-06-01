package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"currency"}
)
public class CurrencyHolder {

    @XmlElement(name = "Currency", required = true)
    protected Currency currency;

}
