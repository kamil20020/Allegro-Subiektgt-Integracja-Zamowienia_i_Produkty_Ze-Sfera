package pl.kamil_dywan.model.unit;

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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductOfferResponseTest {

    @Test
    void shouldSetSubiektIdWithoutSpaces(){

        //given
        String expectedSubiektId = "subiekt-id";

        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        productOfferResponse.setSubiektId(expectedSubiektId);

        String gotSubiektId = productOfferResponse.getSubiektId();

        //then
        assertNotNull(gotSubiektId);
        assertEquals(expectedSubiektId, gotSubiektId);
    }

    @Test
    void shouldSetSubiektIdWithComma(){

        //given
        String inputSubiektId = "\"subiektid\"";
        String expectedSubiektId = "subiektid";

        ProductOfferResponse productOfferResponse = new ProductOfferResponse();

        //when
        productOfferResponse.setSubiektId(inputSubiektId);

        String gotSubiektId = productOfferResponse.getSubiektId();

        //then
        assertNotNull(gotSubiektId);
        assertEquals(expectedSubiektId, gotSubiektId);
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
            expectedPrice, Currency.PLN
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

        Cost price = new Cost(new BigDecimal("20.00"), Currency.PLN);

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
    public void shouldGetExistingProducerCode(){

        //given
        String inputProducerCode = "12345";
        String expectedProducerCode = "12345";

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(123L)
            .name("Kod producenta")
            .values(List.of(inputProducerCode))
            .build();

        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Inny parametr")
            .values(List.of("6789"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter, producerCodeParameter));

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(productRelatedData);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(productsRelatedData)
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNotNull(gotProducerCode);
        assertEquals(expectedProducerCode, gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereIsNotProductSet(){

        //given
        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereIsEmptyProductSet(){

        //given
        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(new ArrayList<>())
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereIsMoreThanOneProduct(){

        //given
        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(123L)
            .name("Kod producenta")
            .values(List.of("12345"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(producerCodeParameter));
        ProductOfferProduct product1 = new ProductOfferProduct();

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);
        ProductOfferProductRelatedData productRelatedData1 = new ProductOfferProductRelatedData(product1);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData, productRelatedData1))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereIsEmptyProduct(){

        //given
        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(null);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereAreNotParameters(){

        //given
        ProductOfferProduct product = new ProductOfferProduct();

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereAreEmptyParameters(){

        //given
        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), new ArrayList<>());

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereIsNotProducerParameter(){

        //given
        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Inny parametr")
            .values(List.of("6789"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter));

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenThereAreNotProducerParameterValues(){

        //given
        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Kod producenta")
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter));

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldNotGetExistingProducerCodeWhenProducerParameterValuesAreEmpty(){

        //given
        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Kod producenta")
            .values(new ArrayList<>())
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter));

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(List.of(productRelatedData))
            .build();

        //when
        String gotProducerCode = productOfferResponse.getProducerCode();

        //then
        assertNull(gotProducerCode);
    }

    @Test
    public void shouldGetExistingEanCode(){

        //given
        String expectedEanCode = "ean";

        OfferProductParameter producerCodeParameter = OfferProductParameter.builder()
            .id(123L)
            .name("EAN (GTIN)")
            .values(List.of(expectedEanCode))
            .build();

        OfferProductParameter otherParameter = OfferProductParameter.builder()
            .id(456L)
            .name("Kod producenta")
            .values(List.of("6789"))
            .build();

        ProductOfferProduct product = new ProductOfferProduct(UUID.randomUUID(), List.of(otherParameter, producerCodeParameter));

        ProductOfferProductRelatedData productRelatedData = new ProductOfferProductRelatedData(product);

        List<ProductOfferProductRelatedData> productsRelatedData = List.of(productRelatedData);

        ProductOfferResponse productOfferResponse = ProductOfferResponse.builder()
            .productSet(productsRelatedData)
            .build();

        //when
        String gotEanCode = productOfferResponse.getEANCode();

        //then
        assertNotNull(gotEanCode);
        assertEquals(expectedEanCode, gotEanCode);
    }

}