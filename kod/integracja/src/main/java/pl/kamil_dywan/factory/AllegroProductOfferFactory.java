package pl.kamil_dywan.factory;

import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.SellingMode;
import pl.kamil_dywan.external.allegro.generated.offer_product.TaxForCountry;
import pl.kamil_dywan.external.allegro.generated.offer_product.TaxSettings;
import pl.kamil_dywan.external.allegro.own.Country;
import pl.kamil_dywan.external.allegro.own.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface AllegroProductOfferFactory {

    public static ProductOfferResponse createDeliveryProductOffer(){

        SellingMode sellingMode = new SellingMode(
            new Cost(
                BigDecimal.ZERO,
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

        return ProductOfferResponse.builder()
            .id(1L)
            .name("Dostawa")
            .sellingMode(sellingMode)
            .taxSettings(taxSettings)
            .build();
    }
}
