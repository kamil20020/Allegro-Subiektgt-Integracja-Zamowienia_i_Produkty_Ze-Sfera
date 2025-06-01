package pl.kamil_dywan.external.subiektgt.own.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import pl.kamil_dywan.external.subiektgt.own.PriceCategory;
import pl.kamil_dywan.file.EppSerializable;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class ProductDetailedPrice extends EppSerializable{

    private String productId;
    private PriceCategory group;
    private BigDecimal unitPriceWithoutTax;
    private BigDecimal unitPriceWithTax;
    private BigDecimal markup;
    private BigDecimal margin;
    private BigDecimal profit;

    public ProductDetailedPrice(String... args){

        super(args);

        productId = args[0];
        group = PriceCategory.valueOf(args[1]);
        unitPriceWithoutTax = new BigDecimal(args[2]);
        unitPriceWithTax = new BigDecimal(args[3]);
        markup = new BigDecimal(args[4]);
        margin = new BigDecimal(args[5]);
        profit = new BigDecimal(args[6]);
    }

    public ProductDetailedPrice(
        String productId,
        PriceCategory group,
        BigDecimal unitPriceWithoutTax,
        BigDecimal unitPriceWithTax,
        BigDecimal markup,
        BigDecimal margin,
        BigDecimal profit
    ){
        super(null);

        this.productId = productId;
        this.group = group;
        this.unitPriceWithoutTax = unitPriceWithoutTax;
        this.unitPriceWithTax = unitPriceWithTax;
        this.markup = markup;
        this.margin = margin;
        this.profit = profit;
    }
}
