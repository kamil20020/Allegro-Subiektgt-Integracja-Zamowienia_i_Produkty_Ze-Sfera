package pl.kamil_dywan.external.subiektgt.own.product;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.own.PriceCategory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDetailedPriceTest {

    @Test
    void shouldCreateWithStringArgs() {

        //given
        String productId = "id";
        PriceCategory group = PriceCategory.RETAIL;
        BigDecimal unitPriceWithoutTax = new BigDecimal("22.02");
        BigDecimal unitPriceWithTax = new BigDecimal("24.22");
        BigDecimal markup = new BigDecimal("24.12");
        BigDecimal margin = new BigDecimal("26.14");
        BigDecimal profit = new BigDecimal("28.26");

        String[] args = {
            productId,
            group.name(),
            unitPriceWithoutTax.toString(),
            unitPriceWithTax.toString(),
            markup.toString(),
            margin.toString(),
            profit.toString()
        };

        //when
        ProductDetailedPrice productDetailedPrice = new ProductDetailedPrice(args);

        //then
        assertEquals(productId, productDetailedPrice.getProductId());
        assertEquals(group, productDetailedPrice.getGroup());
        assertEquals(unitPriceWithoutTax, productDetailedPrice.getUnitPriceWithoutTax());
        assertEquals(unitPriceWithTax, productDetailedPrice.getUnitPriceWithTax());
        assertEquals(markup, productDetailedPrice.getMarkup());
        assertEquals(margin, productDetailedPrice.getMargin());
        assertEquals(profit, productDetailedPrice.getProfit());
    }

    @Test
    public void shouldCreate(){

        //given
        String productId = "id";
        PriceCategory group = PriceCategory.RETAIL;
        BigDecimal unitPriceWithoutTax = new BigDecimal("22.02");
        BigDecimal unitPriceWithTax = new BigDecimal("24.22");
        BigDecimal markup = new BigDecimal("24.12");
        BigDecimal margin = new BigDecimal("26.14");
        BigDecimal profit = new BigDecimal("28.26");

        //when
        ProductDetailedPrice productDetailedPrice = new ProductDetailedPrice(
            productId,
            group,
            unitPriceWithoutTax,
            unitPriceWithTax,
            markup,
            margin,
            profit
        );

        //then
        assertEquals(productId, productDetailedPrice.getProductId());
        assertEquals(group, productDetailedPrice.getGroup());
        assertEquals(unitPriceWithoutTax, productDetailedPrice.getUnitPriceWithoutTax());
        assertEquals(unitPriceWithTax, productDetailedPrice.getUnitPriceWithTax());
        assertEquals(markup, productDetailedPrice.getMarkup());
        assertEquals(margin, productDetailedPrice.getMargin());
        assertEquals(profit, productDetailedPrice.getProfit());
    }
}