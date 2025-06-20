package pl.kamil_dywan.mapper.unit.sfera;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.offer_product.*;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.sfera.generated.ProductSet;
import pl.kamil_dywan.external.sfera.generated.ProductSetProduct;
import pl.kamil_dywan.mapper.sfera.SferaProductMapper;
import pl.kamil_dywan.mapper.sfera.SferaProductSetMapper;
import pl.kamil_dywan.mapper.sfera.SferaProductSetProductMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SferaProductSetMapperTest {

    @Test
    void shouldMap() {

        //given
        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData();
        ProductOfferProductRelatedData productRelatedData1 = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> productSetRelatedData = List.of(productRelatedData, productRelatedData1);

        ProductOfferResponse productSet = ProductOfferResponse.builder()
            .productSet(productSetRelatedData)
            .build();

        Product expectedProductSetDetails = new Product();

        ProductSetProduct expectedProductSetProduct = new ProductSetProduct();
        ProductSetProduct expectedProductSetProduct1 = new ProductSetProduct();

        List<ProductSetProduct> expectedProductSetProducts = List.of(expectedProductSetProduct, expectedProductSetProduct1);

        //when
        try(
                MockedStatic<SferaProductMapper> sferaProductMapperMock = Mockito.mockStatic(SferaProductMapper.class);
                MockedStatic<SferaProductSetProductMapper> sferaProductSetProductMapper = Mockito.mockStatic(SferaProductSetProductMapper.class);
        ){
            sferaProductMapperMock.when(() -> SferaProductMapper.map(any(ProductOfferResponse.class))).thenReturn(expectedProductSetDetails);

            for(int i = 0; i < productSetRelatedData.size(); i++) {

                ProductOfferProductRelatedData expectedProductData = productSetRelatedData.get(i);
                ProductSetProduct expectedProduct = expectedProductSetProducts.get(i);

                sferaProductSetProductMapper.when(() -> SferaProductSetProductMapper.map(expectedProductData)).thenReturn(expectedProduct);
            }

            ProductSet gotProductSet = SferaProductSetMapper.map(productSet);

            //then
            assertNotNull(gotProductSet);
            assertNotNull(gotProductSet.getProductSet());

            List<ProductSetProduct> gotProductSetProducts = gotProductSet.getProducts();

            assertNotNull(gotProductSetProducts);
            assertFalse(gotProductSetProducts.isEmpty());
            assertEquals(productSetRelatedData.size(), gotProductSetProducts.size());

            sferaProductMapperMock.verify(() -> SferaProductMapper.map(productSet));

            for (ProductOfferProductRelatedData expectedProductData : productSetRelatedData) {

                sferaProductSetProductMapper.verify(() -> SferaProductSetProductMapper.map(expectedProductData), times(2));
            }
        }
    }

}