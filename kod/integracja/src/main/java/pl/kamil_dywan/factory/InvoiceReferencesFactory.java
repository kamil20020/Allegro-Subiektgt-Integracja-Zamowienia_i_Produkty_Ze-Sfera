package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.generated.InvoiceReferences;

import java.time.LocalDate;

public interface InvoiceReferencesFactory {

    public static InvoiceReferences create(){

        int actualYear = LocalDate.now().getYear();

        return InvoiceReferences.builder()
            .suppliersInvoiceNumber("1/" + actualYear)
            .build();
    }
}
