package pl.kamil_dywan.service.integration;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.file.read.FileReader;
import pl.kamil_dywan.file.read.JSONFileReader;
import pl.kamil_dywan.service.BasicInfoService;
import pl.kamil_dywan.service.ReceiptService;

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReceiptServiceTestIT {

    private static final String validReceiptsFilePath = "data/subiekt/receipt-minimalized.epp";
    private static final String allegroReceiptsOrdersFilePath = "data/allegro/order-no-invoice-company.json";

    private static final Charset charset = Charset.forName("windows-1250");

    private static String validReceipts = "";

    static {

        try{
            validReceipts = FileReader.loadStrFromFile(validReceiptsFilePath, charset);
            validReceipts = TestUtils.removeWhiteSpace(validReceipts);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static final FileReader<OrderResponse> allegroReceiptsFileReader = new JSONFileReader<>(OrderResponse.class);

    @Test
    void shouldWriteReceiptsToFile() throws Exception {

        //given
        String toSaveFilePath = "test-receipts.epp";

        OrderResponse allegroReceiptsResponse = allegroReceiptsFileReader.load(allegroReceiptsOrdersFilePath);
        List<Order> allegroReceipts = allegroReceiptsResponse.getOrders();

        allegroReceipts
            .forEach(receipt -> receipt.addDeliveryToOrderItems());

        BasicInfoService basicInfoService = new BasicInfoService();

        ReceiptService receiptService = new ReceiptService(basicInfoService);

        //when
        receiptService.writeReceiptsToFile(allegroReceipts, toSaveFilePath);

        String gotSavedReceipts = FileReader.loadStrFromFileOutside(toSaveFilePath, charset);
        gotSavedReceipts = TestUtils.removeWhiteSpace(gotSavedReceipts);

        //then
        assertNotNull(gotSavedReceipts);
        assertEquals(validReceipts, gotSavedReceipts);
    }
}