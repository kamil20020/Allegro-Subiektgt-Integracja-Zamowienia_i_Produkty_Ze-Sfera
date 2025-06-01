package pl.kamil_dywan.external.subiektgt.generated.buyer;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.generated.Address;
import pl.kamil_dywan.external.subiektgt.generated.Contact;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "buyerReferences",
        "party",
        "address",
        "contact"
    }
)
@XmlRootElement(name = "Buyer")
public class Buyer {

    @XmlElement(name = "BuyerReferences", required = true)
    protected BuyerReferences buyerReferences;

    @XmlElement(name = "Party", required = true)
    protected String party;

    @XmlElement(name = "Address", required = true)
    protected Address address;

    @XmlElement(name = "Contact", required = true)
    protected Contact contact;

}
