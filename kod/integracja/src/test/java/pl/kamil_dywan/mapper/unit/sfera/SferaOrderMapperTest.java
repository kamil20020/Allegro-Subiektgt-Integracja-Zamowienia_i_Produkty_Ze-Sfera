package pl.kamil_dywan.mapper.unit.sfera;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.api.sfera.request.CreateOrderRequest;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.delivery.DeliveryTime;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order.Summary;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.mapper.sfera.SferaOrderMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaOrderMapperTest {

    @ParameterizedTest
    @CsvSource(value = {
        "false, Paragon",
        "true, Faktura VAT"
    })
    void shouldMapWithoutDelivery(boolean doesOrderHaveInvoice, String expectedOrderReference) {

        //given
        Cost orderTotalCost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN.toString()
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

        Offer offer = Offer.builder()
            .id(UUID.randomUUID().toString())
            .name("Offer 123")
            .build();

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .quantity(1)
            .price(new Cost(new BigDecimal("24.48"), Currency.PLN.toString()))
            .build();

        Offer offer1 = Offer.builder()
            .id(UUID.randomUUID().toString())
            .name("Offer 345")
            .build();

        OrderItem orderItem1 = OrderItem.builder()
            .offer(offer1)
            .quantity(1)
            .price(new Cost(new BigDecimal("24.42"), Currency.PLN.toString()))
            .build();

        Order allegroOrder = Order.builder()
            .id(UUID.randomUUID())
            .invoice(allegroInvoice)
            .summary(allegroOrderSummary)
            .orderItems(List.of(orderItem, orderItem1))
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

        List<Product> products = createOrderRequest.getProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(offer.getId(), products.get(0).getCode());
        assertEquals(offer1.getId(), products.get(1).getCode());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "false, Paragon",
        "true, Faktura VAT"
    })
    void shouldMapWithDelivery(boolean doesOrderHaveInvoice, String expectedOrderReference) {

        //given
        Cost orderTotalCost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN.toString()
        );

        Summary allegroOrderSummary = new Summary(orderTotalCost);

        InvoiceCompany invoiceCompany = InvoiceCompany.builder()
            .name("Company 123")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(invoiceCompany)
            .build();

        Invoice allegroInvoice = Invoice.builder()
            .required(doesOrderHaveInvoice)
            .address(invoiceAddress)
            .build();

        Offer offer = Offer.builder()
            .id(UUID.randomUUID().toString())
            .name("Offer 123")
            .build();

        OrderItem orderItem = OrderItem.builder()
            .price(new Cost(new BigDecimal("24.48"), Currency.PLN.toString()))
            .quantity(1)
            .offer(offer)
            .build();

        Cost deliveryCost = new Cost(new BigDecimal("22.48"), Currency.PLN.toString());

        Delivery delivery = Delivery.builder()
            .cost(deliveryCost)
            .time(new DeliveryTime(OffsetDateTime.now(), null, null, null))
            .build();

        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(orderItem);

        Order allegroOrder = Order.builder()
            .id(UUID.randomUUID())
            .invoice(allegroInvoice)
            .summary(allegroOrderSummary)
            .delivery(delivery)
            .orderItems(orderItems)
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

        List<Product> products = createOrderRequest.getProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(offer.getId(), products.get(0).getCode());
        assertEquals("DOSTAWA123", products.get(1).getCode());
    }

}