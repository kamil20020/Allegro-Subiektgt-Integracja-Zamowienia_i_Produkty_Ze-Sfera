package pl.kamil_dywan.external.subiektgt.generated.buyer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyerReferencesTest {

    @Test
    void shouldSetSuppliersCodeForBuyer() {

        //given
        String expectedSupplierCodeForBuyer = "123";

        BuyerReferences buyerReferences = new BuyerReferences();

        //when
        buyerReferences.setSuppliersCodeForBuyer(expectedSupplierCodeForBuyer);

        //then
        assertEquals(expectedSupplierCodeForBuyer, buyerReferences.suppliersCodeForBuyer);
    }

    @Test
    void shouldSetSuppliersCodeForBuyerWithSpaces() {

        //given
        String expectedSupplierCodeForBuyer = "123456";

        BuyerReferences buyerReferences = new BuyerReferences();

        //when
        buyerReferences.setSuppliersCodeForBuyer("123 456");

        //then
        assertEquals(expectedSupplierCodeForBuyer, buyerReferences.suppliersCodeForBuyer);
    }

    @Test
    void shouldSetTooLongSuppliersCodeForBuyer() {

        //given
        String expectedSupplierCodeForBuyer = "12345678901234567890";

        BuyerReferences buyerReferences = new BuyerReferences();

        //when
        buyerReferences.setSuppliersCodeForBuyer("123456789012345678901234567890");

        //then
        assertEquals(expectedSupplierCodeForBuyer, buyerReferences.suppliersCodeForBuyer);
    }

    @Test
    void shouldSetNotSuppliersCodeForBuyer() {

        //given
        BuyerReferences buyerReferences = new BuyerReferences();

        //when
        buyerReferences.setSuppliersCodeForBuyer(null);

        //then
        assertNull(buyerReferences.suppliersCodeForBuyer);
    }


}