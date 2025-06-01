package pl.kamil_dywan.mapper.integration;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.external.subiektgt.generated.InvoiceBatch;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.file.read.XMLFileReader;
import pl.kamil_dywan.file.write.FileWriter;
import pl.kamil_dywan.file.write.XMLFileWriter;
import pl.kamil_dywan.mapper.invoice.InvoiceBatchMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceBatchMapperTestIT {

    FileReader<OrderResponse> allegroOrderReader = new JSONFileReader<>(OrderResponse.class);
    FileReader<InvoiceBatch> subiektOrderReader = new XMLFileReader<>(InvoiceBatch.class);
    FileWriter<InvoiceBatch> subiektOrderWriter = new XMLFileWriter<>(InvoiceBatch.class);

    @Test
    void shouldMap() throws Exception {

        //given
        OrderResponse allegroOrderResponse = allegroOrderReader.load("data/allegro/order-invoice-company.json");
        InvoiceBatch expectedSubiektBatch = subiektOrderReader.load("data/subiekt/order-for-allegro-minimalized.xml");
        String expectedSubiektBatchStr = subiektOrderWriter.writeToStr(expectedSubiektBatch);
        expectedSubiektBatchStr = TestUtils.removeWhiteSpace(expectedSubiektBatchStr);

        List<Order> allegroOrders = allegroOrderResponse.getOrders();
        allegroOrders.forEach(order -> order.addDeliveryToOrderItems());

        //when
        InvoiceBatch convertedInvoiceBatch = InvoiceBatchMapper.map("Subiekt", "Miasto", allegroOrders);
        String convertedBatchStr = subiektOrderWriter.writeToStr(convertedInvoiceBatch);
        convertedBatchStr = TestUtils.removeWhiteSpace(convertedBatchStr);

        //then
        assertNotNull(convertedInvoiceBatch);
        assertEquals(expectedSubiektBatchStr, convertedBatchStr);
    }
}