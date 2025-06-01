package pl.kamil_dywan.file.read;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.file.write.FileWriter;
import pl.kamil_dywan.file.write.JSONFileWriter;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class JSONFileReaderTest {

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

    private static final FileReader<OrderResponse> fileReader = new JSONFileReader<>(OrderResponse.class);
    private static final FileWriter<OrderResponse> fileWriter = new JSONFileWriter<>();

    @Test
    void shouldLoadOrderResponse() throws Exception {

        //given

        //when
        OrderResponse gotAllegroOrderResponse = fileReader.load(savedOrderFilePath);
        String encodedGotAllegroOrderResponse = fileWriter.writeToStr(gotAllegroOrderResponse);
        encodedGotAllegroOrderResponse = TestUtils.removeWhiteSpace(encodedGotAllegroOrderResponse);

        //then
        assertEquals(validAllegroOrderResponseStr, encodedGotAllegroOrderResponse);
    }


    @Test
    void shouldGetFromString() throws Exception {

        //given

        //when
        OrderResponse gotOrderResponse = fileReader.loadFromStr(validAllegroOrderResponseStr);
        String gotOrderResponseStr = fileWriter.writeToStr(gotOrderResponse);

        //then
        assertEquals(validAllegroOrderResponseStr, gotOrderResponseStr);
    }
}