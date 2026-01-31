package pl.kamil_dywan.service;

import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.OrderApi;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceRequest;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceFile;
import pl.kamil_dywan.api.allegro.response.DocumentIdResponse;
import pl.kamil_dywan.api.allegro.response.OrderDocumentsResponse;
import pl.kamil_dywan.exception.ConflictException;
import pl.kamil_dywan.exception.UnloggedException;
import pl.kamil_dywan.api.allegro.response.OrderResponse;
import pl.kamil_dywan.external.allegro.generated.order.Order;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class OrderService {

    private final OrderApi orderApi;

    private final SferaOrderService sferaOrderService;

    private static final ExecutorService ordersExecutorService = Executors.newFixedThreadPool(8);

    public OrderService(OrderApi orderApi, SferaOrderService sferaOrderService){

        this.orderApi = orderApi;
        this.sferaOrderService = sferaOrderService;
    }

    public OrderResponse getPage(int offset, int limit) throws UnloggedException, IllegalStateException {

        HttpResponse<String> gotResponse = orderApi.getOrders(offset, limit);

        OrderResponse gotOrderResponse = Api.extractBody(gotResponse, OrderResponse.class);

        List<Order> gotOrders = gotOrderResponse.getOrders().stream()
            .filter(order -> !order.isCancelled())
            .collect(Collectors.toList());

        gotOrderResponse.setOrders(gotOrders);

        setOrdersDocumentsExist(gotOrders);

        gotOrders
            .forEach(order -> order.addDeliveryToOrderItems());

        setOrdersExternalIds(gotOrders);

        return gotOrderResponse;
    }

    public void setOrdersDocumentsExist(List<Order> orders) throws UnloggedException, IllegalStateException{

        List<Callable<Void>> toExecuteList = new ArrayList<>();

        orders
            .forEach(order -> {

                Callable<Void> toExecute = () -> {

                    setOrderDocumentExist(order);

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

    public void setOrderDocumentExist(Order order) throws UnloggedException, IllegalStateException{

        String orderId = order.getId().toString();

        HttpResponse<String> gotResponse = orderApi.getDocuments(orderId);

        if(gotResponse.statusCode() != 200){

            throw new IllegalStateException(gotResponse.body());
        }

        OrderDocumentsResponse orderDocumentsResponse = Api.extractBody(gotResponse, OrderDocumentsResponse.class);

        boolean documentExists = orderDocumentsResponse.documentsExist();

        order.setHasDocument(documentExists);
    }

    public void setOrdersExternalIds(List<Order> orders){

        List<Callable<Void>> toExecuteList = new ArrayList<>();

        for(Order order : orders){

            Callable<Void> toExecute = () -> {

                setOrderExternalId(order);

                return null;
            };

            toExecuteList.add(toExecute);
        }

        try{

            List<Future<Void>> gotFutureList = ordersExecutorService.invokeAll(toExecuteList);

            for(Future<Void> gotFuture : gotFutureList){

                if(gotFuture.isCancelled()){

                    throw new IllegalStateException("Could not get sfera external id");
                }

                gotFuture.get();
            }
        }
        catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not get sfera external ids");
        }
    }

    public void setOrderExternalId(Order order){

        String externalOrderId = order.getId().toString();

        String gotSubiektNr = sferaOrderService.getSubiektIdByExternalId(externalOrderId);

        order.setExternalId(gotSubiektNr);
    }

    public List<Integer> uploadDocuments(List<Order> orders, Map<String, String> errors) throws UnloggedException, IllegalArgumentException {

        List<byte[]> contents = sferaOrderService.getContents(orders);

        List<Callable<Void>> toExecuteList = new ArrayList<>();

        List<Integer> uploadedOrdersIndices = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < orders.size(); i++) {

            Order selectedOrder = orders.get(i);

            byte[] content = contents.get(i);

            int finalI = i;

            Callable<Void> toExecute = () -> {

                try {

                    uploadDocument(selectedOrder, content);

                    uploadedOrdersIndices.add(finalI);
                }
                catch (IllegalStateException | ConflictException e) {

                    String selectedOrderIdStr = selectedOrder.getId().toString();

                    errors.put(selectedOrderIdStr, e.getMessage());

                    e.printStackTrace();
                }

                return null;
            };

            toExecuteList.add(toExecute);
        }

        try{

            List<Future<Void>> gotFutureList = ordersExecutorService.invokeAll(toExecuteList);

            for(Future<Void> gotFuture : gotFutureList){

                if(gotFuture.isCancelled()){

                    throw new IllegalStateException("Could not save Allegro document");
                }

                gotFuture.get();
            }
        }
        catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            throw new IllegalStateException("Could not save Allegro documents");
        }

        return uploadedOrdersIndices;
    }

    public void uploadDocument(Order order, byte[] documentContent) throws IllegalStateException, ConflictException, UnloggedException {

        if(documentContent.length == 0){

            throw new IllegalStateException("Nie odnaleziono dokumentu w Subiekt GT");
        }

        if(order.isHasDocument()){

            throw new ConflictException("Przypisano już dokument do zamówienia w Allegro");
        }

        String orderId = order.getId().toString();

        String createdDocumentId = createDocument(orderId);

        HttpResponse<String> gotResponse = orderApi.saveDocument(orderId, createdDocumentId, documentContent);

        if(gotResponse.statusCode() != 200){

            throw new IllegalStateException(gotResponse.body());
        }
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

    public void redirectToOffer(String orderId){

        orderApi.redirectToOrder(orderId);
    }

}
