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
//        ExternalId externalId = new ExternalId(expectedProducerCode, expectedEanCode);

        //then
//        assertNotNull(externalId);
//        assertEquals(expectedCombinedCode, externalId.getId());
    }

}