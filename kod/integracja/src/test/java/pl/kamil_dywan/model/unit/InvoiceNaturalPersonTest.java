package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceNaturalPersonTest {

    @Test
    void shouldGetSimpleToString() {

        //given
        String firstname = "Adam";
        String lastname = "Nowak";
        String expectedResult = "Adam Nowak";

        InvoiceNaturalPerson invoiceNaturalPerson = new InvoiceNaturalPerson(firstname, lastname);

        //when
        String gotResult = invoiceNaturalPerson.simpleToString();

        //then
        assertNotNull(gotResult);
        assertEquals(expectedResult, gotResult);
    }
}