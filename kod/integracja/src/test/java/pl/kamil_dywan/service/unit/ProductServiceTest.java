package pl.kamil_dywan.service.unit;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.ProductApi;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.service.ProductService;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductApi productApi;

    @InjectMocks
    private ProductService productService;

    private static HttpResponse<String> getTestHttpResponse(int statusCode){

        return TestHttpResponse.builder()
            .uri(URI.create("http://localhost"))
            .statusCode(statusCode)
            .body("{}")
            .build();
    }

//    @Test
    void shouldGetDetailedProductsByIds() {

        //given
        List<Long> productsIds = List.of(1L, 2L);
        List<HttpResponse<String>> productsOffersResponses = List.of(
            getTestHttpResponse(201),
            getTestHttpResponse(202)
        );
        List<ProductOfferResponse> productsOffers = List.of(
            new ProductOfferResponse(),
            new ProductOfferResponse()
        );

        //when
        try(
            MockedStatic<Api> apiMock = Mockito.mockStatic(Api.class);
        ){

            for(int i = 0; i < productsIds.size(); i++){

                Long productId = productsIds.get(i);
                HttpResponse<String> httpResponse = productsOffersResponses.get(i);
                ProductOfferResponse productOfferResponse = productsOffers.get(i);

                Mockito.when(productApi.getProductOfferById(productId)).thenReturn(httpResponse);
//                apiMock.when(() -> Api.extractBody(
//                    argThat(response -> (response instanceof HttpResponse httpResponse1) && httpResponse1.statusCode() == httpResponse.statusCode()),
//                    any()
//                )).thenReturn(productOfferResponse);
            }

            List<ProductOfferResponse> gotProductsOffers = productService.getDetailedProductsByIds(productsIds);

            //then
            for(int i = 0; i < productsIds.size(); i++){

                Long productId = productsIds.get(i);
                HttpResponse httpResponse = productsOffersResponses.get(i);

                Mockito.verify(productApi).getProductOfferById(productId);
                apiMock.verify(() -> Api.extractBody(httpResponse, ProductOfferResponse.class));

                assertEquals(productsOffers.get(i), gotProductsOffers.get(i));
            }
        }

        //then
    }
}