package pl.kamil_dywan.external.subiektgt.own.receipt;

import pl.kamil_dywan.external.subiektgt.own.product.Product;
import pl.kamil_dywan.external.subiektgt.own.product.ProductDetailedPrice;

import java.util.List;

public record ReceiptRelatedData(

    ReceiptHeader receiptHeader,
    List<ReceiptItem> receiptItems,
    List<Product> products,
    List<ProductDetailedPrice> productPriceMappings
){}
