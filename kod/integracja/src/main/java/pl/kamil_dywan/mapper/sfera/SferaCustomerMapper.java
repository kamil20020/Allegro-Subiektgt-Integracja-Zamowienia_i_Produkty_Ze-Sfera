package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.sfera.generated.Customer;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;

public interface SferaCustomerMapper {

    public static Customer map(Order order){

        Invoice invoice = order.getInvoice();

        if(order.hasInvoice()){

            return SferaCustomerMapper.map(invoice);
        }

        return Customer.builder()
            .isInvoiceRequired(false)
            .build();
    }

    private static Customer map(Invoice allegroInvoice){

        InvoiceAddress invoiceAddress = allegroInvoice.getAddress();

        String clientName = allegroInvoice.getClientName();

        String nip = null;

        if(allegroInvoice.hasCompany()){

            InvoiceCompany allegroInvoiceCompany = invoiceAddress.getCompany();;

            nip = allegroInvoiceCompany.getTaxId();
        }

        return Customer.builder()
            .name(clientName)
            .nip(nip)
            .street(invoiceAddress.getStreet())
            .postCode(invoiceAddress.getZipCode())
            .city(invoiceAddress.getCity())
            .isInvoiceRequired(true)
            .build();
    }

}
