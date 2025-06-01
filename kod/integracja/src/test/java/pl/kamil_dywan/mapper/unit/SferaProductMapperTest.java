package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.mapper.sfera.SferaProductMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SferaProductMapperTest {

    @Test
    void shouldMapWithExternalId() {

        //given
        ExternalId externalId = new ExternalId("123");

        Offer offer = Offer.builder()
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
        assertEquals(externalId.getId(), gotProduct.getCode());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(orderItem.getPrice().getAmount(), gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

    @Test
    void shouldMapWithoutExternalId() {

        //given
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
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(orderItem.getPrice().getAmount(), gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

}