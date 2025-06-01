package pl.kamil_dywan.mapper.unit.invoice;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProduct;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProductSet;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.InvoiceLineQuantity;
import pl.kamil_dywan.external.subiektgt.own.product.UOMCode;
import pl.kamil_dywan.mapper.invoice.InvoiceLineQuantityMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceLineQuantityMapperTest {

    @Test
    void shouldMapWhenProductIsIsNotNUll() {

        //given
        OrderProductSet productsSet = new OrderProductSet();
        productsSet.setProducts(List.of(new OrderProduct(), new OrderProduct()));

        Offer allegroOffer = Offer.builder()
            .productSet(productsSet)
            .build();

        OrderItem allegroOrderItem = OrderItem.builder()
            .quantity(22)
            .offer(allegroOffer)
            .build();

        //when
        InvoiceLineQuantity gotInvoiceLineQuantity = InvoiceLineQuantityMapper.map(allegroOrderItem);

        //then
        assertNotNull(gotInvoiceLineQuantity);
        assertEquals(allegroOrderItem.getQuantity(), gotInvoiceLineQuantity.getAmount());
        assertEquals(productsSet.getProducts().size(), gotInvoiceLineQuantity.getPacksize());
        assertEquals(UOMCode.UNIT, gotInvoiceLineQuantity.getUomCode());
    }

    @Test
    public void shouldMapWhenProductSetIsNull(){

        //given
        Offer allegroOffer = Offer.builder()
            .build();

        OrderItem allegroOrderItem = OrderItem.builder()
            .quantity(22)
            .offer(allegroOffer)
            .build();

        //when
        InvoiceLineQuantity gotInvoiceLineQuality = InvoiceLineQuantityMapper.map(allegroOrderItem);

        //then
        assertNotNull(gotInvoiceLineQuality);
        assertEquals(allegroOrderItem.getQuantity(), gotInvoiceLineQuality.getAmount());
        assertEquals(1, gotInvoiceLineQuality.getPacksize());
        assertEquals(UOMCode.UNIT, gotInvoiceLineQuality.getUomCode());
    }
}