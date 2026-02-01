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

        String code = null;

        if(externalId != null){

            code = externalId.getId();

            Signature signature = Signature.extract(code);
            allegroOffer.setSignature(signature);

            allegroOffer.validateSignature();
        }

        return Product.builder()
            .code(code)
            .name(allegroOffer.getName())
            .priceWithTax(allegroOrderItem.getTotalPriceWithTax())
            .quantity(allegroOrderItem.getQuantity())
            .build();
    }

    public static Product map(ProductOfferResponse allegroProduct){

        String externalIdValue = allegroProduct.getExternalIdValue();

        return Product.builder()
            .code(externalIdValue)
            .name(allegroProduct.getName())
            .priceWithTax(allegroProduct.getPriceWithTax())
            .quantity(1)
            .build();
    }

}
