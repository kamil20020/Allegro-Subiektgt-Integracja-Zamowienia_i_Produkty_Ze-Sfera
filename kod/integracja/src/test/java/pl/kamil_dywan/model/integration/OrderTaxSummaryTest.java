package pl.kamil_dywan.model.integration;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.allegro.own.order.OrderTaxSummary;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderTaxSummaryTest {

    @Test
    void shouldGetEmpty() {

        //given
        TaxRateCodeMapping taxRateCodeMapping = TaxRateCodeMapping.H;

        //when
        OrderTaxSummary gotOrderTaxSummary = OrderTaxSummary.getEmpty(taxRateCodeMapping);
        BigDecimal gotTaxRateValue = gotOrderTaxSummary.getTaxRateValue();

        //then
        assertEquals(taxRateCodeMapping.getValue(), gotTaxRateValue.intValue());
    }

    @Test
    void shouldUpdate() {

        //given
        BigDecimal expectedTotalWithoutTax = new BigDecimal("46.72");
        BigDecimal expectedTotalWithTax = new BigDecimal("54.40");
        BigDecimal expectedTotalTaxValue = new BigDecimal("15.16");

        OrderTaxSummary orderTaxSummary = new OrderTaxSummary(
            BigDecimal.valueOf(23),
            new BigDecimal("12.34"),
            new BigDecimal("6.14"),
            new BigDecimal("2.68")
        );

        OrderItemMoneyStats orderItemMoneyStats = new OrderItemMoneyStats(
            null,
            null,
            null,
            new BigDecimal("48.26"),
            new BigDecimal("34.38"),
            new BigDecimal("12.48")
        );

        //when
        orderTaxSummary.update(orderItemMoneyStats);

        //then
        assertEquals(expectedTotalWithoutTax, orderTaxSummary.getTotalWithoutTax());
        assertEquals(expectedTotalWithTax, orderTaxSummary.getTotalWithTax());
        assertEquals(expectedTotalTaxValue, orderTaxSummary.getTotalTaxValue());
    }

    @Test
    void shouldScale() {

        //given
        OrderTaxSummary expectedOrderTaxSummary = new OrderTaxSummary(
            new BigDecimal("23.00"),
            new BigDecimal("23.00"),
            new BigDecimal("23.01"),
            new BigDecimal("23.01")
        );

        OrderTaxSummary gotOrderTaxSummary = new OrderTaxSummary(
            new BigDecimal("23.00"),
            new BigDecimal("23.004"),
            new BigDecimal("23.005"),
            new BigDecimal("23.006")
        );

        //when
        gotOrderTaxSummary.scale(2, RoundingMode.HALF_UP);

        //then
        assertEquals(expectedOrderTaxSummary, gotOrderTaxSummary);
    }

    @Test
    void shouldGetEmptyMappingsForAllTaxesRates() {

        //given

        //when
        Map<TaxRateCodeMapping, OrderTaxSummary> gotMappings = OrderTaxSummary.getEmptyMappingsForAllTaxesRates();

        //then
        for(Map.Entry<TaxRateCodeMapping, OrderTaxSummary> gotMapping : gotMappings.entrySet()){

            TaxRateCodeMapping keyMapping = gotMapping.getKey();
            OrderTaxSummary gotValue = gotMapping.getValue();

            assertEquals(keyMapping.getValue(), gotValue.getTaxRateValue().intValue());
        }
    }

    @Test
    void shouldGetNumberOfPresentTaxes() {

        //given
        int expectedResult = 2;

        Map<TaxRateCodeMapping, OrderTaxSummary> taxesMappings = OrderTaxSummary.getEmptyMappingsForAllTaxesRates();

        taxesMappings.put(
            TaxRateCodeMapping.H,
            OrderTaxSummary.builder()
                .totalWithoutTax(new BigDecimal("23.00"))
                .build()
        );

        taxesMappings.put(
            TaxRateCodeMapping.L,
            OrderTaxSummary.builder()
                .totalWithoutTax(new BigDecimal("10.00"))
                .build()
        );

        //when
        int gotResult = OrderTaxSummary.getNumberOfPresentTaxes(taxesMappings);

        //then
        assertEquals(expectedResult, gotResult);
    }
}