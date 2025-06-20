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
    void shouldGetHasProductsForSingleProduct() {

        //given
        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);

        List<OrderProduct> orderProducts = List.of(orderProduct);

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasProducts();

        //then
        assertTrue(result);
    }

    @Test
    void shouldNotGetHasProductsForNoProductSet() {

        //given
        Offer offer = new Offer();

        //when
        boolean result = offer.hasProducts();

        //then
        assertFalse(result);
    }

    @Test
    void shouldNotGetHasProductsForProductSetNullProducts() {

        //given
        OrderProductSet orderProductSet = new OrderProductSet();

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasProducts();

        //then
        assertFalse(result);
    }

    @Test
    void shouldNotGetHasProductsForProductSetEmptyProducts() {

        //given
        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasProducts();

        //then
        assertFalse(result);
    }

    @Test
    public void shouldGetHasSingleProductsForSingleProduct(){

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
    public void shouldGetHasSingleProductsForManyProducts(){

        //given
        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);
        OrderProduct orderProduct1 = new OrderProduct(UUID.randomUUID(), 4);

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
    public void shouldGetHasManyProductsForManyProduct(){

        //given
        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);
        OrderProduct orderProduct1 = new OrderProduct(UUID.randomUUID(), 4);

        List<OrderProduct> orderProducts = List.of(orderProduct, orderProduct1);

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasManyProducts();

        //then
        assertTrue(result);
    }

    @Test
    public void shouldGetHasManyProductsForSingleProduct(){

        //given
        OrderProduct orderProduct = new OrderProduct(UUID.randomUUID(), 2);

        List<OrderProduct> orderProducts = List.of(orderProduct);

        OrderProductSet orderProductSet = new OrderProductSet(orderProducts);

        Offer offer = Offer.builder()
            .productSet(orderProductSet)
            .build();

        //when
        boolean result = offer.hasManyProducts();

        //then
        assertFalse(result);
    }

}