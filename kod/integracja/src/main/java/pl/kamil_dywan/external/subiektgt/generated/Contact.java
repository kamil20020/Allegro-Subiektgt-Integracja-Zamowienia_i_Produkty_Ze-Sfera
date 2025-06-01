package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
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
    propOrder = {
        "name",
        "switchboard",
        "fax"
    }
)
public class Contact {

    @XmlElement(name = "Name", required = true)
    protected String name;

    @XmlElement(name = "Switchboard", required = true)
    protected String switchboard;

    @XmlElement(name = "Fax", required = true)
    protected String fax = "";

}
