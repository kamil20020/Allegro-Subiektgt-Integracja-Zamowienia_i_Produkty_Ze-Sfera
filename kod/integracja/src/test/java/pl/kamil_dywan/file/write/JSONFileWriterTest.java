package pl.kamil_dywan.file.write;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONFileWriterTest {

    private static final String savedOrderFilePath = "data/allegro/swagger.json";

    private static String validAllegroOrderResponseStr = "";

    static {

        try {
            validAllegroOrderResponseStr = FileReader.loadStrFromFile(savedOrderFilePath);
            validAllegroOrderResponseStr = TestUtils.removeWhiteSpace(validAllegroOrderResponseStr);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static final FileWriter<OrderResponse> fileWriter = new JSONFileWriter<>();
    private static final FileReader<OrderResponse> fileReader = new JSONFileReader<>(OrderResponse.class);

    @Test
    public void shouldWriteOrderResponse() throws Exception {

        //given
        String toCreateNormalFilePath = "./target/classes/allegro-test.json";
        String createdMavenFilePath = "allegro-test.json";

        OrderResponse validOrderResponse = fileReader.loadFromStr(validAllegroOrderResponseStr);

        //when
        fileWriter.save(toCreateNormalFilePath, validOrderResponse);
        
        String savedOrder = FileReader.loadStrFromFile(createdMavenFilePath, StandardCharsets.UTF_8);
        savedOrder = TestUtils.removeWhiteSpace(savedOrder);

        //then
        assertEquals(validAllegroOrderResponseStr, savedOrder);
    }

    @Test
    public void shouldSaveToString() throws Exception {

        //given

        //when
        OrderResponse toSaveOrderResponse = fileReader.loadFromStr(validAllegroOrderResponseStr);
        String gotOrderResponseStr = fileWriter.writeToStr(toSaveOrderResponse);
        gotOrderResponseStr = TestUtils.removeWhiteSpace(gotOrderResponseStr);

        //then
        assertEquals(validAllegroOrderResponseStr, gotOrderResponseStr);
    }

}
