package pl.kamil_dywan.external.subiektgt.own.receipt;

import lombok.Getter;
import lombok.ToString;
import pl.kamil_dywan.external.subiektgt.own.product.ProductType;
import pl.kamil_dywan.external.subiektgt.own.product.UOMCode;
import pl.kamil_dywan.file.EppSerializable;

import java.math.BigDecimal;

@Getter
@ToString
public class ReceiptItem extends EppSerializable {

    private Integer receiptItemIndex;
    private ProductType productType = ProductType.GOODS;
    private String id;
    private UOMCode uomCode = UOMCode.UNIT;
    private Integer quantity;
    private BigDecimal magazinePrice = BigDecimal.ZERO;
    private BigDecimal unitPriceWithoutTax;
    private BigDecimal unitPriceWithPrice;
    private BigDecimal taxRateValue;
    private BigDecimal totalPriceWithoutTax;
    private BigDecimal totalTaxValue;
    private BigDecimal totalPriceWithTax;
    private BigDecimal cost = BigDecimal.ZERO;


    public ReceiptItem(String[] args) {

        super(args);

        this.productType = ProductType.valueOf(args[0]);
    }

    public ReceiptItem(Integer receiptItemIndex, ProductType productType, String id, UOMCode uomCode, Integer quantity, BigDecimal magazinePrice, BigDecimal unitPriceWithoutTax, BigDecimal unitPriceWithPrice, BigDecimal taxRateValue, BigDecimal totalPriceWithoutTax, BigDecimal totalTaxValue, BigDecimal totalPriceWithTax, BigDecimal cost) {

        super(null);

        this.receiptItemIndex = receiptItemIndex;
        this.productType = productType;
        this.id = id;
        this.uomCode = uomCode;
        this.quantity = quantity;
        this.magazinePrice = magazinePrice;
        this.unitPriceWithoutTax = unitPriceWithoutTax;
        this.unitPriceWithPrice = unitPriceWithPrice;
        this.taxRateValue = taxRateValue;
        this.totalPriceWithoutTax = totalPriceWithoutTax;
        this.totalTaxValue = totalTaxValue;
        this.totalPriceWithTax = totalPriceWithTax;
        this.cost = cost;
    }

    public ReceiptItem(Integer receiptItemIndex, String id, Integer quantity, BigDecimal unitPriceWithoutTax, BigDecimal unitPriceWithPrice, BigDecimal taxRateValue, BigDecimal totalPriceWithoutTax, BigDecimal totalTaxValue, BigDecimal totalPriceWithTax) {

        super(null);

        this.receiptItemIndex = receiptItemIndex;
        this.id = id;
        this.quantity = quantity;
        this.unitPriceWithoutTax = unitPriceWithoutTax;
        this.unitPriceWithPrice = unitPriceWithPrice;
        this.taxRateValue = taxRateValue;
        this.totalPriceWithoutTax = totalPriceWithoutTax;
        this.totalTaxValue = totalTaxValue;
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public ReceiptItem() {

        super(null);
    }

    public void setProductType(ProductType productType) {

        this.productType = productType;
    }

    public void setId(String id) {

        this.id = id;
    }
}
