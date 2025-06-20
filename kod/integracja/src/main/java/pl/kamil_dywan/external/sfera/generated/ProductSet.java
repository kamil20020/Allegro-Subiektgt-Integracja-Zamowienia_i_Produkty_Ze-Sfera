package pl.kamil_dywan.external.sfera.generated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Generated;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "productSet",
        "products"
})
@Generated("jsonschema2pojo")
public class ProductSet {

    @JsonProperty("productSet")
    private Product productSet;

    @JsonProperty("products")
    private List<ProductSetProduct> products;

}

