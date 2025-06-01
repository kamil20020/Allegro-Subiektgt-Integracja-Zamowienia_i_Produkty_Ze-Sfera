package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.buyer.BuyerAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.own.Country;
import pl.kamil_dywan.external.subiektgt.generated.Address;
import pl.kamil_dywan.mapper.AddressMapper;

import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    @Test
    void shouldMapForBuyerAddress() {

        //given
        BuyerAddress buyerAddress = BuyerAddress.builder()
            .street("Street 123")
            .city("City 123")
            .postCode("12-234")
            .build();

        //when
        Address gotAddress = AddressMapper.map(buyerAddress);

        //then
        assertNotNull(gotAddress);
        assertEquals(buyerAddress.getStreet(), gotAddress.getStreet());
        assertEquals(buyerAddress.getCity(), gotAddress.getCity());
        assertEquals(buyerAddress.getPostCode(), gotAddress.getPostCode());
    }

    @Test
    void shouldMapForInvoiceAddress() {

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .street("Street 123")
            .city("City 123")
            .zipCode("12-234")
            .build();

        //when
        Address gotAddress = AddressMapper.map(invoiceAddress);

        //then
        assertNotNull(gotAddress);
        assertEquals(invoiceAddress.getStreet(), gotAddress.getStreet());
        assertEquals(invoiceAddress.getCity(), gotAddress.getCity());
        assertEquals(invoiceAddress.getZipCode(), gotAddress.getPostCode());
    }
}