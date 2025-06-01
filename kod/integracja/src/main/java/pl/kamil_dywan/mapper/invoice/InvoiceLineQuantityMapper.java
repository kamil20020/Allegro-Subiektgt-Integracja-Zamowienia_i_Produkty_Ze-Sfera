package pl.kamil_dywan.mapper.invoice;

import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderProductSet;
import pl.kamil_dywan.external.subiektgt.generated.invoice_line.InvoiceLineQuantity;
import pl.kamil_dywan.external.subiektgt.own.product.UOMCode;

import java.math.BigDecimal;

public interface InvoiceLineQuantityMapper {

    static InvoiceLineQuantity map(OrderItem allegroOrderItem){

        BigDecimal quantityValue = BigDecimal.valueOf(allegroOrderItem.getQuantity());

        int packSize = 1;

        Offer allegroOffer = allegroOrderItem.getOffer();
        OrderProductSet allegroProductSet = allegroOffer.getProductSet();

        if(allegroProductSet != null){

            packSize = allegroProductSet.getProducts().size();
        }

        return InvoiceLineQuantity.builder()
            .packsize(packSize)
            .amount(quantityValue.intValue())
            .uomCode(UOMCode.UNIT)
            .build();
    }
}
