package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProduct;
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

        String code = productId;

        String producerCode = null;
        String eanCode = null;

        if(externalId != null && externalId.getId() != null){

            producerCode = externalId.getProducerCode();
            eanCode = externalId.getEanCode();
        }

        if(producerCode != null){

            code = producerCode;
        }

        return Product.builder()
            .code(code)
            .ean(eanCode)
            .name(allegroOffer.getName())
            .priceWithTax(allegroOrderItem.getTotalPriceWithTax())
            .quantity(allegroOrderItem.getQuantity())
            .build();
    }

}
