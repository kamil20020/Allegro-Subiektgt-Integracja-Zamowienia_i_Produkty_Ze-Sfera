package pl.kamil_dywan.service;

import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;
import pl.kamil_dywan.external.subiektgt.own.receipt.ReceiptHeader;
import pl.kamil_dywan.external.subiektgt.own.receipt.ReceiptItem;
import pl.kamil_dywan.file.EppGroupSpecialType;
import pl.kamil_dywan.file.write.EppFileWriter;
import pl.kamil_dywan.file.write.FileWriter;
import pl.kamil_dywan.mapper.receipt.ReceiptHeaderMapper;
import pl.kamil_dywan.mapper.receipt.ReceiptItemMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ReceiptService {

    private final BasicInfoService basicInfoService;

    private static final List<String> lastHeaders = List.of(
        "DATYZAKONCZENIA", "NUMERYIDENTYFIKACYJNENABYWCOW", "PRZYCZYNYKOREKT", "DOKUMENTYFISKALNEVAT",
        "OPLATYDODATKOWE", "WYMAGALNOSCMPP", "OPLATACUKROWA", "DOKUMENTYZNACZNIKIJPKVAT",
        "INFORMACJEWSTO", "DATYUJECIAKOREKT"
    );

    private static final Integer[] receiptHeaderWriteIndexes = new Integer[]{
        0, 1, 2, 3, 6, 18, 19, 20, 21, 22, 24, 25, 26, 27, 28, 29, 30,
        32, 34, 35, 36, 37, 38, 39, 40, 44, 45, 46, 47, 52, 53,
        54, 56, 58, 61
    };

    private static final Integer[] receiptContentWriteIndexes = new Integer[]{
        0, 1, 2, 9, 10, 12, 13, 14, 15, 16, 17, 18, 19
    };

    private static final Integer RECEIPT_HEADER_REAL_LENGTH = 62;
    private static final Integer RECEIPT_CONTENT_REAL_LENGTH = 22;

    public ReceiptService(BasicInfoService basicInfoService){

        this.basicInfoService = basicInfoService;
    }

    private FileWriter<Object> loadFileWriter(int numberOfReceipts){

        List<String> headersNames = new ArrayList<>();
        List<Integer> toWriteHeadersIndexes = new ArrayList<>();
        List<Integer> rowsLengths = new ArrayList<>();
        LinkedHashMap<String, Integer[]> writeIndexes = new LinkedHashMap<>();

        writeIndexes.put(EppGroupSpecialType.EMPTY_CONTENT.toString(), receiptHeaderWriteIndexes);
        writeIndexes.put(EppGroupSpecialType.EMPTY_HEADER.toString(), receiptContentWriteIndexes);

        for(int i = 0; i < numberOfReceipts * 2; i += 2){

            headersNames.add(EppGroupSpecialType.EMPTY_CONTENT.toString());
            headersNames.add(EppGroupSpecialType.EMPTY_HEADER.toString());

            toWriteHeadersIndexes.add(i);
            toWriteHeadersIndexes.add(i + 1);

            rowsLengths.add(RECEIPT_HEADER_REAL_LENGTH);
            rowsLengths.add(RECEIPT_CONTENT_REAL_LENGTH);
        }

        EppFileWriter<Object> subiektParentFileWriter = new EppFileWriter<>(headersNames, toWriteHeadersIndexes, rowsLengths, writeIndexes);

        subiektParentFileWriter.appendHeaderNames(ProductService.getFileWriter());

        headersNames.addAll(lastHeaders);

        return subiektParentFileWriter;
    }

    public void writeReceiptsToFile(List<Order> allegroOrders, String savedFilePath){

        String sellerLocation = basicInfoService.getLocation().orElse("");

        FileWriter<Object> subiektReceiptFileWriter = loadFileWriter(allegroOrders.size());

        List<Object> receiptData = new ArrayList<>();

        for(Order order : allegroOrders){

            OrderMoneyStats orderMoneyStats = OrderMoneyStats.getSummary(order);
            ReceiptHeader receiptHeader = ReceiptHeaderMapper.map(orderMoneyStats.orderTotalMoneyStats(), sellerLocation);

            List<ReceiptItem> receiptItems = getOrderReceiptItems(order, orderMoneyStats);

            receiptData.add(receiptHeader);
            receiptData.add(receiptItems);
        }

        try {
            subiektReceiptFileWriter.save(savedFilePath, receiptData);
        }
        catch (IOException | URISyntaxException e) {

            e.printStackTrace();

            throw new IllegalStateException(e.getMessage());
        }
    }

    private List<ReceiptItem> getOrderReceiptItems(Order order, OrderMoneyStats orderMoneyStats){

        List<ReceiptItem> receiptItems = new ArrayList<>();

        for(int i = 0; i < order.getOrderItems().size(); i++){

            OrderItem orderItem = order.getOrderItems().get(i);
            OrderItemMoneyStats orderItemMoneyStats = orderMoneyStats.orderItemsMoneyStats().get(i);

            ReceiptItem receiptItem = ReceiptItemMapper.map(orderItem, orderItemMoneyStats, i + 1);

            receiptItems.add(receiptItem);
        }

        handleDelivery(order, receiptItems);

        return receiptItems;
    }

    private void handleDelivery(Order order, List<ReceiptItem> receiptItems){

        if(order.hasDelivery()){

            int lastReceiptItemIndex = receiptItems.size() - 1;
            ReceiptItem deliveryReceiptItem = receiptItems.get(lastReceiptItemIndex);

            deliveryReceiptItem.setProductType(ProductType.SERVICES);
            deliveryReceiptItem.setId("DOSTAWA123");
        }
    }

}
