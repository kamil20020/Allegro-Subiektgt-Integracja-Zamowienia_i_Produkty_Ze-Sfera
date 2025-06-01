package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SferaOrderMapperTest {

    @Test
    void shouldMap() {

        //given
        Cost orderTotalCost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        Summary allegroOrderSummary = new Summary(orderTotalCost);

        Invoice allegroInvoice = new Invoice();

        Order allegroOrder = Order.builder()
            .invoice(allegroInvoice)
            .summary(allegroOrderSummary)
            .orderItems(List.of())
            .build();

        //when
        CreateOrderRequest createOrderRequest = SferaOrderMapper.map(allegroOrder);

        //then
        assertNotNull(createOrderRequest);
        assertEquals("", createOrderRequest.getReference());
        assertEquals("", createOrderRequest.getComments());
        assertEquals(orderTotalCost.getAmount(), createOrderRequest.getAmount());
        assertNotNull(createOrderRequest.getCustomer());
        assertNotNull(createOrderRequest.getProducts());
    }

}