package pl.kamil_dywan.external.subiektgt.generated.invoice_line;

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
        "suppliersProductCode",
        "description"
    }
)
public class Product {

    @XmlElement(name = "SuppliersProductCode", required = true)
    protected String suppliersProductCode;

    @XmlElement(name = "Description", required = true)
    protected String description;

}
