package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;

import static org.junit.jupiter.api.Assertions.*;

class ExternalIdTest {

    @Test
    public void shouldCreateExternalId(){

        //given
        String expectedProducerCode = "producer";
        String expectedEanCode = "ean";
        String expectedCombinedCode = "producer#ean";

        //when
        ExternalId externalId = new ExternalId(expectedProducerCode, expectedEanCode);

        //then
        assertNotNull(externalId);
        assertEquals(expectedCombinedCode, externalId.getId());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer, ean, producer#ean",
        ",ean, #ean",
        "producer,, producer#"
    })
    public void shouldGetCombinedCode(String expectedProducerCode, String expectedEanCode, String expectedCombinedCode){

        //given

        //when
        String gotCombinedKey = ExternalId.getCombinedCode(expectedProducerCode, expectedEanCode);

        //then
        assertNotNull(gotCombinedKey);
        assertEquals(expectedCombinedCode, gotCombinedKey);
    }

    @Test
    public void shouldGetCombinedCodeForEmptyCodes(){

        //given

        //when
        String gotCombinedKey = ExternalId.getCombinedCode("", "");

        //then
        assertNull(gotCombinedKey);
    }

    @Test
    public void shouldGetCombinedCodeForNotGivenCodes(){

        //given

        //when
        String gotCombinedKey = ExternalId.getCombinedCode(null, null);

        //then
        assertNull(gotCombinedKey);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer#ean, producer",
        "producer#, producer",
        "producer, producer"
    })
    void shouldGetProducerCodeWhenItIsSet(String expectedCombinedCode, String expectedGotProducerCode) {

        //given
        ExternalId externalId = new ExternalId(expectedCombinedCode);

        //when
        String gotProducerCode = externalId.getProducerCode();

        //then
        assertNotNull(gotProducerCode);
        assertEquals(expectedGotProducerCode, gotProducerCode);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "#ean",
        "#"
    })
    void shouldGetProducerCodeWhenItIsNotSet(String expectedCombinedCode) {

        //given
        ExternalId externalId = new ExternalId(expectedCombinedCode);

        //when
        String gotProducerCode = externalId.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    void shouldGetProducerCodeWhenCombinedCodeIsEmpty() {

        //given
        ExternalId externalId = new ExternalId("");

        //when
        String gotProducerCode = externalId.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    void shouldGetProducerCodeWhenCombinedCodeIsNotGiven() {

        //given
        ExternalId externalId = new ExternalId(null);

        //when
        String gotProducerCode = externalId.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer#ean, ean",
        "#ean, ean"
    })
    void shouldGetEanCodeWhenItIsSet(String expectedCombinedCode, String expectedGotEanCode) {

        //given
        ExternalId externalId = new ExternalId(expectedCombinedCode);

        //when
        String gotEanCode = externalId.getEanCode();

        //then
        assertNotNull(gotEanCode);
        assertEquals(expectedGotEanCode, gotEanCode);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer#",
        "producer",
        "#",
    })
    void shouldGetEanCodeWhenItIsNotSet(String expectedCombinedCode) {

        //given
        ExternalId externalId = new ExternalId(expectedCombinedCode);

        //when
        String gotEanCode = externalId.getEanCode();

        //then
        assertNull(gotEanCode);
    }

    @Test
    void shouldGetEanCodeWhenCombinedCodeIsEmpty() {

        //given
        ExternalId externalId = new ExternalId("");

        //when
        String gotEanCode = externalId.getEanCode();

        //then
        assertNull(gotEanCode);
    }

    @Test
    void shouldGetEanCodeWhenCombinedCodeIsNotGiven() {

        //given
        ExternalId externalId = new ExternalId(null);

        //when
        String gotEanCode = externalId.getEanCode();

        //then
        assertNull(gotEanCode);
    }

}