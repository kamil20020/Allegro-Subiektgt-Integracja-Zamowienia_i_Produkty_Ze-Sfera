package pl.kamil_dywan.api.allegro.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.kamil_dywan.api.allegro.response.ProductOfferResponse;
import pl.kamil_dywan.external.allegro.generated.Cost;
import pl.kamil_dywan.external.allegro.generated.offer_product.*;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.own.Country;
import pl.kamil_dywan.external.allegro.own.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductOfferResponseTest {

    @Test
    void shouldSetSubiektIdWithoutSpaces(){

        //given
        String expectedSubiektId = "subiekt-id";

        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
//        productOfferResponse.setSubiektId(expectedSubiektId);

//        String gotSubiektId = productOfferResponse.getSubiektId();

        //then
//        assertNotNull(gotSubiektId);
//        assertEquals(expectedSubiektId, gotSubiektId);
    }

    @Test
    void shouldSetSubiektIdWithComma(){

        //given
        String inputSubiektId = "\"subiektid\"";
        String expectedSubiektId = "subiektid";

        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
//        productOfferResponse.setSubiektId(inputSubiektId);
//
//        String gotSubiektId = productOfferResponse.getSubiektId();

        //then
//        assertNotNull(gotSubiektId);
//        assertEquals(expectedSubiektId, gotSubiektId);
    }

    @Test
    void shouldGetExternalIdValue(){

        //given
        String expectedExternalIdValue = "123";

        ExternalId externalId = new ExternalId(expectedExternalIdValue);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .externalId(externalId)
            .build();

        //when
        String gotExternalIdValue = productOfferResponse.getExternalIdValue();

        //then
        assertNotNull(gotExternalIdValue);
        assertEquals(expectedExternalIdValue, gotExternalIdValue);
    }

    @Test
    void shouldNotGetExternalIdValueWhenExternalIdIsNull(){

        //given
        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        String gotExternalIdValue = productOfferResponse.getExternalIdValue();

        //then
        assertNull(gotExternalIdValue);
    }

    @Test
    void shouldNotGetExternalIdValueWhenThereExternalIdValueIsNull(){

        //given
        ExternalId externalId = new ExternalId();

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .externalId(externalId)
            .build();

        //when
        String gotExternalIdValue = productOfferResponse.getExternalIdValue();

        //then
        assertNull(gotExternalIdValue);
    }

    @Test
    void shouldGetTaxRateWithGivenTaxSetting() {

        //given
        BigDecimal taxRateValue = new BigDecimal("23");

        TaxForCountry taxForCountry = new TaxForCountry(taxRateValue, Country.PL.toString());

        TaxSettings taxSettings = new TaxSettings(List.of(taxForCountry), "", "");

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .taxSettings(taxSettings)
            .build();

        //when
        BigDecimal gotTaxRateValue = productOfferResponse.getTaxRate();

        //then
        assertNotNull(gotTaxRateValue);
        assertEquals(taxRateValue, gotTaxRateValue);
    }

    @Test
    void shouldGetTaxRateWhenTaxSettingIsNull() {

        //given
        BigDecimal expectedTaxRateValue = new BigDecimal("23");

        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        BigDecimal gotTaxRateValue = productOfferResponse.getTaxRate();

        //then
        assertNotNull(gotTaxRateValue);
        assertEquals(gotTaxRateValue, expectedTaxRateValue);
    }

    @Test
    void shouldGetTaxRateWhenTaxesForCountriesIsNull() {

        //given
        BigDecimal expectedTaxRateValue = new BigDecimal("23");

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .taxSettings(new TaxSettings())
            .build();

        //when
        BigDecimal gotTaxRateValue = productOfferResponse.getTaxRate();

        //then
        assertNotNull(gotTaxRateValue);
        assertEquals(gotTaxRateValue, expectedTaxRateValue);
    }

    @Test
    void shouldGetTaxRateWhenTaxesForCountriesIsEmpty() {

        //given
        BigDecimal expectedTaxRateValue = new BigDecimal("23");

        TaxSettings taxSettings = new TaxSettings(new ArrayList<>(), "", "");

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .taxSettings(taxSettings)
            .build();

        //when
        BigDecimal gotTaxRateValue = productOfferResponse.getTaxRate();

        //then
        assertNotNull(gotTaxRateValue);
        assertEquals(gotTaxRateValue, expectedTaxRateValue);
    }

    @Test
    void shouldGetPriceWithTax() {

        //given
        BigDecimal expectedPrice = new BigDecimal("24.38");

        Cost cost = new Cost(
            expectedPrice, Currency.PLN.toString()
        );

        SellingMode sellingMode = new SellingMode(cost);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .sellingMode(sellingMode)
            .build();

        //when
        BigDecimal price = productOfferResponse.getPriceWithTax();

        //then
        assertNotNull(price);
        assertEquals(expectedPrice, price);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "23.00, 16.26",
        "0.00, 20.00"
    })
    void shouldGetPriceWithoutTax(BigDecimal taxRateValue, BigDecimal expectedPriceWithoutTax) {

        //given
        TaxForCountry taxForCountry = new TaxForCountry(taxRateValue, Country.PL.toString());

        TaxSettings taxSettings = new TaxSettings(List.of(taxForCountry), "", "");

        Cost price = new Cost(new BigDecimal("20.00"), Currency.PLN.toString());

        SellingMode sellingMode = new SellingMode(price);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .taxSettings(taxSettings)
            .sellingMode(sellingMode)
            .build();

        //when
        BigDecimal gotPriceWithoutTax = productOfferResponse.getPriceWithoutTax();

        //then
        assertNotNull(gotPriceWithoutTax);
        assertEquals(expectedPriceWithoutTax, gotPriceWithoutTax);
    }

    @Test
    public void shouldGetHasProductsWhenHas(){

        //given
        ProductOfferProductRelatedData product = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(productsRelatedData)
            .build();

        //when
        boolean gotResult = productOfferResponse.hasProducts();

        //then
        assertTrue(gotResult);
    }

    @Test
    public void shouldGetHasProductsWhenThereAreNoProducts(){

        //given
        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(new ArrayList<>())
            .build();

        //when
        boolean gotResult = productOfferResponse.hasProducts();

        //then
        assertFalse(gotResult);
    }

    @Test
    public void shouldGetHasProductsWhenProductsSetIsNull(){

        //given
        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        boolean gotResult = productOfferResponse.hasProducts();

        //then
        assertFalse(gotResult);
    }

    @Test
    public void shouldGetHasSingleProductsWhenHas(){

        //given
        ProductOfferProductRelatedData product = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(productsRelatedData)
            .build();

        //when
        boolean gotResult = productOfferResponse.hasSingleProduct();

        //then
        assertTrue(gotResult);
    }

    @Test
    public void shouldGetHasSingleProductWhenThereAreMultipleProducts(){

        //given
        ProductOfferProductRelatedData product = new ProductOfferProductRelatedData();
        ProductOfferProductRelatedData product1 = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(product, product1);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(productsRelatedData)
            .build();

        //when
        boolean gotResult = productOfferResponse.hasSingleProduct();

        //then
        assertFalse(gotResult);
    }

    @Test
    public void shouldGetHasSingleProductWhenThereAreNoProducts(){

        //given
        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(new ArrayList<>())
            .build();

        //when
        boolean gotResult = productOfferResponse.hasSingleProduct();

        //then
        assertFalse(gotResult);
    }

    @Test
    public void shouldGetHasSingleProductWhenProductsSetIsNull(){

        //given
        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        boolean gotResult = productOfferResponse.hasSingleProduct();

        //then
        assertFalse(gotResult);
    }

    @Test
    public void shouldGetHasManyProductsWhenHas(){

        //given
        ProductOfferProductRelatedData product1 = new ProductOfferProductRelatedData();
        ProductOfferProductRelatedData product2 = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(product1, product2);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(productsRelatedData)
            .build();

        //when
        boolean gotResult = productOfferResponse.hasManyProducts();

        assertTrue(gotResult);
    }

    @Test
    public void shouldGetHasManyProductsWhenHasOneProduct(){

        //given
        ProductOfferProductRelatedData product1 = new ProductOfferProductRelatedData();

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(product1);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
                .productSet(productsRelatedData)
                .build();

        //when
        boolean gotResult = productOfferResponse.hasManyProducts();

        assertFalse(gotResult);
    }


    @Test
    public void shouldGetHasManyProductsWhenHasNoProducts(){

        //given
        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(new ArrayList<>())
            .build();

        //when
        boolean gotResult = productOfferResponse.hasManyProducts();

        assertFalse(gotResult);
    }

    @Test
    public void shouldGetHasManyProductsWhenProductsAreNull(){

        //given
        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        boolean gotResult = productOfferResponse.hasManyProducts();

        assertFalse(gotResult);
    }

}