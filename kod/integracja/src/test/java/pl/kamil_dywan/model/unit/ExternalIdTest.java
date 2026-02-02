package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.own.offer.Signature;

import static org.junit.jupiter.api.Assertions.*;

class ExternalIdTest {

    @Test
    public void shouldCreateExternalId(){

        //given
        Signature signature = Signature.extract("12#3");

        //when
        ExternalId externalId = new ExternalId(signature);

        //then
        assertEquals(signature.toString(), externalId.getId());
    }

}