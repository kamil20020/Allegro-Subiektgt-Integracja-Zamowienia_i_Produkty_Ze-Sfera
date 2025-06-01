package pl.kamil_dywan.mapper;

import pl.kamil_dywan.external.allegro.generated.buyer.BuyerAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.subiektgt.generated.Address;

public interface AddressMapper {

    static Address map(BuyerAddress buyerAddress){

        return Address.builder()
            .city(buyerAddress.getCity())
            .street(buyerAddress.getStreet())
            .postCode(buyerAddress.getPostCode())
            .build();
    }

    static Address map(InvoiceAddress invoiceAddress){

        return Address.builder()
            .city(invoiceAddress.getCity())
            .street(invoiceAddress.getStreet())
            .postCode(invoiceAddress.getZipCode())
            .build();
    }
}
