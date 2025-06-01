package pl.kamil_dywan.mapper.unit.receipt;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.receipt.ReceiptItem;
import pl.kamil_dywan.mapper.receipt.ReceiptItemMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptItemMapperTest {

    @Test
    void shouldMapWithoutExternal() {

        //given
        int expectedReceiptItemIndex = 2;

        OrderItem orderItem = OrderItem.builder()
            .offer(Offer.builder()
                .id(UUID.randomUUID().toString())
                .build()
            )
            .quantity(2)
            .build();

        OrderItemMoneyStats orderItemMoneyStats = new OrderItemMoneyStats(
            new BigDecimal("23"),
            new BigDecimal("24.34"),
            new BigDecimal("22.26"),
            new BigDecimal("36.48"),
            new BigDecimal("34.28"),
            new BigDecimal("18.24")
        );

        //when
        ReceiptItem receiptItem = ReceiptItemMapper.map(orderItem, orderItemMoneyStats, expectedReceiptItemIndex);

        //then
        assertNotNull(receiptItem);
        assertEquals(expectedReceiptItemIndex, receiptItem.getReceiptItemIndex());
        assertEquals(orderItem.getOffer().getId(), receiptItem.getId());
        assertEquals(orderItem.getQuantity(), receiptItem.getQuantity());
        assertEquals(orderItemMoneyStats.getUnitPriceWithoutTax(), receiptItem.getUnitPriceWithoutTax());
        assertEquals(orderItemMoneyStats.getUnitPriceWithTax(), receiptItem.getUnitPriceWithPrice());
        assertEquals(orderItemMoneyStats.getTaxRatePercentage(), receiptItem.getTaxRateValue());
        assertEquals(orderItemMoneyStats.getTotalPriceWithoutTax(), receiptItem.getTotalPriceWithoutTax());
        assertEquals(orderItemMoneyStats.getTotalTaxValue(), receiptItem.getTotalTaxValue());
        assertEquals(orderItemMoneyStats.getTotalPriceWithTax(), receiptItem.getTotalPriceWithTax());
    }

    @Test
    void shouldMapWithoutPartiallyExternal() {

        //given
        int expectedReceiptItemIndex = 2;

        OrderItem orderItem = OrderItem.builder()
            .offer(Offer.builder()
                .id(UUID.randomUUID().toString())
                .external(new ExternalId())
                .build()
            )
            .quantity(2)
            .build();

        OrderItemMoneyStats orderItemMoneyStats = new OrderItemMoneyStats(
            new BigDecimal("23"),
            new BigDecimal("24.34"),
            new BigDecimal("22.26"),
            new BigDecimal("36.48"),
            new BigDecimal("34.28"),
            new BigDecimal("18.24")
        );

        //when
        ReceiptItem receiptItem = ReceiptItemMapper.map(orderItem, orderItemMoneyStats, expectedReceiptItemIndex);

        //then
        assertNotNull(receiptItem);
        assertEquals(expectedReceiptItemIndex, receiptItem.getReceiptItemIndex());
        assertEquals(orderItem.getOffer().getId(), receiptItem.getId());
        assertEquals(orderItem.getQuantity(), receiptItem.getQuantity());
        assertEquals(orderItemMoneyStats.getUnitPriceWithoutTax(), receiptItem.getUnitPriceWithoutTax());
        assertEquals(orderItemMoneyStats.getUnitPriceWithTax(), receiptItem.getUnitPriceWithPrice());
        assertEquals(orderItemMoneyStats.getTaxRatePercentage(), receiptItem.getTaxRateValue());
        assertEquals(orderItemMoneyStats.getTotalPriceWithoutTax(), receiptItem.getTotalPriceWithoutTax());
        assertEquals(orderItemMoneyStats.getTotalTaxValue(), receiptItem.getTotalTaxValue());
        assertEquals(orderItemMoneyStats.getTotalPriceWithTax(), receiptItem.getTotalPriceWithTax());

    }

    @Test
    void shouldMapWithExternal() {

        //given
        String expectedId = "12345";

        int expectedReceiptItemIndex = 2;

        ExternalId externalId = new ExternalId(expectedId);

        OrderItem orderItem = OrderItem.builder()
            .offer(Offer.builder()
                .id(UUID.randomUUID().toString())
                .external(externalId)
                .build()
            )
            .quantity(2)
            .build();

        OrderItemMoneyStats orderItemMoneyStats = new OrderItemMoneyStats(
                new BigDecimal("23"),
                new BigDecimal("24.34"),
                new BigDecimal("22.26"),
                new BigDecimal("36.48"),
                new BigDecimal("34.28"),
                new BigDecimal("18.24")
        );

        //when
        ReceiptItem receiptItem = ReceiptItemMapper.map(orderItem, orderItemMoneyStats, expectedReceiptItemIndex);

        //then
        assertNotNull(receiptItem);
        assertEquals(expectedReceiptItemIndex, receiptItem.getReceiptItemIndex());
        assertEquals(expectedId, receiptItem.getId());
        assertEquals(orderItem.getQuantity(), receiptItem.getQuantity());
        assertEquals(orderItemMoneyStats.getUnitPriceWithoutTax(), receiptItem.getUnitPriceWithoutTax());
        assertEquals(orderItemMoneyStats.getUnitPriceWithTax(), receiptItem.getUnitPriceWithPrice());
        assertEquals(orderItemMoneyStats.getTaxRatePercentage(), receiptItem.getTaxRateValue());
        assertEquals(orderItemMoneyStats.getTotalPriceWithoutTax(), receiptItem.getTotalPriceWithoutTax());
        assertEquals(orderItemMoneyStats.getTotalTaxValue(), receiptItem.getTotalTaxValue());
        assertEquals(orderItemMoneyStats.getTotalPriceWithTax(), receiptItem.getTotalPriceWithTax());

    }
}