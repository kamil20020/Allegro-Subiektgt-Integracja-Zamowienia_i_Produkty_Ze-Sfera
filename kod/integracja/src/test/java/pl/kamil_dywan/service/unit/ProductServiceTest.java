package pl.kamil_dywan.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.ProductApi;
import pl.kamil_dywan.api.allegro.response.OfferProductResponse;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProductParameter;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedData;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedDataQuantity;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.service.ProductService;
import pl.kamil_dywan.service.SferaProductService;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductApi productApi;

    @Mock
    private SferaProductService sferaProductService;

    @InjectMocks
    private ProductService productService;

    private static HttpResponse<String> getTestHttpResponse(int statusCode){

        return TestHttpResponse.builder()
            .uri(URI.create("http://localhost"))
            .statusCode(statusCode)
            .body("{}")
            .build();
    }

    @Test
    public void shouldGetGeneralProductsPage(){

        //given
        String expectedSearch = "samsung";
        int offset = 10;
        int limit = 20;

        HttpResponse<String> expectedHttpResponse = getTestHttpResponse(200);
        OfferProductResponse expectedResponse = new OfferProductResponse();

        //when
        Mockito.when(productApi.getOffersProducts(anyString(), anyInt(), anyInt())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedResponse);

            OfferProductResponse gotResponse = productService.getGeneralProductsPage(expectedSearch, offset, limit);

            //then
            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, OfferProductResponse.class));

            assertNotNull(gotResponse);
            assertEquals(expectedResponse, gotResponse);
        }

        Mockito.verify(productApi).getOffersProducts(expectedSearch, offset, limit);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer#ean, producer, ean, producer",
        "producer#, producer, , producer",
        "#ean, 123, ean , ",
        "#, 123, , ",
        ", 123, , ",
    })
    public void shouldGetDetailedProductById(String expectedExternalIdValue, String expectedProducerCode, String expectedEan, String expectedSubiektId){

        //given
        Long expectedId = 123L;

        HttpResponse<String> expectedHttpResponse = getTestHttpResponse(200);

        ExternalId externalId = new ExternalId(expectedExternalIdValue);

        ProductOfferResponse expectedResponse = ProductOfferResponse.builder()
            .id(expectedId)
            .externalId(externalId)
            .build();

        //when
        Mockito.when(productApi.getProductOfferById(any())).thenReturn(expectedHttpResponse);
        Mockito.when(sferaProductService.getSubiektIdByCodeOrEan(any(), any())).thenReturn(expectedSubiektId);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
        ){
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(expectedResponse);

            ProductOfferResponse gotResponse = productService.getDetailedProductById(expectedId);

            //then
            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, ProductOfferResponse.class));

            assertNotNull(gotResponse);
            assertEquals(expectedId, gotResponse.getId());
            assertEquals(expectedSubiektId, gotResponse.getSubiektId());
        }

        Mockito.verify(productApi).getProductOfferById(expectedId);
        Mockito.verify(sferaProductService).getSubiektIdByCodeOrEan(expectedProducerCode, expectedEan);
    }

    @Test
    public void shouldGetDetailedProductsByIds() throws InterruptedException {

        //given
        ExecutorService executorServiceMock = Mockito.mock(ExecutorService.class);
        TestUtils.updatePrivateStaticField(ProductService.class, "productsExecutorService", executorServiceMock);

        List<Long> productsIds = List.of(1L, 2L);

        ProductOfferResponse product = ProductOfferResponse.builder()
            .id(123L)
            .build();

        ProductOfferResponse product1 = ProductOfferResponse.builder()
            .id(345L)
            .build();

        List<ProductOfferResponse> products = List.of(product, product1);

        HttpResponse<String> httpResponse = getTestHttpResponse(200);
        HttpResponse<String> httpResponse1 = getTestHttpResponse(201);

        List<HttpResponse<String>> httpResponses = List.of(httpResponse, httpResponse1);

        //when
        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
        ){
            Mockito.when(executorServiceMock.invokeAll(any())).thenAnswer(answer -> {

                List<Callable<ProductOfferResponse>> productsTasks = answer.getArgument(0);
                List<Future<ProductOfferResponse>> productsFutures = new ArrayList<>();

                for(Callable<ProductOfferResponse> task : productsTasks){

                    ProductOfferResponse gotProduct = task.call();

                    productsFutures.add(CompletableFuture.completedFuture(gotProduct));
                }

                return productsFutures;
            });

            for(int i = 0; i < products.size(); i++){

                ProductOfferResponse gotProduct = products.get(i);
                Long gotProductId = productsIds.get(i);
                HttpResponse<String> gotHttpResponse = httpResponses.get(i);

                Mockito.when(productApi.getProductOfferById(gotProductId)).thenReturn(gotHttpResponse);
                apiMock
                    .when(() -> Api.extractBody(
                        argThat((HttpResponse<String> response) -> response.statusCode() == gotHttpResponse.statusCode()),
                        any()
                    ))
                    .thenReturn(gotProduct);
            }

            List<ProductOfferResponse> gotProducts = productService.getDetailedProductsByIds(productsIds);

            //then
            for(int i = 0; i < products.size(); i++){

                Long gotProductId = productsIds.get(i);
                HttpResponse<String> gotHttpResponse =httpResponses.get(i);

                Mockito.verify(productApi).getProductOfferById(gotProductId);
                apiMock.verify(() -> Api.extractBody(gotHttpResponse, ProductOfferResponse.class));
            }

            Mockito.verify(executorServiceMock).invokeAll(any());

            assertNotNull(gotProducts);
            assertEquals(2, gotProducts.size());
            assertTrue(gotProducts.containsAll(products));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
        "producer, ean, producer#ean",
        "producer, , producer#",
        ", ean, #ean"
    })
    public void shouldSetExternalIdForOffer(String expectedProducerCode, String expectedEanCode, String expectedExternalIdValue){

        //given
        Long expectedId = 123L;

        List<String> producerCodeParameterValues = new ArrayList<>();

        producerCodeParameterValues.add(expectedProducerCode);

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(345L)
            .name("Kod producenta")
            .values(producerCodeParameterValues)
            .build();

        List<String> eanCodeParameterValues = new ArrayList<>();

        eanCodeParameterValues.add(expectedEanCode);

        OfferProductParameter eanCodeParameter = OfferProductParameter.builder()
            .id(678L)
            .name("EAN (GTIN)")
            .values(eanCodeParameterValues)
            .build();

        List<OfferProductParameter> parameters = List.of(producerCodeParameter, eanCodeParameter);

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), parameters);

        ProductOfferProductRelatedData productOfferProductRelatedData = new ProductOfferProductRelatedData(
            product,
            new ProductOfferProductRelatedDataQuantity(2)
        );

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .id(expectedId)
            .productSet(List.of(productOfferProductRelatedData))
            .build();

        HttpResponse<String> expectedHttpResponse = getTestHttpResponse(200);

        //when
        Mockito.when(productApi.patchOfferExternalById(any(), any())).thenReturn(expectedHttpResponse);

        productService.setExternalIdForOffer(productOfferResponse);

        //then
        assertEquals(expectedExternalIdValue, productOfferResponse.getExternalIdValue());

        Mockito.verify(productApi).patchOfferExternalById(expectedId, expectedExternalIdValue);
    }

    @Test
    public void shouldSetExternalIdForAllOffers() throws InterruptedException {

        //given
        ExecutorService executorServiceMock = Mockito.mock(ExecutorService.class);
        TestUtils.updatePrivateStaticField(ProductService.class, "productsExecutorService", executorServiceMock);

        List<String> producerCodeParameterValues = new ArrayList<>();

        producerCodeParameterValues.add("producer");

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(345L)
            .name("Kod producenta")
            .values(producerCodeParameterValues)
            .build();

        List<OfferProductParameter> parameters = List.of(producerCodeParameter);

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), parameters);

        ProductOfferProductRelatedData productOfferProductRelatedData = new ProductOfferProductRelatedData(
            product,
            new ProductOfferProductRelatedDataQuantity(2)
        );

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .id(123L)
            .productSet(List.of(productOfferProductRelatedData))
            .build();

        ProductOfferResponse productOfferResponse1 = ProductOfferResponse.builder()
            .id(456L)
            .productSet(List.of(productOfferProductRelatedData))
            .build();

        List<ProductOfferResponse> productOfferResponses = List.of(productOfferResponse, productOfferResponse1);

        HttpResponse<String> expectedHttpResponse = getTestHttpResponse(200);

        //when
        Mockito.when(executorServiceMock.invokeAll(any())).thenAnswer(answer -> {

            List<Callable<ProductOfferResponse>> productsTasks = answer.getArgument(0);
            List<Future<ProductOfferResponse>> productsFutures = new ArrayList<>();

            for(Callable<ProductOfferResponse> task : productsTasks){

                ProductOfferResponse gotProduct = task.call();

                productsFutures.add(CompletableFuture.completedFuture(gotProduct));
            }

            return productsFutures;
        });

        Mockito.when(productApi.patchOfferExternalById(any(), any())).thenReturn(expectedHttpResponse);

        productService.setExternalIdForAllOffers(productOfferResponses);

        //then
        for(ProductOfferResponse productOffer : productOfferResponses){

            Mockito.verify(productApi).patchOfferExternalById(productOffer.getId(), "producer#");
        }

        Mockito.verify(executorServiceMock).invokeAll(any());
    }

}