package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Tax;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemMoneyStatsTest {

    @ParameterizedTest
    @CsvSource(value = {
        "10.00, 1,  8,  9.26, 10.00,  9.26,  0.74",
        "36.90, 2, 23, 30.00, 73.80, 60.00, 13.80"
    })
    void shouldGetSummary(
        BigDecimal priceWithTax,
        int quantity,
        BigDecimal taxRatePercentage,
        BigDecimal expectedUnitPriceWithoutTax,
        BigDecimal expectedTotalPriceWithTax,
        BigDecimal expectedTotalPriceWithoutTax,
        BigDecimal expectedTotalTaxValue
    ){

        //given
        Cost cost = new Cost(priceWithTax, null);

        Tax tax = new Tax(taxRatePercentage, "", "");

        OrderItem orderItem = OrderItem.builder()
            .price(cost)
            .quantity(quantity)
            .tax(tax)
            .build();

        //when
        OrderItemMoneyStats stats = OrderItemMoneyStats.getSummary(orderItem);
        stats.scale(2, RoundingMode.HALF_UP);

        //then
        assertEquals(taxRatePercentage, stats.getTaxRatePercentage());
        assertEquals(priceWithTax, stats.getUnitPriceWithTax());
        assertEquals(expectedUnitPriceWithoutTax, stats.getUnitPriceWithoutTax());
        assertEquals(expectedTotalPriceWithTax, stats.getTotalPriceWithTax());
        assertEquals(expectedTotalPriceWithoutTax, stats.getTotalPriceWithoutTax());
        assertEquals(expectedTotalTaxValue, stats.getTotalTaxValue());
    }

    @Test
    void shouldScale() {

        //given
        OrderItemMoneyStats gotStats = new OrderItemMoneyStats(
            new BigDecimal("23"),
            new BigDecimal("36.000"),
            new BigDecimal("30.004"),
            new BigDecimal("73.005"),
            new BigDecimal("60.006"),
            new BigDecimal("13.009")
        );

        OrderItemMoneyStats expectedStats = new OrderItemMoneyStats(
            new BigDecimal("23"),
            new BigDecimal("36.00"),
            new BigDecimal("30.00"),
            new BigDecimal("73.01"),
            new BigDecimal("60.01"),
            new BigDecimal("13.01")
        );

        //when
        gotStats.scale(2, RoundingMode.HALF_UP);

        //then
        assertEquals(expectedStats, gotStats);
    }
}