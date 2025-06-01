package pl.kamil_dywan.external.subiektgt.generated.settlement;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.own.Code;
import pl.kamil_dywan.external.subiektgt.own.serialization.LocalDateAdapter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"value"}
)
public class SettlementTerms {

    @XmlValue
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    protected LocalDate value;

    @XmlAttribute(name = "Code")
    protected Code code;

}