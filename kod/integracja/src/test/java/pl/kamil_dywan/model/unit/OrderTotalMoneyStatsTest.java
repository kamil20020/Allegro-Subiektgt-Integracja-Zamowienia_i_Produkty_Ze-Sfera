package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class OrderTotalMoneyStatsTest {

    @Test
    void shouldUpdate() {

        //given
        OrderTotalMoneyStats expectedOrderTotalMoneyStats = new OrderTotalMoneyStats(
            0,
            0,
            new BigDecimal("25.86"),
            new BigDecimal("18.86"),
            new BigDecimal("11.02")
        );

        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats(
            0,
            0,
            new BigDecimal("23.38"),
            new BigDecimal("12.48"),
            new BigDecimal("6.68")
        );

        //when
        orderTotalMoneyStats.update(new BigDecimal("2.48"), new BigDecimal("4.34"), new BigDecimal("6.38"));

        //then
        assertEquals(expectedOrderTotalMoneyStats.getTotalWithoutTax(), orderTotalMoneyStats.getTotalWithoutTax());
        assertEquals(expectedOrderTotalMoneyStats.getTotalWithTax(), orderTotalMoneyStats.getTotalWithTax());
        assertEquals(expectedOrderTotalMoneyStats.getTaxValue(), orderTotalMoneyStats.getTaxValue());
    }

    @Test
    void shouldScale() {

        //given
        OrderTotalMoneyStats expectedOrderTotalMoneyStats = new OrderTotalMoneyStats(
            0,
            0,
            new BigDecimal("25.000"),
            new BigDecimal("18.004"),
            new BigDecimal("11.006")
        );

        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats(
            0,
            0,
            new BigDecimal("25.00"),
            new BigDecimal("18.00"),
            new BigDecimal("11.01")
        );

        //when
        expectedOrderTotalMoneyStats.scale(2, RoundingMode.HALF_UP);

        //then
        assertEquals(expectedOrderTotalMoneyStats, orderTotalMoneyStats);
    }

    @Test
    public void shouldSetNumberOfOrderItems(){

        //given
        int expectedNumberOfOrderItems = 2;

        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats();

        //when
        orderTotalMoneyStats.setNumberOfOrderItems(expectedNumberOfOrderItems);

        //then
        assertEquals(expectedNumberOfOrderItems, orderTotalMoneyStats.getNumberOfOrderItems());
    }

    @Test
    public void shouldSetNumberOfTaxes(){

        //given
        int expectedNumberOfTaxes = 3;

        OrderTotalMoneyStats orderTotalMoneyStats = new OrderTotalMoneyStats();

        //when
        orderTotalMoneyStats.setNumberOfTaxes(expectedNumberOfTaxes);

        //then
        assertEquals(expectedNumberOfTaxes, orderTotalMoneyStats.getNumberOfTaxes());
    }
}