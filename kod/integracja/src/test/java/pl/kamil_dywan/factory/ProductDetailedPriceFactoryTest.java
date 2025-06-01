package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.subiektgt.own.product.ProductDetailedPrice;
import pl.kamil_dywan.external.subiektgt.own.PriceCategory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductDetailedPriceFactoryTest {

    @Test
    void shouldCreate() {

        //given
        BigDecimal unitPriceWithoutTax = new BigDecimal("20.00");
        BigDecimal unitPriceWithTax = new BigDecimal("24.60");
        Long expectedProductId = 123L;

        //when
        ProductDetailedPrice gotProductDetailedPrice = ProductDetailedPriceFactory.create(
            expectedProductId.toString(),
            unitPriceWithoutTax,
            unitPriceWithTax
        );

        //then
        assertNotNull(gotProductDetailedPrice);
        assertEquals(expectedProductId.toString(), gotProductDetailedPrice.getProductId());
        assertEquals(PriceCategory.RETAIL, gotProductDetailedPrice.getGroup());
        assertEquals(unitPriceWithoutTax, gotProductDetailedPrice.getUnitPriceWithoutTax());
        assertEquals(unitPriceWithTax, gotProductDetailedPrice.getUnitPriceWithTax());
        assertEquals(BigDecimal.ZERO, gotProductDetailedPrice.getMarkup());
        assertEquals(BigDecimal.ZERO, gotProductDetailedPrice.getMargin());
        assertEquals(BigDecimal.ZERO, gotProductDetailedPrice.getProfit());
    }
}