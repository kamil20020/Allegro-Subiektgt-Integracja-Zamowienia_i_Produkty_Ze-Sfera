package pl.kamil_dywan.mapper.unit.sfera;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProductParameter;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedData;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedDataQuantity;
import pl.kamil_dywan.external.sfera.generated.ProductSetProduct;
import pl.kamil_dywan.mapper.sfera.SferaProductSetProductMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaProductSetProductMapperTest {

    @Test
    void shouldMap() {

        //given
        String expectedCode = "12345";
        String expectedEanCode = "ean";

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(123L)
            .name(ProductOfferProduct.PRODUCER_CODE_KEY)
            .values(List.of(expectedCode))
            .build();

        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Inny parametr")
            .values(List.of("6789"))
            .build();

        OfferProductParameter eanCodeParameter = OfferProductParameter.builder()
            .id(789L)
            .name(ProductOfferProduct.EAN_CODE_KEY)
            .values(List.of(expectedEanCode))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter, producerCodeParameter, eanCodeParameter));

        ProductOfferProductRelatedDataQuantity quantity = new ProductOfferProductRelatedDataQuantity(2);

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product, quantity);

        //when
        ProductSetProduct productSetProduct = SferaProductSetProductMapper.map(productRelatedData);

        //then
        assertNotNull(productSetProduct);
        assertEquals(expectedCode, productSetProduct.getCode());
        assertEquals(expectedEanCode, productSetProduct.getEan());
        assertEquals(quantity.getValue(), productSetProduct.getQuantity());
    }

}