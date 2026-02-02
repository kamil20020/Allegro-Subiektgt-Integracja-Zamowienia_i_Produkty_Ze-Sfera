package pl.kamil_dywan.external.allegro.own.offer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class SignatureTest {

    @CsvSource(value = {
        "1, 1,",
        "12, 12,",
        "1#2, 1, 2",
        "12#3, 12, 3",
        "12#22, 12, 22"
    })
    @ParameterizedTest
    void shouldGetToString(String expectedValue, String subiektSymbol, Integer quantity) {

        //given
        Signature signature = new Signature(subiektSymbol, quantity);

        //when
        String gotValue = signature.toString();

        //then
        assertNotNull(gotValue);
        assertEquals(expectedValue, gotValue);
    }

    @Test
    public void shouldExtractWithoutQuantity(){

        //given
        String externaIdValue = "12";

        //when
        Signature signature = Signature.extract(externaIdValue);

        //then
        assertNotNull(signature);
        assertEquals(externaIdValue, signature.subiektSymbol());
        assertNull(signature.quantity());
    }

    @Test
    public void shouldExtractWithQuantity(){

        //given
        String expectedSubiektSymbol = "12";
        Integer expectedQuantity = 22;
        String externalIdValue = expectedSubiektSymbol + Signature.SEPARATOR + expectedQuantity;

        //when
        Signature signature = Signature.extract(externalIdValue);

        //then
        assertNotNull(signature);
        assertEquals(expectedSubiektSymbol, signature.subiektSymbol());
        assertEquals(expectedQuantity, signature.quantity());
    }

    @Test
    void shouldExtractForNullExternalId() {

        //given
        //when
        Signature signature = Signature.extract(null);

        //then
        assertNull(signature);
    }

    @Test
    void shouldExtractForEmptyExternalId() {

        //given
        //when
        Signature signature = Signature.extract("");

        //then
        assertNull(signature);
    }

    @Test
    void shouldExtractForBlanklExternalId() {

        //given
        //when
        Signature signature = Signature.extract(" ");

        //then
        assertNull(signature);
    }
}