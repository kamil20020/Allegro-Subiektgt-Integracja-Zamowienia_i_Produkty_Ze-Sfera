package pl.kamil_dywan.external.subiektgt.own.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @CsvSource(value = {
        "1, GOODS",
        "2, SERVICES"
    })
    @ParameterizedTest
    void shouldGetValueOf(Integer value, String expectedEnumName) {

        //given
        //when
        ProductType productType = ProductType.valueOf(value);

        //then
        assertNotNull(productType);
        assertEquals(expectedEnumName, productType.name());
    }

    @Test
    public void shouldNotGetValueOfForDifferentValue(){

        //given
        //when
        assertThrows(
            IllegalArgumentException.class,
            () -> ProductType.valueOf("3")
        );
    }
}