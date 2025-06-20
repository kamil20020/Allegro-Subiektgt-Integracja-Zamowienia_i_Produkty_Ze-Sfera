package pl.kamil_dywan.mapper.unit.sfera;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedData;
import pl.kamil_dywan.external.allegro.generated.offer_product.SellingMode;
import pl.kamil_dywan.external.allegro.generated.order_item.*;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.mapper.sfera.SferaProductMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaProductMapperTest {

    @ParameterizedTest
    @CsvSource(value = {
        "offer_id, producer#ean, producer, ean",
        "offer_id, #ean, offer_id, ean",
        "offer_id, producer#, producer, ",
        "offer_id, producer, producer, ",
        "offer_id, #, offer_id, ",
        "offer_id, , offer_id, ",
    })
    void shouldMapWithExternalId(String expectedOfferId, String expectedCombinedCode, String expectedCode, String expectedEan) {

        //given
        BigDecimal expectedTotalPriceWithTax = new BigDecimal("64.96");

        ExternalId externalId = new ExternalId(expectedCombinedCode);

        Offer offer = Offer.builder()
            .id(expectedOfferId)
            .external(externalId)
            .name("Oferta 123")
            .build();

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .price(cost)
            .quantity(2)
            .build();

        //when
        Product gotProduct = SferaProductMapper.map(orderItem);

        //then
        assertNotNull(gotProduct);
        assertEquals(expectedCode, gotProduct.getCode());
        assertEquals(expectedEan, gotProduct.getEan());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(expectedTotalPriceWithTax, gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

    @Test
    void shouldMapWithPartiallyExternalId() {

        //given
        BigDecimal expectedTotalPriceWithTax = new BigDecimal("64.96");

        Offer offer = Offer.builder()
            .id(UUID.randomUUID().toString())
            .name("Oferta 123")
            .external(new ExternalId())
            .build();

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .price(cost)
            .quantity(2)
            .build();

        //when
        Product gotProduct = SferaProductMapper.map(orderItem);

        //then
        assertNotNull(gotProduct);
        assertEquals(offer.getId(), gotProduct.getCode());
        assertNull(gotProduct.getEan());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(expectedTotalPriceWithTax, gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

    @Test
    void shouldMapWithoutExternalId() {

        //given
        BigDecimal expectedTotalPriceWithTax = new BigDecimal("64.96");

        Offer offer = Offer.builder()
            .id(UUID.randomUUID().toString())
            .name("Oferta 123")
            .build();

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .price(cost)
            .quantity(2)
            .build();

        //when
        Product gotProduct = SferaProductMapper.map(orderItem);

        //then
        assertNotNull(gotProduct);
        assertEquals(offer.getId(), gotProduct.getCode());
        assertNull(gotProduct.getEan());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(expectedTotalPriceWithTax, gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

    @Test
    void shouldMapForManyProducts() {

        //given
        BigDecimal expectedTotalPriceWithTax = new BigDecimal("64.96");

        OrderProduct orderProduct = new OrderProduct();
        OrderProduct orderProduct1 = new OrderProduct();

        List<OrderProduct> orderProducts = List.of(orderProduct, orderProduct1);

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .id(UUID.randomUUID().toString())
            .name("Oferta 123")
            .productSet(orderProductSet)
            .build();

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        OrderItem orderItem = OrderItem.builder()
            .offer(offer)
            .price(cost)
            .quantity(2)
            .build();

        //when
        Product gotProduct = SferaProductMapper.map(orderItem);

        //then
        assertNotNull(gotProduct);
        assertEquals("Zestaw-" + offer.getId(), gotProduct.getCode());
        assertNull(gotProduct.getEan());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(expectedTotalPriceWithTax, gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

    @Test
    public void shouldMapFromProductOfferResponse(){

        //given
        Long expectedOfferId = 123L;
        String expectedCombinedCode = "producer#ean";
        String expectedCode = "producer";
        String expectedEan = "ean";

        ExternalId externalId = new ExternalId(expectedCombinedCode);

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        SellingMode sellingMode = new SellingMode(cost);

        ProductOfferResponse product = ProductOfferResponse.builder()
            .id(expectedOfferId)
            .externalId(externalId)
            .name("Oferta 123")
            .sellingMode(sellingMode)
            .build();

        //when
        Product gotProduct = SferaProductMapper.map(product);

        //then
        assertNotNull(gotProduct);
        assertEquals(expectedCode, gotProduct.getCode());
        assertEquals(expectedEan, gotProduct.getEan());
        assertEquals(product.getName(), gotProduct.getName());
        assertEquals(cost.getAmount(), gotProduct.getPriceWithTax());
        assertEquals(1, gotProduct.getQuantity());
    }

    @Test
    public void shouldMapFromProductOfferResponseForManyProducts(){

        //given
        Long expectedOfferId = 123L;

        ProductOfferProductRelatedData product = new ProductOfferProductRelatedData();
        ProductOfferProductRelatedData product1 = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> products = List.of(product, product1);

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN
        );

        SellingMode sellingMode = new SellingMode(cost);

        ProductOfferResponse offer = ProductOfferResponse.builder()
            .id(expectedOfferId)
            .name("Oferta 123")
            .sellingMode(sellingMode)
            .productSet(products)
            .build();

        //when
        Product gotOffer = SferaProductMapper.map(offer);

        //then
        assertNotNull(gotOffer);
        assertEquals("Zestaw-" + expectedOfferId, gotOffer.getCode());
        assertNull(gotOffer.getEan());
        assertEquals(offer.getName(), gotOffer.getName());
        assertEquals(cost.getAmount(), gotOffer.getPriceWithTax());
        assertEquals(1, gotOffer.getQuantity());
    }

}