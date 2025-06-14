package pl.kamil_dywan.model.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProduct;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProductSet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void shouldGetHasSingleProductForSingleProduct() {

        //given
        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);

        List<OrderProduct> orderProducts = List.of(orderProduct);

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasSingleProduct();

        //then
        assertTrue(result);
    }

    @Test
    void shouldNotGetHasSingleProductForMultipleProducts() {

        //given
        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);
        OrderProduct orderProduct1 = new OrderProduct(UUID.randomUUID(), 3);

        List<OrderProduct> orderProducts = List.of(orderProduct, orderProduct1);

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasSingleProduct();

        //then
        assertFalse(result);
    }

    @Test
    void shouldNotGetHasSingleProductForNoProductSet() {

        //given
        Offer offer = new Offer();

        //when
        boolean result = offer.hasSingleProduct();

        //then
        assertFalse(result);
    }

    @Test
    void shouldNotGetHasSingleProductForProductSetNullProducts() {

        //given
        OrderProductSet orderProductSet = new OrderProductSet();

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasSingleProduct();

        //then
        assertFalse(result);
    }

    @Test
    void shouldNotGetHasSingleProductForProductSetEmptyProducts() {

        //given
        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasSingleProduct();

        //then
        assertFalse(result);
    }

}