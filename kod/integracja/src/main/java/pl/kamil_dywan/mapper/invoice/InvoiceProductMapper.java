package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.offer_product.OfferProductParameter;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProduct;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProductSet;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.Product;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface InvoiceProductMapper {

    static Product map(Offer allegroOffer){

        String productId = allegroOffer.getId();

        ExternalId externalId = allegroOffer.getExternal();

        if(externalId != null && externalId.getId() != null){

            productId = externalId.getId();
        }

        return Product.builder()
            .suppliersProductCode(productId)
            .description(allegroOffer.getName())
            .build();
    }

}
