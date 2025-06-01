package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.OrderApi;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceRequest;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceFile;
import pl.kamil_dywan.api.allegro.response.DocumentIdResponse;
import pl.kamil_dywan.api.allegro.response.OrderDocumentsResponse;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.external.allegro.generated.order.Order;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class OrderService {

    private final OrderApi orderApi;

    private static final ExecutorService ordersExecutorService = Executors.newFixedThreadPool(8);

    public OrderService(OrderApi orderApi){

        this.orderApi = orderApi;
    }

    public OrderResponse getPage(int offset, int limit) throws UnloggedException, IllegalStateException {

        HttpResponse<String> gotResponse = orderApi.getOrders(offset, limit);

        OrderResponse gotOrderResponse = Api.extractBody(gotResponse, OrderResponse.class);

        List<Order> gotOrders = gotOrderResponse.getOrders();

        setOrdersDocumentsExist(gotOrders);

        gotOrders
            .forEach(order -> order.addDeliveryToOrderItems());

        return gotOrderResponse;
    }

    private String createDocument(String orderId) throws UnloggedException, IllegalStateException{

        String documentName = "Dokument_sprzedazy.pdf";
        String invoiceNumber = UUID.randomUUID().toString();

        CreateOrderInvoiceFile createOrderInvoiceFile = new CreateOrderInvoiceFile(documentName);

        CreateOrderInvoiceRequest createOrderInvoiceRequest = new CreateOrderInvoiceRequest(createOrderInvoiceFile, invoiceNumber);

        HttpResponse<String> gotResponse = orderApi.createDocument(orderId, createOrderInvoiceRequest);

        if(gotResponse.statusCode() != 201){

            throw new IllegalStateException(gotResponse.body());
        }

        DocumentIdResponse documentIdResponse = Api.extractBody(gotResponse, DocumentIdResponse.class);

        return documentIdResponse.getId();
    }

    private void saveDocument(String orderId, String documentId, byte[] data) throws UnloggedException, IllegalStateException{

        HttpResponse<String> gotResponse = orderApi.saveDocument(orderId, documentId, data);

        if(gotResponse.statusCode() != 200){

            throw new IllegalStateException(gotResponse.body());
        }
    }

    public void uploadDocument(String orderId, File documentFile) throws UnloggedException, IOException, IllegalStateException {

        Path filePath = documentFile.toPath();
        byte[] fileData = Files.readAllBytes(filePath);

        String createdDocumentId = createDocument(orderId);

        saveDocument(orderId, createdDocumentId, fileData);
    }

    private void setOrdersDocumentsExist(List<Order> orders) throws UnloggedException, IllegalStateException{

        List<Callable<Void>> toExecuteList = new ArrayList<>();

        orders
            .forEach(order -> {

                Callable<Void> toExecute = () -> {

                    setDocumentsExist(order);

                    return null;
                };

                toExecuteList.add(toExecute);
            });

        try{

            List<Future<Void>> gotFutureList = ordersExecutorService.invokeAll(toExecuteList);

            for(Future<Void> gotFuture : gotFutureList){

                if(gotFuture.isCancelled()){

                    throw new IllegalStateException("Order document fetch was canceled");
                }

                gotFuture.get();
            }
        }
        catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not fetch orders documents");
        }
    }

    private void setDocumentsExist(Order order) throws UnloggedException, IllegalStateException{

        String orderId = order.getId().toString();

        boolean gotResult = documentsExist(orderId);

        order.setHasDocument(gotResult);
    }

    private boolean documentsExist(String orderId) throws UnloggedException, IllegalStateException{

        HttpResponse<String> gotResponse = orderApi.getDocuments(orderId);

        if(gotResponse.statusCode() != 200){

            throw new IllegalStateException(gotResponse.body());
        }

        OrderDocumentsResponse orderDocumentsResponse = Api.extractBody(gotResponse, OrderDocumentsResponse.class);

        return orderDocumentsResponse.getInvoices().size() > 0;
    }
}
