
package pl.kamil_dywan.external.allegro.generated.order_item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import pl.kamil_dywan.external.allegro.generated.Cost;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "offer",
    "quantity",
    "originalPrice",
    "price",
    "reconciliation",
    "selectedAdditionalServices",
    "vouchers",
    "tax",
    "boughtAt"
})
@Generated("jsonschema2pojo")
public class OrderItem {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("offer")
    private Offer offer;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("originalPrice")
    private Cost originalPrice;

    @JsonProperty("price")
    private Cost price;

    @JsonProperty("reconciliation")
    private OrderItemReconciliation reconciliation;

    @JsonProperty("selectedAdditionalServices")
    private List<OrderItemAdditionalService> selectedAdditionalServices = new ArrayList<>();

    @JsonProperty("vouchers")
    private List<Voucher> vouchers = new ArrayList<>();

    @JsonProperty("tax")
    private Tax tax;

    @JsonProperty("boughtAt")
    private OffsetDateTime boughtAt;

    @JsonIgnore
    public BigDecimal getTaxRatePercentage(){

        if(tax == null){

            return BigDecimal.valueOf(23);
        }

        return tax.getRate();
    }

    @JsonIgnore
    public Integer getQuantity() {

        Integer orderItemQuantity = quantity;

        if(!offer.hasProducts() || offer.hasManyProducts()){

            return orderItemQuantity;
        }

        OrderProductSet orderProductSet = offer.getProductSet();
        OrderProduct orderProduct = orderProductSet.getProducts().get(0);

        return orderItemQuantity * orderProduct.getQuantity();
    }

    @JsonIgnore
    public BigDecimal getTotalPriceWithTax(){

        BigDecimal unitPriceWithTax = price.getAmount();

        BigDecimal quantityConverted = BigDecimal.valueOf(quantity);

        BigDecimal totalPriceWithTax = unitPriceWithTax.multiply(quantityConverted);
        totalPriceWithTax = totalPriceWithTax.setScale(2, RoundingMode.HALF_UP);

        return totalPriceWithTax;
    }

}
