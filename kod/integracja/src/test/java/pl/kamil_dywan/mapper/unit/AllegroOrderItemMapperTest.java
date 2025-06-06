package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.delivery.DeliveryTime;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.mapper.AllegroOrderItemMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AllegroOrderItemMapperTest {

    @Test
    void shouldMapDeliveryToLineItem() {

        //given
        Delivery allegroDelivery = Delivery.builder()
            .cost(new Cost(new BigDecimal("34.56"), Currency.PLN))
            .time(new DeliveryTime(OffsetDateTime.now(), null, null, null))
            .build();

        BigDecimal expectedTaxRatePercentage = new BigDecimal("23");

        //when
        OrderItem gotDeliveryOrderItem = AllegroOrderItemMapper.mapDeliveryToLineItem(allegroDelivery);

        //then
        assertNotNull(gotDeliveryOrderItem);
        assertEquals(1, gotDeliveryOrderItem.getQuantity());
        assertEquals(allegroDelivery.getCost().getAmount(), gotDeliveryOrderItem.getPrice().getAmount());
        assertEquals(allegroDelivery.getCost().getAmount(), gotDeliveryOrderItem.getOriginalPrice().getAmount());
        assertEquals(allegroDelivery.getTime().getFrom(), gotDeliveryOrderItem.getBoughtAt());
        assertEquals("DOSTAWA123", gotDeliveryOrderItem.getOffer().getId());
        assertEquals("Dostawa do klienta", gotDeliveryOrderItem.getOffer().getName());
        assertEquals(expectedTaxRatePercentage, gotDeliveryOrderItem.getTax().getRate());
    }

}