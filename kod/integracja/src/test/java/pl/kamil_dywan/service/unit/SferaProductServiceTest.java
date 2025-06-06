package pl.kamil_dywan.service.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.TestHttpResponse;
import pl.kamil_dywan.TestUtils;
import pl.kamil_dywan.api.Api;
import pl.kamil_dywan.api.allegro.request.GetProductByCodeAndEanRequest;
import pl.kamil_dywan.api.sfera.SferaProductApi;
import pl.kamil_dywan.api.sfera.response.ErrorResponse;
import pl.kamil_dywan.api.sfera.response.GeneralResponse;
import pl.kamil_dywan.service.SferaProductService;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

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
}