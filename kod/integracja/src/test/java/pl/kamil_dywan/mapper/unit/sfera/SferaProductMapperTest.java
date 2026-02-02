package pl.kamil_dywan.mapper.unit.sfera;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedData;
import pl.kamil_dywan.external.allegro.generated.offer_product.SellingMode;
import pl.kamil_dywan.external.allegro.generated.order_item.*;
import pl.kamil_dywan.external.allegro.own.offer.Signature;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.mapper.sfera.SferaProductMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaProductMapperTest {

    @Test
    void shouldMapWithExternalId() {

        //given
        BigDecimal expectedTotalPriceWithTax = new BigDecimal("64.96");

        ExternalId externalId = new ExternalId("12");

        Offer offer = Offer.builder()
            .id("offer_id")
            .external(externalId)
            .name("Oferta 123")
            .build();

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN.toString()
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
        assertEquals(externalId.getId(), gotProduct.getCode());
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
            Currency.PLN.toString()
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
            Currency.PLN.toString()
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

        Signature signature = new Signature("12", 2);
        ExternalId externalId = new ExternalId(signature);

        Offer offer = Offer.builder()
            .id(UUID.randomUUID().toString())
            .external(externalId)
            .name("Oferta 123")
            .productSet(orderProductSet)
            .build();

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN.toString()
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
        assertEquals(signature.subiektSymbol(), gotProduct.getCode());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(expectedTotalPriceWithTax, gotProduct.getPriceWithTax());
        assertEquals(signature.quantity() * orderItem.getQuantity(), gotProduct.getQuantity());
    }

    @Test
    public void shouldMapFromProductOfferResponse(){

        //given
        Long expectedOfferId = 123L;
        Signature signature = new Signature("12", 2);

        ExternalId externalId = new ExternalId(signature);

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN.toString()
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
        assertEquals(signature.subiektSymbol(), gotProduct.getCode());
        assertEquals(product.getName(), gotProduct.getName());
        assertEquals(cost.getAmount(), gotProduct.getPriceWithTax());
        assertEquals(signature.quantity(), gotProduct.getQuantity());
    }

    @Test
    public void shouldMapFromProductOfferResponseForManyProducts(){

        //given
        Long expectedOfferId = 123L;
        Signature signature = new Signature("12", 2);

        ProductOfferProductRelatedData product = new ProductOfferProductRelatedData();
        ProductOfferProductRelatedData product1 = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> products = List.of(product, product1);

        Cost cost = new Cost(
            new BigDecimal("32.48"),
            Currency.PLN.toString()
        );

        SellingMode sellingMode = new SellingMode(cost);

        ExternalId externalId = new ExternalId(signature);

        ProductOfferResponse offer = ProductOfferResponse.builder()
            .id(expectedOfferId)
            .externalId(externalId)
            .name("Oferta 123")
            .sellingMode(sellingMode)
            .productSet(products)
            .build();

        //when
        Product gotOffer = SferaProductMapper.map(offer);

        //then
        assertNotNull(gotOffer);
        assertEquals(signature.subiektSymbol(), gotOffer.getCode());
        assertEquals(offer.getName(), gotOffer.getName());
        assertEquals(cost.getAmount(), gotOffer.getPriceWithTax());
        assertEquals(signature.quantity(), gotOffer.getQuantity());
    }

}