package pl.kamil_dywan.mapper.sfera;

import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.sfera.generated.ProductSet;
import pl.kamil_dywan.external.sfera.generated.ProductSetProduct;

import java.util.List;
import java.util.stream.Collectors;

public interface SferaProductSetMapper {

    public static ProductSet map(ProductOfferResponse product){

        Product productSet = SferaProductMapper.map(product);

        List<ProductSetProduct> productSetProducts = product.getProductSet().stream()
            .map(productSetProduct -> SferaProductSetProductMapper.map(productSetProduct))
            .collect(Collectors.toList());

        return ProductSet.builder()
            .productSet(productSet)
            .products(productSetProducts)
            .build();
    }

}
