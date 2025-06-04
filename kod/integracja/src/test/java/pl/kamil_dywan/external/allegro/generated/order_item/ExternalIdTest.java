package pl.kamil_dywan.external.allegro.generated.order_item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ExternalIdTest {

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
    public void shouldGetCombinedCodeForEmptyId(){

        //given

        //when
        String gotCombinedKey = ExternalId.getCombinedCode("", "");

        //then
        assertNull(gotCombinedKey);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer#ean, producer",
        "producer#, producer"
    })
    void shouldGetProducerCodeWhenIsSet(String expectedCombinedCode, String expectedGotProducerCode) {

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
        "#ean,",
        "#,",
    })
    void shouldGetProducerCodeWhenIsNotSet(String expectedCombinedCode) {

        //given
        ExternalId externalId = new ExternalId(expectedCombinedCode);

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
    void shouldGetEanCodeWhenIsSet(String expectedCombinedCode, String expectedGotEanCode) {

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
        "producer#,",
        "#,",
    })
    void shouldGetEanCodeWhenIsNotSet(String expectedCombinedCode) {

        //given
        ExternalId externalId = new ExternalId(expectedCombinedCode);

        //when
        String gotEanCode = externalId.getEanCode();

        //then
        assertNull(gotEanCode);
    }

}