package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.api.sfera.request.CreateProductsSetRequest;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.sfera.generated.ProductSetProduct;

import java.util.List;
import java.util.stream.Collectors;

public interface SferaProductSetMapper {

    public static CreateProductsSetRequest map(ProductOfferResponse product){

        String code = "Zestaw-" + product.getId();

        List<ProductSetProduct> productSetProducts = product.getProductSet().stream()
            .map(productSetProduct -> SferaProductSetProductMapper.map(productSetProduct))
            .collect(Collectors.toList());

        return CreateProductsSetRequest.builder()
            .code(code)
            .name(product.getName())
            .priceWithTax(product.getPriceWithTax())
            .products(productSetProducts)
            .build();
    }

}
