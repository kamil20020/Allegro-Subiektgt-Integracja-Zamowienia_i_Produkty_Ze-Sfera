package pl.kamil_dywan.external.allegro.generated.order_item;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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

}