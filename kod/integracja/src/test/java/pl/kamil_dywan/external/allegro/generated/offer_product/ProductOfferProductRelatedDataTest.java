package pl.kamil_dywan.external.allegro.generated.offer_product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductOfferProductRelatedDataTest {

    @Test
    void shouldCreate() {

        //given
        ProductOfferProduct product = new ProductOfferProduct();

        //when
        ProductOfferProductRelatedData data = new ProductOfferProductRelatedData(product);

        assertEquals(product, data.getProduct());
    }
}