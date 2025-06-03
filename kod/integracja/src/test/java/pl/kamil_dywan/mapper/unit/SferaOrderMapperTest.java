package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaOrderMapperTest {

    @ParameterizedTest
    @CsvSource(value = {
        "false, Paragon",
        "true, Faktura VAT"
    })
    void shouldMap(boolean doesOrderHaveInvoice, String expectedOrderReference) {

        //given
        Cost orderTotalCost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        Summary allegroOrderSummary = new Summary(orderTotalCost);

        InvoiceNaturalPerson invoiceNaturalPerson = InvoiceNaturalPerson.builder()
            .firstName("Adam")
            .lastName("Nowak")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(invoiceNaturalPerson)
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .required(doesOrderHaveInvoice)
            .address(invoiceAddress)
            .build();

        Order allegroOrder = Order.builder()
            .id(UUID.randomUUID())
            .invoice(allegroInvoice)
            .summary(allegroOrderSummary)
            .orderItems(List.of())
            .build();

        //when
        CreateOrderRequest createOrderRequest = SferaOrderMapper.map(allegroOrder);

        //then
        assertNotNull(createOrderRequest);
        assertEquals(expectedOrderReference, createOrderRequest.getReference());
        assertEquals(allegroOrder.getId().toString(), createOrderRequest.getExternalId());
        assertEquals("", createOrderRequest.getComments());
        assertEquals(orderTotalCost.getAmount(), createOrderRequest.getAmount());
        assertNotNull(createOrderRequest.getCustomer());
        assertNotNull(createOrderRequest.getProducts());
    }

}