package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.subiektgt.generated.Contact;
import pl.kamil_dywan.mapper.invoice.InvoiceContactMapper;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceContactMapperTest {

    @Test
    void shouldMapForNotNullInvoiceNaturalPerson() {

        //given
        String firstName = "Kamil";
        String lastName = "Nowak";
        String expectedName = firstName + " " + lastName;

        InvoiceNaturalPerson invoiceNaturalPerson = InvoiceNaturalPerson.builder()
            .firstName(firstName)
            .lastName(lastName)
            .build();

        //when
        Contact gotContact = InvoiceContactMapper.map(invoiceNaturalPerson);

        //then
        assertNotNull(gotContact);
        assertEquals(expectedName, gotContact.getName());
        assertEquals("", gotContact.getSwitchboard());
    }

    @Test
    void shouldMapForNullInvoiceNaturalPerson(){

        //given
        InvoiceNaturalPerson invoiceNaturalPerson = null;

        //when
        Contact gotContact = InvoiceContactMapper.map(invoiceNaturalPerson);

        //then
        assertNull(gotContact);
    }
}