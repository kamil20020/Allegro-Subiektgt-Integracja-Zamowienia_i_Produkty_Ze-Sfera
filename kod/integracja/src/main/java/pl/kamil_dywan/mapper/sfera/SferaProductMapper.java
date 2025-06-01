package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;

import java.math.BigDecimal;

public interface SferaProductMapper {

    public static Product map(OrderItem allegroOrderItem){

        Offer allegroOffer = allegroOrderItem.getOffer();

        String productId = allegroOffer.getId();

        ExternalId externalId = allegroOffer.getExternal();

        if(externalId != null && externalId.getId() != null){

            productId = externalId.getId();
        }

        BigDecimal priceWithTax = allegroOrderItem.getPrice().getAmount();

        return Product.builder()
            .code(productId)
            .name(allegroOffer.getName())
            .priceWithTax(priceWithTax)
            .quantity(allegroOrderItem.getQuantity())
            .build();
    }
}
