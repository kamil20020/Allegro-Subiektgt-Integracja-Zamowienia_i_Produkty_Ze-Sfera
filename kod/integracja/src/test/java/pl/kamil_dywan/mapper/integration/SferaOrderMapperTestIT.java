package pl.kamil_dywan.mapper.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class SferaOrderMapperTestIT {

    private static final FileReader<OrderResponse> allegroOrderReader = new JSONFileReader<>(OrderResponse.class);

    private static Order loadOrder(String allegroOrderFilePath){

        try {

            OrderResponse orderResponse = allegroOrderReader.load(allegroOrderFilePath);

            Order order = orderResponse.getOrders().get(0);

            order.addDeliveryToOrderItems();

            return order;
        }
        catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource(value = {
        "data/allegro/order-invoice-person.json, data/sfera/invoice-person.json",
        "data/allegro/order-invoice-company.json, data/sfera/invoice-company.json",
        "data/allegro/order-no-invoice-company.json, data/sfera/receipt.json",
    })
    void shouldMap(String allegroOrderFilePath,  String expectedSferaOrderFilePath) throws JsonProcessingException, URISyntaxException {

        //given
        Order allegroOrder = loadOrder(allegroOrderFilePath);

        String expectedSferaOrderStr = FileReader.loadStrFromFile(expectedSferaOrderFilePath);
        expectedSferaOrderStr = TestUtils.removeWhiteSpace(expectedSferaOrderStr);

        //when
        CreateOrderRequest createOrderRequest = SferaOrderMapper.map(allegroOrder);
        String createOrderRequestStr = objectMapper.writeValueAsString(createOrderRequest);
        createOrderRequestStr = TestUtils.removeWhiteSpace(createOrderRequestStr);

        //then
        assertNotNull(createOrderRequest);
        assertEquals(expectedSferaOrderStr, createOrderRequestStr);
    }

}