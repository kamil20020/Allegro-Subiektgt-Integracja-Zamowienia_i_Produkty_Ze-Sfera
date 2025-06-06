package pl.kamil_dywan.service.unit;

import org.apache.commons.lang.math.IntRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.OrderApi;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceFile;
import pl.kamil_dywan.api.allegro.request.CreateOrderInvoiceRequest;
import pl.kamil_dywan.api.allegro.response.DocumentIdResponse;
import pl.kamil_dywan.api.allegro.response.OrderDocumentsResponse;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.service.OrderService;
import pl.kamil_dywan.service.SferaOrderService;

import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderApi orderApi;

    @Mock
    private SferaOrderService sferaOrderService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldSetOrdersDocumentsExist() throws InterruptedException {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .build();

        Order order1 = Order.builder()
            .id(UUID.randomUUID())
            .build();

        List<Order> orders = List.of(order, order1);

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        OrderDocumentsResponse expectedResponse = new OrderDocumentsResponse(new ArrayList<>());

        ExecutorService executorServiceMock = Mockito.mock(ExecutorService.class);
        TestUtils.updatePrivateStaticField(OrderService.class, "ordersExecutorService", executorServiceMock);

        //when
        Mockito.when(executorServiceMock.invokeAll(any())).thenAnswer(a -> {

            List<Callable<Void>> tasks = a.getArgument(0);

            List<Future<Void>> tasksFutures = new ArrayList<>();

            for(Callable<Void> task : tasks){

                task.call();

                tasksFutures.add(CompletableFuture.completedFuture(null));
            }

            return tasksFutures;
        });

        Mockito.when(orderApi.getDocuments(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedResponse);

            orderService.setOrdersDocumentsExist(orders);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, OrderDocumentsResponse.class), times(2));
        }

        //then
        Mockito.verify(executorServiceMock).invokeAll(any());

        for(Order o : orders){

            String orderId = o.getId().toString();

            Mockito.verify(orderApi).getDocuments(orderId);
        }
    }

    @Test
    void shouldSetDocumentExistWhenExist() {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .build();

        String expectedOrderId = order.getId().toString();

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        List<Object> invoices = List.of(new Object(), new Object());

        OrderDocumentsResponse response = new OrderDocumentsResponse(invoices);

        //when
        Mockito.when(orderApi.getDocuments(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(response);

            orderService.setOrderDocumentExist(order);

            boolean doesDocumentExist = order.isHasDocument();

            //then
            assertTrue(doesDocumentExist);

            apiMock.verify(() -> Api.extractBody(any(TestHttpResponse.class), eq(OrderDocumentsResponse.class)));
        }

        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(orderApi).getDocuments(orderIdCaptor.capture());

        String gotOrderId = orderIdCaptor.getValue();

        assertNotNull(gotOrderId);
        assertEquals(expectedOrderId, gotOrderId);
    }

    @Test
    void shouldSetDocumentExistWhenDoesNotExist() {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .build();

        String expectedOrderId = order.getId().toString();

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        OrderDocumentsResponse response = new OrderDocumentsResponse(new ArrayList<>());

        //when
        Mockito.when(orderApi.getDocuments(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(response);

            orderService.setOrderDocumentExist(order);

            boolean doesDocumentExist = order.isHasDocument();

            //then
            assertFalse(doesDocumentExist);

            apiMock.verify(() -> Api.extractBody(any(TestHttpResponse.class), eq(OrderDocumentsResponse.class)));
        }

        ArgumentCaptor<String> orderIdCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(orderApi).getDocuments(orderIdCaptor.capture());

        String gotOrderId = orderIdCaptor.getValue();

        assertNotNull(gotOrderId);
        assertEquals(expectedOrderId, gotOrderId);
    }

    @Test
    void shouldSetOrdersExternalIds() throws InterruptedException {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .build();

        Order order1 = Order.builder()
            .id(UUID.randomUUID())
            .build();

        List<Order> orders = List.of(order, order1);

        List<String> expectedExternalIds = List.of("external", "external1");

        ExecutorService executorServiceMock = Mockito.mock(ExecutorService.class);
        TestUtils.updatePrivateStaticField(OrderService.class, "ordersExecutorService", executorServiceMock);

        //when
        for(int i = 0; i < orders.size(); i++){

            Order o = orders.get(i);
            String orderId = o.getId().toString();
            String expectedExternalId = expectedExternalIds.get(i);

            Mockito.when(sferaOrderService.getSubiektIdByExternalId(orderId)).thenReturn(expectedExternalId);
        }

        Mockito.when(executorServiceMock.invokeAll(any())).thenAnswer(a -> {

            List<Callable<Void>> tasks = a.getArgument(0);

            List<Future<Void>> tasksFutures = new ArrayList<>();

            for(Callable<Void> task : tasks){

                task.call();

                tasksFutures.add(CompletableFuture.completedFuture(null));
            }

            return tasksFutures;
        });

        orderService.setOrdersExternalIds(orders);

        //then
        for (int i = 0; i < orders.size(); i++) {

            Order o = orders.get(i);

            String orderId = o.getId().toString();

            String expectedExternalId = expectedExternalIds.get(i);

            assertEquals(expectedExternalId, o.getExternalId());

            Mockito.verify(sferaOrderService).getSubiektIdByExternalId(orderId);
        }

        Mockito.verify(executorServiceMock).invokeAll(any());
    }

    @Test
    void shouldSetOrderExternalIdWhenItExist() {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .build();

        String expectedOrderId = order.getId().toString();
        String expectedExternalId = "subiekt";

        //when
        Mockito.when(sferaOrderService.getSubiektIdByExternalId(any())).thenReturn(expectedExternalId);

        orderService.setOrderExternalId(order);

        String gotExternalId = order.getExternalId();

        //then
        assertNotNull(gotExternalId);
        assertEquals(expectedExternalId, gotExternalId);

        Mockito.verify(sferaOrderService).getSubiektIdByExternalId(expectedOrderId);
    }

    @Test
    void shouldSetOrderExternalIdWhenItDoesNotExist() {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .build();

        String expectedOrderId = order.getId().toString();

        //when
        Mockito.when(sferaOrderService.getSubiektIdByExternalId(any())).thenReturn(null);

        orderService.setOrderExternalId(order);

        String gotExternalId = order.getExternalId();

        //then
        assertNull(gotExternalId);

        Mockito.verify(sferaOrderService).getSubiektIdByExternalId(expectedOrderId);
    }

    @Test
    void uploadDocuments() throws InterruptedException {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .hasDocument(true)
            .build();

        Order order1 = Order.builder()
            .id(UUID.randomUUID())
            .hasDocument(false)
            .build();

        List<Order> orders = List.of(order, order1);

        byte[] content = "content".getBytes(StandardCharsets.UTF_8);
        byte[] content1 = "content1".getBytes(StandardCharsets.UTF_8);

        List<byte[]> contents = List.of(content, content1);

        List<Integer> expectedCreatedDocumentsOrdersIndices = List.of(1);

        ExecutorService executorServiceMock = Mockito.mock(ExecutorService.class);
        TestUtils.updatePrivateStaticField(OrderService.class, "ordersExecutorService",executorServiceMock);

        HttpResponse<String> createHttpResponse = TestHttpResponse.builder()
            .statusCode(201)
            .uri(URI.create("http://localhost:9000"))
            .build();

        HttpResponse<String> uploadHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .uri(URI.create("http://localhost:9000"))
            .build();

        DocumentIdResponse documentIdResponse = new DocumentIdResponse("123");

        //when
        Mockito.when(sferaOrderService.getContents(any())).thenReturn(contents);
        Mockito.when(orderApi.createDocument(any(), any())).thenReturn(createHttpResponse);
        Mockito.when(orderApi.saveDocument(any(), any(), any())).thenReturn(uploadHttpResponse);

        Mockito.when(executorServiceMock.invokeAll(any())).thenAnswer(a -> {

            List<Callable<Void>> tasks = a.getArgument(0);

            List<Future<Void>> tasksFutures = new ArrayList<>();

            for(Callable<Void> task : tasks){

                task.call();

                tasksFutures.add(CompletableFuture.completedFuture(null));
            }

            return tasksFutures;
        });
        List<Integer> createdIndexesOfOrdersDocuments;

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){

            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(documentIdResponse);

            createdIndexesOfOrdersDocuments = orderService.uploadDocuments(orders);

            apiMock.verify(() -> Api.extractBody(any(TestHttpResponse.class), eq(DocumentIdResponse.class)));
        }

        //then
        assertNotNull(createdIndexesOfOrdersDocuments);
        assertTrue(createdIndexesOfOrdersDocuments.size() > 0);
        assertTrue(createdIndexesOfOrdersDocuments.containsAll(expectedCreatedDocumentsOrdersIndices));

        String lastOrderId = orders.get(1).getId().toString();

        Mockito.verify(orderApi).createDocument(eq(lastOrderId), any());
        Mockito.verify(orderApi).saveDocument(eq(lastOrderId), any(), any());
        Mockito.verify(sferaOrderService).getContents(orders);
        Mockito.verify(executorServiceMock).invokeAll(any());
    }

    @Test
    void shouldUploadDocument() {

        //given
        String orderId = UUID.randomUUID().toString();

        byte[] documentContent = "content".getBytes(StandardCharsets.UTF_8);

        HttpResponse<String> createHttpResponse = TestHttpResponse.builder()
            .statusCode(201)
            .uri(URI.create("http://localhost:9000"))
            .build();

        HttpResponse<String> uploadHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .uri(URI.create("http://localhost:9000"))
            .build();

        DocumentIdResponse documentIdResponse = new DocumentIdResponse("123");

        //when
        Mockito.when(orderApi.createDocument(any(), any())).thenReturn(createHttpResponse);
        Mockito.when(orderApi.saveDocument(any(), any(), any())).thenReturn(uploadHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){

            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class),any())).thenReturn(documentIdResponse);

            orderService.uploadDocument(orderId, documentContent);

            apiMock.verify(() -> Api.extractBody(any(TestHttpResponse.class), eq(DocumentIdResponse.class)));
        }

        //then
        ArgumentCaptor<CreateOrderInvoiceRequest> createRequestCaptor = ArgumentCaptor.forClass(CreateOrderInvoiceRequest.class);

        Mockito.verify(orderApi).createDocument(eq(orderId), createRequestCaptor.capture());

        CreateOrderInvoiceRequest gotRequest = createRequestCaptor.getValue();

        assertNotNull(gotRequest);
        assertNotNull(gotRequest.getInvoiceNumber());

        CreateOrderInvoiceFile createOrderInvoiceFile = gotRequest.getCreateOrderInvoiceFile();

        assertNotNull(createOrderInvoiceFile);
        assertEquals("Dokument_sprzedazy.pdf", createOrderInvoiceFile.getName());

        Mockito.verify(orderApi).saveDocument(orderId, documentIdResponse.getId(), documentContent);
    }
}