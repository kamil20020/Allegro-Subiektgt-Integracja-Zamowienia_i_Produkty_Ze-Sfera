package pl.kamil_dywan.external.subiektgt.own.product;

import lombok.*;
import pl.kamil_dywan.file.EppSerializable;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
public class Product extends EppSerializable {

    private ProductType type;
    private String id;
    private String name;
    private BigDecimal taxRatePercentage;
    private BigDecimal unitPriceWithoutTax;

    public Product(String... args){

        super(args);

        type = ProductType.valueOf(Integer.valueOf(args[0]));
        id = args[1];
        name = args[2];
        taxRatePercentage = new BigDecimal(args[3]);
        unitPriceWithoutTax = new BigDecimal(args[4]);
    }

    public Product(){

        super(null);
    }

    public Product(ProductType type, String id, String name, BigDecimal taxRatePercentage, BigDecimal unitPriceWithoutTax) {

        super(null);

        this.type = type;
        this.id = id;
        this.name = name;
        this.taxRatePercentage = taxRatePercentage;
        this.unitPriceWithoutTax = unitPriceWithoutTax;
    }

    public void setId(String id) {

        this.id = id;
    }
}
