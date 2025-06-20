package pl.kamil_dywan.mapper.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.mapper.sfera.SferaProductSetMapper;

import static org.junit.jupiter.api.Assertions.*;

class SferaProductSetMapperTestIT {

    private static final FileReader<ProductOfferResponse> allegroProductSetReader = new JSONFileReader<>(ProductOfferResponse.class);

    private static final String expectedAllegroProductProductSetFilePath = "data/allegro/product-offer-set-detailed-minimalized.json";
    private static final String expectedSferaProductSetFilePath = "data/sfera/product-set.json";

    private static ProductOfferResponse expectedAllegroProductSet;
    private static String expectedSferaProductSetStr = null;

    static {

        try {

            expectedAllegroProductSet = allegroProductSetReader.load(expectedAllegroProductProductSetFilePath);

            expectedSferaProductSetStr = FileReader.loadStrFromFile(expectedSferaProductSetFilePath);
            expectedSferaProductSetStr = TestUtils.removeWhiteSpace(expectedSferaProductSetStr);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldMap() throws JsonProcessingException {

        //given

        //when
        ProductSet gotProductSet = SferaProductSetMapper.map(expectedAllegroProductSet);

        String gotProductSetStr = objectMapper.writeValueAsString(gotProductSet);
        gotProductSetStr = TestUtils.removeWhiteSpace(gotProductSetStr);

        //then
        assertNotNull(gotProductSet);
        assertEquals(expectedSferaProductSetStr, gotProductSetStr);
    }
}