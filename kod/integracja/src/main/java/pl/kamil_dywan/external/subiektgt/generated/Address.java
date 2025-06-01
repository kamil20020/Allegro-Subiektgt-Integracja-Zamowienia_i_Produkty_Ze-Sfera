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
        "street",
        "city",
        "postCode"
    }
)
public class Address {

    @XmlElement(name = "Street", required = true)
    protected String street;

    @XmlElement(name = "City", required = true)
    protected String city;

    @XmlElement(name = "PostCode", required = true)
    protected String postCode;
}
