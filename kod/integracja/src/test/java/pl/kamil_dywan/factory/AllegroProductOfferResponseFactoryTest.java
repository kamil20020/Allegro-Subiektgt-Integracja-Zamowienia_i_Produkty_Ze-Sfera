package pl.kamil_dywan.factory;

import org.junit.jupiter.api.Test;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.offer_product.SellingMode;
import pl.kamil_dywan.external.allegro.generated.offer_product.TaxSettings;
import pl.kamil_dywan.external.allegro.own.Country;
import pl.kamil_dywan.external.allegro.own.Currency;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AllegroProductOfferResponseFactoryTest {

    @Test
    void shouldCreateDeliveryProductOffer() {

        //given

        //when
        ProductOfferResponse deliveryProductOfferResponse = AllegroProductOfferFactory.createDeliveryProductOffer();

        //then
        assertNotNull(deliveryProductOfferResponse);

        SellingMode sellingMode = deliveryProductOfferResponse.getSellingMode();
        TaxSettings taxSettings = deliveryProductOfferResponse.getTaxSettings();

        assertNotNull(sellingMode);
        assertEquals(1L, deliveryProductOfferResponse.getId());
        assertEquals("Dostawa", deliveryProductOfferResponse.getName());

        assertNotNull(sellingMode);
        assertEquals(BigDecimal.ZERO, sellingMode.getPrice().getAmount());
        assertEquals(Currency.PLN, sellingMode.getPrice().getCurrency());

        assertNotNull(taxSettings);
        assertNotNull(taxSettings.getTaxesFoCountries());
        assertEquals(1, taxSettings.getTaxesFoCountries().size());
        assertEquals(BigDecimal.valueOf(23), taxSettings.getTaxesFoCountries().get(0).getTaxRate());
        assertEquals(Country.PL.toString(), taxSettings.getTaxesFoCountries().get(0).getCountry());
    }
}