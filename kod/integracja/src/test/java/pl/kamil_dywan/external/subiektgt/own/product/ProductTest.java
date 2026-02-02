package pl.kamil_dywan.external.subiektgt.own.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldSetId() {

        //given
        String expectedId = "id";
        Product product = new Product();

        //when
        product.setId(expectedId);

        //then
        assertEquals(expectedId, product.getId());
    }

    @Test
    public void shouldCreateWithStringArgs(){

        //given
        ProductType type = ProductType.GOODS;
        String id = "123";
        String name = "name";
        BigDecimal taxRatePercentage = new BigDecimal("23");
        BigDecimal unitPriceWithoutTax = new BigDecimal("22.24");

        String[] args = {
            type.toString(),
            id,
            name,
            taxRatePercentage.toString(),
            unitPriceWithoutTax.toString()
        };

        //when
        Product product = new Product(args);

        //then
        assertEquals(type, product.getType());
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(taxRatePercentage, product.getTaxRatePercentage());
        assertEquals(unitPriceWithoutTax, product.getUnitPriceWithoutTax());
    }

    @Test
    public void shouldCreate(){

        //given
        ProductType type = ProductType.GOODS;
        String id = "123";
        String name = "name";
        BigDecimal taxRatePercentage = new BigDecimal("23");
        BigDecimal unitPriceWithoutTax = new BigDecimal("22.24");

        //when
        Product product = new Product(type, id, name, taxRatePercentage, unitPriceWithoutTax);

        //then
        assertEquals(type, product.getType());
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(taxRatePercentage, product.getTaxRatePercentage());
        assertEquals(unitPriceWithoutTax, product.getUnitPriceWithoutTax());
    }

    @Test
    public void shouldCreateNoArgs(){

        //given
        //when
        Product product = new Product();

        //then
        assertNull(product.getType());
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getTaxRatePercentage());
        assertNull(product.getUnitPriceWithoutTax());
    }
}