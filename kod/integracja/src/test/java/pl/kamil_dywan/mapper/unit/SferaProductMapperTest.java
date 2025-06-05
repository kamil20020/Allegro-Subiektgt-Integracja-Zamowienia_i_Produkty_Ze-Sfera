package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource(value = {
        "offer_id, producer#ean, producer, ean",
        "offer_id, #ean, offer_id, ean",
        "offer_id, producer#, producer, ",
        "offer_id, , offer_id, ",
    })
    void shouldMapWithExternalId(String expectedOfferId, String expectedCombinedCode, String expectedCode, String expectedEan) {

        //given
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
        assertNull(gotProduct.getEan());
        assertEquals(offer.getName(), gotProduct.getName());
        assertEquals(orderItem.getPrice().getAmount(), gotProduct.getPriceWithTax());
        assertEquals(orderItem.getQuantity(), gotProduct.getQuantity());
    }

}