package pl.kamil_dywan.mapper.unit.sfera;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.sfera.generated.Customer;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.mapper.sfera.SferaCustomerMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaCustomerMapperTest {

    @Test
    void shouldMapForPrivateWithInvoice() {

        //given
        String expectedFirstname = "Adam";
        String expectedSurname = "Nowak";
        String expectedName = expectedFirstname + " " + expectedSurname;

        InvoiceNaturalPerson invoiceNaturalPerson = InvoiceNaturalPerson.builder()
            .firstName(expectedFirstname)
            .lastName(expectedSurname)
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(invoiceNaturalPerson)
            .street("Street 123")
            .zipCode("12-345")
            .city("City 123")
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .address(invoiceAddress)
            .required(true)
            .build();

        Order allegroOrder = Order.builder()
            .invoice(allegroInvoice)
            .build();

        //when
        Customer gotCustomer = SferaCustomerMapper.map(allegroOrder);

        //then
        assertNotNull(gotCustomer);
        assertEquals(expectedName, gotCustomer.getName());
        assertNull(gotCustomer.getNip());
        assertEquals(invoiceAddress.getStreet(), gotCustomer.getStreet());
        assertEquals(invoiceAddress.getZipCode(), gotCustomer.getPostCode());
        assertEquals(invoiceAddress.getCity(), gotCustomer.getCity());
        assertTrue(gotCustomer.isInvoiceRequired());
    }

    @Test
    void shouldMapForCompanyWithInvoice() {

        //given
        InvoiceCompany invoiceCompany = InvoiceCompany.builder()
            .name("Firma jakaœ")
            .taxId("123")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(invoiceCompany)
            .street("Street 123")
            .zipCode("12-345")
            .city("City 123")
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .address(invoiceAddress)
            .required(true)
            .build();

        Order allegroOrder = Order.builder()
            .invoice(allegroInvoice)
            .build();

        //when
        Customer gotCustomer = SferaCustomerMapper.map(allegroOrder);

        //then
        assertNotNull(gotCustomer);
        assertEquals(invoiceCompany.getName(), gotCustomer.getName());
        assertEquals(invoiceCompany.getTaxId(), gotCustomer.getNip());
        assertEquals(invoiceAddress.getStreet(), gotCustomer.getStreet());
        assertEquals(invoiceAddress.getZipCode(), gotCustomer.getPostCode());
        assertEquals(invoiceAddress.getCity(), gotCustomer.getCity());
        assertTrue(gotCustomer.isInvoiceRequired());
    }

    @Test
    void shouldMapWithoutInvoice() {

        //given
        Invoice allegroInvoice = Invoice.builder()
            .required(false)
            .build();

        Order allegroOrder = Order.builder()
            .invoice(allegroInvoice)
            .build();

        //when
        Customer gotCustomer = SferaCustomerMapper.map(allegroOrder);

        //then
        assertNotNull(gotCustomer);
        assertFalse(gotCustomer.isInvoiceRequired());
    }

}