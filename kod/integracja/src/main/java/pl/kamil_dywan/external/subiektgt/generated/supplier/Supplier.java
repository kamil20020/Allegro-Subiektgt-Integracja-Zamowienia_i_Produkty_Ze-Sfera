package pl.kamil_dywan.external.subiektgt.generated.supplier;

import jakarta.xml.bind.annotation.*;
import lombok.*;
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
        "supplierReferences",
        "party",
        "address",
        "contact"
    }
)
@XmlRootElement(name = "Supplier")
public class Supplier {

    @XmlElement(name = "SupplierReferences", required = true)
    protected SupplierReferences supplierReferences;

    @XmlElement(name = "Party", required = true)
    protected String party;

    @XmlElement(name = "Address", required = true)
    protected Address address;

    @XmlElement(name = "Contact", required = true)
    protected Contact contact;

}
