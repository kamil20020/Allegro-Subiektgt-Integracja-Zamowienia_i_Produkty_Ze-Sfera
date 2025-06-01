package pl.kamil_dywan.external.allegro.own.order;

import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.subiektgt.own.product.TaxRateCodeMapping;
import pl.kamil_dywan.mapper.AllegroOrderItemMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record OrderMoneyStats(
    List<OrderItemMoneyStats> orderItemsMoneyStats,
    List<OrderTaxSummary> orderTaxesSummaries,
    OrderTotalMoneyStats orderTotalMoneyStats
){
    private static final int ROUNDING_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private OrderMoneyStats(Order order, int numberOfOrderItems){

        this(new ArrayList<>(numberOfOrderItems), new ArrayList<>(), new OrderTotalMoneyStats());

        Map<TaxRateCodeMapping, OrderTaxSummary> taxesMappings = OrderTaxSummary.getEmptyMappingsForAllTaxesRates();

        orderTaxesSummaries.addAll(taxesMappings.values());

        order.getOrderItems()
            .forEach(orderItem -> {

                OrderItemMoneyStats orderItemMoneyStats = OrderItemMoneyStats.getSummary(orderItem);
                orderItemsMoneyStats.add(orderItemMoneyStats);

                BigDecimal taxRatePercentage = orderItemMoneyStats.getTaxRatePercentage();

                updateTaxesMappings(taxesMappings, taxRatePercentage, orderItemMoneyStats);
                orderItemMoneyStats.scale(ROUNDING_SCALE, ROUNDING_MODE);
            });

        initTotalSummary(taxesMappings);

        scaleTaxesMappings();
        orderTotalMoneyStats.scale(ROUNDING_SCALE, ROUNDING_MODE);
    }

    public static OrderMoneyStats getSummary(Order order){

        int numberOfOrderItems = order.getOrderItems().size();

        return new OrderMoneyStats(order, numberOfOrderItems);
    }

    private static void updateTaxesMappings(
        Map<TaxRateCodeMapping, OrderTaxSummary> taxesMappings,
        BigDecimal taxRatePercentage,
        OrderItemMoneyStats orderItemMoneyStats
    ){
        TaxRateCodeMapping taxRateCodeMapping = TaxRateCodeMapping.getByValue(taxRatePercentage);
        OrderTaxSummary actualTaxSummary = taxesMappings.get(taxRateCodeMapping);

        actualTaxSummary.update(orderItemMoneyStats);
    }

    private void initTotalSummary(Map<TaxRateCodeMapping, OrderTaxSummary> taxesMappings){

        orderTotalMoneyStats.setNumberOfOrderItems(orderItemsMoneyStats.size());
        orderTotalMoneyStats.setNumberOfTaxes(OrderTaxSummary.getNumberOfPresentTaxes(taxesMappings));

        orderTaxesSummaries
            .forEach(taxSummary -> {

                orderTotalMoneyStats.update(
                    taxSummary.getTotalWithoutTax(),
                    taxSummary.getTotalTaxValue(),
                    taxSummary.getTotalWithTax()
                );
            });
    }

    private void scaleTaxesMappings(){

        orderTaxesSummaries
            .forEach(taxSummary -> {

                taxSummary.scale(ROUNDING_SCALE, ROUNDING_MODE);
            });
    }
}
