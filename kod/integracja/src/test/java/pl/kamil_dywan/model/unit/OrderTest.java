package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.buyer.Buyer;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.delivery.DeliveryTime;
import pl.kamil_dywan.external.allegro.generated.invoice.Invoice;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceAddress;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceCompany;
import pl.kamil_dywan.external.allegro.generated.invoice.InvoiceNaturalPerson;
import pl.kamil_dywan.external.allegro.generated.order.Fulfillment;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.allegro.own.order.FulFillmentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @ParameterizedTest
    @CsvSource(value = {
        "true",
        "false"
    })
    void shouldGetHasInvoice(boolean doesOrderHaveInvoice) {

        //given
        Invoice invoice = Invoice.builder()
            .required(doesOrderHaveInvoice)
            .build();

        Order order = Order.builder()
            .invoice(invoice)
            .build();

        //when
        boolean gotResult = order.hasInvoice();

        //then
        assertEquals(doesOrderHaveInvoice, gotResult);
    }

    @Test
    void shouldGetIsBuyerCompanyWhenIsGivenInvoiceAndCompany() {

        //given
        InvoiceCompany invoiceCompany = InvoiceCompany.builder()
            .name("Company 123")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(invoiceCompany)
            .build();

        Invoice invoice = Invoice.builder()
            .required(true)
            .address(invoiceAddress)
            .build();

        Order order = Order.builder()
            .invoice(invoice)
            .build();

        //when
        boolean result = order.isBuyerCompany();

        //then
        assertTrue(result);
    }

    @Test
    void shouldGetIsBuyerCompanyWhenIsNotGivenInvoiceAndIsGivenCompany(){

        //given
        Buyer buyer = Buyer.builder()
            .companyName("Company 123")
            .build();

        Invoice invoice = Invoice.builder()
            .required(false)
            .build();

        Order order = Order.builder()
            .buyer(buyer)
            .invoice(invoice)
            .build();

        //when
        boolean result = order.isBuyerCompany();

        //then
        assertTrue(result);
    }

    @Test
    void shouldGetIsBuyerCompanyWhenIsGivenInvoiceAndIsPerson(){

        //given
        InvoiceNaturalPerson invoiceNaturalPerson = InvoiceNaturalPerson.builder()
            .firstName("Adam")
            .lastName("Nowak")
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(invoiceNaturalPerson)
            .build();

        Invoice invoice = Invoice.builder()
            .required(true)
            .address(invoiceAddress)
            .build();

        Order order = Order.builder()
            .invoice(invoice)
            .build();

        //when
        boolean result = order.isBuyerCompany();

        //then
        assertFalse(result);
    }

    @Test
    void shouldGetIsBuyerCompanyWhenIsNotGivenInvoiceAndIsPerson(){

        //given
        Buyer buyer = Buyer.builder()
            .firstName("Adam")
            .lastName("Nowak")
            .build();

        Invoice invoice = Invoice.builder()
            .required(false)
            .build();

        Order order = Order.builder()
            .buyer(buyer)
            .invoice(invoice)
            .build();

        //when
        boolean result = order.isBuyerCompany();

        //then
        assertFalse(result);
    }

    @Test
    void shouldGetClientNameForInvoiceAndCompany() {

        //given
        String name = "Company 123";

        InvoiceCompany invoiceCompany = InvoiceCompany.builder()
            .name(name)
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .company(invoiceCompany)
            .build();

        Invoice invoice = Invoice.builder()
            .required(true)
            .address(invoiceAddress)
            .build();

        Order order = Order.builder()
            .invoice(invoice)
            .build();

        //when
        String gotName = order.getClientName();

        //then
        assertEquals(name, gotName);
    }

    @Test
    void shouldGetClientNameForNotInvoiceAndCompany() {

        //given
        String name = "Company 123";

        Buyer buyer = Buyer.builder()
            .companyName(name)
            .build();

        Invoice invoice = Invoice.builder()
            .required(false)
            .build();

        Order order = Order.builder()
            .buyer(buyer)
            .invoice(invoice)
            .build();

        //when
        String gotName = order.getClientName();

        //then
        assertEquals(name, gotName);
    }

    @Test
    void shouldGetClientNameForInvoiceAndPerson() {

        //given
        String firstName = "Adam";
        String lastName = "Nowak";
        String expectedName = firstName + " " + lastName;

        InvoiceNaturalPerson invoiceNaturalPerson = InvoiceNaturalPerson.builder()
            .firstName(firstName)
            .lastName(lastName)
            .build();

        InvoiceAddress invoiceAddress = InvoiceAddress.builder()
            .naturalPerson(invoiceNaturalPerson)
            .build();

        Invoice invoice = Invoice.builder()
            .required(true)
            .address(invoiceAddress)
            .build();

        Order order = Order.builder()
            .invoice(invoice)
            .build();

        //when
        String gotName = order.getClientName();

        //then
        assertEquals(expectedName, gotName);
    }

    @Test
    void shouldGetClientNameForNotInvoiceAndPerson() {

        //given
        String firstName = "Adam";
        String lastName = "Nowak";
        String expectedName = firstName + " " + lastName;

        Buyer buyer = Buyer.builder()
            .firstName(firstName)
            .lastName(lastName)
            .build();

        Invoice invoice = Invoice.builder()
            .required(false)
            .build();

        Order order = Order.builder()
            .buyer(buyer)
            .invoice(invoice)
            .build();

        //when
        String gotName = order.getClientName();

        //then
        assertEquals(expectedName, gotName);
    }

    @Test
    void shouldGetHasDeliveryWhenHas() {

        //given
        Cost cost = new Cost(new BigDecimal("12.28"), Currency.PLN.toString());

        DeliveryTime deliveryTime = new DeliveryTime(OffsetDateTime.now(), null, null, null);

        Delivery delivery = Delivery.builder()
            .cost(cost)
            .time(deliveryTime)
            .build();

        Order order = Order.builder()
            .delivery(delivery)
            .orderItems(new ArrayList<>())
            .build();

        //when
        boolean gotResult = order.hasDelivery();

        //then
        assertTrue(gotResult);
    }

    @Test
    void shouldGetHasDeliveryWhenDeliveryCostIs0() {

        //given
        Cost cost = new Cost(new BigDecimal("0.00"), Currency.PLN.toString());

        Delivery delivery = Delivery.builder()
            .cost(cost)
            .build();

        Order order = Order.builder()
                .delivery(delivery)
                .build();

        //when
        boolean gotResult = order.hasDelivery();

        //then
        assertFalse(gotResult);
    }

    @Test
    void shouldGetHasDeliveryWhenDeliveryIsNull() {

        //given
        Order order = new Order();

        //when
        boolean gotResult = order.hasDelivery();

        //then
        assertFalse(gotResult);
    }

    @Test
    void shouldGetHasDeliveryWhenDeliveryCostIsNull() {

        //given
        Delivery delivery = new Delivery();

        Order order = Order.builder()
            .delivery(delivery)
            .build();

        //when
        boolean gotResult = order.hasDelivery();

        //then
        assertFalse(gotResult);
    }

    @Test
    void shouldGetHasDeliveryWhenDeliveryCostAmountIsNull() {

        //given
        Cost cost = new Cost();

        Delivery delivery = Delivery.builder()
            .cost(cost)
            .build();

        Order order = Order.builder()
            .delivery(delivery)
            .build();

        //when
        boolean gotResult = order.hasDelivery();

        //then
        assertFalse(gotResult);
    }

    @Test
    void shouldCreateWithoutDelivery() {

        //given
        OrderItem orderItem = OrderItem.builder()
            .quantity(1)
            .build();

        //when
        Order order = Order.builder()
            .orderItems(List.of(orderItem))
            .build();

        //then
        assertNotNull(order);
        assertTrue(order.getOrderItems().contains(orderItem));
    }

    @Test
    void shouldCreateWithDelivery() {

        //given
        OrderItem orderItem = OrderItem.builder()
            .quantity(1)
            .offer(Offer.builder()
                .name("Product 123")
                .build()
            )
            .build();

        Delivery delivery = Delivery.builder()
            .cost(new Cost(new BigDecimal("1"), Currency.PLN.toString()))
            .time(new DeliveryTime(OffsetDateTime.now(), null, null, null))
            .build();

        List<OrderItem> orderItems = new ArrayList<>();

        orderItems.add(orderItem);

        //when
        Order order = Order.builder()
            .delivery(delivery)
            .orderItems(orderItems)
            .build();

        //then
        assertNotNull(order);
        assertEquals(2, order.getOrderItems().size());
        assertTrue(order.getOrderItems().contains(orderItem));
        assertEquals("Product 123", order.getOrderItems().get(0).getOffer().getName());
        assertEquals("DOSTAWA123", order.getOrderItems().get(1).getOffer().getId());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "external-id, external-id",
        "\"external-id\", external-id",
    })
    public void shouldSetExternalId(String inputExternalId, String expectedExternalId){

        //given
        Order order = Order.builder()
            .orderItems(new ArrayList<>())
            .build();

        //when
        order.setExternalId(inputExternalId);

        String gotExternalId = order.getExternalId();

        //then
        assertNotNull(gotExternalId);
        assertEquals(expectedExternalId, gotExternalId);
    }

    @Test
    public void shouldSetExternalIdWhenItIsNull(){

        //given
        Order order = Order.builder()
            .orderItems(new ArrayList<>())
            .build();

        //when
        order.setExternalId(null);

        String gotExternalId = order.getExternalId();

        //then
        assertNull(gotExternalId);
    }

    @Test
    public void shouldGetIsCancelledWhenIsNot(){

        //given
        Fulfillment fulfillment = new Fulfillment(FulFillmentStatus.NEW, null);

        Order order = Order.builder()
            .orderItems(new ArrayList<>())
            .fulfillment(fulfillment)
            .build();

        //when
        boolean result = order.isCancelled();

        //then
        assertFalse(result);
    }

    @Test
    public void shouldGetIsCancelledWhenItIs(){

        //given
        Fulfillment fulfillment = new Fulfillment(FulFillmentStatus.CANCELLED, null);

        Order order = Order.builder()
            .orderItems(new ArrayList<>())
            .fulfillment(fulfillment)
            .build();

        //when
        boolean result = order.isCancelled();

        //then
        assertTrue(result);
    }

    @Test
    public void shouldGetIsCancelledWhenFullfillmentIsNull(){

        //given
        Order order = Order.builder()
            .orderItems(new ArrayList<>())
            .build();

        //when
        boolean result = order.isCancelled();

        //then
        assertFalse(result);
    }

    @Test
    public void shouldGetIsCancelledWhenFullfillmentStatusIsNull(){

        //given
        Fulfillment fulfillment = new Fulfillment(null, null);

        Order order = Order.builder()
            .orderItems(new ArrayList<>())
            .fulfillment(fulfillment)
            .build();

        //when
        boolean result = order.isCancelled();

        //then
        assertFalse(result);
    }

}