package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.own.Code;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"value"}
)
public class Type {

    @XmlValue
    protected String value;

    @XmlAttribute(name = "Code")
    protected Code code;

}
