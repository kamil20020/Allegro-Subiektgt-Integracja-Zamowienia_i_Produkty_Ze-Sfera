package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.subiektgt.own.product.ProductDetailedPrice;
import pl.kamil_dywan.external.subiektgt.own.PriceCategory;

import java.math.BigDecimal;

public interface ProductDetailedPriceFactory {

    public static ProductDetailedPrice create(String productId, BigDecimal unitPriceWithoutTax, BigDecimal unitPriceWithTax){

        return ProductDetailedPrice.builder()
            .productId(productId)
            .group(PriceCategory.RETAIL)
            .unitPriceWithoutTax(unitPriceWithoutTax)
            .unitPriceWithTax(unitPriceWithTax)
            .markup(BigDecimal.ZERO)
            .margin(BigDecimal.ZERO)
            .profit(BigDecimal.ZERO)
            .build();
    }
}
