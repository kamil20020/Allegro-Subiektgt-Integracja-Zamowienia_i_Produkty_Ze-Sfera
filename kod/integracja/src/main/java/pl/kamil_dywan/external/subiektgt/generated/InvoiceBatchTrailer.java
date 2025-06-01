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
    propOrder = {
        "itemCurrency",
        "checksum"
    }
)
@XmlRootElement(name = "BatchTrailer")
public class InvoiceBatchTrailer {

    @XmlElement(name = "ItemCurrency", required = true)
    protected CurrencyHolder itemCurrency;

    @XmlElement(name = "Checksum", required = true)
    protected String checksum = null;

}
