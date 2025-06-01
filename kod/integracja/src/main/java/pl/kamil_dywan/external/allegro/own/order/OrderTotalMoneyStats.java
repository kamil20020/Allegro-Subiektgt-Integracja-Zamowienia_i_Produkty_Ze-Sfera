package pl.kamil_dywan.external.allegro.own.order;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class OrderTotalMoneyStats {

    private Integer numberOfOrderItems = 0;
    private Integer numberOfTaxes = 0;
    private BigDecimal totalWithoutTax = BigDecimal.ZERO;
    private BigDecimal totalWithTax = BigDecimal.ZERO;
    private BigDecimal taxValue = BigDecimal.ZERO;

    public void setNumberOfOrderItems(Integer numberOfOrderItems) {

        this.numberOfOrderItems = numberOfOrderItems;
    }

    public void setNumberOfTaxes(Integer numberOfTaxes) {

        this.numberOfTaxes = numberOfTaxes;
    }

    public void update(BigDecimal totalPriceWithoutTax, BigDecimal taxValue, BigDecimal totalWithTax){

        this.totalWithoutTax = this.totalWithoutTax.add(totalPriceWithoutTax);
        this.taxValue = this.taxValue.add(taxValue);
        this.totalWithTax = this.totalWithTax.add(totalWithTax);
    }

    public void scale(int scale, RoundingMode roundingMode){

        totalWithoutTax = totalWithoutTax.setScale(scale, roundingMode);
        taxValue = taxValue.setScale(scale, roundingMode);
        totalWithTax = totalWithTax.setScale(scale, roundingMode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTotalMoneyStats that = (OrderTotalMoneyStats) o;
        return Objects.equals(numberOfOrderItems, that.numberOfOrderItems) && Objects.equals(numberOfTaxes, that.numberOfTaxes) && Objects.equals(totalWithoutTax, that.totalWithoutTax) && Objects.equals(totalWithTax, that.totalWithTax) && Objects.equals(taxValue, that.taxValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfOrderItems, numberOfTaxes, totalWithoutTax, totalWithTax, taxValue);
    }
}