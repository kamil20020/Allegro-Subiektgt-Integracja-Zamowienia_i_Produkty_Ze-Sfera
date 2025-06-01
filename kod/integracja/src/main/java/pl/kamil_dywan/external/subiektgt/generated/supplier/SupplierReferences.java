package pl.kamil_dywan.external.subiektgt.generated.supplier;

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
        "buyersCodeForSupplier",
        "taxNumber"
    }
)
public class SupplierReferences {

    @XmlElement(name = "BuyersCodeForSupplier", required = true)
    protected String buyersCodeForSupplier;

    @XmlElement(name = "TaxNumber", required = true)
    protected String taxNumber;

}