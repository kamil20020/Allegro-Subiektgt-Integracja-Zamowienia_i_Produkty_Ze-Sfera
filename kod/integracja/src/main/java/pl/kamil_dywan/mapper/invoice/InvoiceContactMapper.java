package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.subiektgt.generated.Contact;

public interface InvoiceContactMapper {

    static Contact map(InvoiceNaturalPerson invoiceNaturalPerson){

        if(invoiceNaturalPerson == null){
            return null;
        }

        return Contact.builder()
            .name(invoiceNaturalPerson.getFirstName() + " " + invoiceNaturalPerson.getLastName())
            .switchboard("")
            .fax("")
            .build();
    }
}
