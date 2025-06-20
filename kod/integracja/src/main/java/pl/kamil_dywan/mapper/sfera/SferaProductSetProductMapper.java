package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProduct;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedData;
import pl.kamil_dywan.external.allegro.generated.offer_product.ProductOfferProductRelatedDataQuantity;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.sfera.generated.ProductSetProduct;

public interface SferaProductSetProductMapper {

    public static ProductSetProduct map(ProductOfferProductRelatedData productRelatedData){

        ProductOfferProduct product = productRelatedData.getProduct();

        String productId = product.getId().toString();

        ExternalId externalId = new ExternalId(product.getProducerCode(), product.getEANCode());

        String code = SferaProductMapper.getCode(productId, externalId);
        String ean = SferaProductMapper.getEanCode(externalId);

        ProductOfferProductRelatedDataQuantity quantityRaw = productRelatedData.getQuantity();

        return ProductSetProduct.builder()
            .code(code)
            .ean(ean)
            .quantity(quantityRaw.getValue())
            .build();
    }
}
