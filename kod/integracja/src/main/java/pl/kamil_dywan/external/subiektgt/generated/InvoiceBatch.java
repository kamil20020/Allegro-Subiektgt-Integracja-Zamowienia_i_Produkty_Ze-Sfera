package pl.kamil_dywan.external.subiektgt.generated;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.subiektgt.own.document.DocType;
import pl.kamil_dywan.external.subiektgt.own.serialization.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {
        "date",
        "number",
        "supplierName",
        "docType",
        "invoices",
        "batchTrailer"
    }
)
@XmlRootElement(name = "Batch")
public class InvoiceBatch {

    @XmlAttribute(name = "Date", required = true)
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate date;

    @XmlAttribute(name = "Number", required = true)
    private Integer number;

    @XmlAttribute(name = "SupplierName", required = true)
    private String supplierName;

    @XmlAttribute(name = "DocType", required = true)
    private DocType docType;

    @XmlElement(name = "Invoice", required = true)
    protected List<Invoice> invoices;

    @XmlElement(name = "BatchTrailer", required = true)
    protected InvoiceBatchTrailer batchTrailer;

}
