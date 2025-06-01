package pl.kamil_dywan.external.allegro.own.order;

import lombok.*;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class OrderItemMoneyStats {

    private BigDecimal taxRatePercentage;
    private BigDecimal unitPriceWithTax;
    private BigDecimal unitPriceWithoutTax;
    private BigDecimal totalPriceWithTax;
    private BigDecimal totalPriceWithoutTax;
    private BigDecimal totalTaxValue;

    private OrderItemMoneyStats(OrderItem orderItem){

        BigDecimal quantityValue = BigDecimal.valueOf(orderItem.getQuantity());

        this.taxRatePercentage = orderItem.getTaxRatePercentage();
        this.unitPriceWithTax = orderItem.getPrice().getAmount();
        this.unitPriceWithoutTax = orderItem.getUnitPriceWithoutTax();
        this.totalPriceWithTax = this.unitPriceWithTax.multiply(quantityValue);
        this.totalPriceWithoutTax = this.unitPriceWithoutTax.multiply(quantityValue);
        this.totalTaxValue = this.totalPriceWithTax.subtract(this.totalPriceWithoutTax);
    }

    public static OrderItemMoneyStats getSummary(OrderItem orderItem){

        return new OrderItemMoneyStats(orderItem);
    }

    public void scale(int scale, RoundingMode roundingMode){

        unitPriceWithTax = unitPriceWithTax.setScale(scale, roundingMode);
        unitPriceWithoutTax = unitPriceWithoutTax.setScale(scale, roundingMode);
        totalPriceWithTax = totalPriceWithTax.setScale(scale, roundingMode);
        totalPriceWithoutTax = totalPriceWithoutTax.setScale(scale, roundingMode);
        totalTaxValue = totalTaxValue.setScale(scale, roundingMode);
    }

}

