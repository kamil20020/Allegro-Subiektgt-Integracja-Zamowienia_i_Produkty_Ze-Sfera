package pl.kamil_dywan.api.allegro.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.allegro.generated.offer_product.*;
import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "id",
    "name",
    "sellingMode",
    "taxSettings",
    "createdAt",
    "productSet",
    "external"
})
@Generated("jsonschema2pojo")
public class ProductOfferResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("sellingMode")
    private SellingMode sellingMode;

    @JsonProperty("taxSettings")
    private TaxSettings taxSettings;

    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;

    @JsonProperty("productSet")
    private List<ProductOfferProductRelatedData> productSet;

    @JsonProperty("external")
    private ExternalId externalId;

    @JsonIgnore
    private String subiektId;

    @JsonIgnore
    private static final String PRODUCER_CODE_KEY = "Kod producenta";
    private static final String EAN_CODE_KEY = "EAN (GTIN)";

    @JsonIgnore
    public void setSubiektId(String subiektId) {

        this.subiektId = subiektId;

        if(subiektId == null){

            return;
        }

        this.subiektId = subiektId.replaceAll("\"", "");
    }

    @JsonIgnore
    public String getExternalIdValue(){

        if(externalId == null || externalId.getId() == null){
            return null;
        }

        return externalId.getId();
    }

    @JsonIgnore
    public BigDecimal getTaxRate(){

        TaxSettings taxSettings = getTaxSettings();

        if(taxSettings != null){

            List<TaxForCountry> taxesForCountries = taxSettings.getTaxesFoCountries();

            if(taxesForCountries != null && taxesForCountries.size() > 0){

                return taxesForCountries.get(0).getTaxRate();
            }
        }

        return BigDecimal.valueOf(23);
    }

    @JsonIgnore
    public BigDecimal getPriceWithTax(){

        return sellingMode.getPrice().getAmount();
    }

    @JsonIgnore
    public BigDecimal getPriceWithoutTax(){

        BigDecimal taxRate = getTaxRate();
        BigDecimal unitPriceWithTax = getPriceWithTax();

        BigDecimal taxRateValue = taxRate.multiply(new BigDecimal("0.01"));

        return unitPriceWithTax.divide(
            BigDecimal.ONE.add(taxRateValue),
            RoundingMode.HALF_UP
        );
    }

    @JsonIgnore
    public Optional<String> getEANCode(){

        return getParameterCode(EAN_CODE_KEY);
    }

    @JsonIgnore
    public Optional<String> getProducerCode(){

        return getParameterCode(PRODUCER_CODE_KEY);
    }

    @JsonIgnore
    private Optional<String> getParameterCode(String parameterName){

        Optional<String> emptyResult = Optional.empty();

        Optional<ProductOfferProduct> firstProductOfferProduct = getFirstProductOfferProduct();

        if(firstProductOfferProduct.isEmpty()){
            return emptyResult;
        }

        List<OfferProductParameter> parameters = firstProductOfferProduct.get().getParameters();

        if(parameters == null || parameters.isEmpty()) {
            return emptyResult;
        }

        Optional<OfferProductParameter> producerCodeParameter = parameters.stream()
            .filter(parameter -> Objects.equals(parameter.getName(), parameterName))
            .findFirst();

        if(producerCodeParameter.isEmpty()) {
            return emptyResult;
        }

        List<String> parameterValues = producerCodeParameter.get().getValues();

        if(parameterValues == null || parameterValues.isEmpty()) {
            return emptyResult;
        }

        String gotExternalIdValue = parameterValues.get(0);

        if(gotExternalIdValue != null){

            gotExternalIdValue = gotExternalIdValue.replaceAll("\\s", "");
        }

        return Optional.ofNullable(gotExternalIdValue);
    }

    @JsonIgnore
    private Optional<ProductOfferProduct> getFirstProductOfferProduct(){

        Optional<ProductOfferProduct> emptyResult = Optional.empty();

        if(productSet == null || productSet.isEmpty() || productSet.size() > 1) {
            return emptyResult;
        }

        ProductOfferProductRelatedData productOfferProductRelatedData = productSet.get(0);

        ProductOfferProduct product = productSet.get(0).getProduct();

        if(product == null){
            return emptyResult;
        }

        return Optional.ofNullable(product);
    }

}
