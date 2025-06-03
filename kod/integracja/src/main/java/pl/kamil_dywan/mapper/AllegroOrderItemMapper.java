package pl.kamil_dywan.mapper;

import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.delivery.Delivery;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.Tax;
import pl.kamil_dywan.external.allegro.own.Currency;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface AllegroOrderItemMapper {

    static OrderItem mapDeliveryToLineItem(Delivery allegroDelivery){

        BigDecimal taxRatePercentage = new BigDecimal("23");

        Offer deliveryOffer = Offer.builder()
            .id("DOSTAWA123")
            .name("Dostawa do klienta")
            .build();

        BigDecimal price = allegroDelivery.getCost().getAmount();
        OffsetDateTime buyAt = allegroDelivery.getTime().getFrom();

        return OrderItem.builder()
            .quantity(1)
            .originalPrice(new Cost(price, Currency.PLN))
            .price(new Cost(price, Currency.PLN))
            .offer(deliveryOffer)
            .boughtAt(buyAt)
            .tax(new Tax(taxRatePercentage, "", ""))
            .build();
    }
}
