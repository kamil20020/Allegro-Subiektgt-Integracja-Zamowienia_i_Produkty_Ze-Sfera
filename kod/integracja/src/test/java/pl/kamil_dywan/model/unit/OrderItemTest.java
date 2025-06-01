package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Tax;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    public void shouldGetUnitPriceWithoutTax(){

        //given
        BigDecimal unitPriceWithTax = new BigDecimal("10.00");
        BigDecimal taxRatePercentage = BigDecimal.valueOf(8);
        BigDecimal expectedUnitPriceWithoutTax = new BigDecimal("9.26");

        Cost cost = new Cost(unitPriceWithTax, null);

        Tax tax = new Tax(taxRatePercentage, "", "");

        OrderItem orderItem = OrderItem.builder()
            .price(cost)
            .tax(tax)
            .build();

        //when
        BigDecimal gotUnitPriceWithoutTax = orderItem.getUnitPriceWithoutTax();
        gotUnitPriceWithoutTax = gotUnitPriceWithoutTax.setScale(2, RoundingMode.HALF_UP);

        //then
        assertEquals(expectedUnitPriceWithoutTax, gotUnitPriceWithoutTax);
    }

    @Test
    public void shouldGetTaxRatePercentage(){

        //given
        BigDecimal expectedTaxRate = new BigDecimal("34.38");

        Tax tax = new Tax(expectedTaxRate, "", "");

        OrderItem orderItem = OrderItem.builder()
            .tax(tax)
            .build();

        //when
        BigDecimal gotTaxRate = orderItem.getTaxRatePercentage();

        //then
        assertEquals(expectedTaxRate, gotTaxRate);
    }

    @Test
    public void shouldGetDefaultTaxRatePercentage(){

        //given
        BigDecimal expectedTaxRate = new BigDecimal("23");

        Tax tax = new Tax(expectedTaxRate, "", "");

        OrderItem orderItem = OrderItem.builder()
            .tax(tax)
            .build();

        //when
        BigDecimal gotTaxRate = orderItem.getTaxRatePercentage();

        //then
        assertEquals(expectedTaxRate, gotTaxRate);
    }
}