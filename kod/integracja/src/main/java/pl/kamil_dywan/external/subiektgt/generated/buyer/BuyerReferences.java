package pl.kamil_dywan.external.subiektgt.generated.buyer;

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
        "suppliersCodeForBuyer",
        "taxNumber"
    }
)
public class BuyerReferences {

    @XmlElement(name = "SuppliersCodeForBuyer", required = true)
    protected String suppliersCodeForBuyer;

    @XmlElement(name = "TaxNumber")
    protected String taxNumber;

    public void setSuppliersCodeForBuyer(String suppliersCodeForBuyer) {

        if(suppliersCodeForBuyer != null){

            if(suppliersCodeForBuyer.length() > 20){

                suppliersCodeForBuyer = suppliersCodeForBuyer.substring(0, 20);
            }

            suppliersCodeForBuyer = suppliersCodeForBuyer.replaceAll("\\s", "");
        }

        this.suppliersCodeForBuyer = suppliersCodeForBuyer;
    }
}
