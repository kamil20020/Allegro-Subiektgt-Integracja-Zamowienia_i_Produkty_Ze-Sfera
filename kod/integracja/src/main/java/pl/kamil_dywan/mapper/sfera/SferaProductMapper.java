package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.order.Order;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProduct;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.allegro.own.offer.Signature;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;

import java.math.BigDecimal;

public interface SferaProductMapper {

    public static Product map(OrderItem allegroOrderItem) throws IllegalStateException{

        Offer allegroOffer = allegroOrderItem.getOffer();

        ExternalId externalId = allegroOffer.getExternal();

        String code = allegroOffer.getId();
        Integer quantity = allegroOrderItem.getQuantity();

        if(externalId != null && externalId.getId() != null){

            Signature signature = Signature.extract(externalId.getId());

            code = signature.subiektSymbol();

            if(signature.quantity() != null && allegroOrderItem.getQuantity() != null){

                quantity *= signature.quantity();
            }
        }

        return Product.builder()
            .code(code)
            .name(allegroOffer.getName())
            .priceWithTax(allegroOrderItem.getTotalPriceWithTax())
            .quantity(quantity)
            .build();
    }

    public static Product map(ProductOfferResponse allegroProduct){

        String externalIdValue = allegroProduct.getExternalIdValue();
        String code = allegroProduct.getId().toString();

        Integer quantity = 1;

        if(externalIdValue != null){

            Signature signature = Signature.extract(externalIdValue);
            code = signature.subiektSymbol();

            if(signature.quantity() != null){

                quantity = signature.quantity();
            }
        }

        return Product.builder()
            .code(code)
            .name(allegroProduct.getName())
            .priceWithTax(allegroProduct.getPriceWithTax())
            .quantity(quantity)
            .build();
    }

}
