package pl.kamil_dywan.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.sfera.request.GetDocumentByExternalIdRequest;
import pl.kamil_dywan.api.sfera.SferaOrderApi;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.api.sfera.request.GetOrderRequest;
import pl.kamil_dywan.api.sfera.response.CreatedDocumentResponse;
import pl.kamil_dywan.api.sfera.response.DocumentResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.sfera.generated.ResponseStatus;
import pl.kamil_dywan.service.SferaOrderService;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SferaOrderServiceTest {

    @Mock
    private SferaOrderApi sferaOrderApi;

    @InjectMocks
    private SferaOrderService sferaOrderService;

    @Test
    void shouldCreateOrders() {

        //given
        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        int expectedNumberOfCreatedOrders = 2;

        Cost summaryCost = new Cost(new BigDecimal("22.48"), Currency.PLN);
        
        Summary summary = new Summary(summaryCost);

        Invoice invoice = Invoice.builder()
            .required(false)
            .build();
        
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .externalId("external")
            .build();

        Order order1 = Order.builder()
            .id(UUID.randomUUID())
            .hasDocument(true)
            .build();

        Order order2 = Order.builder()
            .id(UUID.randomUUID())
            .summary(summary)
            .invoice(invoice)
            .orderItems(new ArrayList<>())
            .externalId("")
            .build();

        Order order3 = Order.builder()
            .id(UUID.randomUUID())
            .summary(summary)
            .invoice(invoice)
            .orderItems(new ArrayList<>())
            .build();

        List<Order> orders = List.of(order, order1, order2, order3);

        List<String> ordersIds = orders.stream()
            .map(o -> o.getId().toString())
            .collect(Collectors.toList());

        String expectedResponseStr = "{\"order_ref:\" \"external\"}";

        GeneralResponse expectedResponse = GeneralResponse.builder()
            .status(ResponseStatus.SUCCESS.toString())
            .data(expectedResponseStr)
            .build();

        CreatedDocumentResponse expectedDocumentResponse = new CreatedDocumentResponse("external");

        //when
        Mockito.when(sferaOrderApi.create(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), eq(GeneralResponse.class))).thenReturn(expectedResponse);
            apiMock.when(() -> Api.extractBody(expectedResponseStr, CreatedDocumentResponse.class)).thenReturn(expectedDocumentResponse);

            int gotNumberOfCreatedOrders = sferaOrderService.create(orders, new HashMap<>());

            assertEquals(expectedNumberOfCreatedOrders, gotNumberOfCreatedOrders);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class), times(expectedNumberOfCreatedOrders));
            apiMock.verify(() -> Api.extractBody(expectedResponseStr, CreatedDocumentResponse.class), times(expectedNumberOfCreatedOrders));
        }

        //then
        for(int i = orders.size() - expectedNumberOfCreatedOrders; i < orders.size(); i++){

            String expectedOrderId = ordersIds.get(i);

            Mockito
                .verify(sferaOrderApi)
                .create(
                    argThat(
                        request -> request.getExternalId().equals(expectedOrderId)
                    )
                );
        }
    }

    @Test
    void shouldCreate() {

        //given
        Cost summaryCost = new Cost(new BigDecimal("22.48"), Currency.PLN);

        Summary summary = new Summary(summaryCost);

        Invoice invoice = Invoice.builder()
            .required(false)
            .build();

        Order order = Order.builder()
            .id(UUID.randomUUID())
            .summary(summary)
            .invoice(invoice)
            .orderItems(new ArrayList<>())
            .delivery(new Delivery())
            .build();

        String expectedOrderId = order.getId().toString();

        String expectedExternalId = "subiekt";

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        String expectedDocumentResponseStr = "{\"order_ref:\" \"" + expectedExternalId + "\"}";

        GeneralResponse expectedResponse = GeneralResponse.builder()
            .status(ResponseStatus.SUCCESS.toString())
            .data(expectedDocumentResponseStr)
            .build();

        CreatedDocumentResponse expectedDocumentResponse = new CreatedDocumentResponse(expectedExternalId);

        //when
        Mockito.when(sferaOrderApi.create(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), eq(GeneralResponse.class))).thenReturn(expectedResponse);
            apiMock.when(() -> Api.extractBody(any(String.class), eq(CreatedDocumentResponse.class))).thenReturn(expectedDocumentResponse);

            sferaOrderService.create(order);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class));
            apiMock.verify(() -> Api.extractBody(expectedDocumentResponseStr, CreatedDocumentResponse.class));
        }

        String gotExternalId = order.getExternalId();

        //then
        assertNotNull(gotExternalId);
        assertEquals(expectedExternalId, gotExternalId);

        ArgumentCaptor<CreateOrderRequest> requestCaptor = ArgumentCaptor.forClass(CreateOrderRequest.class);

        Mockito.verify(sferaOrderApi).create(requestCaptor.capture());

        CreateOrderRequest gotRequest = requestCaptor.getValue();

        assertNotNull(gotRequest);
        assertEquals(expectedOrderId, gotRequest.getExternalId());
    }

    @Test
    void shouldGetSubiektIdByExternalIdWhenItExist() {

        //given
        String expectedOrderId = UUID.randomUUID().toString();

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        String expectedSubiektId = "subiekt";

        GeneralResponse expectedGeneralResponse = GeneralResponse.builder()
            .status(ResponseStatus.SUCCESS.toString())
            .data(expectedSubiektId)
            .build();

        //when
        Mockito.when(sferaOrderApi.getSubiektIdByExternalId(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedGeneralResponse);

            String gotSubiektId = sferaOrderService.getSubiektIdByExternalId(expectedOrderId);

            assertNotNull(gotSubiektId);
            assertEquals(expectedSubiektId, gotSubiektId);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class));
        }

        //then
        ArgumentCaptor<GetDocumentByExternalIdRequest> requestCaptor = ArgumentCaptor.forClass(GetDocumentByExternalIdRequest.class);

        Mockito.verify(sferaOrderApi).getSubiektIdByExternalId(requestCaptor.capture());

        GetDocumentByExternalIdRequest gotRequest = requestCaptor.getValue();

        assertNotNull(gotRequest);
        assertEquals(expectedOrderId, gotRequest.getExternalId());
    }

    @Test
    void shouldGetSubiektIdByExternalIdWhenItDoesNotExist() {

        //given
        String expectedOrderId = UUID.randomUUID().toString();

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        GeneralResponse expectedGeneralResponse = GeneralResponse.builder()
            .status(ResponseStatus.SUCCESS.toString())
            .data("null")
            .build();

        //when
        Mockito.when(sferaOrderApi.getSubiektIdByExternalId(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedGeneralResponse);

            String gotSubiektId = sferaOrderService.getSubiektIdByExternalId(expectedOrderId);

            assertNull(gotSubiektId);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class));
        }

        //then
        ArgumentCaptor<GetDocumentByExternalIdRequest> requestCaptor = ArgumentCaptor.forClass(GetDocumentByExternalIdRequest.class);

        Mockito.verify(sferaOrderApi).getSubiektIdByExternalId(requestCaptor.capture());

        GetDocumentByExternalIdRequest gotRequest = requestCaptor.getValue();

        assertNotNull(gotRequest);
        assertEquals(expectedOrderId, gotRequest.getExternalId());
    }

    @Test
    void shouldGetContents() {

        //given
        Order order = Order.builder()
            .id(UUID.randomUUID())
            .externalId("external")
            .build();

        Order order1 = Order.builder()
            .id(UUID.randomUUID())
            .build();

        Order order2 = Order.builder()
            .id(UUID.randomUUID())
            .build();

        List<Order> orders = List.of(order, order1, order2);

        List<byte[]> expectedContents = List.of(
            "content".getBytes(StandardCharsets.UTF_8),
            new byte[0],
            new byte[0]
        );

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        String expectedContentBase64 = "Y29udGVudA==";
        byte[] expectedContent = "content".getBytes(StandardCharsets.UTF_8);

        String expectedResponseStr = "{\"pdf_file\": \"" + expectedContentBase64 + "\"";

        GeneralResponse expectedResponse = GeneralResponse.builder()
            .status(ResponseStatus.SUCCESS.toString())
            .data(expectedResponseStr)
            .build();

        DocumentResponse expectedDocumentResponse = DocumentResponse.builder()
            .content(expectedContentBase64)
            .build();

        //when
        Mockito.when(sferaOrderApi.getDocumentContent(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedResponse);
            apiMock.when(() -> Api.extractBody(any(String.class), any())).thenReturn(expectedDocumentResponse);

            List<byte[]> gotContents = sferaOrderService.getContents(orders);

            assertNotNull(gotContents);
            assertTrue(gotContents.size() > 0);
            assertEquals(expectedContents.size(), gotContents.size());

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class), times(1));
            apiMock.verify(() -> Api.extractBody(expectedResponseStr, DocumentResponse.class), times(1));
        }

        //then
        for(int i = 0; i < 1; i++){

            Order o = orders.get(i);
            String gotExternalId = o.getExternalId();

            Mockito
                .verify(sferaOrderApi)
                .getDocumentContent(
                    argThat(
                        request -> request.getOrderExternalId().equals(gotExternalId)
                    )
                );
        }
    }

    @Test
    void shouldGetDocumentContentWithExternalId() {

        //given
        String expectedExternalId = "subiekt";
        String expectedContentBase64 = "Y29udGVudA==";
        byte[] expectedContent = "content".getBytes(StandardCharsets.UTF_8);

        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .build();

        String expectedResponseStr = "{\"pdf_file\": \"" + expectedContentBase64 + "\"";

        GeneralResponse expectedResponse = GeneralResponse.builder()
            .status(ResponseStatus.SUCCESS.toString())
            .data(expectedResponseStr)
            .build();

        DocumentResponse expectedDocumentResponse = DocumentResponse.builder()
            .content(expectedContentBase64)
            .build();

        //when
        Mockito.when(sferaOrderApi.getDocumentContent(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedResponse);
            apiMock.when(() -> Api.extractBody(any(String.class), any())).thenReturn(expectedDocumentResponse);

            byte[] gotContent = sferaOrderService.getDocumentContent(expectedExternalId);

            assertNotNull(gotContent);
            assertArrayEquals(expectedContent, gotContent);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class));
            apiMock.verify(() -> Api.extractBody(expectedResponseStr, DocumentResponse.class));
        }

        //then
        ArgumentCaptor<GetOrderRequest> requestCaptor = ArgumentCaptor.forClass(GetOrderRequest.class);

        Mockito.verify(sferaOrderApi).getDocumentContent(requestCaptor.capture());

        GetOrderRequest gotRequest = requestCaptor.getValue();

        assertNotNull(gotRequest);
        assertEquals(expectedExternalId, gotRequest.getOrderExternalId());
    }

    @Test
    void shouldGetDocumentContentWithoutExternalId() {

        //given
        //when

        //then
        assertThrows(
            IllegalStateException.class,
            () -> sferaOrderService.getDocumentContent(null)
        );
    }
}