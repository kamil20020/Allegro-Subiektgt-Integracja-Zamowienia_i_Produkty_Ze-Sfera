package pl.kamil_dywan.api.allegro.response;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDocumentsResponseTest {

    @ParameterizedTest
    @CsvSource(value = {
        "true",
        "false",
    })
    void documentsExistWhenThereAreInvoicesFiles(boolean expectedHasExternalInvoice) {

        //given
        List<Object> invoicesFiles = List.of(new Object(), new Object());

        OrderDocumentsResponse orderDocumentsResponse = new OrderDocumentsResponse(invoicesFiles, expectedHasExternalInvoice);

        //when
        boolean result = orderDocumentsResponse.documentsExist();

        //then
        assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "true, true",
        "false, false",
    })
    void documentsExistWhenThereAreNullInvoicesFiles(boolean expectedHasExternalInvoices, boolean expectedResult) {

        //given
        OrderDocumentsResponse orderDocumentsResponse = new OrderDocumentsResponse(null, expectedHasExternalInvoices);

        //when
        boolean gotResult = orderDocumentsResponse.documentsExist();

        //then
        assertEquals(expectedResult, gotResult);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "true, true",
        "false, false",
    })
    void documentsExistWhenThereAreEmptyInvoicesFiles(boolean expectedHasExternalInvoices, boolean expectedResult) {

        //given
        OrderDocumentsResponse orderDocumentsResponse = new OrderDocumentsResponse(new ArrayList<>(), expectedHasExternalInvoices);

        //when
        boolean gotResult = orderDocumentsResponse.documentsExist();

        //then
        assertEquals(expectedResult, gotResult);
    }
}