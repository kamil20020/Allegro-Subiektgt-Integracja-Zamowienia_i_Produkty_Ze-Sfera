package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.order_item.*;
import pl.kamil_dywan.external.allegro.own.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void shouldGetTaxRatePercentageWhenItIsSet() {

        //given
        BigDecimal expectedTaxPercentage = new BigDecimal("8");

        Tax tax = new Tax(expectedTaxPercentage, "", "");

        OrderItem orderItem = OrderItem.builder()
            .tax(tax)
            .build();

        //when
        BigDecimal gotTaxPercentage = orderItem.getTaxRatePercentage();

        //then
        assertNotNull(gotTaxPercentage);
        assertEquals(expectedTaxPercentage, gotTaxPercentage);
    }

    @Test
    public void shouldGetTaxRatePercentageWhenItIsNotSet(){

        //given
        BigDecimal expectedTaxPercentage = new BigDecimal(23);

        OrderItem orderItem = new OrderItem();

        //when
        BigDecimal gotTaxPercentage = orderItem.getTaxRatePercentage();

        //then
        assertNotNull(gotTaxPercentage);
        assertEquals(expectedTaxPercentage, gotTaxPercentage);
    }

    @Test
    public void shouldGetQuantityForMultipleProducts(){

        //given
        Integer expectedQuantity = 4;

        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);
        OrderProduct orderProduct1 = new OrderProduct(UUID.randomUUID(), 3);

        List<OrderProduct> orderProducts = List.of(orderProduct, orderProduct1);

        OrderProductSet productSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(productSet)
            .build();

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .quantity(4)
            .build();

        //when
        Integer gotQuantity = orderItem.getQuantity();

        //then
        assertNotNull(gotQuantity);
        assertEquals(expectedQuantity, gotQuantity);
    }

    @Test
    public void shouldGetQuantityForSingleProduct(){

        //given
        Integer expectedQuantity = 4;

        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);

        List<OrderProduct> orderProducts = List.of(orderProduct);

        OrderProductSet productSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(productSet)
            .build();

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .quantity(2)
            .build();

        //when
        Integer gotQuantity = orderItem.getQuantity();

        //then
        assertNotNull(gotQuantity);
        assertEquals(expectedQuantity, gotQuantity);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "21.36, 1, 21.36",
        "21.36, 2, 42.72",
        "21.36000, 2, 42.72",
    })
    public void shouldGetTotalPriceWithTax(BigDecimal unitPriceWithTax, Integer quantity, BigDecimal expectedTotalPriceWithTax){

        //given
        Cost cost = new Cost(unitPriceWithTax, Currency.PLN.toString());

        OrderItem orderItem = OrderItem.builder()
            .price(cost)
            .quantity(quantity)
            .build();

        //when
        BigDecimal gotTotalPriceWithTax = orderItem.getTotalPriceWithTax();

        //then
        assertNotNull(gotTotalPriceWithTax);
        assertEquals(expectedTotalPriceWithTax, gotTotalPriceWithTax);
    }

}