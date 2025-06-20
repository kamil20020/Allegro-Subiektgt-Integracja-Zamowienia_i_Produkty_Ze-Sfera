package pl.kamil_dywan.api.sfera.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil_dywan.external.sfera.generated.Product;
import pl.kamil_dywan.external.sfera.generated.ProductSet;
import pl.kamil_dywan.external.sfera.generated.ProductSetProduct;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
    "productsSets"
})
@Generated("jsonschema2pojo")
public class CreateProductsSetsRequest {

    @JsonProperty("productsSets")
    private List<ProductSet> productsSets;

}
