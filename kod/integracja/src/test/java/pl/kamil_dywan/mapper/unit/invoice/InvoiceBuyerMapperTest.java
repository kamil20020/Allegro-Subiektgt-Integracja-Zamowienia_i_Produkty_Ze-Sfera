package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.subiektgt.generated.Address;
import pl.kamil_dywan.external.subiektgt.generated.Contact;
import pl.kamil_dywan.mapper.AddressMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceBuyerMapper;
import pl.kamil_dywan.mapper.invoice.InvoiceContactMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class InvoiceBuyerMapperTest {

    @Test
    void shouldMapWithCompanyName() {

        //given
        InvoiceCompany invoiceCompany = InvoiceCompany.builder()
            .name("Company 123")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(invoiceCompany)
            .city("City 123")
            .street("Street 123")
            .zipCode("12-345")
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .address(invoiceAddress)
            .build();

        //when
        try(
            var mockedAddressMapper = Mockito.mockStatic(AddressMapper.class);
            var mockedContactMapper = Mockito.mockStatic(InvoiceContactMapper.class);
        ){

            mockedAddressMapper.when(() -> AddressMapper.map(any(InvoiceAddress.class))).thenReturn(new Address());

            pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer gotBuyer = InvoiceBuyerMapper.map(allegroInvoice);

            //then
            assertEquals(invoiceCompany.getName(), gotBuyer.getParty());
            assertNotNull(gotBuyer.getAddress());
            assertNull(gotBuyer.getContact());

            mockedAddressMapper.verify(() -> AddressMapper.map(invoiceAddress));
            mockedContactMapper.verify(() -> InvoiceContactMapper.map(null));
        }
    }

    @Test
    public void shouldMapWithoutCompanyName(){

        //given
        InvoiceNaturalPerson invoiceNaturalPerson = InvoiceNaturalPerson.builder()
            .firstName("Adam")
            .lastName("Nowak")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(invoiceNaturalPerson)
            .city("City 123")
            .street("Street 123")
            .zipCode("12-345")
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .address(invoiceAddress)
            .build();

        //when
        try(
                MockedStatic<AddressMapper> mockedAddressMapper = Mockito.mockStatic(AddressMapper.class);
                MockedStatic<InvoiceContactMapper> mockedContactMapper = Mockito.mockStatic(InvoiceContactMapper.class);
        ){

            mockedAddressMapper.when(() -> AddressMapper.map(any(InvoiceAddress.class))).thenReturn(new Address());
            mockedContactMapper.when(() -> InvoiceContactMapper.map(any(InvoiceNaturalPerson.class))).thenReturn(new Contact());

            pl.kamil_dywan.external.subiektgt.generated.buyer.Buyer gotBuyer = InvoiceBuyerMapper.map(allegroInvoice);

            //then
            assertNotNull(gotBuyer);
            assertEquals(invoiceNaturalPerson.getFirstName() + " " + invoiceNaturalPerson.getLastName(), gotBuyer.getParty());
            assertNotNull(gotBuyer.getAddress());
            assertNotNull(gotBuyer.getContact());

            mockedAddressMapper.verify(() -> AddressMapper.map(invoiceAddress));
            mockedContactMapper.verify(() -> InvoiceContactMapper.map(invoiceNaturalPerson));
        }
    }
}