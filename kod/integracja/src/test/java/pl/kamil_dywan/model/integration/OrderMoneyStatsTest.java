package pl.kamil_dywan.model.integration;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.delivery.DeliveryTime;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Tax;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderTaxSummary;
import pl.kamil_dywan.external.allegro.own.order.OrderTotalMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMoneyStatsTest {

    @Test
    void shouldGetSummaryWithoutDelivery() {

        //given
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemMoneyStats> expectedOrderItemsMoneyStats = new ArrayList<>();

        orderItems.add(OrderItem.builder()
            .quantity(70)
            .price(new Cost(new BigDecimal("146.493"), Currency.PLN))
            .tax(new Tax(new BigDecimal(TaxRateCodeMapping.H.getValue()), "", ""))
            .build()
        );

        expectedOrderItemsMoneyStats.add(new OrderItemMoneyStats(
            new BigDecimal(TaxRateCodeMapping.H.getValue()),
            new BigDecimal("146.49"),
            new BigDecimal("119.10"),
            new BigDecimal("10254.51"),
            new BigDecimal("8337.00"),
            new BigDecimal("1917.51")
        ));

        orderItems.add(OrderItem.builder()
            .quantity(70)
            .price(new Cost(new BigDecimal("35.98"), Currency.PLN))
            .tax(new Tax(new BigDecimal(TaxRateCodeMapping.Z.getValue()), "", ""))
            .build()
        );

        expectedOrderItemsMoneyStats.add(new OrderItemMoneyStats(
            new BigDecimal(TaxRateCodeMapping.Z.getValue()),
            new BigDecimal("35.98"),
            new BigDecimal("35.98"),
            new BigDecimal("2518.60"),
            new BigDecimal("2518.60"),
            new BigDecimal("0.00")
        ));

        orderItems.add(OrderItem.builder()
            .quantity(50)
            .price(new Cost(new BigDecimal("349.92"), Currency.PLN))
            .tax(new Tax(new BigDecimal(TaxRateCodeMapping.L.getValue()), "", ""))
            .build()
        );

        expectedOrderItemsMoneyStats.add(new OrderItemMoneyStats(
            new BigDecimal(TaxRateCodeMapping.L.getValue()),
            new BigDecimal("349.92"),
            new BigDecimal("324.00"),
            new BigDecimal("17496.00"),
            new BigDecimal("16200.00"),
            new BigDecimal("1296.00")
        ));

        orderItems.add(OrderItem.builder()
            .quantity(50)
            .price(new Cost(new BigDecimal("171.00"), Currency.PLN))
            .tax(new Tax(new BigDecimal(TaxRateCodeMapping.Z.getValue()), "", ""))
            .build()
        );

        expectedOrderItemsMoneyStats.add(new OrderItemMoneyStats(
            new BigDecimal(TaxRateCodeMapping.Z.getValue()),
            new BigDecimal("171.00"),
            new BigDecimal("171.00"),
            new BigDecimal("8550.00"),
            new BigDecimal("8550.00"),
            new BigDecimal("0.00")
        ));

        orderItems.add(OrderItem.builder()
            .quantity(50)
            .price(new Cost(new BigDecimal("427.68"), Currency.PLN))
            .tax(new Tax(new BigDecimal(TaxRateCodeMapping.L.getValue()), "", ""))
            .build()
        );

        expectedOrderItemsMoneyStats.add(new OrderItemMoneyStats(
            new BigDecimal(TaxRateCodeMapping.L.getValue()),
            new BigDecimal("427.68"),
            new BigDecimal("396.00"),
            new BigDecimal("21384.00"),
            new BigDecimal("19800.00"),
            new BigDecimal("1584.00")
        ));

        Order order = Order.builder()
            .orderItems(orderItems)
            .build();

        List<OrderTaxSummary> expectedOrderTaxSummaries = new ArrayList<>();

        expectedOrderTaxSummaries.add(OrderTaxSummary.builder()
            .taxRateValue(BigDecimal.valueOf(TaxRateCodeMapping.H.getValue()))
            .totalWithoutTax(new BigDecimal("8337.00"))
            .totalWithTax(new BigDecimal("10254.51"))
            .totalTaxValue(new BigDecimal("1917.51"))
            .build()
        );

        expectedOrderTaxSummaries.add(OrderTaxSummary.builder()
            .taxRateValue(BigDecimal.valueOf(TaxRateCodeMapping.L.getValue()))
            .totalWithoutTax(new BigDecimal("36000.00"))
            .totalWithTax(new BigDecimal("38880.00"))
            .totalTaxValue(new BigDecimal("2880.00"))
            .build()
        );

        expectedOrderTaxSummaries.add(OrderTaxSummary.builder()
            .taxRateValue(BigDecimal.valueOf(TaxRateCodeMapping.Z.getValue()))
            .totalWithoutTax(new BigDecimal("11068.60"))
            .totalWithTax(new BigDecimal("11068.60"))
            .totalTaxValue(new BigDecimal("0.00"))
            .build()
        );

        OrderTotalMoneyStats expectedOrderTotalMoneyStats = new OrderTotalMoneyStats(
            5,
            3,
            new BigDecimal("55405.60"),
            new BigDecimal("60203.11"),
            new BigDecimal("4797.51")
        );

        //when
        OrderMoneyStats orderMoneyStats = OrderMoneyStats.getSummary(order);

        //then
        assertEquals(expectedOrderTotalMoneyStats, orderMoneyStats.orderTotalMoneyStats());
        assertArrayEquals(expectedOrderTaxSummaries.toArray(), orderMoneyStats.orderTaxesSummaries().toArray());
        assertArrayEquals(expectedOrderItemsMoneyStats.toArray(), orderMoneyStats.orderItemsMoneyStats().toArray());
    }
}