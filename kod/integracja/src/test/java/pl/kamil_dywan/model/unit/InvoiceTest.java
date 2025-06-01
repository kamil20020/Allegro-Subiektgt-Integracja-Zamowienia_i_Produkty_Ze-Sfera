package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    @Test
    void shouldGetHasCompanyForPerson(){

        //given
        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(new InvoiceNaturalPerson())
            .build();

        Invoice invoice = Invoice.builder()
            .address(invoiceAddress)
            .build();

        //when
        boolean doesInvoiceHaveCompany = invoice.hasCompany();

        //then
        assertFalse(doesInvoiceHaveCompany);
    }

    @Test
    void shouldGetHasCompanyForCompany(){

        //given
        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(new InvoiceCompany())
            .build();

        Invoice invoice = Invoice.builder()
            .address(invoiceAddress)
            .build();

        //when
        boolean doesInvoiceHaveCompany = invoice.hasCompany();

        //then
        assertTrue(doesInvoiceHaveCompany);
    }

    @Test
    void shouldGetClientNameFromPerson() {

        //given
        String expectedClientName = "Kamil Nowak";

        InvoiceNaturalPerson invoiceNaturalPerson = new InvoiceNaturalPerson("Kamil", "Nowak");

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(invoiceNaturalPerson)
            .build();

        Invoice invoice = Invoice.builder()
            .address(invoiceAddress)
            .build();

        //when
        String gotClientName = invoice.getClientName();

        //then
        assertEquals(expectedClientName, gotClientName);
    }

    @Test
    void shouldGetClientNameFromCompany() {

        //given
        String expectedClientName = "Firma 123";

        InvoiceCompany invoiceCompany = InvoiceCompany.builder()
            .name(expectedClientName)
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(invoiceCompany)
            .build();

        Invoice invoice = Invoice.builder()
            .address(invoiceAddress)
            .build();

        //when
        String gotClientName = invoice.getClientName();

        //then
        assertEquals(expectedClientName, gotClientName);
    }
}