package pl.kamil_dywan.mapper;

import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.SellingMode;
import pl.kamil_dywan.external.allegro.generated.offer_product.TaxSettings;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.own.Currency;
import pl.kamil_dywan.external.subiektgt.own.product.Product;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface ProductOfferMapper {

    public static Product map(ProductOfferResponse allegroProductOfferResponse, ProductType productType){

        if(allegroProductOfferResponse == null){

            return null;
        }

        String productId = allegroProductOfferResponse.getId().toString();

        ExternalId externalId = allegroProductOfferResponse.getExternalId();

        if(externalId != null && externalId.getId() != null){

            productId = externalId.getId();
        }

        TaxSettings taxSettings = allegroProductOfferResponse.getTaxSettings();
        SellingMode sellingMode = allegroProductOfferResponse.getSellingMode();

        BigDecimal taxRatePercentage = getTaxRatePercentage(taxSettings);
        BigDecimal taxRateValue = taxRatePercentage.multiply(BigDecimal.valueOf(0.01));

        BigDecimal unitPriceWithTax = sellingMode.getPrice() != null ? sellingMode.getPrice().getAmount() : BigDecimal.ZERO;

        BigDecimal unitPriceWithoutTax = unitPriceWithTax.divide(
            BigDecimal.ONE.add(taxRateValue),
            RoundingMode.HALF_UP
        );

        return Product.builder()
            .id(productId)
            .name(allegroProductOfferResponse.getName())
            .type(productType)
            .unitPriceWithoutTax(unitPriceWithoutTax)
            .taxRatePercentage(taxRatePercentage)
            .build();
    }

    private static BigDecimal getTaxRatePercentage(TaxSettings taxSettings){

        if(taxSettings == null){

            return BigDecimal.valueOf(23);
        }

        return taxSettings.getTaxesFoCountries().get(0).getTaxRate();
    }
}
