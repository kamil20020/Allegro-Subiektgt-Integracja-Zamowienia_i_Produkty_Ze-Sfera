package pl.kamil_dywan.mapper.unit;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.offer_product.*;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.own.Country;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.subiektgt.own.product.Product;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;
import pl.kamil_dywan.mapper.ProductOfferMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductOfferResponseMapperTest {

    @Test
    void shouldMapWithoutExternalId(){

        //given
        SellingMode sellingMode = new SellingMode(
            new Cost(
                new BigDecimal("34.48"),
                Currency.PLN
            )
        );

        TaxSettings taxSettings = new TaxSettings(
            List.of(
                new TaxForCountry(new BigDecimal("23"), Country.PL.toString())
            ),
            "",
            ""
        );

        ProductOfferResponse allegroProductOfferResponse = ProductOfferResponse.builder()
            .id(12L)
            .name("Product offer 123")
            .sellingMode(sellingMode)
            .taxSettings(taxSettings)
            .build();

        BigDecimal expectedTaxRate = taxSettings.getTaxesFoCountries().get(0).getTaxRate();
        BigDecimal expectedUnitPriceWithTax = sellingMode.getPrice().getAmount();
        BigDecimal expectedUnitPriceWithoutTax = new BigDecimal("28.03");

        ProductType expectedProductType = ProductType.GOODS;

        //when
        Product subiektProduct = ProductOfferMapper.map(allegroProductOfferResponse, expectedProductType);

        //then
        assertNotNull(subiektProduct);
        assertEquals(allegroProductOfferResponse.getId().toString(), subiektProduct.getId());
        assertEquals(allegroProductOfferResponse.getName(), subiektProduct.getName());
        assertEquals(expectedProductType, subiektProduct.getType());
        assertEquals(expectedUnitPriceWithoutTax, subiektProduct.getUnitPriceWithoutTax());
        assertEquals(expectedTaxRate, subiektProduct.getTaxRatePercentage());
    }

    @Test
    void shouldMapWithoutPartiallyExternalId(){

        //given
        SellingMode sellingMode = new SellingMode(
            new Cost(
                new BigDecimal("34.48"),
                Currency.PLN
            )
        );

        TaxSettings taxSettings = new TaxSettings(
            List.of(
                new TaxForCountry(new BigDecimal("23"), Country.PL.toString())
            ),
            "",
            ""
        );

        ProductOfferResponse allegroProductOfferResponse = ProductOfferResponse.builder()
            .id(12L)
            .name("Product offer 123")
            .externalId(new ExternalId())
            .sellingMode(sellingMode)
            .taxSettings(taxSettings)
            .build();

        BigDecimal expectedTaxRate = taxSettings.getTaxesFoCountries().get(0).getTaxRate();
        BigDecimal expectedUnitPriceWithTax = sellingMode.getPrice().getAmount();
        BigDecimal expectedUnitPriceWithoutTax = new BigDecimal("28.03");

        //when
        Product subiektProduct = ProductOfferMapper.map(allegroProductOfferResponse, ProductType.GOODS);

        //then
        assertNotNull(subiektProduct);
        assertEquals(allegroProductOfferResponse.getId().toString(), subiektProduct.getId());
        assertEquals(allegroProductOfferResponse.getName(), subiektProduct.getName());
        assertEquals(ProductType.GOODS, subiektProduct.getType());
        assertEquals(expectedUnitPriceWithoutTax, subiektProduct.getUnitPriceWithoutTax());
        assertEquals(expectedTaxRate, subiektProduct.getTaxRatePercentage());
    }

    @Test
    void shouldMapWithExternalId(){

        //given
        SellingMode sellingMode = new SellingMode(
            new Cost(
                new BigDecimal("34.48"),
                Currency.PLN
            )
        );

        TaxSettings taxSettings = new TaxSettings(
            List.of(
                new TaxForCountry(new BigDecimal("23"), Country.PL.toString())
            ),
            "",
            ""
        );

        String expectedId = "12";

        ExternalId externalId = new ExternalId(expectedId);

        ProductOfferResponse allegroProductOfferResponse = ProductOfferResponse.builder()
            .id(Long.valueOf(expectedId))
            .name("Product offer 123")
            .externalId(externalId)
            .sellingMode(sellingMode)
            .taxSettings(taxSettings)
            .build();

        BigDecimal expectedTaxRate = taxSettings.getTaxesFoCountries().get(0).getTaxRate();
        BigDecimal expectedUnitPriceWithTax = sellingMode.getPrice().getAmount();
        BigDecimal expectedUnitPriceWithoutTax = new BigDecimal("28.03");

        //when
        Product subiektProduct = ProductOfferMapper.map(allegroProductOfferResponse, ProductType.GOODS);

        //then
        assertNotNull(subiektProduct);
        assertEquals(ProductType.GOODS, subiektProduct.getType());
        assertEquals(expectedId, subiektProduct.getId());
        assertEquals(allegroProductOfferResponse.getName(), subiektProduct.getName());
        assertEquals(expectedTaxRate, subiektProduct.getTaxRatePercentage());
        assertEquals(expectedUnitPriceWithoutTax, subiektProduct.getUnitPriceWithoutTax());
    }
}