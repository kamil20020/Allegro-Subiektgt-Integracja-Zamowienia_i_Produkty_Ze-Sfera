package pl.kamil_dywan.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.api.sfera.request.CreateProductsSetRequest;
import pl.kamil_dywan.api.sfera.request.GetProductByCodeAndEanRequest;
import pl.kamil_dywan.api.sfera.SferaProductApi;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.mapper.sfera.SferaProductSetMapper;
import pl.kamil_dywan.service.SferaProductService;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SferaProductServiceTest {

    @Mock
    private SferaProductApi sferaProductApi;

    @InjectMocks
    private SferaProductService sferaProductService;

    @ParameterizedTest
    @CsvSource(value = {
        "producer, ean, subiekt, subiekt",
        "producer1, ean, null, ",
    })
    void shouldGetSubiektIdByCodeOrEan(String expectedCode, String expectedEan, String expectedResponseSubiektId, String expecteSubiektId) {

        //given
        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .uri(URI.create("http://localhost:9000"))
            .build();

        GeneralResponse generalResponse = GeneralResponse.builder()
            .status("success")
            .data(expectedResponseSubiektId)
            .build();

        //when
        Mockito.when(sferaProductApi.getSubiektIdByCodeAndEan(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class)
        ){

            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(generalResponse);

            String gotSubiektId = sferaProductService.getSubiektIdByCodeOrEan(expectedCode, expectedEan);

            //then
            assertEquals(expecteSubiektId, gotSubiektId);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class));
        }

        ArgumentCaptor<GetProductByCodeAndEanRequest> requestCaptor = ArgumentCaptor.forClass(GetProductByCodeAndEanRequest.class);

        Mockito.verify(sferaProductApi).getSubiektIdByCodeAndEan(requestCaptor.capture());

        GetProductByCodeAndEanRequest gotRequest = requestCaptor.getValue();

        assertNotNull(gotRequest);
        assertEquals(expectedCode, gotRequest.getCode());
        assertEquals(expectedEan, gotRequest.getEan());
    }

    @Test
    public void shouldSaveProductsSets(){

        //given
        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .uri(URI.create("http://localhost:9000"))
            .build();

        GeneralResponse generalResponse = GeneralResponse.builder()
            .status("success")
            .data(null)
            .build();

        ProductOfferResponse expectedRawProductsSet = new ProductOfferResponse();
        ProductOfferResponse expectedRawProductsSet1 = new ProductOfferResponse();

        List<ProductOfferResponse> expectedRawProductsSets = List.of(expectedRawProductsSet, expectedRawProductsSet1);

        CreateProductsSetRequest expectedProductSetRequest = new CreateProductsSetRequest();

        Integer expectedSavedProductsSetsNumberOf = 2;

        //when
        Mockito.when(sferaProductApi.saveProductsSet(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<SferaProductSetMapper> sferaProductSetMapper = Mockito.mockStatic(SferaProductSetMapper.class);
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
        ){

            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(generalResponse);

            for (ProductOfferResponse rawProduct : expectedRawProductsSets) {

                sferaProductSetMapper.when(() -> SferaProductSetMapper.map(rawProduct)).thenReturn(expectedProductSetRequest);
            }

            Integer gotSavedProductsSetsNumberOf = sferaProductService.saveProductsSets(expectedRawProductsSets);

            //then
            assertNotNull(gotSavedProductsSetsNumberOf);
            assertEquals(expectedSavedProductsSetsNumberOf, gotSavedProductsSetsNumberOf);

            for (ProductOfferResponse rawProduct : expectedRawProductsSets) {

                sferaProductSetMapper.verify(() -> SferaProductSetMapper.map(rawProduct), times(2));
            }
        }

        Mockito.verify(sferaProductApi, times(2)).saveProductsSet(expectedProductSetRequest);
    }

    @Test
    public void shouldSaveProductsSet(){

        //given
        HttpResponse<String> expectedHttpResponse = TestHttpResponse.builder()
            .statusCode(200)
            .uri(URI.create("http://localhost:9000"))
            .build();

        GeneralResponse generalResponse = GeneralResponse.builder()
            .status("success")
            .data(null)
            .build();

        ProductOfferResponse expectedRawProductsSet = new ProductOfferResponse();

        CreateProductsSetRequest expectedProductSetRequest = new CreateProductsSetRequest();

        //when
        Mockito.when(sferaProductApi.saveProductsSet(any())).thenReturn(expectedHttpResponse);

        try(
            MockedStatic<SferaProductSetMapper> sferaProductSetMapper = Mockito.mockStatic(SferaProductSetMapper.class);
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
        ){

            sferaProductSetMapper.when(() -> SferaProductSetMapper.map(any())).thenReturn(expectedProductSetRequest);
            apiMock.when(() -> Api.extractBody(any(TestHttpResponse.class), any())).thenReturn(generalResponse);

            sferaProductService.saveProductsSet(expectedRawProductsSet);

            //then
            ArgumentCaptor<CreateProductsSetRequest> requestCaptor = ArgumentCaptor.forClass(CreateProductsSetRequest.class);

            Mockito.verify(sferaProductApi).saveProductsSet(requestCaptor.capture());

            CreateProductsSetRequest gotRequest = requestCaptor.getValue();

            assertNotNull(gotRequest);
            assertEquals(expectedProductSetRequest, gotRequest);

            apiMock.verify(() -> Api.extractBody(expectedHttpResponse, GeneralResponse.class));
            sferaProductSetMapper.verify(() -> SferaProductSetMapper.map(expectedRawProductsSet));
        }

        Mockito.verify(sferaProductApi).saveProductsSet(expectedProductSetRequest);
    }
}