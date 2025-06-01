package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceReferences;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceReferencesFactoryTest {

    @Test
    void shouldCreate() {

        //given
        int actualYear = LocalDate.now().getYear();
        String expectedSuppliersInvoiceNumber = "1/" + actualYear;

        //when
        InvoiceReferences gotReferences = InvoiceReferencesFactory.create();

        //then
        assertEquals(expectedSuppliersInvoiceNumber, gotReferences.getSuppliersInvoiceNumber());
    }
}