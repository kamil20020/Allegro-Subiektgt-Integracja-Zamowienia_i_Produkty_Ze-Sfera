package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProductParameter;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductOfferProductTest {

    @Test
    public void shouldGetExistingProducerCode(){

        //given
        String inputProducerCode = "12345";
        String expectedProducerCode = "12345";

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(123L)
            .name("Kod producenta")
            .values(List.of(inputProducerCode))
            .build();

        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Inny parametr")
            .values(List.of("6789"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter, producerCodeParameter));

        //when
        String gotProducerCode = product.getProducerCode();

        //then
        assertNotNull(gotProducerCode);
        assertEquals(expectedProducerCode, gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereAreNotParameters(){

        //given
        ProductOfferProduct product = new ProductOfferProduct();

        //when
        String gotProducerCode = product.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereAreEmptyParameters(){

        //given
        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), new ArrayList<>());

        //when
        String gotProducerCode = product.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereIsNotProducerParameter(){

        //given
        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Inny parametr")
            .values(List.of("6789"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter));

        //when
        String gotProducerCode = product.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereAreNotProducerParameterValues(){

        //given
        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Kod producenta")
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter));

        //when
        String gotProducerCode = product.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenProducerParameterValuesAreEmpty(){

        //given
        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Kod producenta")
            .values(new ArrayList<>())
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter));

        //when
        String gotProducerCode = product.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldGetExistingEanCode(){

        //given
        String expectedEanCode = "ean";

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(123L)
            .name("EAN (GTIN)")
            .values(List.of(expectedEanCode))
            .build();

        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Kod producenta")
            .values(List.of("6789"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter, producerCodeParameter));

        //when
        String gotEanCode = product.getEANCode();

        //then
        assertNotNull(gotEanCode);
        assertEquals(expectedEanCode, gotEanCode);
    }

}