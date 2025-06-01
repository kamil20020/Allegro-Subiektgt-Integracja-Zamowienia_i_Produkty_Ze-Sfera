package pl.kamil_dywan.mapper.receipt;

import pl.kamil_dywan.external.allegro.generated.order_item.ExternalId;
import pl.kamil_dywan.external.allegro.generated.order_item.Offer;
import pl.kamil_dywan.external.allegro.generated.order_item.OrderItem;
import pl.kamil_dywan.external.allegro.own.order.OrderItemMoneyStats;
import pl.kamil_dywan.external.subiektgt.own.receipt.ReceiptItem;

public interface ReceiptItemMapper {

    public static ReceiptItem map(OrderItem orderItem, OrderItemMoneyStats orderItemMoneyStats, int receiptItemIndex){

        Offer offer = orderItem.getOffer();

        String orderItemId = offer.getId();

        ExternalId externalId = offer.getExternal();

        if(externalId != null && externalId.getId() != null){

            orderItemId = externalId.getId();
        }

        Integer quantity = orderItem.getQuantity();

        return new ReceiptItem(
            receiptItemIndex,
            orderItemId,
            quantity,
            orderItemMoneyStats.getUnitPriceWithoutTax(),
            orderItemMoneyStats.getUnitPriceWithTax(),
            orderItemMoneyStats.getTaxRatePercentage(),
            orderItemMoneyStats.getTotalPriceWithoutTax(),
            orderItemMoneyStats.getTotalTaxValue(),
            orderItemMoneyStats.getTotalPriceWithTax()
        );
    }
}
