package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
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

        String code = getCode(productId, externalId);
        String eanCode = getEanCode(externalId);

        if(allegroOffer.hasManyProducts()){

            code = "Zestaw-" + productId;
        }

        return Product.builder()
            .code(code)
            .ean(eanCode)
            .name(allegroOffer.getName())
            .priceWithTax(allegroOrderItem.getTotalPriceWithTax())
            .quantity(allegroOrderItem.getQuantity())
            .build();
    }

    public static Product map(ProductOfferResponse allegroProduct){

        Long productId = allegroProduct.getId();

        ExternalId externalId = allegroProduct.getExternalId();

        String code = getCode(productId.toString(), externalId);
        String eanCode = getEanCode(externalId);

        if(allegroProduct.hasManyProducts()){

            code = "Zestaw-" + productId;
        }

        return Product.builder()
            .code(code)
            .ean(eanCode)
            .name(allegroProduct.getName())
            .priceWithTax(allegroProduct.getPriceWithTax())
            .quantity(1)
            .build();
    }

    public static String getCode(String offerId, ExternalId externalId){

        String code = offerId;

        if(externalId != null && externalId.getId() != null){

            code = externalId.getProducerCode();
        }

        if(code == null){

            code = offerId;
        }

        return code;
    }

    public static String getEanCode(ExternalId externalId){

        if(externalId == null || externalId.getId() == null){

            return null;
        }

        return externalId.getEanCode();
    }

}
