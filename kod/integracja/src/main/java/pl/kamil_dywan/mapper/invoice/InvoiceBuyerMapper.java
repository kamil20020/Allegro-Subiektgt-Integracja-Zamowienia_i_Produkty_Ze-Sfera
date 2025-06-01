package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.subiektgt.generated.Address;
import pl.kamil_dywan.external.subiektgt.generated.Contact;
import pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer;
import pl.kamil_dywan.external.subiektgt.generated.buyer.BuyerReferences;
import pl.kamil_dywan.mapper.AddressMapper;

public interface InvoiceBuyerMapper {

    static Buyer map(Invoice allegroInvoice){

        if(allegroInvoice == null){
            return null;
        }

        InvoiceAddress allegroInvoiceAddress = allegroInvoice.getAddress();
        InvoiceCompany allegroInvoiceCompany = allegroInvoiceAddress.getCompany();
        InvoiceNaturalPerson allegroInvoiceNaturalPerson = allegroInvoiceAddress.getNaturalPerson();

        String supplierCodeForBuyer = allegroInvoice.getClientName();

        BuyerReferences buyerReferences = new BuyerReferences();

        buyerReferences.setSuppliersCodeForBuyer("********************");

        if(allegroInvoice.hasCompany()){

            String nip = allegroInvoiceCompany.getTaxId();

            if(nip != null){

                buyerReferences.setTaxNumber(nip);
            }
        }

        Address address = AddressMapper.map(allegroInvoiceAddress);
        Contact contact = InvoiceContactMapper.map(allegroInvoiceNaturalPerson);

        return Buyer.builder()
            .party(supplierCodeForBuyer)
            .buyerReferences(buyerReferences)
            .address(address)
            .contact(contact)
            .build();
    }
}
