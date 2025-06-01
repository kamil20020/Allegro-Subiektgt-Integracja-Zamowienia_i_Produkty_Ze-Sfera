package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.time.Month;
import java.time.Year;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"suppliersInvoiceNumber"}
)
@XmlRootElement(name = "InvoiceReferences")
public class InvoiceReferences {

    public InvoiceReferences(Month month, Year year){

        suppliersInvoiceNumber = month.name() + "/" + year.toString();
    }

    @XmlElement(name = "SuppliersInvoiceNumber", required = true)
    private String suppliersInvoiceNumber;

}
